package com.cu.gastosmerchant1.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.cu.gastosmerchant1.Dashboard.Home;
import com.cu.gastosmerchant1.databinding.ActivityOnboardedBinding;
import com.cu.gastosmerchant1.details.Account_Information;
import com.cu.gastosmerchant1.details.Payment_Information;
import com.cu.gastosmerchant1.details.Shop_Information;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Objects;

public class onboarded extends AppCompatActivity {

    ActivityOnboardedBinding binding;
    private Shop_Information shop_information;
    private SharedPreferences sharedPreferences;
    private final String NAME = "MY_NAME";
    private final String MyPrefs = "MY_PREFERENCE";
    private SharedPreferences.Editor editor;
    private Account_Information account_information;
    private FirebaseAuth firebaseAuth;
    private ArrayList<Payment_Information> upiArrayList;
    DatabaseReference mDatabase;
    private boolean userAgreement = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityOnboardedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mDatabase= FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        userAgreement = getIntent().getBooleanExtra("userAgreement", false);

        if (userAgreement)
        {
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

//        binding.jumpToHome.setOnClickListener(v->{
//            Intent intent = new Intent(onboarded.this, Home.class);
//            intent.putExtra("shop_information", shop_information);
//            intent.putExtra("account_information", account_information);
//            intent.putExtra("upiArrayList",upiArrayList);
//            intent.putExtra("userAgreement",true);
//            startActivity(intent);
//        });

    }
    void uploadToDatabase() {
        if (!userAgreement) {
            return;
        }
        binding.progressBar.setVisibility(View.VISIBLE);

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
                            Intent intent = new Intent(onboarded.this, Home.class);
                            intent.putExtra("shop_information", shop_information);
                            intent.putExtra("account_information", account_information);
                            intent.putExtra("upiArrayList", upiArrayList);
                            intent.putExtra("userAgreement", true);
                            startActivity(intent);
                            binding.progressBar.setVisibility(View.GONE);
                        }
                    });
                } else {
                    Log.v("Failure upload", "here");
                }
            }
        });
    }
}
