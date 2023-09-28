package com.cu.gastosmerchant1.Dashboard.membership;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.cu.gastosmerchant1.databinding.ActivityBillPaymentBinding;
import com.cu.gastosmerchant1.registration.CashFree_Activity;
import com.cu.gastosmerchant1.registration.RegistrationPayActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Bill_payment extends AppCompatActivity {

    ActivityBillPaymentBinding binding;
    double amount,gst,service_amount;
    FirebaseAuth auth;
    FirebaseUser user;
    String plan;
    String email_info,phone;
    ProgressDialog  pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityBillPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        plan=getIntent().getStringExtra("Sending_plan");
        get_details(0);

        if(plan.equals("year")){
            amount=529.82;
            gst=80.82;
            service_amount=449.00;
            String msg="Marketplace Membership (Yearly)";
            binding.textView29.setText(msg);
            binding.serviceCharge.setText("₹449.00");
            binding.gst.setText("₹80.82");
            binding.grandTotal.setText("₹529.82");
        }
        else{
            amount=57.82;
            gst=8.82;
            service_amount=49.00;
            String msg="Marketplace Membership (Monthly)";
            binding.textView29.setText(msg);
            binding.serviceCharge.setText("₹49.00");
            binding.gst.setText("₹8.82");
            binding.grandTotal.setText("₹57.82");
        }

        binding.imageView12.setOnClickListener(v->{
            finish();
        });
        binding.proceedPay.setOnClickListener(v->{
            pd = new ProgressDialog(this);
            pd.setTitle("Fetching Details");
            pd.setCancelable(false);
            pd.setMessage("Please Wait!!");
            pd.setIndeterminate(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
            get_details(1);
        });
    }
    void get_details(int check){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Merchant_data");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                email_info=snapshot.child(user.getUid()).child("Account_Information").child("emailAddress").getValue(String.class);
                phone=snapshot.child(user.getUid()).child("Account_Information").child("phoneNumber").getValue(String.class);
                if(check==1) {
                    Intent intent = new Intent(Bill_payment.this, CashFree_Activity.class);
                    intent.putExtra("amount", String.valueOf(amount));
                    //intent.putExtra("amount", "1");
                    intent.putExtra("from_bill_payments","mem_pay_bill");
                    intent.putExtra("phoneNumber", phone);
                    intent.putExtra("sending_plan", plan);
                    intent.putExtra("sending_discount", gst);
                    intent.putExtra("sending_service_amt", service_amount);
                    intent.putExtra("email", email_info);
                    //intent to cash free
                    startActivity(intent);
                    pd.dismiss();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}
