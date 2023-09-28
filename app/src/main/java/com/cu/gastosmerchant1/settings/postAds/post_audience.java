package com.cu.gastosmerchant1.settings.postAds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cu.gastosmerchant1.R;
import com.cu.gastosmerchant1.databinding.ActivityPostAudienceBinding;
import com.cu.gastosmerchant1.registration.GetStates;
import com.cu.gastosmerchant1.registration.State;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import it.sephiroth.android.library.rangeseekbar.RangeSeekBar;

public class post_audience extends AppCompatActivity {

    ActivityPostAudienceBinding binding;
    private ArrayList<String> categories = new ArrayList<>();
    String type,content;
    private ArrayList<String> locations = new ArrayList<>();
    private ArrayList<String> states = new ArrayList<>();
    String url_city = "https://raw.githubusercontent.com/zimmy9537/Indian-States-And-Districts/master/states-and-districts.json";
    String city_json;
    ArrayList<State> statesArrayList = new ArrayList<>();
    private Spinner categoryDropDown, locationDropDown, stateDropDown;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    String shop_state,shop_name;
    String selectedFileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding= ActivityPostAudienceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        type=getIntent().getStringExtra("sending_type");
        content=getIntent().getStringExtra("sending_content");
        selectedFileUri=getIntent().getStringExtra("sending_file_uri");

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        binding.imageView.setOnClickListener(v->{
            finish();
        });
        reference=FirebaseDatabase.getInstance().getReference().child("Merchant_data");
        check_sales_code();
        binding.AddData.setOnClickListener(v->{
            Intent intent=new Intent(post_audience.this, demo_message.class);
            startActivity(intent);
        });

        binding.seekBarLuminosite.setMax(50);
        binding.seekBarLuminosite.setProgress(7);
        fillCategories(categories);
        binding.years.setText((binding.rangeSeekBar.getProgressStart()+18)+" To "+(binding.rangeSeekBar.getProgressEnd()+18) +" Yrs");

        binding.seekBarLuminosite.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(i<5){
                    binding.seekBarLuminosite.setProgress(5);
                }
                else {
                    binding.km.setText(i + " Km");
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        binding.rangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(
                    final RangeSeekBar seekBar, final int progressStart, final int progressEnd, final boolean fromUser) {
//                if(progressStart<18)
//                    binding.years.setText(18+" To "+progressEnd+" Yrs");
//                else
                    binding.years.setText((progressStart+18)+" To "+(progressEnd+18) +" Yrs");
            }

            @Override
            public void onStartTrackingTouch(final RangeSeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(final RangeSeekBar seekBar) { }
        });

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(post_audience.this, R.layout.support_simple_spinner_dropdown_item, categories) {
            @Override
            public int getCount() {
                return super.getCount();
            }
        };

        categoryAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        binding.categorySpinner.setAdapter(categoryAdapter);
        binding.categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                binding.selectcat.setText(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //do nothing
            }
        });

        binding.next.setOnClickListener(v->{
            Intent intent=new Intent(post_audience.this, budget.class);
            if(!binding.selectcity.getText().toString().equals("Select City")){
                if(!binding.selectcat.getText().toString().equals("")){
                    //
                    intent.putExtra("sending_type",type);
                    intent.putExtra("sending_file_uri", selectedFileUri);
                    intent.putExtra("sending_content",content);
                    intent.putExtra("sending_shop_name",shop_name);
                    Log.e("showing",type+"");
                    //
                    intent.putExtra("sending_city",binding.selectcity.getText().toString());
                    intent.putExtra("sending_category",binding.selectcat.getText().toString());
                    intent.putExtra("sending_reach",binding.seekBarLuminosite.getProgress()+"");
//                    if(binding.rangeSeekBar.getProgressStart()<18)
//                        intent.putExtra("sending_start_age",18+"");
//                    else
                        intent.putExtra("sending_start_age",(binding.rangeSeekBar.getProgressStart()+18)+"");
                    intent.putExtra("sending_end_age",(binding.rangeSeekBar.getProgressEnd()+18)+"");
                    startActivity(intent);
                }
                else{
                    Snackbar.make(binding.relativeLayout3,"Please Select Your Category",Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.parseColor("#ea4a1f"))
                            .setTextColor(Color.parseColor("#000000"))
                            .setBackgroundTint(Color.parseColor("#D9F5F8"))
                            .show();
                }
            }
            else {
                Snackbar.make(binding.relativeLayout3,"Please Select Your City",Snackbar.LENGTH_LONG)
                        .setActionTextColor(Color.parseColor("#ea4a1f"))
                        .setTextColor(Color.parseColor("#000000"))
                        .setBackgroundTint(Color.parseColor("#D9F5F8"))
                        .show();
            }
        });
        getState_of_merchant();
    }

    private void check_sales_code() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(user.getUid()).child("Account_Information").child("salesCode").getValue(String.class)!=null) {
                    if (snapshot.child(user.getUid()).child("Account_Information").child("salesCode").getValue(String.class).equals("Test Code"))
                        binding.AddData.setVisibility(View.VISIBLE);
                    else
                        binding.AddData.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void fillCategories(ArrayList<String> arr) {
        arr.add("Select Category");
        arr.add("Food & Beverages");
        arr.add("Fashion");
        arr.add("Salon & spa");
        arr.add("Stores");
        arr.add("Medical");
        arr.add("others");
    }

    void getCityData() {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_city, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                city_json = response;

                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                GetStates getStates = gson.fromJson(city_json, GetStates.class);

                statesArrayList = new ArrayList<>();
                statesArrayList = getStates.getStates();

                State state0 = new State();
                ArrayList<String> state0arrayList=new ArrayList<>();
                state0arrayList.add("Select Shop City");
                state0.setState("Select Shop State");
                state0.setDistricts(state0arrayList);

                statesArrayList.add(0, state0);

                states = new ArrayList<>();
                for (State state : statesArrayList) {
                        if (state.getState().equals(shop_state)) {
                            locations = new ArrayList<>();
                            locations = state.getDistricts();
                            Log.e("stt",state.getDistricts()+"");
                            locations.add(0, "Select Shop City");
                        }
                }

                ArrayAdapter<String> locationAdapter = new ArrayAdapter<String>(post_audience.this, R.layout.support_simple_spinner_dropdown_item, locations);
                locationAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                binding.citySpinner.setAdapter(locationAdapter);
                binding.citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        binding.selectcity.setText(adapterView.getItemAtPosition(i).toString());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("errorResponse", error.getMessage());
            }
        });

        queue.add(stringRequest);

    }

    private void getState_of_merchant() {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Merchant_data");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                shop_state = snapshot.child(user.getUid()).child("Shop_Information").child("shopState").getValue(String.class);
                shop_name = snapshot.child(user.getUid()).child("Shop_Information").child("shopName").getValue(String.class);
                Log.e("shop",shop_state+"");
                Log.e("shop",shop_name+"");
                getCityData();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}