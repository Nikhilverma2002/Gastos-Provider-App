package com.cu.gastosmerchant1.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cu.gastosmerchant1.R;

public class TermsOfService extends AppCompatActivity {

    private ImageView prev;
    WebView mTncWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_of_service);
        mTncWeb = findViewById(R.id.mTncWebView);

        prev=findViewById(R.id.prev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mTncWeb.loadUrl("https://gastos-termsofservice.netlify.app/");

        WebSettings webSettings = mTncWeb.getSettings();
        webSettings.setJavaScriptEnabled(true);



    }
}
