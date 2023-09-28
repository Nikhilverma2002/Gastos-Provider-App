package com.cu.gastosmerchant1.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.gastosmerchant1.Dashboard.membership.membership_status;
import com.cu.gastosmerchant1.R;
import com.cu.gastosmerchant1.details.Account_Information;
import com.cu.gastosmerchant1.details.Payment_Information;
import com.cu.gastosmerchant1.details.Shop_Information;
import com.cu.gastosmerchant1.recvd_paymt;
import com.cu.gastosmerchant1.Settings;
import com.cu.gastosmerchant1.selectPackage.selectPackage;
import com.cu.gastosmerchant1.settings.ProviderWallet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class Home extends AppCompatActivity {

    private ImageView shopImage;
    private TextView shopName,userName;
    private String shopImageUri;
    private ImageView adsImage;
    private String ads_String;

    CardView addQrButton,generateQr,settingsButton;
    private LinearLayout load_payments;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private final String MyPrefs = "MY_PREFERENCE";
    private final String NAME = "MY_NAME";
    //
    private ArrayList<Payment_Information> upiArrayList;
    private Shop_Information shop_information;
    private Account_Information account_information;
    //
    private DatabaseReference mDatabase;
    private FirebaseAuth mfirebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    CardView marketMembership,providerWallet;
    ConstraintLayout membership;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_home);//activity_home

        shopName = findViewById(R.id.shopName);
        shopImage = findViewById(R.id.shopImage);
        marketMembership = findViewById(R.id.marketMembership);
        providerWallet = findViewById(R.id.providerWallet);
        userName = findViewById(R.id.userName);
        membership = findViewById(R.id.double_dhamaka);

        adsImage = findViewById(R.id.ads_image_home);
        load_payments=findViewById(R.id.load_payments);

        mfirebaseAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences(MyPrefs, MODE_PRIVATE);
        //
        shop_information = (Shop_Information) getIntent().getSerializableExtra("shop_information");
        account_information = (Account_Information) getIntent().getSerializableExtra("account_information");
        upiArrayList = (ArrayList<Payment_Information>) getIntent().getSerializableExtra("upiArrayList");
        //





        firebaseDatabase = FirebaseDatabase.getInstance();


        firebaseDatabase.getReference().

                child("Merchant_data").

                child(Objects.requireNonNull(mfirebaseAuth.getUid())).

                child("Account_Information").

                child("ownerName").

                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d("vall", "onDataChange: "+snapshot.getValue());
                        userName.setText("By "+snapshot.getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        firebaseDatabase.getReference().child("Merchant_data").child(Objects.requireNonNull(mfirebaseAuth.getUid())).child("Shop_Information").child("shopName").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.getValue(String.class).equals(sharedPreferences.getString(NAME, "cafe Bistro"))) {
                    editor = sharedPreferences.edit();
                    editor.putString(NAME, snapshot.getValue(String.class));
                    editor.apply();
                }
                Log.d("getValue", "onDataChange: "+snapshot.getValue());
                shopName.setText(sharedPreferences.getString(NAME, "Cafe Bistro"));
                Log.d("sName", "onDataChange: "+shopName.getText());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        firebaseDatabase.getReference().

                child("Merchant_ads").

                child("ads").

                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            ads_String = snapshot.getValue(String.class);
                            Picasso.get().load(Uri.parse(ads_String)).into(adsImage);
                        } catch (Exception e) {

                            Toast.makeText(Home.this, "something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        firebaseDatabase.getReference().

                child("Merchant_data").

                child(Objects.requireNonNull(mfirebaseAuth.getUid())).

                child("Shop_Information").

                child("shopImageUri").

                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String imageUri = snapshot.getValue(String.class);
                        try {
                            Picasso.get().load(Uri.parse(imageUri)).into(shopImage);
                        } catch (Exception e) {
                            Toast.makeText(Home.this, "Something went wrong to load image", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        addQrButton = findViewById(R.id.manageQr);
        generateQr = findViewById(R.id.posQr);
        settingsButton = findViewById(R.id.settings);

        addQrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, pymt_info.class));
            }
        });


        generateQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this,GenerateQrActivity.class));
            }
        });

        load_payments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, recvd_paymt.class));
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home.this, Settings.class);
                startActivity(i);
            }
        });
        marketMembership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               Intent i=new Intent(getApplicationContext(), membership_status.class);
               startActivity(i);
            }
        });
        membership.setOnClickListener(v->{
            Intent i=new Intent(getApplicationContext(), selectPackage.class);
            startActivity(i);
        });
        providerWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ProviderWallet.class));
            }
        });
    }
}
