package com.cu.gastosmerchant1.settings;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cu.gastosmerchant1.Dashboard.Home;
import com.cu.gastosmerchant1.R;
import com.cu.gastosmerchant1.details.CashFreeData;
import com.cu.gastosmerchant1.registration.CashFree_Activity;
import com.cu.gastosmerchant1.settings.postAds.ad_payment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class payment_status extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private final String MyPrefs = "MY_PREFERENCE";
    private final String REG_PAYMENT = "REG_PAYMENT";
    String city,category,reach,start_age,end_age;
    String type,content,sending_shop_name;

    final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
    private Timestamp timestamp;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    private AppCompatButton jumpToHome;
    private RelativeLayout relativeLayout;
    private ImageButton info_btn;
    private TextView payment_status, date, time, service_amount, savings, retry, activateTv;
    private ImageView payment_status_img;
    private TextView gst, amountTv;
    DatabaseReference promotion_reference;
    private static final int PAYMENT_SUCCESS = 1;
    private static final int PAYMENT_FAILED = -1;
    private String phoneNumber;
    private String email;
    private String orderId;
    private String amount;
    private int paymentStatus;
    // TODO -> initialize these values when received from bundle intent
    private String str_date, str_time;
    private CashFreeData cashFreeData;
    double balance,dis,wallet_value,savings_amt;
    double total_charge;
    ProgressDialog pd;
    int total_coustomer;
    boolean applied;
    FirebaseAuth auth;
    FirebaseUser user;
    String selectedFileUri,file_link;
    String check_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_status2);

        //getIntent
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        check_intent=getIntent().getStringExtra("sending_from_payment_gateway");
        email = getIntent().getStringExtra("email");
        amount = getIntent().getStringExtra("amount");
        orderId = getIntent().getStringExtra("orderId");
        balance=getIntent().getDoubleExtra("balance", 0.0);
        dis=getIntent().getDoubleExtra("dis", 0.0);

        type=getIntent().getStringExtra("sending_type");
        wallet_value=getIntent().getDoubleExtra("sending_wallet_amount",0.0);
        content=getIntent().getStringExtra("sending_content");
        total_charge=getIntent().getDoubleExtra("amt_of_customers",2000.00);
        total_coustomer=getIntent().getIntExtra("num_of_customers",4000);
        city=getIntent().getStringExtra("sending_city");
        category=getIntent().getStringExtra("sending_category");
        reach=getIntent().getStringExtra("sending_reach");
        start_age=getIntent().getStringExtra("sending_start_age");
        end_age=getIntent().getStringExtra("sending_end_age");
        sending_shop_name=getIntent().getStringExtra("sending_shop_name");
        applied=getIntent().getBooleanExtra("applied",false);
        savings_amt=getIntent().getDoubleExtra("sending_savings",0.0);
        selectedFileUri=getIntent().getStringExtra("sending_file_uri");

        paymentStatus = getIntent().getIntExtra("transaction_status", 1);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        jumpToHome = findViewById(R.id.jumpToHome);

        gst = findViewById(R.id.gst);
        amountTv = findViewById(R.id.amounttv);
        activateTv = findViewById(R.id.activationTv);
        relativeLayout = findViewById(R.id.relativeLayout);
        info_btn = findViewById(R.id.info);
        payment_status = findViewById(R.id.payment_msg);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        service_amount = findViewById(R.id.service_charge);
        savings = findViewById(R.id.savings);
        retry = findViewById(R.id.retry);
        payment_status_img = findViewById(R.id.payment_status_img);

        timestamp = new Timestamp(System.currentTimeMillis());


        if(check_intent.equals("ad_payment")){
            set_success();
        }
        //make the node here
        if(paymentStatus==0)
        {
            cashFreeData = new CashFreeData(orderId, amount, "TXN_SUCCESS", sdf1.format(timestamp));

        } else {
            cashFreeData = new CashFreeData(orderId, amount, "TXN_FAILURE", sdf1.format(timestamp));
        }

        promotion_reference=FirebaseDatabase.getInstance().getReference().child("Promotion_Data");

        jumpToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(payment_status.this, Home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        Log.v("amount is ",amount+"");

        if (cashFreeData.getTransaction_status().equals("TXN_SUCCESS") && check_intent.equals("gateway")) {
            //success
            setLayoutElements(PAYMENT_SUCCESS);
        } else if(check_intent.equals("gateway")) {
            //failure
            setLayoutElements(PAYMENT_FAILED);
        }

        // TODO -> initialize values from bundle intent and call setLayoutElements function

    }

    private void set_success() {
        Format f = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
        str_time = f.format(new Date());

        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        str_date = formatter.format(new Date());

        service_amount.setText(String.valueOf("\u20B9 " + total_charge));
        amountTv.setText(String.valueOf("\u20B9 " + (total_charge)));//total_charge+dis if gst applicable.

        date.setText(str_date);

        time.setText(str_time);

        relativeLayout.setBackgroundResource(R.drawable.green_gradient);
        info_btn.setVisibility(View.GONE);
        payment_status.setText(R.string.text_page);
        payment_status_img.setImageResource(R.drawable.pymnt_succs);
        savings.setText("-"+savings_amt);
        if(applied)
            gst.setText("\u20B9 0");
        else
            gst.setText("\u20B9 " + dis);
        activateTv.setText("Payment Successful");

        sharedPreferences = getSharedPreferences(MyPrefs, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean(REG_PAYMENT, true);

        retry.setVisibility(View.GONE);
    }

    private void setLayoutElements(int payment_Status) {
        Format f = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
        str_time = f.format(new Date());

        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        str_date = formatter.format(new Date());

        service_amount.setText(String.valueOf("\u20B9 " + total_charge));
        amountTv.setText(String.valueOf("\u20B9 " + (total_charge+dis)));

        date.setText(str_date);

        time.setText(str_time);

        //TODO : push data
        //databaseReference.child("Merchant_data").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Registration_Payment_Info").setValue(cashFreeData);

        if (payment_Status == PAYMENT_FAILED) {
            // Payment Failed
            relativeLayout.setBackgroundResource(R.drawable.red_gradient);
            info_btn.setVisibility(View.GONE);
            payment_status.setText(R.string.bill_payment_failed);
            if(applied)
                savings.setText("-"+savings_amt);
            else
                savings.setText("-");
            if(applied)
                gst.setText("\u20B9 0");
            else
                gst.setText("\u20B9 " + dis);
            payment_status_img.setImageResource(R.drawable.pymnt_failed);
            jumpToHome.setVisibility(View.GONE);
            activateTv.setText("Failed");

            sharedPreferences = getSharedPreferences(MyPrefs, MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.putBoolean(REG_PAYMENT, false);


            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.e("shopName",sending_shop_name);
                    Log.e("Type",type);
                    Log.e("walletValue",wallet_value+"");
                    Log.e("content",content);
                    Log.e("total_charge",total_charge+"");
                    Log.e("city",city);
                    Log.e("category",category);
                    Log.e("reach",reach);
                    Log.e("start_Age",start_age);
                    Log.e("end_age",end_age);
                    Log.e("total_customer",total_coustomer+"");

                    Intent intent = new Intent(payment_status.this, CashFree_Activity.class);
                    intent.putExtra("phoneNumber", phoneNumber);
                    intent.putExtra("email", email);
                    intent.putExtra("amount", amount);
                    intent.putExtra("change_Screen", 1);

                    intent.putExtra("sending_shop_name", sending_shop_name);

                    intent.putExtra("sending_type", type);
                    intent.putExtra("sending_wallet_amount", wallet_value);
                    intent.putExtra("sending_content", content);
                    intent.putExtra("amt_of_customers", total_charge);
                    intent.putExtra("sending_city", city);
                    intent.putExtra("sending_category", category);
                    intent.putExtra("sending_reach", reach);
                    intent.putExtra("sending_start_age", start_age);
                    intent.putExtra("sending_end_age", end_age);
                    intent.putExtra("num_of_customers", total_coustomer);
                    intent.putExtra("applied", applied);
                    intent.putExtra("sending_savings", savings_amt);

                    intent.putExtra("sending_prev_balance", balance);
                    intent.putExtra("sending_discount", dis);
                    startActivity(intent);
                    finish();
                }
            });

        } else if (payment_Status == PAYMENT_SUCCESS) {
            // Payment Success
            relativeLayout.setBackgroundResource(R.drawable.green_gradient);
            info_btn.setVisibility(View.GONE);
            payment_status.setText(R.string.text_page);
            payment_status_img.setImageResource(R.drawable.pymnt_succs);
            if(applied)
                savings.setText("-"+savings_amt);
            else
                savings.setText("-");
            if(applied)
                gst.setText("\u20B9 0");
            else
                gst.setText("\u20B9 " + dis);
            activateTv.setText("Payment Successful");

            sharedPreferences = getSharedPreferences(MyPrefs, MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.putBoolean(REG_PAYMENT, true);

            retry.setVisibility(View.GONE);

            String pushkey = user.getUid() + promotion_reference.push().getKey();
            promotion_reference.child(pushkey).child("Registration_Payment_Info").setValue(cashFreeData);
            promotion_reference.child(pushkey).child("amount").setValue(String.valueOf(amount));
            promotion_reference.child(pushkey).child("phoneNumber").setValue(phoneNumber);
            promotion_reference.child(pushkey).child("email").setValue(email);
            promotion_reference.child(pushkey).child("type").setValue(type);
            promotion_reference.child(pushkey).child("date").setValue(str_date);
            promotion_reference.child(pushkey).child("time").setValue(str_time);
            promotion_reference.child(pushkey).child("timestamp").setValue(timestamp.getTime());
            promotion_reference.child(pushkey).child("status").setValue("false");
            switch (content) {
                case "1":
                    promotion_reference.child(pushkey).child("content").child("discount").setValue("10");
                    promotion_reference.child(pushkey).child("content").child("min_order").setValue("1000");
                    promotion_reference.child(pushkey).child("content").child("shop_name").setValue(sending_shop_name);
                    break;
                case "2":
                    promotion_reference.child(pushkey).child("content").child("discount").setValue("20");
                    promotion_reference.child(pushkey).child("content").child("min_order").setValue("3000");
                    promotion_reference.child(pushkey).child("content").child("shop_name").setValue(sending_shop_name);
                    break;
                case "3":
                    promotion_reference.child(pushkey).child("content").child("discount").setValue("30");
                    promotion_reference.child(pushkey).child("content").child("min_order").setValue("10000");
                    promotion_reference.child(pushkey).child("content").child("shop_name").setValue(sending_shop_name);
                    break;
                default:
                    promotion_reference.child(pushkey).child("content").setValue(content);
                    break;
            }
            promotion_reference.child(pushkey).child("total_charge").setValue(total_charge);
            promotion_reference.child(pushkey).child("city").setValue(city);
            promotion_reference.child(pushkey).child("category").setValue(category);
            promotion_reference.child(pushkey).child("reach").setValue(reach);
            promotion_reference.child(pushkey).child("start_age").setValue(start_age);
            promotion_reference.child(pushkey).child("end_age").setValue(end_age);
            promotion_reference.child(pushkey).child("total_customer").setValue(total_coustomer);
            promotion_reference.child(pushkey).child("key").setValue(pushkey);
            promotion_reference.child(pushkey).child("uid").setValue(user.getUid());
            if(selectedFileUri!=null)
                upload_file(selectedFileUri,pushkey);
            pd.setTitle("Sending Confirmation");
            pd.setMessage("Please Wait!");
            String api_str = "https://cashfree-server-production.up.railway.app/api/welcome-message";
            httpCall(api_str, phoneNumber);
            /*Welcome to Gastos Provider Club!
                    Now you can attract customers as
            your profile is now active and you can avail our Promotional Service*/
        }
    }
    private void upload_file(String selectedImageUri, String pushkey) {

        pd = new ProgressDialog(this);
        pd.setTitle("WhatsApp File");
        pd.setMessage("Uploading Please Wait!");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();

        String pdfstamp = user.getUid()+pushkey;
        String pdfpath = "WhatsappFiles/";
        StorageReference storageReference1 = FirebaseStorage.getInstance().getReference().child(pdfpath);
        final StorageReference filepath = storageReference1.child(pdfstamp);
        filepath.putFile(Uri.parse(selectedImageUri))
                .addOnSuccessListener(taskSnapshot1 ->
                        taskSnapshot1.getStorage().getDownloadUrl().addOnCompleteListener(
                                task1 -> {
                                    file_link = Objects.requireNonNull(task1.getResult()).toString();
                                    promotion_reference.child(pushkey).child("file_link").setValue(file_link);
                                }));
    }
    public void httpCall(String url,String number) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("phoneNumber",number);
            jsonBody.put("textOne","new audience");
            jsonBody.put("textTwo","Services");
            Log.d("body", "httpCall_collect: " + jsonBody);
        } catch (Exception e) {
            Log.e("Error", "JSON ERROR");
        }

        RequestQueue queue = Volley.newRequestQueue(payment_status.this);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // enjoy your response
                        if(!payment_status.this.isFinishing()) {
                            pd.dismiss();
                        }
                        Log.e("response", ""+response);
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // enjoy your error status
                Log.e("Status of code = ", "Wrong");
            }
        });
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 15000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 15000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
            }
        });
        Log.d("string", stringRequest.toString());
        queue.add(stringRequest);
    }
}