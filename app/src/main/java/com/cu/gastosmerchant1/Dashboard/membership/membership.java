package com.cu.gastosmerchant1.Dashboard.membership;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.cu.gastosmerchant1.R;
import com.cu.gastosmerchant1.databinding.ActivityMembershipBinding;

public class membership extends AppCompatActivity {

    ActivityMembershipBinding binding;
    String plan="year";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityMembershipBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.check.setBackgroundResource(R.drawable.ic_radio_mem_on);
        binding.check1.setBackgroundResource(R.drawable.ic_unselect_radio);

        binding.constraintLayout.setOnClickListener(v->{
            plan="year";
            binding.check.setBackgroundResource(R.drawable.ic_radio_mem_on);
            binding.check1.setBackgroundResource(R.drawable.ic_unselect_radio);
        });

        binding.cons1.setOnClickListener(v->{
            plan="month";
            binding.check.setBackgroundResource(R.drawable.ic_unselect_radio);
            binding.check1.setBackgroundResource(R.drawable.ic_radio_mem_on);
        });

        binding.continueBtn.setOnClickListener(v->{
            Intent intent=new Intent(membership.this,Bill_payment.class);
            intent.putExtra("Sending_plan",plan);
            startActivity(intent);
        });

        binding.imageView12.setOnClickListener(v->{
            finish();
        });
    }
}