package com.cu.gastosmerchant1.registration;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cu.gastosmerchant1.PaymentStatus;
import com.cu.gastosmerchant1.R;
import com.cu.gastosmerchant1.details.Account_Information;
import com.cu.gastosmerchant1.details.Payment_Information;
import com.cu.gastosmerchant1.details.Shop_Information;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class RegistrationPayActivity extends AppCompatActivity {

    private TextView promoAmount, totalAmount, payAmt, promoAmountText, TotalAmountText, savingsCharge, serviceCharge, gstCharge;
    private Button promoButton, payBtn;
    private EditText promoCode;
    private ImageView promoBanner;
    private BottomSheetDialog sheetDialog;
    private CardView promoCardView;
    private TextView promoText;
    private LinearLayout mLinearDiscount;

    private String promoCodeString;
    private Account_Information account_information;
    private Shop_Information shop_information;
    private ArrayList<Payment_Information> upiArrayList;
    private boolean isPromoApplied = false;
    private double amount = 706.82;
    private boolean databaseExist = false;
    private boolean isPaymentDone = false;
    private String phoneNumber;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private final String MyPrefs = "MY_PREFERENCE";
    private final String NAME = "MY_NAME";
    private boolean userAgreement = false;
    private final int UPI_PAYMENT = 999;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;


    LinearLayout promo;
    CardView card_promo;
    TextView npPromocode;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_pay);

        if (ContextCompat.checkSelfPermission(RegistrationPayActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RegistrationPayActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
        }


        promo = findViewById(R.id.promo);
        back = findViewById(R.id.prev_btn_reg_pay);
        card_promo = findViewById(R.id.card_promo);
        npPromocode = findViewById(R.id.noPromo);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        npPromocode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                demo();
            }
        });
        if (!isConnectionAvailable(RegistrationPayActivity.this)) {
            Toast.makeText(this, "Internet not available", Toast.LENGTH_SHORT).show();
            return;
        }


        firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabase = firebaseDatabase.getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        userAgreement = getIntent().getBooleanExtra("userAgreement", false);

        if (userAgreement) {
            shop_information = (Shop_Information) getIntent().getSerializableExtra("shop_information");
            account_information = (Account_Information) getIntent().getSerializableExtra("account_information");
            upiArrayList = (ArrayList<Payment_Information>) getIntent().getSerializableExtra("upiArrayList");
            uploadToDatabase();
        } else {
            mDatabase.child("Merchant_data").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Account_Information").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    account_information = snapshot.getValue(Account_Information.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        setUpViews();

        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isConnectionAvailable(RegistrationPayActivity.this)) {
                    Toast.makeText(RegistrationPayActivity.this, "Internet not available", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isPromoApplied) {
                    //

                } else {
                    payBtn.setClickable(false);
                }
                if (userAgreement) {
                    Intent intent = new Intent(RegistrationPayActivity.this, CashFree_Activity.class);
                    intent.putExtra("amount", String.valueOf(amount));
                    intent.putExtra("isPromoApplied", isPromoApplied);
                    intent.putExtra("promoCode", promoCodeString);
                    intent.putExtra("phoneNumber", account_information.getPhoneNumber());
                    intent.putExtra("email", account_information.getEmailAddress());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    //intent to cash free
                    startActivity(intent);
                    return;
                }

                phoneNumber = account_information.getPhoneNumber();
                String email = account_information.getEmailAddress();
                Log.v("phoneNumber ", phoneNumber);
                //remember promo code is equivalent to discount
                Intent intent = new Intent(RegistrationPayActivity.this, CashFree_Activity.class);
                intent.putExtra("amount", String.valueOf(amount));
                intent.putExtra("isPromoApplied", isPromoApplied);
                intent.putExtra("promoCode", promoCodeString);
                intent.putExtra("phoneNumber", phoneNumber);
                intent.putExtra("email", email);
                //intent to cash free
                startActivity(intent);
            }
        });

        Log.v("uid is ", firebaseAuth.getUid());

        promoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promoCodeString = promoCode.getText().toString().toUpperCase().trim();

                if (promoCodeString.isEmpty()) {
                    Toast.makeText(RegistrationPayActivity.this, "No Promo entered", Toast.LENGTH_SHORT).show();
                    return;
                }

                mDatabase.child("Merchant_data").child("BDSales").child(promoCodeString).child("name").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(@NonNull @NotNull DataSnapshot dataSnapshot) {
                        try {
                            if (!dataSnapshot.getValue().toString().isEmpty()) {
                                Toast.makeText(RegistrationPayActivity.this, "promo code applied Successfully", Toast.LENGTH_SHORT).show();
                                isPromoApplied = true;
                                Log.v(RegistrationPayActivity.class.getSimpleName(), "promo");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        promoButton.setClickable(false);
                                        promoCode.setFocusable(false);
                                        applyPromo();
                                        Log.v(RegistrationPayActivity.class.getSimpleName(), "promo done");
                                        promoCardView.setVisibility(View.GONE);
                                        promoText.setVisibility(View.VISIBLE);
                                    }
                                });
