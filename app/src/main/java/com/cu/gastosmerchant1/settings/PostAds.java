package com.cu.gastosmerchant1.settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.cu.gastosmerchant1.Ad_content;
import com.cu.gastosmerchant1.R;
import com.cu.gastosmerchant1.databinding.ActivityPostAdsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PostAds extends AppCompatActivity {

    ActivityPostAdsBinding binding;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;
    Intent intent=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityPostAdsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        binding.imageView.setOnClickListener(view -> finish());

        binding.cardVie3.setOnClickListener(v->{
            binding.whatsapp.setBackgroundResource(R.drawable.stroke);
            binding.imageView9.setVisibility(View.GONE);
            binding.sms.setBackgroundResource(R.drawable.stroke_select);
            binding.imageView7.setVisibility(View.VISIBLE);
            intent=new Intent(PostAds.this, Ad_content.class);
            intent.putExtra("Type_of_service","SMS");
            //startActivity(intent);
        });

        binding.cardView4.setOnClickListener(v->{
            binding.whatsapp.setBackgroundResource(R.drawable.stroke_select);
            binding.imageView9.setVisibility(View.VISIBLE);
            binding.sms.setBackgroundResource(R.drawable.stroke);
            binding.imageView7.setVisibility(View.GONE);
            intent=new Intent(PostAds.this, Ad_content.class);
            intent.putExtra("Type_of_service","WhatsApp");
            //startActivity(intent);
        });



        binding.next.setOnClickListener(v->{
            if (intent!=null){
                startActivity(intent);
            }
            else {
                Toast.makeText(this, "Choose the Advertisement Type", Toast.LENGTH_SHORT).show();
            }
        });

        reference= FirebaseDatabase.getInstance().getReference().child("Merchant_data");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String wallet=snapshot.child(user.getUid()).child("Account_Information").child("walletPromotion").getValue(String.class);
                    if(wallet!=null){
                        binding.walletAmt.setText("Rs "+wallet);
                    }
                    else{
                        binding.walletAmt.setText("Rs 0");
                    }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

    }
}
