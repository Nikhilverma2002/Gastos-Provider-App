package com.cu.gastosmerchant1.settings.postAds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;

import com.cu.gastosmerchant1.databinding.ActivityBudgetBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class budget extends AppCompatActivity {

    ActivityBudgetBinding binding;
    double answer=2000.00;
    int total_coustomer=4000;
    FirebaseAuth auth;
    String selectedFileUri;
    FirebaseUser user;
    String type,content;
    String city,category,reach,start_age,end_age,sending_shop_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding= ActivityBudgetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        type=getIntent().getStringExtra("sending_type");
        content=getIntent().getStringExtra("sending_content");
        city=getIntent().getStringExtra("sending_city");
        category=getIntent().getStringExtra("sending_category");
        reach=getIntent().getStringExtra("sending_reach");
        start_age=getIntent().getStringExtra("sending_start_age");
        end_age=getIntent().getStringExtra("sending_end_age");
        sending_shop_name=getIntent().getStringExtra("sending_shop_name");
        selectedFileUri=getIntent().getStringExtra("sending_file_uri");

        get_wallet();

        binding.imageView.setOnClickListener(v->{
            finish();
        });

        binding.seekBarLuminosite.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(i<100){
                    total_coustomer=100;
                    binding.total.setText(100+" Messages");
                    answer = 100/2.0;
                    binding.num.setText("Rs "+answer);
                    binding.walletAmt.setText(100+" Messages");
                }
                else{
                    total_coustomer=i;
                    binding.total.setText(i+" Messages");
                    answer = i/2.0;
                    binding.num.setText("Rs "+answer);
                    binding.walletAmt.setText(i+" Messages");
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        binding.next.setOnClickListener(v->{
            Intent intent=new Intent(budget.this, ad_payment.class);
            intent.putExtra("sending_type",type);
            intent.putExtra("sending_content",content);
            intent.putExtra("amt_of_customers",answer);
            intent.putExtra("sending_city",city);
            intent.putExtra("sending_category",category);
            intent.putExtra("sending_reach",reach);
            intent.putExtra("sending_start_age",start_age);
            intent.putExtra("sending_end_age",end_age);
            intent.putExtra("num_of_customers",total_coustomer);
            intent.putExtra("sending_shop_name",sending_shop_name);
            intent.putExtra("sending_file_uri", selectedFileUri);
            startActivity(intent);
        });

    }
    void get_wallet(){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Merchant_data");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String wallet=snapshot.child(user.getUid()).child("Account_Information").child("walletPromotion").getValue(String.class);
                if(wallet!=null){
                    Log.e("Waaal",wallet+"");
                    Log.e("Waaal",user.getUid()+"");
                    binding.wallet.setText("Wallet Balance : "+wallet);
                }
                else{
                    binding.wallet.setText("Wallet Balance : 0");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}