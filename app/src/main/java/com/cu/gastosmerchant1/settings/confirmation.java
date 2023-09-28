package com.cu.gastosmerchant1.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cu.gastosmerchant1.Dashboard.Home;
import com.cu.gastosmerchant1.PaymentStatus;
import com.cu.gastosmerchant1.R;

public class confirmation extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        String number=getIntent().getStringExtra("sending_phone_number");
        ImageView back=findViewById(R.id.imageView);
        back.setOnClickListener(v->{
            finish();
        });

        TextView home=findViewById(R.id.next);
        home.setOnClickListener(v->{
            Intent intent = new Intent(confirmation.this, Home.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        TextView num=findViewById(R.id.textView12);
        num.setText(number);

    }
}