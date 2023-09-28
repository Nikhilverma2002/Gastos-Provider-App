package com.cu.gastosmerchant1.Dashboard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cu.gastosmerchant1.R;
import com.cu.gastosmerchant1.details.Payment_Information;
import com.cu.gastosmerchant1.registration.AddQR;
import com.cu.gastosmerchant1.upiparse.QRCodeAdapter;
import com.cu.gastosmerchant1.upiparse.QRScannerActivity;
import com.cu.gastosmerchant1.upiparse.UpiView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class pymt_info extends AppCompatActivity {

    private int CHANGES_MADE = 0;
    private int PYMT_INFO=1;

    private Button backButton, mSave;

    ArrayList<Payment_Information> upiArrayList = new ArrayList<>();
    ArrayList<UpiView> upiViewList = new ArrayList<>();
    private QRCodeAdapter qrCodeAdapter;

    Button addUpi;
    RecyclerView qrRecyclerView;

    private DatabaseReference mDatabase;
    private FirebaseAuth mfirebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pymt_info);

        qrRecyclerView = findViewById(R.id.rcv);

        backButton = findViewById(R.id.prev);

//        qrCodeAdapter = new QRCodeAdapter(pymt_info.this, upiViewList, upiArrayList,PYMT_INFO);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
//        qrRecyclerView.setLayoutManager(gridLayoutManager);
//        qrRecyclerView.setAdapter(qrCodeAdapter);
        addUpi = findViewById(R.id.button3);
        mSave = findViewById(R.id.bt_save);

        mfirebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabase = firebaseDatabase.getReference().child("Merchant_data").child(Objects.requireNonNull(mfirebaseAuth.getUid())).child("Payment_Information");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                upiViewList.clear();
                upiArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    upiArrayList.add(dataSnapshot.getValue(Payment_Information.class));
                }
                for (Payment_Information payment_information : upiArrayList) {
                    int resourceId;
                    switch (payment_information.getUpiName()) {
                        case "GooglePay UPI":
                            resourceId = R.drawable.googlepay_mini_qr;
                            break;
                        case "PayTm UPI":
                            resourceId = R.drawable.paytm_mini_qr;
                            break;
                        case "BHIM UPI":
                            resourceId = R.drawable.bhim_upi_mini_qr;
                            break;
                        case "PhonePe UPI":
                            resourceId = R.drawable.phonepe_mini_qr;
                            break;
                        case "Amazon UPI":
                            resourceId = R.drawable.amazon_upi_mini_qr;
                            break;
                        case "Airtel UPI":
                            resourceId = R.drawable.airtel_mini_qr;
                            break;
                        case "BharatPe UPI":
                            resourceId = R.drawable.bharatpe_mini_qr;
                            break;
                        case "Other Upi":
                            resourceId = R.drawable.other_upi_mini_qr;
                            break;
                        default:
                            resourceId = R.drawable.other_upi_mini_qr;
                            break;
                    }
                    upiViewList.add(new UpiView(resourceId, payment_information.getUpiId(), payment_information.getUpiName(), payment_information.isPrimary()));
                }
                //TODO FIREBASE DATA FETCH
                qrCodeAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (CHANGES_MADE == 1) {
//                    mDatabase.setValue(upiArrayList).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void unused) {
//                            startActivity(new Intent(pymt_info.this, Home.class));
//                        }
//                    });
//                } else {
//                    startActivity(new Intent(pymt_info.this, Home.class));
//                }
                finish();
            }
        });

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.setValue(upiArrayList).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        finish();
                    }
                });
            }
        });



        addUpi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //gallery open and pick a qr photo.
                Intent i = new Intent(pymt_info.this, QRScannerActivity.class);
                startActivityForResult(i, 0000);
            }
        });
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
                    Toast.makeText(pymt_info.this, "No Upi received", Toast.LENGTH_SHORT).show();
                    return;
                }

                int flag = 0;


                for (Payment_Information payment_information : upiArrayList) {
                    if (upiId.equals(payment_information.getUpiId())) {
                        flag = 1;
                        Toast.makeText(pymt_info.this, "This UPI Already Exist!! ", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

                if (upiArrayList.isEmpty()) {
                    isPrimary = true;
                }

                if (flag == 0) {

                    if (upiId.contains("BHARATPE")) {
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
                    CHANGES_MADE = 1;
                    qrCodeAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        mDatabase.setValue(upiArrayList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                Toast.makeText(pymt_info.this, "Qr Updated", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        qrCodeAdapter = new QRCodeAdapter(pymt_info.this, upiViewList, upiArrayList,PYMT_INFO);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        qrRecyclerView.setLayoutManager(gridLayoutManager);
        qrRecyclerView.setAdapter(qrCodeAdapter);
    }
}
