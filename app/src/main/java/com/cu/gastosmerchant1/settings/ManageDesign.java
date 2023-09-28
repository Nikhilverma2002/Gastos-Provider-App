package com.cu.gastosmerchant1.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.cu.gastosmerchant1.R;

public class ManageDesign extends AppCompatActivity {

    ImageView mPrev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_design);

        mPrev = findViewById(R.id.prev);

        mPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
