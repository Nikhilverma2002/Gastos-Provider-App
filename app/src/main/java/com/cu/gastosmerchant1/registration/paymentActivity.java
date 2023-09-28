package com.cu.gastosmerchant1.registration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.gastosmerchant1.Dashboard.Home;
import com.cu.gastosmerchant1.R;
import com.cu.gastosmerchant1.details.Account_Information;
import com.cu.gastosmerchant1.details.Shop_Information;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//import com.paytm.pg.merchant.PaytmChecksum;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.paytm.pgsdk.TransactionManager;
//import com.google.gson.Gson;
//import com.paytm.pgsdk.PaytmOrder;
//import com.paytm.pgsdk.PaytmPGService;
//import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
//
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class paymentActivity extends AppCompatActivity {
    private String TAG = "PaymentActivity";

    //for production
    private static final String MERCHANT_ID = "nQsiiK69936497682254";
    private static final String MERCHANT_KEY = "8bbLQiwHf8M3@SQW";
    private static final String WEBSITE = "DEFAULT";
    String checkSumHash = "";
    private static final String CHECKSUM_URL = "https://us-central1-gastosprovidermvp.cloudfunctions.net/getChecksum";
    private static String CALLBACK_URL = "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=";

    //for staging
    /*
    private static final String MERCHANT_ID="";
    private static final String MERCHANT_KEY="";
    private static final String WEBSITE="";
    */

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mDatabase;

    private static final String INDUSTRY_TYPE = "Retail";
    private static final String CHANNEL_ID = "WAP";

    private String txnAmountString = "", orderIdString = "", custIdString = "", txnTokenString;
    private Button btnPayNow, btnApplyPromo;
    private EditText eT_promoCode;
    private TextView tV_registrationCost, tV_gstCost, tV_savingsCost, tV_totalAmount;
    private boolean isPromoApplied = false;
    private boolean isPaymentDone = false;

    private boolean databaseExist = false;

    private Account_Information account_information;
    private Shop_Information shop_information;

    String Payment_log = "PaymentResponse";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        btnApplyPromo = findViewById(R.id.btn_apply_promo_reg_pay);
        eT_promoCode = findViewById(R.id.edit_promo);
        tV_registrationCost = findViewById(R.id.tV_pay_service_charge);
        tV_gstCost = findViewById(R.id.tV_pay_gst);
        tV_savingsCost = findViewById(R.id.tV_pay_savings);
        tV_totalAmount = findViewById(R.id.tV_pay_total);

        btnPayNow = findViewById(R.id.btn_pay_reg_pay);
        shop_information = (Shop_Information) getIntent().getSerializableExtra("shop_information");
        account_information = (Account_Information) getIntent().getSerializableExtra("account_information");

        orderIdString = getOrderID();
        CALLBACK_URL = CALLBACK_URL + orderIdString;
        custIdString = getCustomerID();


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabase = firebaseDatabase.getReference();
        btnApplyPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = eT_promoCode.getText().toString().toUpperCase().trim();
                mDatabase.child("Merchant_data").child("BDInterns").child(code).child("name").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(@NonNull @NotNull DataSnapshot dataSnapshot) {
                        try {
                            if (!dataSnapshot.getValue().toString().isEmpty()) {
                                Toast.makeText(paymentActivity.this, "promo code applied Successfully", Toast.LENGTH_SHORT).show();
                                isPromoApplied = true;
                                Log.v(RegistrationPaymentActivity.class.getSimpleName(), "promo");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        btnApplyPromo.setClickable(false);
                                        btnApplyPromo.setFocusable(false);

                                        isPromoApplied = true;
                                        tV_savingsCost.setText("Rs.30");
                                        tV_totalAmount.setText("Rs.86.82");

                                        Log.v(RegistrationPaymentActivity.class.getSimpleName(), "promo done");
                                    }
                                });
//                                account_information.setIntern(null);
                                mDatabase.child("Merchant_data").child("BDInterns").child(code).child("Providers").child(account_information.getPhoneNumber()).child("account_information").setValue(account_information);
                                mDatabase.child("Merchant_data").child("BDInterns").child(code).child("Providers").child(account_information.getPhoneNumber()).child("payment_done").setValue(isPaymentDone);
                                //todo update this node when payment is done.

                                account_information.setSalesCode(dataSnapshot.getValue().toString());


                            }
                        } catch (Exception e) {
                            Toast.makeText(paymentActivity.this, "Some problem Occurred...", Toast.LENGTH_SHORT).show();
                            Log.e("Bd_Intern", e.toString());
                        }
                    }
                });
            }
        });

        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PaytmPGService Service = PaytmPGService.getProductionService();
