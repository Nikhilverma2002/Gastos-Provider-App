package com.cu.gastosmerchant1.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.gastosmerchant1.R;
import com.cu.gastosmerchant1.details.Account_Information;
import com.cu.gastosmerchant1.details.Payment_Information;
import com.cu.gastosmerchant1.details.Shop_Information;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserAgreement extends AppCompatActivity {

    private TextView text;
    private CheckBox checkBox;
    private Button confirmBtn;
    private ProgressBar pBar;

    private ImageView back;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private Shop_Information shop_information;
    private Account_Information account_information;
    private ArrayList<Payment_Information> upiArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_agreement);
        // set the user agreement in this text
        text = findViewById(R.id.text);
        checkBox = findViewById(R.id.checkbox);
        confirmBtn = findViewById(R.id.confirm_button); 

        pBar=findViewById(R.id.pBar);
        back=findViewById(R.id.prev);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        setAgreementText();


        shop_information = (Shop_Information) getIntent().getSerializableExtra("shop_information");
        account_information = (Account_Information) getIntent().getSerializableExtra("account_information");
        upiArrayList = (ArrayList<Payment_Information>) getIntent().getSerializableExtra("upiArrayList");

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                if (checkBox.isChecked()) {

                    if(!isConnectionAvailable(UserAgreement.this)){
                        Toast.makeText(UserAgreement.this, "Internet not available", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    Intent intent = new Intent(UserAgreement.this, onboarded.class);
                    intent.putExtra("shop_information", shop_information);
                    intent.putExtra("account_information", account_information);
                    intent.putExtra("upiArrayList",upiArrayList);
                    intent.putExtra("userAgreement",true);
                    startActivity(intent);
                } else {
                    Toast.makeText(UserAgreement.this, "Please accept the user agreement.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void setAgreementText()
    {

        FirebaseDatabase.getInstance().getReference().child("Merchant_agreements").child("user_agreement").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userAgreement = snapshot.getValue(String.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        text.setText(userAgreement);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
}

