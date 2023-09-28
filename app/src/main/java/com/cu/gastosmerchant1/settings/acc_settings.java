package com.cu.gastosmerchant1.settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.gastosmerchant1.R;
import com.cu.gastosmerchant1.details.Account_Information;
import com.cu.gastosmerchant1.registration.PhoneAuthentication;
import com.cu.gastosmerchant1.registration.RegistrationPaymentActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class acc_settings extends AppCompatActivity {

    private ImageView previous;

    private EditText ownerName;
    private EditText phoneNumber;
    private EditText emailAddress;
    private EditText promoCode;

    private CardView applyCardView;
    private Button applyButton;
    private CardView internCardView;
    private TextView appliedIntern;
    FirebaseAuth auth;
    FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    Account_Information account_information;
    String emailPattern = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";

    private TextView logOut;

    private boolean isPromoApplied = false;

    private String intern;
    private boolean internApplied = false;

    private DatabaseReference mDatabase;

    //todo need to take care of the case where the shopkeeper registers him self alone. his details must be uploaded into database on the payment completion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc_settings);

        ownerName = findViewById(R.id.ownernameEt);
        phoneNumber = findViewById(R.id.phnumberEt);
        emailAddress = findViewById(R.id.emailadressEt);
        previous = findViewById(R.id.prev);
        logOut = findViewById(R.id.p8);
        applyButton = findViewById(R.id.btn_apply_promo_reg_pay);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        promoCode = findViewById(R.id.edit_promo);
        applyCardView = findViewById(R.id.applyCardView);
        internCardView = findViewById(R.id.cardView);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        appliedIntern = findViewById(R.id.appliedText);

        databaseReference = firebaseDatabase.getReference();

        databaseReference.child("Merchant_data").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Account_Information").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                account_information = snapshot.getValue(Account_Information.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ownerName.setText(account_information.getOwnerName());
                        phoneNumber.setText(account_information.getPhoneNumber());
                        emailAddress.setText(account_information.getEmailAddress());
                        intern = account_information.getSalesCode();
                        doSomethingAboutApplyCardView(intern);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOutDialogFunc();
            }
        });

        mDatabase = firebaseDatabase.getReference();

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = promoCode.getText().toString().toUpperCase().trim();
                mDatabase.child("Merchant_data").child("BDSales").child(code).child("name").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(@NonNull @NotNull DataSnapshot dataSnapshot) {
                        try {
                            if (!dataSnapshot.getValue().toString().isEmpty()) {
                                Toast.makeText(acc_settings.this, "promo code applied Successfully", Toast.LENGTH_SHORT).show();
                                isPromoApplied = true;
                                Log.v(RegistrationPaymentActivity.class.getSimpleName(), "promo");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        applyButton.setClickable(false);
                                        promoCode.setFocusable(false);
                                        Log.v(RegistrationPaymentActivity.class.getSimpleName(), "promo done");
                                    }
                                });

                                account_information.setRegistrationPaymentDone(true);

                                Account_Information account_information_sales_code = new Account_Information(account_information.getEmailAddress(), account_information.getOwnerName(), account_information.getPhoneNumber());
                                account_information_sales_code.setRegistrationPaymentDone(true);
                                mDatabase.child("Merchant_data").child("BDSales").child(code).child("Providers").child(account_information.getPhoneNumber()).child("account_information").setValue(account_information_sales_code);
                                mDatabase.child("Merchant_data").child("BDSales").child(code).child("Providers").child(account_information.getPhoneNumber()).child("payment_amount").setValue("599.00");
                                Format f = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
                                String str_time = f.format(new Date());

                                SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
                                String str_date = formatter.format(new Date());
                                mDatabase.child("Merchant_data").child("BDSales").child(code).child("Providers").child(account_information.getPhoneNumber()).child("date_time").setValue(str_date+" - "+str_time);
                                mDatabase.child("Merchant_data").child("BDSales").child(code).child("Providers").child(account_information.getPhoneNumber()).child("uid").setValue(user.getUid());
                                account_information.setSalesCode(dataSnapshot.getValue().toString());

                                databaseReference.child("Merchant_data").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Account_Information").child("salesCode").setValue(dataSnapshot.getValue().toString());

                                applyButton.setVisibility(View.GONE);
                                appliedIntern.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            Toast.makeText(acc_settings.this, "Some problem Occurred...", Toast.LENGTH_SHORT).show();
                            Log.e("Bd_Intern", e.toString());
                        }
                    }
                });
            }
        });


        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //record changes here
                updateData();
                finish();
            }
        });
    }

    private void logOutDialogFunc() {

        class LogoutDialog extends Dialog {
            Button cancelButton;
            TextView logout_text;

            public LogoutDialog(@NonNull Context context) {
                super(context);
            }

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                requestWindowFeature(Window.FEATURE_NO_TITLE);
                setContentView(R.layout.logout_alert_dialog);
                cancelButton = findViewById(R.id.cancel_logout);
                logout_text = findViewById(R.id.logout_text);
            }
        }
        LogoutDialog logoutDialog = new LogoutDialog(acc_settings.this);
        logoutDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        logoutDialog.show();
        logoutDialog.logout_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(acc_settings.this, PhoneAuthentication.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        logoutDialog.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutDialog.dismiss();
            }
        });


    }

    void doSomethingAboutApplyCardView(String intern) {
        if (intern.equals("")) {
            applyCardView.setVisibility(View.VISIBLE);
            internCardView.setVisibility(View.VISIBLE);
            return;
        }
        applyCardView.setVisibility(View.GONE);
        promoCode.setText(intern);
        promoCode.setEnabled(false);
    }

    void updateData() {
        if (!account_information.getEmailAddress().equals(emailAddress.getText().toString()) |
                !account_information.getOwnerName().equals(ownerName.getText().toString()) |
                !account_information.getPhoneNumber().equals(phoneNumber.getText().toString())) {
            if (emailAddress.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "email address can not be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!emailAddress.getText().toString().matches(emailPattern)) {
                Toast.makeText(getApplicationContext(), "Enter valid email id", Toast.LENGTH_SHORT).show();
                return;
            }

            String OwnerName = ownerName.getText().toString();
            String PhoneNumber = phoneNumber.getText().toString();

            if (OwnerName.isEmpty()) {
                ownerName.setError("This field is required");
                return;
            }

            if (PhoneNumber.isEmpty()) {
                phoneNumber.setError("Phone number is required");
                return;
            }

            account_information.setEmailAddress(emailAddress.getText().toString());
            account_information.setOwnerName(OwnerName);
            account_information.setPhoneNumber(PhoneNumber);

            databaseReference.child("Merchant_data").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Account_Information").setValue(account_information);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        updateData();
    }
}
