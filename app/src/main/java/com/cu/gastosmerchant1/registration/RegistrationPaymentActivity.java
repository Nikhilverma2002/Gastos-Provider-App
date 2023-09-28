package com.cu.gastosmerchant1.registration;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.cu.gastosmerchant1.Dashboard.Home;
import com.cu.gastosmerchant1.R;
import com.cu.gastosmerchant1.details.Account_Information;
import com.cu.gastosmerchant1.details.Payment_Information;
import com.cu.gastosmerchant1.details.Shop_Information;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import dev.shreyaspatil.easyupipayment.EasyUpiPayment;
import dev.shreyaspatil.easyupipayment.listener.PaymentStatusListener;
import dev.shreyaspatil.easyupipayment.model.PaymentApp;
import dev.shreyaspatil.easyupipayment.model.TransactionDetails;

public class RegistrationPaymentActivity extends AppCompatActivity implements PaymentResultListener, PaymentStatusListener {
    Button payBtn;
    TextView payAmt;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    TextView totalAmount, promoAmount;
    double amount = 0;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private final String MyPrefs = "MY_PREFERENCE";
    private final String REG_PAYMENT = "REG_PAYMENT";
    private final String NAME = "MY_NAME";

    private EasyUpiPayment easyUpiPayment;

    String rawString = "upi://pay?pa=paytmqr281005050101mm617cyacrl1@paytm&pn=Paytm Merchant&mc=5499&mode=02&orgid=000000&paytmqr=281005050101MM617CYACRL1&sign=MEQCID0NFi3MYLXf8Yqjqwp7AqyIM7K0nlnQNBmke8X6Ou0fAiBErCzcP25K2wUYvXyt8nJG2OOqoDEAYyVkFKVjhloZYQ==";
    String upiId = "paytmqr281005050101mm617cyacrl1@paytm";
    String merchantId = "JsuLrn83183937545946";
    String accountName = "SAHGAL KUMAR";
    //todo check the merchant id
    //todo must be allowed to proceed without payment


    String selected = "";
    CardView debit, credit, net, phonepe, paytm, googlepay, bhim;

    private DatabaseReference mDatabase;

    private Account_Information account_information;
    private Shop_Information shop_information;
    private ArrayList<Payment_Information> upiArrayList;


    private Button promoButton;
    private EditText promoCode;
    private boolean isPromoApplied = false;
    private boolean isPaymentDone = false;

    private boolean databaseExist = false;

    CardView phonepe_selected, googlepay_selected, bhim_selected, paytm_selected, debit_selected, credit_selected, net_selected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_payment);

        setUpViews();

        shop_information = (Shop_Information) getIntent().getSerializableExtra("shop_information");
        account_information = (Account_Information) getIntent().getSerializableExtra("account_information");
        upiArrayList = (ArrayList<Payment_Information>) getIntent().getSerializableExtra("upiArrayList");

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();

        mDatabase = firebaseDatabase.getReference();


        promoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RegistrationPaymentActivity.this, "here", Toast.LENGTH_SHORT).show();
                String code = promoCode.getText().toString().toUpperCase().trim();
                mDatabase.child("Merchant_data").child("BDInterns").child(code).child("name").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(@NonNull @NotNull DataSnapshot dataSnapshot) {
                        try {
                            if (!dataSnapshot.getValue().toString().isEmpty()) {
                                Toast.makeText(RegistrationPaymentActivity.this, "promo code applied Successfully", Toast.LENGTH_SHORT).show();
                                isPromoApplied = true;
                                Log.v(RegistrationPaymentActivity.class.getSimpleName(), "promo");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        promoButton.setClickable(false);
                                        promoCode.setFocusable(false);
                                        applyPromo();
                                        Log.v(RegistrationPaymentActivity.class.getSimpleName(), "promo done");
                                    }
                                });
