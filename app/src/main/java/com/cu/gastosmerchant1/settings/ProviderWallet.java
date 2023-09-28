package com.cu.gastosmerchant1.settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cu.gastosmerchant1.R;
import com.cu.gastosmerchant1.details.Account_Information;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ProviderWallet extends AppCompatActivity {

    TextView mTotalAmount, mPromotionAmount, mBrandingAmount;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    Account_Information account_information;
    ImageView mBack;
    float TotalAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_wallet);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        mTotalAmount = findViewById(R.id.total);
        mPromotionAmount = findViewById(R.id.promotion);
        mBrandingAmount = findViewById(R.id.branding);
        mBack = findViewById(R.id.imageButton);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        databaseReference.child("Merchant_data").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Account_Information").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                account_information = snapshot.getValue(Account_Information.class);

                float PromotionBalance = Float.parseFloat(account_information.getWalletPromotion());
                float BrandingBalance = Float.parseFloat(account_information.getWalletBranding());

                TotalAmount = PromotionBalance+BrandingBalance;

                /*
                if (TotalAmount.equals("299")){
                    mTotalAmount.setText("1,200");
                    mPromotionAmount.setText("200");
                    mBrandingAmount.setText("1,000");
                }
                else {
                    mTotalAmount.setText("3,000");
                    mPromotionAmount.setText("600");
                    mBrandingAmount.setText("2,400");
                }

                 */
                mTotalAmount.setText(TotalAmount+"");
              //  mPromotionAmount.setText(PromotionBalance+"");
              //  mBrandingAmount.setText(BrandingBalance+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






    }
}
