package com.cu.gastosmerchant1.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.cu.gastosmerchant1.R;
import com.cu.gastosmerchant1.databinding.ActivityPayScreenBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import pl.allegro.finance.tradukisto.MoneyConverters;
import pl.allegro.finance.tradukisto.ValueConverters;

public class pay_screen extends AppCompatActivity {

    static ActivityPayScreenBinding binding;
    static private Timestamp timestamp;
    static String str_time,str_date;
    DatabaseReference reference,reference_bdsales;
    static String amount,decimal_amt;
    FirebaseAuth auth;
    FirebaseUser user;
    static String plan,orderid,status;
    static Context context;
    String package_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityPayScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context=this.getApplicationContext();

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        reference_bdsales=FirebaseDatabase.getInstance().getReference().child("Merchant_data");

        amount=getIntent().getStringExtra("amount");
        status=getIntent().getStringExtra("txt_status");
        plan=getIntent().getStringExtra("sending_plan");
        orderid=getIntent().getStringExtra("orderId");
        String phone=getIntent().getStringExtra("phoneNumber");
        double gst=getIntent().getDoubleExtra("sending_gst",0.0);
        double service_amt=getIntent().getDoubleExtra("sending_service_amt",0.0);
        int transaction_status=getIntent().getIntExtra("transaction_status",0);

        timestamp = new Timestamp(System.currentTimeMillis());
        Format f = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
        str_time = f.format(new Date());

        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        str_date = formatter.format(new Date());