//                                account_information.setIntern(null);
                                account_information.setRegistrationPaymentDone(false);
                                Log.v(RegistrationPaymentActivity.class.getSimpleName(), "intern 1");
                                mDatabase.child("Merchant_data").child("BDInterns").child(code).child("Providers").child(account_information.getPhoneNumber()).child("account_information").setValue(account_information);
                                Log.v(RegistrationPaymentActivity.class.getSimpleName(), "intern 2");
                                mDatabase.child("Merchant_data").child("BDInterns").child(code).child("Providers").child(account_information.getPhoneNumber()).child("payment_done").setValue(isPaymentDone);
                                //todo update this node when payment is done.
                                Log.v(RegistrationPaymentActivity.class.getSimpleName(), "intern 3");
                                account_information.setSalesCode(dataSnapshot.getValue().toString());
                                Log.v(RegistrationPaymentActivity.class.getSimpleName(), "setIntern");
                                uploadToDatabase();
                            }
                        } catch (Exception e) {
                            Toast.makeText(RegistrationPaymentActivity.this, "Some problem Occurred...", Toast.LENGTH_SHORT).show();
                            Log.e("Bd_Intern", e.toString());
                        }
                    }
                });
            }
        });


        debit.setOnClickListener(v -> {
            selected = "debit";
            debit_selected.setVisibility(View.VISIBLE);
            credit_selected.setVisibility(View.GONE);
            net_selected.setVisibility(View.GONE);
            bhim_selected.setVisibility(View.GONE);
            googlepay_selected.setVisibility(View.GONE);
            paytm_selected.setVisibility(View.GONE);
            phonepe_selected.setVisibility(View.GONE);
        });

        credit.setOnClickListener(v -> {
            selected = "credit";

            debit_selected.setVisibility(View.GONE);
            credit_selected.setVisibility(View.VISIBLE);
            net_selected.setVisibility(View.GONE);
            bhim_selected.setVisibility(View.GONE);
            googlepay_selected.setVisibility(View.GONE);
            paytm_selected.setVisibility(View.GONE);
            phonepe_selected.setVisibility(View.GONE);
        });

        net.setOnClickListener(v -> {
            selected = "net";

            debit_selected.setVisibility(View.GONE);
            credit_selected.setVisibility(View.GONE);
            net_selected.setVisibility(View.VISIBLE);
            bhim_selected.setVisibility(View.GONE);
            googlepay_selected.setVisibility(View.GONE);
            paytm_selected.setVisibility(View.GONE);
            phonepe_selected.setVisibility(View.GONE);
        });

        phonepe.setOnClickListener(v -> {
            selected = "phonepe";

            debit_selected.setVisibility(View.GONE);
            credit_selected.setVisibility(View.GONE);
            net_selected.setVisibility(View.GONE);
            bhim_selected.setVisibility(View.GONE);
            googlepay_selected.setVisibility(View.GONE);
            paytm_selected.setVisibility(View.GONE);
            phonepe_selected.setVisibility(View.VISIBLE);
        });

        googlepay.setOnClickListener(v -> {
            selected = "googlepay";

            debit_selected.setVisibility(View.GONE);
            credit_selected.setVisibility(View.GONE);
            net_selected.setVisibility(View.GONE);
            bhim_selected.setVisibility(View.GONE);
            googlepay_selected.setVisibility(View.VISIBLE);
            paytm_selected.setVisibility(View.GONE);
            phonepe_selected.setVisibility(View.GONE);
        });

        bhim.setOnClickListener(v -> {
            selected = "bhim";

            debit_selected.setVisibility(View.GONE);
            credit_selected.setVisibility(View.GONE);
            net_selected.setVisibility(View.GONE);
            bhim_selected.setVisibility(View.VISIBLE);
            googlepay_selected.setVisibility(View.GONE);
            paytm_selected.setVisibility(View.GONE);
            phonepe_selected.setVisibility(View.GONE);
        });

        paytm.setOnClickListener(v -> {
            selected = "paytm";

            debit_selected.setVisibility(View.GONE);
            credit_selected.setVisibility(View.GONE);
            net_selected.setVisibility(View.GONE);
            bhim_selected.setVisibility(View.GONE);
            googlepay_selected.setVisibility(View.GONE);
            paytm_selected.setVisibility(View.VISIBLE);
            phonepe_selected.setVisibility(View.GONE);
        });

        payBtn.setOnClickListener(v -> {
            //TODO: All upi
            if (selected.equals("paytm")) {
                upiPay(PaymentApp.PAYTM);
            } else if (selected.equals("googlepay")) {
                upiPay(PaymentApp.GOOGLE_PAY);
            } else if (selected.equals("bhim")) {
                upiPay(PaymentApp.BHIM_UPI);
            } else if (selected.equals("phonepe")) {
                upiPay(PaymentApp.PHONE_PE);
            } else if (selected.equals("debit") || selected.equals("credit") || selected.equals("net")) {
                Checkout checkout = new Checkout();
                checkout.setKeyID("rzp_test_nqwqU4WLEiDaHr");
                checkout.setImage(R.drawable.logo);
                JSONObject object = new JSONObject();
                try {
                    // to put name
                    object.put("name", "Registration Payment");

                    // put description
                    object.put("description", "Welcome Pack\nRegistration Cost\tRs.99.00\nGST(18%)\tRs.17.82\nTotal Amount\tRs.116.82");

                    // to set theme color
                    object.put("theme.color", R.color.gastos);

                    // put the currency
                    object.put("currency", "INR");

                    // put amount
                    object.put("amount", amount);

                    // open razorpay to checkout activity
                    checkout.open(RegistrationPaymentActivity.this, object);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private String getTransactionId() {
        String transactionId = "TID" + System.currentTimeMillis();
        return transactionId;
    }

    private void upiPay(PaymentApp app) {

        String transactionId = getTransactionId();
        EasyUpiPayment.Builder builder = new EasyUpiPayment.Builder(this)
                .with(app)
                .setPayeeVpa(upiId)
                .setPayeeName(accountName)
                .setTransactionId(transactionId)
                .setTransactionRefId(transactionId)
                .setPayeeMerchantCode(merchantId)
                .setDescription("Payment for Registration with GASTOS")
                .setAmount(String.valueOf(amount));

        try {
            easyUpiPayment = builder.build();
            easyUpiPayment.setPaymentStatusListener(this);
            easyUpiPayment.startPayment();
        } catch (Exception exception) {
            exception.printStackTrace();
            Toast.makeText(this, "Error:- " + exception.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    boolean checkIfPresent() {
        mDatabase.child("Merchant_data").child(Objects.requireNonNull(firebaseAuth.getUid())).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    databaseExist = true;
                    return;
                }
            }
        });
        return databaseExist;
    }

    void uploadToDatabase() {
        if (checkIfPresent()) {
            return;
        }
        Toast.makeText(RegistrationPaymentActivity.this, "Uploading Data...", Toast.LENGTH_SHORT).show();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(firebaseAuth.getUid() + "_" + account_information.getPhoneNumber()).child("Shop_Image");
        sharedPreferences = getSharedPreferences(MyPrefs, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(NAME, shop_information.getShopName());
        editor.apply();

        Uri uri = Uri.parse(shop_information.getShopImageUri());
        storageReference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.v(AddQR.class.getSimpleName(), "uri is " + uri.toString());
                            Toast.makeText(RegistrationPaymentActivity.this, uri.toString(), Toast.LENGTH_SHORT).show();
                            shop_information.setShopImageUri(uri.toString());
                            mDatabase.child("Merchant_data").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Account_Information").setValue(account_information);
                            mDatabase.child("Merchant_data").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Shop_Information").setValue(shop_information);
                            mDatabase.child("Merchant_data").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Payment_Information").setValue(upiArrayList);
                        }
                    });
                }
            }
        });
    }

    void applyPromo() {
        //amount change happens here
        amount = 81.42;
        totalAmount.setText("Rs.81.42");
        isPromoApplied = true;
        promoAmount.setText("Rs.30");
        payAmt.setText("Rs.81.42");
    }

    private void setUpViews() {
        promoAmount = findViewById(R.id.tV_pay_savings);
        totalAmount = findViewById(R.id.tV_pay_total);

        promoButton = findViewById(R.id.btn_apply_promo_reg_pay);
        promoCode = findViewById(R.id.edit_promo);
        phonepe_selected = findViewById(R.id.phonepe_selected);
        paytm_selected = findViewById(R.id.paytm_selected);
        googlepay_selected = findViewById(R.id.googlepay_selected);
        bhim_selected = findViewById(R.id.bhim_selected);
        debit_selected = findViewById(R.id.debit_selected);
        credit_selected = findViewById(R.id.credit_selected);
        net_selected = findViewById(R.id.net_selected);
        payAmt = findViewById(R.id.text_amount_reg_pay);
        payBtn = findViewById(R.id.btn_pay_reg_pay);

        phonepe = findViewById(R.id.card_phonepe);
        paytm = findViewById(R.id.card_paytm);
        googlepay = findViewById(R.id.card_googlepay);
        bhim = findViewById(R.id.card_bhim);
        credit = findViewById(R.id.card_credit);
        debit = findViewById(R.id.card_debit);
        net = findViewById(R.id.card_net);

    }

    @Override
    public void onPaymentSuccess(String s) {
        successDialog(s);

    }

    @Override
    public void onPaymentError(int i, String s) {
        failureDialog(i,s);

    }

    private void successDialog(String s)
    {
        //todo remove intents
        //todo handle intents
        mDatabase.child("Merchant_data").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Account_Information").child("isRegistrationPaymentDone").setValue(true);

        sharedPreferences=getSharedPreferences(MyPrefs,MODE_PRIVATE);
        editor=sharedPreferences.edit();
        editor.putBoolean(REG_PAYMENT,true);
        editor.apply();

        final Dialog dialog = new Dialog(RegistrationPaymentActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
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
                Toast.makeText(getApplicationContext(), "Payment Successful: " + s, Toast.LENGTH_LONG).show();
                Intent i = new Intent(RegistrationPaymentActivity.this, Home.class);
                startActivity(i);
            }
        });
        dialog.show();
    }

    private void failureDialog(int i,String s)
    {
        Toast.makeText(this, "Payment Error: " + s + "\n Please try again", Toast.LENGTH_SHORT).show();
        final Dialog dialog = new Dialog(RegistrationPaymentActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
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

    @Override
    public void onTransactionCancelled() {
        Toast.makeText(this, "Transaction cancelled Repay again", Toast.LENGTH_SHORT).show();
    }

    private void onTransactionSubmitted() {
        Toast.makeText(this, "Transaction Pending", Toast.LENGTH_SHORT).show();
    }

    private void onTransactionFailure() {
//        Toast.makeText(this, "Transaction Failed", Toast.LENGTH_SHORT).show();
        failureDialog(0,"change here");
        //todo change here
    }

    private void onTransactionSuccess() {
//        Toast.makeText(this, "Transaction Successful", Toast.LENGTH_SHORT).show();
        successDialog("change here");
    }

    @Override
    public void onTransactionCompleted(@NonNull TransactionDetails transactionDetails) {
        Toast.makeText(this, "Transaction Completed", Toast.LENGTH_SHORT).show();
        Log.v(RegistrationPaymentActivity.class.getSimpleName(), "Transaction details  :- " + transactionDetails);
        switch (transactionDetails.getTransactionStatus()) {
            case SUCCESS:
                onTransactionSuccess();
                break;
            case FAILURE:
                onTransactionFailure();
                break;
            case SUBMITTED:
                onTransactionSubmitted();
                break;
        }
    }
}