//                                account_information.setIntern(null);
                                Account_Information account_information_intern = new Account_Information(account_information.getEmailAddress(), account_information.getOwnerName(), account_information.getPhoneNumber());
                                account_information.setRegistrationPaymentDone(false);
                                mDatabase.child("Merchant_data").child("BDSales").child(promoCodeString).child("Providers").child(account_information.getPhoneNumber()).child("account_information").setValue(account_information_intern);
//                                mDatabase.child("Merchant_data").child("BDSales").child(promoCodeString).child("Providers").child(account_information.getPhoneNumber()).child("payment_done").setValue(isPaymentDone);
                                account_information.setSalesCode(dataSnapshot.getValue().toString());

                            }
                        } catch (Exception e) {
                            Toast.makeText(RegistrationPayActivity.this, "No such code exists", Toast.LENGTH_SHORT).show();
                            Log.e("Bd_Intern", e.toString());
                        }
                    }
                });
            }
        });
    }

    void applyPromo() {
        //amount change happens here

        mLinearDiscount.setVisibility(View.VISIBLE);


        promoBanner.setImageResource(R.drawable.banner_ad_payment_discount);
        amount = 352.82;
        gstCharge.setText("Rs. 53.82");
        totalAmount.setText("Rs. 352.82");
        totalAmount.setVisibility(View.VISIBLE);
        isPromoApplied = true;
        promoAmount.setText("Rs.300");
        promoAmount.setVisibility(View.VISIBLE);
        payAmt.setText("Rs. 352.82");

        if (promoCodeString.equals("G0000")) {
            //change the textviews.
            promoAmount.setText("Rs. 0.00");
            serviceCharge.setText("Rs. 1.00");
            payAmt.setText("Rs. 1.00");
            totalAmount.setText("Rs. 1.00");
            gstCharge.setText("Rs. 0.00");
            amount = 1.00;
        }

        promoAmountText.setVisibility(View.VISIBLE);
        TotalAmountText.setVisibility(View.VISIBLE);
    }

    private void setUpViews() {

        gstCharge = findViewById(R.id.tV_pay_discount);
        promoAmount = findViewById(R.id.tV_pay_gst);
        totalAmount = findViewById(R.id.tV_pay_total);

        promoAmountText = findViewById(R.id.tV_payment_gst);
        TotalAmountText = findViewById(R.id.tV_payment_total);

        promoButton = findViewById(R.id.btn_apply_promo_reg_pay);
        promoCode = findViewById(R.id.edit_promo);
        payAmt = findViewById(R.id.text_amount_reg_pay);
        payAmt.setText("Rs. 706.82");
        gstCharge.setText("Rs. 107.82");
        totalAmount.setText("Rs. 706.82");
        payBtn = findViewById(R.id.btn_pay_reg_pay);

        savingsCharge = findViewById(R.id.tV_pay_savings);
        //gstCharge = findViewById(R.id.tV_pay_gst);
        serviceCharge = findViewById(R.id.tV_pay_service_charge);

        promoBanner = findViewById(R.id.image_banner);

        promoCardView = findViewById(R.id.promoCardView);
        promoText = findViewById(R.id.promoText);
        mLinearDiscount = findViewById(R.id.linear_discount);
    }

    void uploadToDatabase() {
        if (!userAgreement) {
            return;
        }

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
//                            Toast.makeText(RegistrationPayActivity.this, uri.toString(), Toast.LENGTH_SHORT).show();
                            shop_information.setShopImageUri(uri.toString());
                            mDatabase.child("Merchant_data").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Account_Information").setValue(account_information);
                            mDatabase.child("Merchant_data").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Shop_Information").setValue(shop_information);
                            mDatabase.child("Merchant_data").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Payment_Information").setValue(upiArrayList);
                        }
                    });
                } else {
                    Log.v("Failure upload", "here");
                }
            }
        });
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    public void demo() {
        int v = (card_promo.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
        TransitionManager.beginDelayedTransition(promo, new AutoTransition());
        card_promo.setVisibility(v);
    }


}

