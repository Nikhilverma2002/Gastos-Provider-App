package com.cu.gastosmerchant1.registration;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cu.gastosmerchant1.Dashboard.Home;
import com.cu.gastosmerchant1.R;
import com.cu.gastosmerchant1.details.Account_Information;
import com.cu.gastosmerchant1.details.Payment_Information;
import com.cu.gastosmerchant1.details.Shop_Information;
import com.cu.gastosmerchant1.upiparse.QRCodeAdapter;
import com.cu.gastosmerchant1.upiparse.QRScannerActivity;
import com.cu.gastosmerchant1.upiparse.UpiView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class AddQR extends AppCompatActivity {

    private String shopName, shopAddress, shopCategory, location, latitude, longitude, shopImageUri, shopArea, shopState;
    private long creationTimeStamp;
    public static TextView mTitlePrimary;
    public static TextView mNamePrimary;

    private final int REGIS = 1;
    private final int QR_ACTIVITY = 2;
    private int activity_type;
    private int ADD_QR=0;


    ArrayList<Payment_Information> upiArrayList = new ArrayList<>();
    ArrayList<UpiView> upiViewList = new ArrayList<>();
    private QRCodeAdapter qrCodeAdapter;

    Button addUpi;
    RecyclerView qrRecyclerView;

    ImageView back;

    Button qrnextBtn;
    Account_Information account_information;
    Shop_Information shop_information;

    private DatabaseReference mDatabase;
    private FirebaseAuth mfirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_qr);

        mTitlePrimary = findViewById(R.id.tv_tittle_primary_qr);
        mNamePrimary = findViewById(R.id.tv_primary_qr_name);

        //todo primary qr text set

        back = findViewById(R.id.prev);

        account_information = (Account_Information) getIntent().getSerializableExtra("accountInformation");
        shopName = getIntent().getStringExtra("shopName");
        shopAddress = getIntent().getStringExtra("shopAddress");
        shopCategory = getIntent().getStringExtra("shopCategory");
        location = getIntent().getStringExtra("location");
        latitude = getIntent().getStringExtra("latitude");
        longitude = getIntent().getStringExtra("longitude");
        shopImageUri = getIntent().getStringExtra("shopImageUri");
        creationTimeStamp = getIntent().getLongExtra("creationTimeStamp", new Date().getTime());
        shopArea = getIntent().getStringExtra("area");
        shopState = getIntent().getStringExtra("state");


        activity_type = getIntent().getIntExtra("activity_type", 1);


        qrRecyclerView = findViewById(R.id.qrRecycleViewAddQr);


        qrnextBtn = findViewById(R.id.next);
        addUpi = findViewById(R.id.button3);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mfirebaseAuth = FirebaseAuth.getInstance();
        qrnextBtn.setClickable(true);

        addUpi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //gallery open and pick a qr photo.
                Intent i = new Intent(AddQR.this, QRScannerActivity.class);
                startActivityForResult(i, 0000);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        qrnextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //code belonging to this activity is bellow

                if(upiArrayList.isEmpty()){
                    Toast.makeText(AddQR.this, "Add at least one QR", Toast.LENGTH_SHORT).show();
                    return;
                }


                Intent i = new Intent(AddQR.this, Commission.class);
                i.putExtra("accountInformation", account_information);
                i.putExtra("shopName", shopName);
                i.putExtra("shopAddress", shopAddress);
                i.putExtra("shopCategory", shopCategory);
                i.putExtra("latitude", latitude);
                i.putExtra("longitude", longitude);
                i.putExtra("location", location);
                i.putExtra("shopImageUri", shopImageUri);
                i.putExtra("creationTimeStamp", creationTimeStamp);
                i.putExtra("area", shopArea);
                i.putExtra("upiArrayList", upiArrayList);
                i.putExtra("state", shopState);
                startActivity(i);


            }
        });

        qrCodeAdapter = new QRCodeAdapter(AddQR.this, upiViewList, upiArrayList,ADD_QR);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        qrRecyclerView.setLayoutManager(gridLayoutManager);
        qrRecyclerView.setAdapter(qrCodeAdapter);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0000) {
            if (resultCode == RESULT_OK) {
                String rawString = data.getStringExtra("rawString");
                String upiId = data.getStringExtra("UPI");
                String merchantId = data.getStringExtra("merchantId");
                boolean isPrimary = false;

                if (rawString == null) {
                    Toast.makeText(AddQR.this, "Invalid Qr", Toast.LENGTH_SHORT).show();
                    return;
                }

                int flag = 0;
                for (Payment_Information payment_information : upiArrayList) {
                    if (upiId.equals(payment_information.getUpiId())) {
                        flag = 1;
                        Toast.makeText(AddQR.this, "This UPI Already Exist!! ", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

                if (upiArrayList.isEmpty()) {
                    isPrimary = true;
                }

                if (flag == 0) {
                    if (upiId.contains("BHARATPE")) {//todo check
                        upiArrayList.add(new Payment_Information(upiId, rawString, "BharatPe UPI", isPrimary, merchantId));
                        upiViewList.add(new UpiView(R.drawable.bharatpe_mini_qr, upiId, "BharatPe UPI", isPrimary));
                    } else if (upiId.contains("@ok")) {
                        upiArrayList.add(new Payment_Information(upiId, rawString, "GooglePay UPI", isPrimary, merchantId));
                        upiViewList.add(new UpiView(R.drawable.googlepay_mini_qr, upiId, "GooglePay UPI", isPrimary));
                    } else if (upiId.contains("@paytm")) {
                        upiArrayList.add(new Payment_Information(upiId, rawString, "PayTm UPI", isPrimary, merchantId));
                        upiViewList.add(new UpiView(R.drawable.paytm_mini_qr, upiId, "PayTm UPI", isPrimary));
                    } else if (upiId.contains("@upi")) {
                        upiArrayList.add(new Payment_Information(upiId, rawString, "BHIM UPI", isPrimary, merchantId));
                        upiViewList.add(new UpiView(R.drawable.bhim_upi_mini_qr, upiId, "BHIM UPI", isPrimary));
                    } else if (upiId.contains("@ibl")) {
                        upiArrayList.add(new Payment_Information(upiId, rawString, "PhonePe UPI", isPrimary, merchantId));
                        upiViewList.add(new UpiView(R.drawable.phonepe_mini_qr, upiId, "PhonePe UPI", isPrimary));
                    } else if (upiId.contains("@ybl")) {
                        upiArrayList.add(new Payment_Information(upiId, rawString, "PhonePe UPI", isPrimary, merchantId));
                        upiViewList.add(new UpiView(R.drawable.phonepe_mini_qr, upiId, "PhonePe UPI", isPrimary));
                    } else if (upiId.contains("@axl")) {
                        upiArrayList.add(new Payment_Information(upiId, rawString, "PhonePe UPI", isPrimary, merchantId));
                        upiViewList.add(new UpiView(R.drawable.phonepe_mini_qr, upiId, "PhonePe UPI", isPrimary));
                    } else if (upiId.contains("@ybl")) {
                        upiArrayList.add(new Payment_Information(upiId, rawString, "PhonePe UPI", isPrimary, merchantId));
                        upiViewList.add(new UpiView(R.drawable.phonepe_mini_qr, upiId, "PhonePe UPI", isPrimary));
                    } else if (upiId.contains("@apl")) {
                        upiArrayList.add(new Payment_Information(upiId, rawString, "Amazon UPI", isPrimary, merchantId));
                        upiViewList.add(new UpiView(R.drawable.amazon_upi_mini_qr, upiId, "Amazon UPI", isPrimary));
                    } else if (upiId.contains("@airtel")) {
                        upiArrayList.add(new Payment_Information(upiId, rawString, "Airtel UPI", isPrimary, merchantId));
                        upiViewList.add(new UpiView(R.drawable.airtel_mini_qr, upiId, "Airtel UPI", isPrimary));
                    } else {
                        upiArrayList.add(new Payment_Information(upiId, rawString, "Other Upi", isPrimary, merchantId));
                        upiViewList.add(new UpiView(R.drawable.other_upi_mini_qr, upiId, "Other Upi", isPrimary));//todo needed
                    }
                    qrCodeAdapter.notifyDataSetChanged();
                    isPrimary = false;
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        qrCodeAdapter = new QRCodeAdapter(AddQR.this, upiViewList, upiArrayList,ADD_QR);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        qrRecyclerView.setLayoutManager(gridLayoutManager);
        qrRecyclerView.setAdapter(qrCodeAdapter);
    }
}
