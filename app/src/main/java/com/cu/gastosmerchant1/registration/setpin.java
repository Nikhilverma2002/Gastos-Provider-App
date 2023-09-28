package com.cu.gastosmerchant1.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cu.gastosmerchant1.R;

public class setpin extends AppCompatActivity {


    ImageView back;

    Button btnver;
    EditText pin1;
    EditText pin2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setpin);

        back=findViewById(R.id.prev);

        pin1 = findViewById(R.id.enterPin);
        pin2 = findViewById(R.id.renterPin);

        btnver = findViewById(R.id.setBtn);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pinString = pin1.getText().toString();
                String pin2String = pin2.getText().toString();
                //pin max size is 4
                if (pinString.length() != 4) {
                    Toast.makeText(setpin.this, "Minimum pin size not matching", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!pinString.equals(pin2String)) {
                    Toast.makeText(setpin.this, "pins are not matching", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = new Intent(setpin.this, BasicDetails.class);
//                Intent i = new Intent(setpin.this, Payment_web.class);
                i.putExtra("pin", pinString);
                startActivity(i);
            }
        });


    }
}
