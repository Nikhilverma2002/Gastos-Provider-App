package com.cu.gastosmerchant1.settings.postAds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cu.gastosmerchant1.databinding.ActivityAdPaymentBinding;
import com.cu.gastosmerchant1.registration.CashFree_Activity;
import com.cu.gastosmerchant1.settings.payment_status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ad_payment extends AppCompatActivity {

    ActivityAdPaymentBinding binding;
    double total_charge,wallet_value,savings;
    int total_coustomer,amount;
    FirebaseAuth auth;
    boolean applied;
    FirebaseUser user;
    String email_info,phone;
    double balance=0.0;
    String selectedFileUri,file_link;
    double dis;
    private String str_date, str_time;
    String city,category,reach,start_age,end_age,sending_shop_name;
    String type,content;
    DatabaseReference promotion_reference;
    private Timestamp timestamp;
    ProgressDialog  pd;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        type=getIntent().getStringExtra("sending_type");
        content=getIntent().getStringExtra("sending_content");
        total_charge=getIntent().getDoubleExtra("amt_of_customers",2000.00);
        total_coustomer=getIntent().getIntExtra("num_of_customers",4000);
        city=getIntent().getStringExtra("sending_city");
        category=getIntent().getStringExtra("sending_category");
        reach=getIntent().getStringExtra("sending_reach");
        start_age=getIntent().getStringExtra("sending_start_age");
        end_age=getIntent().getStringExtra("sending_end_age");
        sending_shop_name=getIntent().getStringExtra("sending_shop_name");
        selectedFileUri=getIntent().getStringExtra("sending_file_uri");

        binding=ActivityAdPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        promotion_reference=FirebaseDatabase.getInstance().getReference().child("Promotion_Data");
        timestamp = new Timestamp(System.currentTimeMillis());
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        binding.imageView.setOnClickListener(v->{
            finish();
        });

        get_wallet();

        binding.txt.setText("Your campaign will be live in next 24-48 hrs\nreach expected : "+total_coustomer+" Customers");

        dis=0.18*total_charge;
        binding.gstAmt.setText("₹"+Math.round(dis));
        binding.totalAmt.setText("Rs"+(dis+total_charge)+"");
        amount= (int) Math.ceil(dis+total_charge);
        savings=0;
        binding.servTotal.setText("₹"+total_charge+"");
        binding.switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    applied=true;
                    binding.est.setVisibility(View.VISIBLE);
                    binding.total.setVisibility(View.VISIBLE);
                    if(total_charge<balance){
                        double total=balance-total_charge;
                        amount=0;
                        savings=total_charge;
                        binding.total.setText("-"+total_charge);
                        wallet_value=Math.round(total);
                        binding.total.setText("-"+total_charge);
                        binding.gstAmt.setText("₹0");
                        binding.totalAmt.setText("Rs 0");
                    }
                    else{
                        double total=total_charge-balance;
                        dis=0.18*total;
                        wallet_value=0;
                        savings=balance;
                        binding.total.setText("-"+balance);
                        amount= (int) Math.ceil(dis+total);
                        binding.gstAmt.setText("₹"+ Math.round(dis));
                        binding.totalAmt.setText("Rs"+(dis+total)+"");
                    }
                }
                else {
                    applied=false;
                    binding.est.setVisibility(View.GONE);
                    binding.total.setVisibility(View.GONE);
                    dis=0.18*total_charge;
                    savings=0;
                    amount= (int) Math.ceil(dis+total_charge);
                    binding.gstAmt.setText("₹"+Math.round(dis));
                    binding.totalAmt.setText("Rs"+(dis+total_charge)+"");
                }
            }
        });
        get_details();
        binding.next.setOnClickListener(v->{
            if(amount!=0) {
                Intent intent = new Intent(ad_payment.this, CashFree_Activity.class);
                if (email_info != null && phone != null) {
                    intent.putExtra("amount", String.valueOf(amount));
                    //intent.putExtra("amount", 1+"");
                    intent.putExtra("phoneNumber", phone);
                    intent.putExtra("change_Screen", 1);
                    intent.putExtra("email", email_info);
                    //
                    intent.putExtra("sending_type", type);
                    intent.putExtra("applied", applied);
                    intent.putExtra("sending_discount", dis);
                    intent.putExtra("sending_prev_balance", balance);
                    intent.putExtra("sending_wallet_amount", wallet_value);
                    intent.putExtra("sending_content", content);
                    intent.putExtra("amt_of_customers", total_charge);
                    intent.putExtra("sending_city", city);
                    intent.putExtra("sending_category", category);
                    intent.putExtra("sending_reach", reach);
                    intent.putExtra("sending_start_age", start_age);
                    intent.putExtra("sending_end_age", end_age);
                    intent.putExtra("num_of_customers", total_coustomer);
                    intent.putExtra("sending_shop_name", sending_shop_name);
                    intent.putExtra("sending_savings", savings);
                    intent.putExtra("sending_file_uri", selectedFileUri);
                    //
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    //intent to cash free
                    startActivity(intent);
                }
            }
            else{
                Format f = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
                str_time = f.format(new Date());

                SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
                str_date = formatter.format(new Date());

                update_wallet(wallet_value+"");
                String pushkey=user.getUid()+promotion_reference.push().getKey();
                if(selectedFileUri!=null)
                    upload_file(selectedFileUri,pushkey);
                promotion_reference.child(pushkey).child("amount").setValue("0");
                promotion_reference.child(pushkey).child("phoneNumber").setValue(phone);
                promotion_reference.child(pushkey).child("email").setValue(email_info);
                promotion_reference.child(pushkey).child("type").setValue(type);
                promotion_reference.child(pushkey).child("date").setValue(str_date);
                promotion_reference.child(pushkey).child("time").setValue(str_time);
                promotion_reference.child(pushkey).child("timestamp").setValue(timestamp.getTime());
                promotion_reference.child(pushkey).child("status").setValue("false");
                switch (content) {
                    case "1":
                        promotion_reference.child(pushkey).child("content").child("discount").setValue("10");
                        promotion_reference.child(pushkey).child("content").child("min_order").setValue("1000");
                        promotion_reference.child(pushkey).child("content").child("shop_name").setValue(sending_shop_name);
                        break;
                    case "2":
                        promotion_reference.child(pushkey).child("content").child("discount").setValue("20");
                        promotion_reference.child(pushkey).child("content").child("min_order").setValue("3000");
                        promotion_reference.child(pushkey).child("content").child("shop_name").setValue(sending_shop_name);
                        break;
                    case "3":
                        promotion_reference.child(pushkey).child("content").child("discount").setValue("30");
                        promotion_reference.child(pushkey).child("content").child("min_order").setValue("10000");
                        promotion_reference.child(pushkey).child("content").child("shop_name").setValue(sending_shop_name);
                        break;
                    default:
                        promotion_reference.child(pushkey).child("content").setValue(content);
                        break;
                }
                promotion_reference.child(pushkey).child("total_charge").setValue(total_charge);
                promotion_reference.child(pushkey).child("city").setValue(city);
                promotion_reference.child(pushkey).child("category").setValue(category);
                promotion_reference.child(pushkey).child("reach").setValue(reach);
                promotion_reference.child(pushkey).child("start_age").setValue(start_age);
                promotion_reference.child(pushkey).child("end_age").setValue(end_age);
                promotion_reference.child(pushkey).child("total_customer").setValue(total_coustomer);
                promotion_reference.child(pushkey).child("key").setValue(pushkey);
                promotion_reference.child(pushkey).child("uid").setValue(user.getUid());
                pd.setTitle("Sending Confirmation");
                pd.setMessage("Please Wait!");
                String api_str="https://cashfree-server-production.up.railway.app/api/welcome-message";
                httpCall(api_str,phone);
            }
        });

    }

    private void upload_file(String selectedImageUri, String pushkey) {

        pd = new ProgressDialog(this);
        pd.setTitle("WhatsApp File");
        pd.setCancelable(false);
        pd.setMessage("Uploading Please Wait!");
        pd.setIndeterminate(true);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();

        String pdfstamp = user.getUid()+pushkey;
        String pdfpath = "WhatsappFiles/";
        StorageReference storageReference1 = FirebaseStorage.getInstance().getReference().child(pdfpath);
        final StorageReference filepath = storageReference1.child(pdfstamp);
        filepath.putFile(Uri.parse(selectedImageUri))
                .addOnSuccessListener(taskSnapshot1 ->
                        taskSnapshot1.getStorage().getDownloadUrl().addOnCompleteListener(
                                task1 -> {
                                    file_link = Objects.requireNonNull(task1.getResult()).toString();
                                    promotion_reference.child(pushkey).child("file_link").setValue(file_link);
                                }));
    }

    public void httpCall(String url,String number) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("phoneNumber",number);
            jsonBody.put("textOne","new audience");
            jsonBody.put("textTwo","Services");
            Log.d("body", "httpCall_collect: " + jsonBody);
        } catch (Exception e) {
            Log.e("Error", "JSON ERROR");
        }

        RequestQueue queue = Volley.newRequestQueue(ad_payment.this);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // enjoy your response
                        Log.e("response", ""+response);
                        if( ad_payment.this!=null && !ad_payment.this.isFinishing()) {
                            pd.dismiss();
                        }
                        Intent intent = new Intent(ad_payment.this, payment_status.class);
                        intent.putExtra("amount", "0");
                        intent.putExtra("sending_from_payment_gateway", "ad_payment");
                        intent.putExtra("email", email_info);
                        intent.putExtra("phoneNumber", phone);
                        intent.putExtra("balance", balance);
                        intent.putExtra("amt_of_customers", total_charge);
                        intent.putExtra("dis", dis);
                        intent.putExtra("applied", applied);
                        intent.putExtra("sending_shop_name", sending_shop_name);
                        intent.putExtra("sending_savings", savings);
                        intent.putExtra("amt_of_customers", total_charge);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // enjoy your error status
                Log.e("Status of code = ", "Wrong");
            }
        });
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 15000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 15000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
            }
        });
        Log.d("string", stringRequest.toString());
        queue.add(stringRequest);
    }

    private void update_wallet(String value) {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Merchant_data");
        //TODO: What if uid data is not present?
        reference.child(user.getUid()).child("Account_Information").child("walletPromotion").setValue(value);
    }
    void get_wallet(){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Merchant_data");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String wallet=snapshot.child(user.getUid()).child("Account_Information").child("walletPromotion").getValue(String.class);
                if(wallet!=null){
                    balance=Double.parseDouble(wallet);
                    //binding.total.setText("-"+wallet);
                    binding.balanceWallet.setText("Balance : "+wallet);
                }
                else{
                    binding.total.setText("-0");
                    binding.balanceWallet.setText("Balance : 0");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    void get_details(){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Merchant_data");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                email_info=snapshot.child(user.getUid()).child("Account_Information").child("emailAddress").getValue(String.class);
                phone=snapshot.child(user.getUid()).child("Account_Information").child("phoneNumber").getValue(String.class);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}