//                txnTokenString = getToken();
                HashMap<String, String> paramMap = new HashMap<String, String>();
                paramMap.put("mid", MERCHANT_ID);
                paramMap.put("orderid", orderIdString);
                paramMap.put("amount", "100");//tV_totalAmount.getText().toString());
                paramMap.put("callbackurl", CALLBACK_URL);
                paramMap.put("callbackurl", checkSumHash);

                try {
//                    getTokenUpdate();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Token","Error occured while generating transaction token");
                }


                PaytmOrder paytmOrder = new PaytmOrder(orderIdString, MERCHANT_ID, checkSumHash, "100", CALLBACK_URL);


                TransactionManager transactionManager = new TransactionManager(paytmOrder, new PaytmPaymentTransactionCallback() {
                    @Override
                    public void onTransactionResponse(@Nullable @org.jetbrains.annotations.Nullable Bundle bundle) {
                        Log.d(Payment_log, bundle.toString());
                    }

                    @Override
                    public void networkNotAvailable() {
                        Log.d(Payment_log, "network Error");
                    }

                    @Override
                    public void onErrorProceed(String s) {
                        Log.d(Payment_log, s);
                    }

                    @Override
                    public void clientAuthenticationFailed(String s) {
                        Log.d(Payment_log, s);
                    }

                    @Override
                    public void someUIErrorOccurred(String s) {
                        Log.d(Payment_log, s);
                    }

                    @Override
                    public void onErrorLoadingWebPage(int i, String s, String s1) {
                        Log.d(Payment_log, s);
                    }

                    @Override
                    public void onBackPressedCancelTransaction() {
                        Log.d(Payment_log, "cancel pressed");
                    }

                    @Override
                    public void onTransactionCancel(String s, Bundle bundle) {
                        Log.d(Payment_log, s);
                    }
                }); // code statement);

            }
        });
    }

    private String getOrderID() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
        String date = df.format(c.getTime());
        Random rand = new Random();
        int min = 1000, max = 9999; // nextInt as provided by Random is exclusive of the top value so you need to add 1
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return date + String.valueOf(randomNum);
    }


    private String getTokenUpdate() throws Exception {
        JSONObject paytmParams = new JSONObject();

        JSONObject body = new JSONObject();
        body.put("requestType", "Payment");
        body.put("mid", MERCHANT_ID);//"pryhXx26872540524198");
        body.put("websiteName", "DEFAULT");
        body.put("orderId", orderIdString);
        body.put("callbackUrl", CALLBACK_URL);

        JSONObject txnAmount = new JSONObject();
        txnAmount.put("value", "1.00");
        txnAmount.put("currency", "INR");

        JSONObject userInfo = new JSONObject();
        userInfo.put("custId", "CUST_001");
        body.put("txnAmount", txnAmount);
        body.put("userInfo", userInfo);

        /*
         * Generate checksum by parameters we have in body
         * You can get Checksum JAR from https://developer.paytm.com/docs/checksum/
         * Find your Merchant Key in your Paytm Dashboard at https://dashboard.paytm.com/next/apikeys
         */

//        String checksum = PaytmChecksum.generateSignature(body.toString(), "BsFy0OVK4KZl8_yZ");

        String checksum = getChecksum();

        JSONObject head = new JSONObject();
        head.put("signature", checksum);

        paytmParams.put("body", body);
        paytmParams.put("head", head);

        String post_data = paytmParams.toString();

        /* for Staging */
//        URL url = new URL("https://securegw-stage.paytm.in/theia/api/v1/initiateTransaction?mid=pryhXx26872540524198&orderId=ORDERID_98765");

        /* for Production */
        URL url = new URL("https://securegw.paytm.in/theia/api/v1/initiateTransaction?mid="+MERCHANT_ID+"&orderId="+orderIdString);

        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            DataOutputStream requestWriter = new DataOutputStream(connection.getOutputStream());
            requestWriter.writeBytes(post_data);
            requestWriter.close();
            String responseData = "";
            InputStream is = connection.getInputStream();
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
            if ((responseData = responseReader.readLine()) != null) {
                System.out.append("Response: " + responseData);
                Log.d("token",responseData);
            }
            responseReader.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return "done";
    }


    private String getToken() {

        JSONObject paytmParams = new JSONObject();

        JSONObject body = new JSONObject();
        JSONObject txnAmount = new JSONObject();
        JSONObject userInfo = new JSONObject();

        try {
            body.put("requestType", "Payment");
            body.put("mid", MERCHANT_ID);
            body.put("websiteName", WEBSITE);
            body.put("orderId", orderIdString);
            body.put("callbackUrl", "https://");

            txnAmount.put("value","100" );//txnAmountString);
            txnAmount.put("currency", "INR");

            userInfo.put("custId", custIdString);
            body.put("txnAmount", txnAmount);
            body.put("userInfo", userInfo);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*
         * Generate checksum by parameters we have in body
         * You can get Checksum JAR from https://developer.paytm.com/docs/checksum/
         * Find your Merchant Key in your Paytm Dashboard at https://dashboard.paytm.com/next/apikeys
         */

        String checksum = getChecksum();

        JSONObject head = new JSONObject();
        try {
            head.put("signature", checksum);
            paytmParams.put("body", body);
            paytmParams.put("head", head);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String post_data = paytmParams.toString();

        /* for Staging */
        URL url = null;
        try {
            url = new URL("https://securegw.paytm.in/theia/api/v1/initiateTransaction?mid=" + MERCHANT_ID + "&orderId=" + orderIdString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        /* for Production */
        // URL url = new URL("https://securegw.paytm.in/theia/api/v1/initiateTransaction?mid=YOUR_MID_HERE&orderId=ORDERID_98765");

        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            DataOutputStream requestWriter = new DataOutputStream(connection.getOutputStream());
            requestWriter.writeBytes(post_data);
            requestWriter.close();
            String responseData = "";
            InputStream is = connection.getInputStream();
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
            if ((responseData = responseReader.readLine()) != null) {
                System.out.append("Response: " + responseData);
                Log.e("Response", responseData);
            }
            responseReader.close();
            JSONObject txnTokenJSON = new JSONObject(responseData);
            txnTokenString = txnTokenJSON.getString("txnToken");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return txnTokenString;
    }

    private String getChecksum() {
        OkHttpClient client = new OkHttpClient();


        HttpUrl.Builder urlBuilder = HttpUrl.parse(CHECKSUM_URL).newBuilder();
        urlBuilder.addQueryParameter("oId", orderIdString);
        urlBuilder.addQueryParameter("custId", custIdString);
        urlBuilder.addQueryParameter("amount", txnAmountString);
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                String result = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject checksumJSON = jsonObject.getJSONObject("checksum");
                    checkSumHash = checksumJSON.getString("CHECKSUMHASH");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return checkSumHash;
    }

    private String getCustomerID() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MMddyyyy");
        String date = df.format(c.getTime());
        Random rand = new Random();
        int min = 1000, max = 9999; // nextInt as provided by Random is exclusive of the top value so you need to add 1
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return date + String.valueOf(randomNum);
    }

    private void showPaymentSuccessDialog() {
        final Dialog dialog = new Dialog(paymentActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_payment_info);
        TextView paymentActivated, paymentDate, paymentTime;
        ImageView successIcon, succcessIconShadow, backgroundGradient, backBtn;
        Button next;

        paymentActivated = dialog.findViewById(R.id.tV_payment_activated);
        paymentDate = dialog.findViewById(R.id.tV_pay_date);
        backBtn = dialog.findViewById(R.id.iV_back);
        paymentTime = dialog.findViewById(R.id.tV_pay_time);
        successIcon = dialog.findViewById(R.id.iV_icon);
        succcessIconShadow = dialog.findViewById(R.id.iV_icon_shadow);
        backgroundGradient = dialog.findViewById(R.id.iV_payment_gradient);
        next = dialog.findViewById(R.id.btn_Next);

        backBtn.setVisibility(View.GONE);
        paymentTime.setText(new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date()));
        paymentDate.setText(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));
        next.setText("Jump to Home");
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(paymentActivity.this, Home.class);
                startActivity(i);
            }
        });
        dialog.show();
    }

    private void showPaymentFailureDialog() {
        final Dialog dialog = new Dialog(paymentActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_payment_info);
        TextView paymentActivated, paymentDate, paymentTime, title;
        ImageView successIcon, succcessIconShadow, backgroundGradient, backBtn;
        Button next;
        CardView failureIcon, failureShadow;

        paymentActivated = dialog.findViewById(R.id.tV_payment_activated);
        paymentDate = dialog.findViewById(R.id.tV_pay_date);
        backBtn = dialog.findViewById(R.id.iV_back);

        paymentTime = dialog.findViewById(R.id.tV_pay_time);
        successIcon = dialog.findViewById(R.id.iV_icon);
        succcessIconShadow = dialog.findViewById(R.id.iV_icon_shadow);
        backgroundGradient = dialog.findViewById(R.id.iV_payment_gradient);
        next = dialog.findViewById(R.id.btn_Next);
        title = dialog.findViewById(R.id.tV_title);
        failureIcon = dialog.findViewById(R.id.cardView_failIcon);
        failureShadow = dialog.findViewById(R.id.cardView_failIcon_shadow);

        failureIcon.setVisibility(View.VISIBLE);
        failureShadow.setVisibility(View.VISIBLE);

        title.setText("Payment failed for\nGASTOS PROVIDER CLUB");
        paymentActivated.setText("Failed");
        successIcon.setVisibility(View.GONE);
        succcessIconShadow.setVisibility(View.GONE);
        backgroundGradient.setImageResource(R.drawable.payment_failure_gradient);
        paymentTime.setText(new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date()));
        paymentDate.setText(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));
        next.setText("Close");
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.show();
    }


}