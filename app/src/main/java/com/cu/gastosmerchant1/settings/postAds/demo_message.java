package com.cu.gastosmerchant1.settings.postAds;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cu.gastosmerchant1.databinding.ActivityDemoMessageBinding;
import com.cu.gastosmerchant1.settings.confirmation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class demo_message extends AppCompatActivity {

    ActivityDemoMessageBinding binding;
    private Timestamp timestamp;
    private String str_date, str_time;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityDemoMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        reference= FirebaseDatabase.getInstance().getReference().child("demo_messages");
        timestamp = new Timestamp(System.currentTimeMillis());

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        //sender - user.getphonenumber()
        //timestamp - timestamp.gettime()
        //receiver - edittext.gettext().tostring()
        // Date
        // time

        binding.imageView.setOnClickListener(v->{
            finish();
        });

        binding.next.setOnClickListener(v->{
            if(!binding.phnumber.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Sending...", Toast.LENGTH_SHORT).show();
                String api_str="https://cashfree-server-production.up.railway.app/api/campaign-conformation";
                httpCall(api_str,binding.phnumber.getText().toString().trim());
            }
        });
    }
    public void httpCall(String url,String number) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("phoneNumber",number);
            jsonBody.put("textOne","Your ad request");
            jsonBody.put("textTwo","within 48 hrs");
            Log.d("body", "httpCall_collect: " + jsonBody);
        } catch (Exception e) {
            Log.e("Error", "JSON ERROR");
        }
        Format f = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
        str_time = f.format(new Date());

        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        str_date = formatter.format(new Date());


        RequestQueue queue = Volley.newRequestQueue(demo_message.this);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // enjoy your response
                        String pushkey=user.getUid()+reference.push().getKey();
                        Map<String,String> data=new HashMap<>();
                        data.put("sender",user.getPhoneNumber().substring(1));
                        data.put("timestamp",timestamp.getTime()+"");
                        data.put("receiver","91"+number);
                        data.put("date",str_date);
                        data.put("time",str_time);
                        data.put("pushkey",pushkey);
                        data.put("status","false");
                        data.put("uid",user.getUid()+"");
                        reference.child(pushkey).setValue(data);
                        Intent intent=new Intent(demo_message.this, confirmation.class);
                        intent.putExtra("sending_phone_number",binding.phnumber.getText().toString());
                        startActivity(intent);
                        Log.e("response", ""+response);
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
}