package com.cu.gastosmerchant1.upiparse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.cu.gastosmerchant1.R;
import com.google.zxing.Result;

public class QRScannerActivity extends AppCompatActivity {

    private ImageView back;
    private EditText type;
    private EditText upiIdEt;
    private EditText mId;
    private Button nextButton;

    private String rawString;
    private String upiId;

    private CodeScanner mCodeScanner;
    private boolean isPermissionGranted = false;
    private final int RequestCameraPermissionId = 50;
    private Intent paymentInfoIntent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_r_scanner);


        back = findViewById(R.id.prev);
        type = findViewById(R.id.idEdtType);
        upiIdEt = findViewById(R.id.idEdtUPI);
        mId = findViewById(R.id.idEdtMid);
        nextButton = findViewById(R.id.add);

        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(QRScannerActivity.this, new String[]{Manifest.permission.CAMERA}
                    , RequestCameraPermissionId);
            return;
        }

        try {
            isPermissionGranted = true;
            startScanner();

        } catch (Exception e) {
            e.printStackTrace();
        }

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (mId.getText().toString().isEmpty()) {
                    Toast.makeText(QRScannerActivity.this, "please enter the merchant id", Toast.LENGTH_SHORT).show();
                    return;
                }*/

                paymentInfoIntent.putExtra("merchantId", mId.getText().toString().trim());
                setResult(Activity.RESULT_OK, paymentInfoIntent);
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPermissionGranted)
            startScanner();
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        super.onPause();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermissionId:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    try {
                        isPermissionGranted = true;
                        startScanner();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;
        }
    }

    String upiName(int id) {
        switch (id) {
            case 1:
                return "Bharat Pe";
            case 2:
                return "PayTm";
            case 3:
                return "Amazon pay Upi";
            case 4:
                return "Google Pay";
            case 5:
                return "Phone Pe";
            case 6:
                return "Airtel Upi";
            case 7:
                return "Whatsapp pay";
            case 8:
                return "Bhim upi";
            case 9:
                return "Bank Upi";
            default:
                return "Other Upi";
        }
    }

    public void startScanner() {
        mCodeScanner.startPreview();
        mCodeScanner.setCamera(CodeScanner.CAMERA_BACK);// or CAMERA_FRONT or specific camera id
        mCodeScanner.setFormats(CodeScanner.ALL_FORMATS); // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        mCodeScanner.setAutoFocusMode(AutoFocusMode.SAFE); // or CONTINUOUS
        mCodeScanner.setScanMode(ScanMode.SINGLE); // or CONTINUOUS or PREVIEW
        mCodeScanner.setAutoFocusEnabled(true); // Whether to enable auto focus or not
        mCodeScanner.setFlashEnabled(false);// Whether to enable flash or not
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onDecoded(@NonNull final Result result) {
                Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(500);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        Toast.makeText(QRScannerActivity.this, result.getText(), Toast.LENGTH_SHORT).show();

                        rawString = result.getText();
//                        Pattern pattern = Pattern.compile("[a-zA-z0-9]+@[a-zA-z0-9]+");
//                        Pattern pattern = Pattern.compile("[a-zA-z0-9]");

                        if(rawString.length()<9){
                            Toast.makeText(QRScannerActivity.this, "Invalid Qr", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }

                        String firstWord = rawString.substring(0, 9);
//                        Toast.makeText(QRScannerActivity.this, firstWord, Toast.LENGTH_SHORT).show();
                        if (!firstWord.equals("upi://pay")) {
                            Toast.makeText(QRScannerActivity.this, "Invalid Qr", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {

                            Log.v("rawstring", rawString);
                            paymentInfoIntent.putExtra("rawString", rawString);

                            upiId = FindUpi.findUpiId(rawString);
                            paymentInfoIntent.putExtra("UPI", upiId);
                            type.setText(upiName(FindUpi.findUpi(rawString)));

                            if (upiId.matches("[a-zA-Z0-9\\.\\-]{2,256}\\@[a-zA-Z][a-zA-Z]{2,64}")) {
                                upiIdEt.setText(upiId);

                            } else {
                                Toast.makeText(QRScannerActivity.this, "Invalid UPI !! ", Toast.LENGTH_SHORT).show();
                                startScanner();
                            }
                        }
                    }
                });
            }
        });
    }
}