        binding.proceedPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),Home.class);
                startActivity(i);
                finish();
            }
        });

        if(status!=null && status.equals("f"))
        {
            binding.textView14.setText("Payment UnSuccessful");
            binding.textView21.setText("Bill Payment was unsuccessful!");
            binding.animate.setAnimation("failed.json");
            binding.textView28.setText("InActive");
            binding.textView28.setBackgroundResource(R.drawable.bg_inactive);
            binding.textView28.setTextColor(Color.parseColor("#FFFFFFFF"));
        }

        binding.date.setText(str_date);
        binding.time.setText(str_time);
        binding.serviceCharge.setText("Rs "+service_amt);
        binding.gst.setText("Rs "+gst);
        binding.textView29.setText("Rs "+amount);



        split_from_decimal(amount);
        ValueConverters converter = ValueConverters.ENGLISH_INTEGER;
        String valueAsWords = converter.asWords(Integer.parseInt(amount));
        if(decimal_amt!=null){
            String valueAsWords1= converter.asWords(Integer.parseInt(decimal_amt));
            binding.textView33.setText("Rupees "+valueAsWords+" and "+valueAsWords1+" paise");
        }
        else
            binding.textView33.setText("Rupees "+valueAsWords);

        if(status!=null && status.equals("s"))
            push_data();

        if(status!=null)
            binding.validateLayout.setVisibility(View.GONE);
        else
            binding.validateLayout.setVisibility(View.VISIBLE);

        Intent intent=getIntent();
        String myValues="callFromSelectPackages_show_pay_success_membership";
        String compare=intent.getStringExtra("myValues");
        if(intent!=null && compare!=null)
        {

            if(compare.equals(myValues))
            {
                String email=intent.getStringExtra("email");
                String phNumber=intent.getStringExtra("phNumber");
                String s=intent.getStringExtra("s");
                String amount=intent.getStringExtra("amount");
                String coins=intent.getStringExtra("coins");
                String packages=intent.getStringExtra("packages");
                String status=intent.getStringExtra("status");
                String tnxStatus=intent.getStringExtra("transaction_status");
                double serviceAmount=intent.getDoubleExtra("serviceAmount",0.0);
                double Gst=intent.getDoubleExtra("gst",0.0);
                if(status.equals("success"))
                {
                    package_=packages;
                    binding.serviceCharge.setText("Rs "+serviceAmount);
                    binding.gst.setText("Rs "+Gst);
                    binding.textView34.setText(packages+" Package");
                    send_for_package(packages,coins);

                }
                else {
                    binding.textView14.setText("Payment UnSuccessful");
                    binding.textView21.setText("Bill Payment was unsuccessful!");
                    binding.animate.setAnimation("failed.json");
                    binding.textView28.setText("InActive");
                    binding.textView34.setText(packages+" Package");
                    binding.validateLayout.setVisibility(View.GONE);
                    binding.textView28.setBackgroundResource(R.drawable.bg_inactive);
                    binding.textView28.setTextColor(Color.parseColor("#FFFFFFFF"));
                    binding.serviceCharge.setText("Rs."+serviceAmount);
                    Log.d("ggst", "onCreate: "+Gst);
                    binding.gst.setText("Rs."+Gst);
                }
            }

        }
        binding.progressBar2.setVisibility(View.GONE);

        binding.textView36.setOnClickListener(v->{
            if(!binding.guidedby.getText().toString().trim().equals("")){
                binding.progressBar2.setVisibility(View.VISIBLE);
                binding.textView36.setVisibility(View.GONE);
                check_and_validate(binding.guidedby.getText().toString().trim());
            }
        });
    }

    private void check_and_validate(String code) {
        reference_bdsales.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String merchant_phone=snapshot.child(user.getUid()).child("Account_Information").child("phoneNumber").getValue(String.class)+"";
                if(snapshot.child("BDSales").child("hierarchy").child(code).exists()){ //For G-Code
                    if(snapshot.child("BDSales").child("hierarchy").child(code).child("total").exists()) {
                        //Updating  G-Code total
                        String total_g = snapshot.child("BDSales").child("hierarchy").child(code).child("total").getValue(String.class);
                        long g_total = Long.parseLong(total_g) + Long.parseLong(amount);
                        reference_bdsales.child("BDSales").child("hierarchy").child(code).child("total").setValue(g_total+"");
                        //Updating Main total
                        String total_sum=snapshot.child("BDSales").child("hierarchy").child("total").getValue(String.class);
                        long total_sum_final=Long.parseLong(total_sum)+Long.parseLong(amount);
                        reference_bdsales.child("BDSales").child("hierarchy").child("total").setValue(total_sum_final+"");
                    }
                    else {
                        //Updating Main total
                        String total_sum=snapshot.child("BDSales").child("hierarchy").child("total").getValue(String.class);
                        long total_sum_final=Long.parseLong(total_sum)+Long.parseLong(amount);
                        reference_bdsales.child("BDSales").child("hierarchy").child("total").setValue(total_sum_final+"");
                        //Setting total to G-Code
                        reference_bdsales.child("BDSales").child("hierarchy").child(code).child("total").setValue(amount);
                    }
                }
                else{ //For T-Code
                    int c=0;
                    for(DataSnapshot ds_g:snapshot.child("BDSales").child("hierarchy").getChildren()){
                        if(snapshot.child("BDSales").child("hierarchy").child(ds_g.getKey()).child(code).exists()){
                            if(snapshot.child("BDSales").child("hierarchy").child(ds_g.getKey()).child(code).child("total").exists()) {
                                c=1;
                                //updating T-Code total
                                String total_t = snapshot.child("BDSales").child("hierarchy").child(ds_g.getKey()).child(code).child("total").getValue(String.class);
                                long t_total = Long.parseLong(total_t) + Long.parseLong(amount);
                                reference_bdsales.child("BDSales").child("hierarchy").child(ds_g.getKey()).child(code).child("total").setValue(t_total+"");
                                //Updating G-Code total
                                if(snapshot.child("BDSales").child("hierarchy").child(ds_g.getKey()).child("total").exists()) {
                                    String sum_g = snapshot.child("BDSales").child("hierarchy").child(ds_g.getKey()).child("total").getValue(String.class);
                                    long sum_total_g = Long.parseLong(sum_g) + Long.parseLong(amount);
                                    reference_bdsales.child("BDSales").child("hierarchy").child(ds_g.getKey()).child("total").setValue(sum_total_g + "");
                                }
                                else{
                                    reference_bdsales.child("BDSales").child("hierarchy").child(ds_g.getKey()).child("total").setValue(amount);
                                }
                                //Updating Main total
                                String total_sum=snapshot.child("BDSales").child("hierarchy").child("total").getValue(String.class);
                                long total_sum_final=Long.parseLong(total_sum)+Long.parseLong(amount);
                                reference_bdsales.child("BDSales").child("hierarchy").child("total").setValue(total_sum_final+"");
                                break;
                            }
                            else {
                                c=1;
                                //Updating Main total
                                String total_sum=snapshot.child("BDSales").child("hierarchy").child("total").getValue(String.class);
                                long total_sum_final=Long.parseLong(total_sum)+Long.parseLong(amount);
                                reference_bdsales.child("BDSales").child("hierarchy").child("total").setValue(total_sum_final+"");
                                //Updating G-code total
                                if(snapshot.child("BDSales").child("hierarchy").child(ds_g.getKey()).child("total").exists()) {
                                    String sum_g = snapshot.child("BDSales").child("hierarchy").child(ds_g.getKey()).child("total").getValue(String.class);
                                    long sum_total_g = Long.parseLong(sum_g) + Long.parseLong(amount);
                                    reference_bdsales.child("BDSales").child("hierarchy").child(ds_g.getKey()).child("total").setValue(sum_total_g + "");
                                }
                                else{
                                    reference_bdsales.child("BDSales").child("hierarchy").child(ds_g.getKey()).child("total").setValue(amount);
                                }
                                //Setting T-code total
                                reference_bdsales.child("BDSales").child("hierarchy").child(ds_g.getKey()).child(code).child("total").setValue(amount);
                                break;
                            }
                        }
                    } // For E-Code
                    if (c == 0) {
                        for(DataSnapshot ds_g:snapshot.child("BDSales").child("hierarchy").getChildren()){
                            for(DataSnapshot ds_t:snapshot.child("BDSales").child("hierarchy").child(ds_g.getKey()).getChildren()){
                                if(snapshot.child("BDSales").child("hierarchy").child(ds_g.getKey()).child(ds_t.getKey()).child(code).exists()){
                                    if(snapshot.child("BDSales").child("hierarchy").child(ds_g.getKey()).child(ds_t.getKey()).child(code).child("total").exists()) {
                                        //Updating E-Code total
                                        String total_e = snapshot.child("BDSales").child("hierarchy").child(ds_g.getKey()).child(ds_t.getKey()).child(code).child("total").getValue(String.class);
                                        long e_total = Long.parseLong(total_e) + Long.parseLong(amount);
                                        reference_bdsales.child("BDSales").child("hierarchy").child(ds_g.getKey()).child(ds_t.getKey()).child(code).child("total").setValue(e_total+"");
                                        //Updating Main total
                                        String total_sum=snapshot.child("BDSales").child("hierarchy").child("total").getValue(String.class);
                                        long total_sum_final=Long.parseLong(total_sum)+Long.parseLong(amount);
                                        reference_bdsales.child("BDSales").child("hierarchy").child("total").setValue(total_sum_final+"");
                                        //Updating G-Code total
                                        if(snapshot.child("BDSales").child("hierarchy").child(ds_g.getKey()).child("total").exists()) {
                                            String sum_g = snapshot.child("BDSales").child("hierarchy").child(ds_g.getKey()).child("total").getValue(String.class);
                                            long sum_total_g = Long.parseLong(sum_g) + Long.parseLong(amount);
                                            reference_bdsales.child("BDSales").child("hierarchy").child(ds_g.getKey()).child("total").setValue(sum_total_g + "");
                                        }
                                        else{
                                            reference_bdsales.child("BDSales").child("hierarchy").child(ds_g.getKey()).child("total").setValue(amount);
                                        }
                                        //Updating T-Code total
                                        if(snapshot.child("BDSales").child("hierarchy").child(ds_g.getKey()).child(ds_t.getKey()).child("total").exists()) {
                                            String sum_t = snapshot.child("BDSales").child("hierarchy").child(ds_g.getKey()).child(ds_t.getKey()).child("total").getValue(String.class);
                                            long sum_total_t = Long.parseLong(sum_t) + Long.parseLong(amount);
                                            reference_bdsales.child("BDSales").child("hierarchy").child(ds_g.getKey()).child(ds_t.getKey()).child("total").setValue(sum_total_t + "");
                                        }
                                        else{
                                            reference_bdsales.child("BDSales").child("hierarchy").child(ds_g.getKey()).child(ds_t.getKey()).child("total").setValue(amount);
                                        }
                                        break;
                                    }
                                    else {
                                        //Setting E-Code total
                                        reference_bdsales.child("BDSales").child("hierarchy").child(ds_g.getKey()).child(ds_t.getKey()).child(code).child("total").setValue(amount);
                                        //Updating Main total
                                        String total_sum=snapshot.child("BDSales").child("hierarchy").child("total").getValue(String.class);
                                        long total_sum_final=Long.parseLong(total_sum)+Long.parseLong(amount);
                                        reference_bdsales.child("BDSales").child("hierarchy").child("total").setValue(total_sum_final+"");
                                        //Updating G-Code total
                                        if(snapshot.child("BDSales").child("hierarchy").child(ds_g.getKey()).child("total").exists()) {
                                            String sum_g = snapshot.child("BDSales").child("hierarchy").child(ds_g.getKey()).child("total").getValue(String.class);
                                            long sum_total_g = Long.parseLong(sum_g) + Long.parseLong(amount);
                                            reference_bdsales.child("BDSales").child("hierarchy").child(ds_g.getKey()).child("total").setValue(sum_total_g + "");
                                        }
                                        else{
                                            reference_bdsales.child("BDSales").child("hierarchy").child(ds_g.getKey()).child("total").setValue(amount);
                                        }
                                        //Updating T-Code total
                                        if(snapshot.child("BDSales").child("hierarchy").child(ds_g.getKey()).child(ds_t.getKey()).child("total").exists()) {
                                            String sum_t = snapshot.child("BDSales").child("hierarchy").child(ds_g.getKey()).child(ds_t.getKey()).child("total").getValue(String.class);
                                            long sum_total_t = Long.parseLong(sum_t) + Long.parseLong(amount);
                                            reference_bdsales.child("BDSales").child("hierarchy").child(ds_g.getKey()).child(ds_t.getKey()).child("total").setValue(sum_total_t + "");
                                        }
                                        else{
                                            reference_bdsales.child("BDSales").child("hierarchy").child(ds_g.getKey()).child(ds_t.getKey()).child("total").setValue(amount);
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                if(snapshot.child("BDSales").child(code).exists()){
                    String name=snapshot.child("BDSales").child(code).child("name").getValue(String.class);
                    if(snapshot.child(user.getUid()).child("package_info").exists()){
                        long total1=snapshot.child(user.getUid()).child("package_info").getChildrenCount();
                        reference.child(user.getUid()).child("package_info").child((total1-1)+"").child("sales_name").setValue(name);
                    }
                    else{
                        reference.child(user.getUid()).child("package_info").child("0").child("sales_name").setValue(name);
                    }

                    if(snapshot.child("BDSales").child(code).child("Providers").child(merchant_phone).child("package_info").exists()){
                        long total=snapshot.child("BDSales").child(code).child("Providers").child(merchant_phone).child("package_info").getChildrenCount();

                        reference_bdsales.child("BDSales").child(code).child("Providers").child(merchant_phone).child("package_info").child(total+"").child("package_name").setValue(package_);
                        reference_bdsales.child("BDSales").child(code).child("Providers").child(merchant_phone).child("package_info").child(total+"").child("sales_name").setValue(name);
                        reference_bdsales.child("BDSales").child(code).child("Providers").child(merchant_phone).child("package_info").child(total+"").child("order_amount").setValue(amount);
                        reference_bdsales.child("BDSales").child(code).child("Providers").child(merchant_phone).child("package_info").child(total+"").child("txn_date").setValue(str_date);
                        reference_bdsales.child("BDSales").child(code).child("Providers").child(merchant_phone).child("package_info").child(total+"").child("time").setValue(str_time);

                    }
                    else{
                        reference_bdsales.child("BDSales").child(code).child("Providers").child(merchant_phone).child("package_info").child("0").child("package_name").setValue(package_);
                        reference_bdsales.child("BDSales").child(code).child("Providers").child(merchant_phone).child("package_info").child("0").child("sales_name").setValue(name);
                        reference_bdsales.child("BDSales").child(code).child("Providers").child(merchant_phone).child("package_info").child("0").child("order_amount").setValue(amount);
                        reference_bdsales.child("BDSales").child(code).child("Providers").child(merchant_phone).child("package_info").child("0").child("txn_date").setValue(str_date);
                        reference_bdsales.child("BDSales").child(code).child("Providers").child(merchant_phone).child("package_info").child("0").child("time").setValue(str_time);
                    }
                    binding.guidedby.setText(name);
                    binding.guidedby.setEnabled(false);
                    binding.textView36.setText("Validated");
                    Toast.makeText(pay_screen.this, "Validated Code", Toast.LENGTH_SHORT).show();
                }
                else{
                    binding.progressBar2.setVisibility(View.GONE);
                    binding.guidedby.setVisibility(View.VISIBLE);
                    Toast.makeText(pay_screen.this, "Invalid Code", Toast.LENGTH_SHORT).show();
                }
                binding.progressBar2.setVisibility(View.GONE);
                binding.textView36.setVisibility(View.GONE);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void send_for_package(String package_,String coins) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        if(package_.equals("Premium"))
        {
            cal.add(Calendar.MONTH, 3);
        }
        else if(package_.equals("Standard")) {
            cal.add(Calendar.MONTH, 1);
        }
        reference= FirebaseDatabase.getInstance().getReference().child("Merchant_data");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(user.getUid()).child("Account_Information").child("wallet").exists()){
                    String pre_coins=snapshot.child(user.getUid()).child("Account_Information").child("wallet").getValue(String.class);
                    long coins_total=Long.parseLong(pre_coins)+Long.parseLong(coins);
                    reference.child(user.getUid()).child("Account_Information").child("wallet").setValue(coins_total+"");
                }
                else
                    reference.child(user.getUid()).child("Account_Information").child("wallet").setValue(coins+"");

                if(snapshot.child(user.getUid()).child("package_info").exists()){
                    long total=snapshot.child(user.getUid()).child("package_info").getChildrenCount();

                    reference.child(user.getUid()).child("package_info").child(total+"").child("sales_name").setValue("");
                    reference.child(user.getUid()).child("package_info").child(total+"").child("package_name").setValue(package_);
                    reference.child(user.getUid()).child("package_info").child(total+"").child("order_amount").setValue(amount);
                    reference.child(user.getUid()).child("package_info").child(total+"").child("txn_date").setValue(str_date);
                    reference.child(user.getUid()).child("package_info").child(total+"").child("time").setValue(str_time);

                }
                else{
                    reference.child(user.getUid()).child("package_info").child("0").child("sales_name").setValue("");
                    reference.child(user.getUid()).child("package_info").child("0").child("package_name").setValue(package_);
                    reference.child(user.getUid()).child("package_info").child("0").child("order_amount").setValue(amount);
                    reference.child(user.getUid()).child("package_info").child("0").child("txn_date").setValue(str_date);
                    reference.child(user.getUid()).child("package_info").child("0").child("time").setValue(str_time);
                }
                //for membership
                if(snapshot.child(user.getUid()).child("membership").exists()){
                    long total=snapshot.child(user.getUid()).child("membership").getChildrenCount();

                    String expiry=snapshot.child(user.getUid()).child("membership").child("expiry").getValue(String.class);
                    try {
                        if(package_.equals("Premium")) {
                            String date= addMonths(expiry,3);
                            binding.expiryDate.setText(date);
                            reference.child(user.getUid()).child("membership").child(total + "").child("expiry").setValue(date+ "");
                            reference.child(user.getUid()).child("membership").child("expiry").setValue(date+ "");
                        }
                        else if(package_.equals("Standard")){
                            String date= addMonths(expiry,1);
                            binding.expiryDate.setText(date);
                            reference.child(user.getUid()).child("membership").child(total+"").child("expiry").setValue(date +"");
                            reference.child(user.getUid()).child("membership").child("expiry").setValue(date +"");
                        }
                        else{
                            binding.expiryDate.setText(expiry);
                            reference.child(user.getUid()).child("membership").child(total+"").child("expiry").setValue(expiry);
                            reference.child(user.getUid()).child("membership").child("expiry").setValue(expiry);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    reference.child(user.getUid()).child("membership").child(total+"").child("plan").setValue("month");
                    reference.child(user.getUid()).child("membership").child(total+"").child("order_amount").setValue(amount);
                    reference.child(user.getUid()).child("membership").child(total+"").child("txn_date").setValue(str_date);
                    reference.child(user.getUid()).child("membership").child(total+"").child("time").setValue(str_time);
                    reference.child(user.getUid()).child("membership").child(total+"").child("orderId").setValue(orderid);
                    reference.child(user.getUid()).child("membership").child(total+"").child("transaction_status").setValue("TXN_SUCCESS");
                    reference.child(user.getUid()).child("membership").child(total+"").child("timestamp").setValue(timestamp.getTime());
                }
                else {
                    reference.child(user.getUid()).child("membership").child("0").child("plan").setValue("month");
                    reference.child(user.getUid()).child("membership").child("0").child("order_amount").setValue(amount);
                    reference.child(user.getUid()).child("membership").child("0").child("expiry").setValue(sdf.format(cal.getTime()) + "");
                    reference.child(user.getUid()).child("membership").child("expiry").setValue(sdf.format(cal.getTime()) + "");
                    reference.child(user.getUid()).child("membership").child("0").child("txn_date").setValue(str_date);
                    reference.child(user.getUid()).child("membership").child("0").child("time").setValue(str_time);
                    reference.child(user.getUid()).child("membership").child("0").child("orderId").setValue(orderid);
                    reference.child(user.getUid()).child("membership").child("0").child("transaction_status").setValue("TXN_SUCCESS");
                    reference.child(user.getUid()).child("membership").child("0").child("timestamp").setValue(timestamp.getTime());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void push_data() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        if(plan.equals("month"))
            cal.add(Calendar.MONTH, 1);
        else
            cal.add(Calendar.YEAR, 1);
        reference= FirebaseDatabase.getInstance().getReference().child("Merchant_data");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(user.getUid()).child("membership").exists()){
                    long total=snapshot.child(user.getUid()).child("membership").getChildrenCount();
                    try {
                        String expiry = snapshot.child(user.getUid()).child("membership").child("expiry").getValue(String.class);
                        Date date11 = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                        Date date1 = sdf.parse(expiry + "");
                        Date date2 = sdf.parse(sdf.format(date11));
                        if (date1.compareTo(date2) > 0) {
                            if (plan.equals("month")) {
                                try {
                                    String date = addMonths(expiry, 1);
                                    binding.expiryDate.setText(date);
                                    reference.child(user.getUid()).child("membership").child(total + "").child("expiry").setValue(date + "");
                                    reference.child(user.getUid()).child("membership").child("expiry").setValue(date + "");
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            } else if (plan.equals("year")) {
                                try {
                                    String date = addyears(expiry, 1);
                                    binding.expiryDate.setText(date);
                                    reference.child(user.getUid()).child("membership").child(total + "").child("expiry").setValue(date + "");
                                    reference.child(user.getUid()).child("membership").child("expiry").setValue(date + "");
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        else {
                            binding.expiryDate.setText(sdf.format(cal.getTime())+"");
                            reference.child(user.getUid()).child("membership").child(total+"").child("expiry").setValue(sdf.format(cal.getTime()) + "");
                            reference.child(user.getUid()).child("membership").child("expiry").setValue(sdf.format(cal.getTime()) + "");
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    reference.child(user.getUid()).child("membership").child(total+"").child("plan").setValue(plan);
                    reference.child(user.getUid()).child("membership").child(total+"").child("order_amount").setValue(amount);
                    reference.child(user.getUid()).child("membership").child(total+"").child("txn_date").setValue(str_date);
                    reference.child(user.getUid()).child("membership").child(total+"").child("time").setValue(str_time);
                    reference.child(user.getUid()).child("membership").child(total+"").child("orderId").setValue(orderid);
                    reference.child(user.getUid()).child("membership").child(total+"").child("transaction_status").setValue("TXN_SUCCESS");
                    reference.child(user.getUid()).child("membership").child(total+"").child("timestamp").setValue(timestamp.getTime());
                }
                else {
                    binding.expiryDate.setText(sdf.format(cal.getTime()));
                    reference.child(user.getUid()).child("membership").child("0").child("plan").setValue(plan);
                    reference.child(user.getUid()).child("membership").child("0").child("order_amount").setValue(amount);
                    reference.child(user.getUid()).child("membership").child("0").child("expiry").setValue(sdf.format(cal.getTime()) + "");
                    reference.child(user.getUid()).child("membership").child("expiry").setValue(sdf.format(cal.getTime()) + "");
                    reference.child(user.getUid()).child("membership").child("0").child("txn_date").setValue(str_date);
                    reference.child(user.getUid()).child("membership").child("0").child("time").setValue(str_time);
                    reference.child(user.getUid()).child("membership").child("0").child("orderId").setValue(orderid);
                    reference.child(user.getUid()).child("membership").child("0").child("transaction_status").setValue("TXN_SUCCESS");
                    reference.child(user.getUid()).child("membership").child("0").child("timestamp").setValue(timestamp.getTime());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void split_from_decimal(String amt) {
        for(int i=0; i<amt.length();i++){
            if(amt.charAt(i)=='.'){
                amount=amt.substring(0,i);
                decimal_amt=amt.substring(i+1);
            }
        }
    }


    public static void packagesData(String email,String phNumber,String orderid,String amount,String coins,String packages,double gst,String statusSF)
    {

       if(statusSF.equals("success"))
       {
           Calendar cal = Calendar.getInstance();
           FirebaseAuth auth=FirebaseAuth.getInstance();
           DatabaseReference reference;
           FirebaseUser user=auth.getCurrentUser();

           SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
           if(packages.equals("gold"))
           {
               cal.add(Calendar.MONTH, 3);
           }
           else if(packages.equals("silver")) {
               cal.add(Calendar.MONTH, 1);
           }
           reference= FirebaseDatabase.getInstance().getReference().child("Merchant_data");
           reference.child(user.getUid()).child("Account_Information").child("wallet").setValue(coins);
           reference.child(user.getUid()).child("Account_Information").child("package").setValue(packages);
           reference.child(user.getUid()).child("membership").child("order_amount").setValue(amount);
           reference.child(user.getUid()).child("membership").child("expiry").setValue(sdf.format(cal.getTime())+"");
           reference.child(user.getUid()).child("membership").child("date").setValue(str_date);
           reference.child(user.getUid()).child("membership").child("time").setValue(str_time);
           reference.child(user.getUid()).child("membership").child("orderId").setValue(orderid);
           reference.child(user.getUid()).child("membership").child("transaction_status").setValue("TXN_SUCCESS");
           reference.child(user.getUid()).child("membership").child("timestamp").setValue(timestamp.getTime());
       }
       else {
           binding.textView14.setText("Payment UnSuccessful");
           binding.textView21.setText("Bill Payment was unsuccessful!");
           binding.animate.setAnimation("failed.json");
           binding.textView28.setText("InActive");
           binding.textView28.setBackgroundResource(R.drawable.bg_inactive);
           binding.textView28.setTextColor(Color.parseColor("#FFFFFFFF"));
       }
    }
    public static String  addMonths(String dateAsString, int nbMonths) throws ParseException {
        String format = "dd MMM yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date dateAsObj = sdf.parse(dateAsString);
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateAsObj);
        cal.add(Calendar.MONTH, nbMonths);
        Date dateAsObjAfterAMonth = cal.getTime();
        return sdf.format(dateAsObjAfterAMonth);
    }
    public static String  addyears(String dateAsString, int nbMonths) throws ParseException {
        String format = "dd MMM yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date dateAsObj = sdf.parse(dateAsString);
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateAsObj);
        cal.add(Calendar.YEAR, nbMonths);
        Date dateAsObjAfterAMonth = cal.getTime();
        return sdf.format(dateAsObjAfterAMonth);
    }
}
