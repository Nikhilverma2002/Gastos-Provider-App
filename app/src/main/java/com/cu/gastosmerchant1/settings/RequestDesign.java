package com.cu.gastosmerchant1.settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cu.gastosmerchant1.R;
import com.cu.gastosmerchant1.details.Account_Information;
import com.cu.gastosmerchant1.registration.BasicDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class RequestDesign extends AppCompatActivity {
    TextView mTextWalletAmount;
    FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    Account_Information account_information;
    ImageView mBack;
    TextView select_design;
    private Spinner designSpinner;
    private LinearLayout mLinearTypeSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_design);

        select_design=findViewById(R.id.select_design);
        mTextWalletAmount = findViewById(R.id.design_balance_text);
        mBack = findViewById(R.id.prev);
        designSpinner=findViewById(R.id.design_spinner);
        mLinearTypeSelector = findViewById(R.id.select_design_type);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        ArrayList<String> design_type=new ArrayList<>();
        design_type.add("Visiting Card");
        design_type.add("Flyer");
        design_type.add("Brochure (Trifold)");
        design_type.add("Logo");
        design_type.add("Banner");
        design_type.add("Menu");
        design_type.add("Other (Specifiy)");


        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(RequestDesign.this, R.layout.support_simple_spinner_dropdown_item, design_type) {
            @Override
            public int getCount() {
                return super.getCount();
            }
        };

        categoryAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        designSpinner.setAdapter(categoryAdapter);
        designSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                select_design.setText(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //do nothing
            }
        });

        mLinearTypeSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                designSpinner.performClick();
            }
        });


        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        databaseReference.child("Merchant_data").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Account_Information").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                account_information = snapshot.getValue(Account_Information.class);
                int BrandingBalance = Integer.parseInt(account_information.getWalletBranding());

                mTextWalletAmount.setText("Rs "+BrandingBalance);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
