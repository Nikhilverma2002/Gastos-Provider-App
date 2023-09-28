package com.cu.gastosmerchant1.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cu.gastosmerchant1.R;

public class PhoneAuthentication extends AppCompatActivity {
    Button otp_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_authentication);

        otp_button = findViewById(R.id.myotpbuttonphone);
        final EditText phone_num=findViewById(R.id.number);
        otp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num=phone_num.getText().toString().trim();

                if(!num.isEmpty()) {
                    if (num.length() == 10) {
                        Intent intent = new Intent(PhoneAuthentication.this, Getotp_activity.class);
                        intent.putExtra("mobile",num);
                        startActivity(intent);


                    } else {
                        Toast.makeText(PhoneAuthentication.this, "Please enter correct phone number", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(PhoneAuthentication.this, "Enter phone number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}