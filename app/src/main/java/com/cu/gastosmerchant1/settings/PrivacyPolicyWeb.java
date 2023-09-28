package com.cu.gastosmerchant1.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.cu.gastosmerchant1.R;

public class PrivacyPolicyWeb extends AppCompatActivity {

    WebView mPrivacyWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy_web);

        mPrivacyWeb = findViewById(R.id.privacy_web);

        mPrivacyWeb.loadUrl("https://gastos-privacypolicy.netlify.app/");

        WebSettings webSettings = mPrivacyWeb.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }
}