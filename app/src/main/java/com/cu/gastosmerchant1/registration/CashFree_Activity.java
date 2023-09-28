package com.cu.gastosmerchant1.registration;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cashfree.pg.api.CFPaymentGatewayService;
import com.cashfree.pg.core.api.CFSession;
import com.cashfree.pg.core.api.CFTheme;
import com.cashfree.pg.core.api.callback.CFCheckoutResponseCallback;
import com.cashfree.pg.core.api.exception.CFException;
import com.cashfree.pg.core.api.upi.CFUPI;
import com.cashfree.pg.core.api.upi.CFUPIPayment;
import com.cashfree.pg.core.api.utils.CFErrorResponse;
import com.cashfree.pg.ui.api.CFDropCheckoutPayment;
import com.cashfree.pg.ui.api.CFPaymentComponent;
import com.cu.gastosmerchant1.Dashboard.pay_screen;
import com.cu.gastosmerchant1.PaymentStatus;
import com.cu.gastosmerchant1.R;
import com.cu.gastosmerchant1.settings.payment_status;
import com.furkanakdemir.surroundcardview.SurroundCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

import kotlin.jvm.internal.Intrinsics;

public class CashFree_Activity extends AppCompatActivity {

    String city,category,reach,start_age,end_age,sending_shop_name;
    String type,content,from_mem_bill;
    static double balance=0.0;
    static double wallet_value;
    static double dis;
    static double savings;
    static double service_amt;
    double total_charge;
    FirebaseAuth auth;
    FirebaseUser user;
    String selectedFileUri;
    int total_coustomer,change_Screen;
    ImageView prev;
    String phone, email, amount, promoCodeString,coins,_package_;
    boolean isPromoApplied;
    DatabaseReference promotion_reference;
    //changes
    SurroundCardView google_pay, amazon_pay, phone_pay, bhim, paytm, mobikwik;
    String package_name;
    long oId;
    boolean applied;
    Dialog dialog;
    String plan,selectPackages;
    Intent unique;
    static Context context;
    CFSession.Environment cfEnvironment = CFSession.Environment.PRODUCTION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_free);
        context=getApplicationContext();
        phone = getIntent().getStringExtra("phoneNumber");
        email = getIntent().getStringExtra("email");
        amount = getIntent().getStringExtra("amount");

        from_mem_bill = getIntent().getStringExtra("from_bill_payments");
        plan=getIntent().getStringExtra("sending_plan");
        _package_=getIntent().getStringExtra("sending_package");
        service_amt = getIntent().getDoubleExtra("sending_service_amt",0.0);

        isPromoApplied = getIntent().getBooleanExtra("isPromoApplied", false);
        if (isPromoApplied) {
            promoCodeString = getIntent().getStringExtra("promoCode");
            Log.v("cash free", promoCodeString);
        }

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();



        unique=getIntent();
        selectPackages="selectPackages";
        if(unique!=null)
        {
            if(selectPackages.equals(unique.getStringExtra("checkMe")))
            {
                amount=unique.getStringExtra("amount");
                phone=unique.getStringExtra("phoneNumber");
                dis=unique.getDoubleExtra("sending_discount",0.0);
                service_amt=unique.getDoubleExtra("sending_service_amt",0.0);
                email=unique.getStringExtra("email");
                coins=unique.getStringExtra("coins");
            }
        }


        type=getIntent().getStringExtra("sending_type");
        balance=getIntent().getDoubleExtra("sending_prev_balance",0.0);//
        wallet_value=getIntent().getDoubleExtra("sending_wallet_amount",0.0);
        change_Screen=getIntent().getIntExtra("change_Screen",0);//
        content=getIntent().getStringExtra("sending_content");
        total_charge=getIntent().getDoubleExtra("amt_of_customers",2000.00);
        total_coustomer=getIntent().getIntExtra("num_of_customers",4000);
        city=getIntent().getStringExtra("sending_city");
        category=getIntent().getStringExtra("sending_category");
        reach=getIntent().getStringExtra("sending_reach");
        start_age=getIntent().getStringExtra("sending_start_age");
        end_age=getIntent().getStringExtra("sending_end_age");
        dis=getIntent().getDoubleExtra("sending_discount",0.0);//
        sending_shop_name=getIntent().getStringExtra("sending_shop_name");
        savings=getIntent().getDoubleExtra("sending_savings",0.0);
        applied=getIntent().getBooleanExtra("applied",false);
        selectedFileUri=getIntent().getStringExtra("sending_file_uri");

        Log.e("disc c",dis+"");
        promotion_reference= FirebaseDatabase.getInstance().getReference().child("Promotion_Data");

        findViewById(R.id.collect).setOnClickListener(v -> {
            httpCall_collect();
        });

        google_pay = findViewById(R.id.google_pay);
        amazon_pay = findViewById(R.id.amazon_pay);
        phone_pay = findViewById(R.id.phone_pay);
        bhim = findViewById(R.id.bhim);
        paytm = findViewById(R.id.paytm);
        mobikwik = findViewById(R.id.mobikwik);
        prev=findViewById(R.id.prev);

        prev.setOnClickListener(view -> finish());

        google_pay.setOnClickListener(v -> {
            if (!google_pay.isCardSurrounded()) {
                package_name = "com.google.android.apps.nbu.paisa.user";
                google_pay.setSurroundStrokeWidth(R.dimen.width_card);
                google_pay.surround();
                amazon_pay.release();
                phone_pay.release();
                bhim.release();
                paytm.release();
                mobikwik.release();
                new Handler(Looper.myLooper()).postDelayed(() -> google_pay.release(), 1000);
                if (checkApplication(package_name))
                    httpCall_intent(package_name);
                else {
                    Toast.makeText(this, "App not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        amazon_pay.setOnClickListener(v -> {
            if (!amazon_pay.isCardSurrounded()) {
                package_name = "in.amazon.mShop.android.shopping";
                amazon_pay.setSurroundStrokeWidth(R.dimen.width_card);
                amazon_pay.surround();
                google_pay.release();
                phone_pay.release();
                bhim.release();
                paytm.release();
                mobikwik.release();
                new Handler(Looper.myLooper()).postDelayed(() -> amazon_pay.release(), 1000);
                if (checkApplication(package_name))
                    httpCall_intent(package_name);
                else {
                    Toast.makeText(this, "App not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        phone_pay.setOnClickListener(v -> {
            if (!phone_pay.isCardSurrounded()) {
                package_name = "com.phonepe.app";
                phone_pay.setSurroundStrokeWidth(R.dimen.width_card);
                phone_pay.surround();
                google_pay.release();
                amazon_pay.release();
                bhim.release();
                paytm.release();
                mobikwik.release();
                new Handler(Looper.myLooper()).postDelayed(() -> phone_pay.release(), 1000);
                if (checkApplication(package_name))
                    httpCall_intent(package_name);
                else {
                    Toast.makeText(this, "App not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bhim.setOnClickListener(v -> {
            if (!bhim.isCardSurrounded()) {
                package_name = "in.org.npci.upiapp";
                bhim.setSurroundStrokeWidth(R.dimen.width_card);
                bhim.surround();
                google_pay.release();
                amazon_pay.release();
                phone_pay.release();
                paytm.release();
                mobikwik.release();
                new Handler(Looper.myLooper()).postDelayed(() -> bhim.release(), 1000);
                if (checkApplication(package_name))
                    httpCall_intent(package_name);
                else {
                    Toast.makeText(this, "App not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        paytm.setOnClickListener(v -> {
            if (!paytm.isCardSurrounded()) {
                package_name = "net.one97.paytm";
                paytm.setSurroundStrokeWidth(R.dimen.width_card);
                paytm.surround();
                google_pay.release();
                amazon_pay.release();
                phone_pay.release();
                bhim.release();
                mobikwik.release();
                new Handler(Looper.myLooper()).postDelayed(() -> paytm.release(), 1000);
                if (checkApplication(package_name))
                    httpCall_intent(package_name);
                else {
                    Toast.makeText(this, "App not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mobikwik.setOnClickListener(v -> {
            if (!mobikwik.isCardSurrounded()) {
                package_name = "com.mobikwik_new";
                mobikwik.setSurroundStrokeWidth(R.dimen.width_card);
                mobikwik.surround();
                google_pay.release();
                amazon_pay.release();
                phone_pay.release();
                bhim.release();
                paytm.release();
                new Handler(Looper.myLooper()).postDelayed(() -> mobikwik.release(), 1000);
                if (checkApplication(package_name))
                    httpCall_intent(package_name);
                else {
                    Toast.makeText(this, "App not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void httpCall_collect() {
        dialog = new Dialog(CashFree_Activity.this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.layout_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        oId = System.currentTimeMillis();
        StringBuilder input1 = new StringBuilder();
        input1.append(oId);
        input1.reverse();

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("order_amount", amount);
            jsonBody.put("customer_id", input1 + "");
            jsonBody.put("order_id", oId + "");
            jsonBody.put("order_note", "Subscription");
            jsonBody.put("customer_email", email);
            jsonBody.put("customer_phone", phone);
            Log.d("body", "httpCall_collect: " + jsonBody);
        } catch (Exception e) {
            Log.e("Error", "JSON ERROR");
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "https://cashfree-server-production.up.railway.app/api/create-order";

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonBody,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // enjoy your response
                        dialog.dismiss();
                        String token = response.optJSONObject("response").optString("order_token");

                        Log.e("response", response.optJSONObject("response").optString("order_token"));
                        Log.e("response", response.toString());

                        try {
                            CFSession cfSession = new CFSession.CFSessionBuilder()
                                    .setEnvironment(cfEnvironment)
                                    .setOrderToken(token)
                                    .setOrderId(oId + "")
                                    .build();
                            CFPaymentComponent cfPaymentComponent = new CFPaymentComponent.CFPaymentComponentBuilder()
                                    // Shows only Card and UPI modes
                                    .add(CFPaymentComponent.CFPaymentModes.CARD)
                                    .add(CFPaymentComponent.CFPaymentModes.UPI)
                                    .add(CFPaymentComponent.CFPaymentModes.WALLET)
                                    .add(CFPaymentComponent.CFPaymentModes.PAY_LATER)
                                    .add(CFPaymentComponent.CFPaymentModes.EMI)
                                    .add(CFPaymentComponent.CFPaymentModes.PAYPAL)
                                    .add(CFPaymentComponent.CFPaymentModes.NB)
                                    .build();
                            // Replace with your application's theme colors
                            CFTheme cfTheme = new CFTheme.CFThemeBuilder()
                                    .setNavigationBarBackgroundColor("#00aacc")
                                    .setNavigationBarTextColor("#ffffff")
                                    .setButtonBackgroundColor("#2D796D")
                                    .setButtonTextColor("#ffffff")
                                    .setPrimaryTextColor("#000000")
                                    .setSecondaryTextColor("#000000")
                                    .build();
                            CFDropCheckoutPayment cfDropCheckoutPayment = new CFDropCheckoutPayment.CFDropCheckoutPaymentBuilder()
                                    .setSession(cfSession)
                                    .setCFUIPaymentModes(cfPaymentComponent)
                                    .setCFNativeCheckoutUITheme(cfTheme)
                                    .build();
                            CFPaymentGatewayService gatewayService = CFPaymentGatewayService.getInstance();
                            gatewayService.doPayment(CashFree_Activity.this, cfDropCheckoutPayment);
                            gatewayService.setCheckoutCallback(new CFCheckoutResponseCallback() {
                                @Override
                                public void onPaymentVerify(String s) {

                                    Log.d("hhh", "onPaymentVerify: "+selectPackages.equals(unique.getStringExtra("checkMe")));
                                    if(change_Screen==0 && from_mem_bill==null && !selectPackages.equals(unique.getStringExtra("checkMe"))) {

                                        Intent intent = new Intent(CashFree_Activity.this, PaymentStatus.class);
                                        intent.putExtra("amount", amount);
                                        intent.putExtra("email", email);
                                        intent.putExtra("orderId", s);
                                        intent.putExtra("isPromoApplied", isPromoApplied);
                                        intent.putExtra("promoCode", promoCodeString);
                                        intent.putExtra("phoneNumber", phone);
                                        intent.putExtra("transaction_status", 0);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else if(from_mem_bill!=null){
                                        show_pay_success_membership(s,"s");
                                    }
                                    else if(selectPackages.equals(unique.getStringExtra("checkMe")))
                                    {
                                        //String email,String phNumber,String s,String info,String amount,String coins,String packages
                                        callFromSelectPackages_show_pay_success_membership(email,phone,s,amount,coins,_package_,"success",service_amt,dis);

                                    }
                                    else{
                                        if (applied) {
                                            update_wallet(wallet_value + "");
                                        }
                                        if(applied)
                                            update_wallet(wallet_value+"");
                                        Intent intent = new Intent(CashFree_Activity.this, payment_status.class);
                                        intent.putExtra("amount", amount);
                                        intent.putExtra("sending_from_payment_gateway", "gateway");
                                        intent.putExtra("email", email);
                                        intent.putExtra("balance", balance);
                                        intent.putExtra("dis", dis);
                                        intent.putExtra("applied", applied);
                                        intent.putExtra("sending_shop_name", sending_shop_name);
                                        intent.putExtra("sending_savings", savings);
                                        intent.putExtra("sending_file_uri", selectedFileUri);

                                        intent.putExtra("sending_type", type);
                                        intent.putExtra("sending_wallet_amount", wallet_value);
                                        intent.putExtra("sending_content", content);
                                        intent.putExtra("amt_of_customers", total_charge);
                                        intent.putExtra("sending_city", city);
                                        intent.putExtra("sending_category", category);
                                        intent.putExtra("sending_reach", reach);
                                        intent.putExtra("sending_start_age", start_age);
                                        intent.putExtra("sending_end_age", end_age);
                                        intent.putExtra("num_of_customers", total_coustomer);

                                        intent.putExtra("orderId", s);
                                        intent.putExtra("isPromoApplied", isPromoApplied);
                                        intent.putExtra("promoCode", promoCodeString);
                                        intent.putExtra("phoneNumber", phone);
                                        intent.putExtra("transaction_status", 0);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }

                                @Override
                                public void onPaymentFailure(CFErrorResponse cfErrorResponse, String s)
                                {
                                    if(change_Screen==0 && from_mem_bill==null && !selectPackages.equals(unique.getStringExtra("checkMe")))
                                    {
                                        Toast.makeText(CashFree_Activity.this, "one", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(CashFree_Activity.this, PaymentStatus.class);
                                        intent.putExtra("amount", amount);
                                        intent.putExtra("orderId", s);
                                        intent.putExtra("isPromoApplied", isPromoApplied);
                                        intent.putExtra("promoCode", promoCodeString);
                                        intent.putExtra("phoneNumber", phone);
                                        intent.putExtra("transaction_status", 1);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        Log.v(CashFree_Activity.class.getSimpleName(), "started new activity");
                                        finish();
                                    }
                                    else if(from_mem_bill!=null){
                                        Toast.makeText(CashFree_Activity.this, "two", Toast.LENGTH_SHORT).show();
                                        show_pay_success_membership(s,"f");
                                    }
                                    else if(selectPackages.equals(unique.getStringExtra("checkMe")))
                                    {
                                        //String email,String phNumber,String s,String info,String amount,String coins,String packages
                                        callFromSelectPackages_show_pay_success_membership(email,phone,s,amount,coins,_package_,"fail",service_amt,dis);
                                    }
                                    else{
                                        Toast.makeText(CashFree_Activity.this, "four", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(CashFree_Activity.this, payment_status.class);
                                        intent.putExtra("amount", amount);
                                        intent.putExtra("sending_from_payment_gateway", "gateway");
                                        intent.putExtra("orderId", s);
                                        intent.putExtra("balance", balance);
                                        intent.putExtra("dis", dis);
                                        intent.putExtra("applied", applied);
                                        intent.putExtra("sending_shop_name", sending_shop_name);
                                        intent.putExtra("sending_savings", savings);
                                        intent.putExtra("sending_file_uri", selectedFileUri);

                                        intent.putExtra("sending_type", type);
                                        intent.putExtra("sending_wallet_amount", wallet_value);
                                        intent.putExtra("sending_content", content);
                                        intent.putExtra("amt_of_customers", total_charge);
                                        intent.putExtra("sending_city", city);
                                        intent.putExtra("sending_category", category);
                                        intent.putExtra("sending_reach", reach);
                                        intent.putExtra("sending_start_age", start_age);
                                        intent.putExtra("sending_end_age", end_age);
                                        intent.putExtra("num_of_customers", total_coustomer);

                                        intent.putExtra("isPromoApplied", isPromoApplied);
                                        intent.putExtra("promoCode", promoCodeString);
                                        intent.putExtra("phoneNumber", phone);
                                        intent.putExtra("transaction_status", 1);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        Log.v(CashFree_Activity.class.getSimpleName(), "started new activity");
                                        finish();
                                    }
                                }
                            });
                        } catch (CFException exception) {
                            exception.printStackTrace();
                        }

                        Log.e("response", token);
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
        requestQueue.add(stringRequest);
    }

    public void httpCall_intent(String package_name) {
        dialog = new Dialog(CashFree_Activity.this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.layout_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        oId = System.currentTimeMillis();
        StringBuilder input1 = new StringBuilder();

        // append a string into StringBuilder input1
        input1.append(oId + "");

        // reverse StringBuilder input1
        input1.reverse();
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("order_amount", amount);
            jsonBody.put("customer_id", input1 + "");
            jsonBody.put("order_id", oId + "");
            jsonBody.put("order_note", "Subscription");
            jsonBody.put("customer_email", email);
            jsonBody.put("customer_phone", phone);
            Log.d("body", "httpCall_collect: " + jsonBody);
        } catch (Exception e) {

        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "https://cashfree-server-production.up.railway.app/api/create-order";

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonBody,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.dismiss();
                        if (!isAppUpiReady(package_name)) {
                            Toast.makeText(CashFree_Activity.this, "App not ready please add bank details", Toast.LENGTH_SHORT).show();
                        }
                        // enjoy your response
                        String token = response.optJSONObject("response").optString("order_token");

                        Log.e("response", response.optJSONObject("response").optString("order_token"));
                        Log.e("response", response.toString());
                        try {
                            CFSession cfSession = new CFSession.CFSessionBuilder()
                                    .setEnvironment(cfEnvironment)
                                    .setOrderToken(token)
                                    .setOrderId(oId + "")
                                    .build();
                            CFUPI cfupi = new CFUPI.CFUPIBuilder()
                                    .setMode(CFUPI.Mode.INTENT)
                                    .setUPIID(package_name) //Google Pay's package name = "com.google.android.apps.nbu.paisa.user"
                                    .build();
                            CFUPIPayment cfupiPayment = new CFUPIPayment.CFUPIPaymentBuilder()
                                    .setSession(cfSession)
                                    .setCfUPI(cfupi)
                                    .build();
                            CFPaymentGatewayService gatewayService = CFPaymentGatewayService.getInstance();
                            gatewayService.doPayment(CashFree_Activity.this, cfupiPayment);
                            gatewayService.setCheckoutCallback(new CFCheckoutResponseCallback() {
                                @Override
                                public void onPaymentVerify(String s) {
                                   // pay_screen.packagesData(email,phNumber,s,info,amount,coins,packages,dis);
                                    if(change_Screen==0 && from_mem_bill==null && !selectPackages.equals(unique.getStringExtra("checkMe"))) {
                                        Intent intent = new Intent(CashFree_Activity.this, PaymentStatus.class);
                                        intent.putExtra("amount", amount);
                                        intent.putExtra("email", email);
                                        intent.putExtra("orderId", s);
                                        intent.putExtra("isPromoApplied", isPromoApplied);
                                        intent.putExtra("promoCode", promoCodeString);
                                        intent.putExtra("phoneNumber", phone);
                                        intent.putExtra("transaction_status", 0);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else if(from_mem_bill!=null){
                                        show_pay_success_membership(s,"s");
                                    }
                                    else if(selectPackages.equals(unique.getStringExtra("checkMe")))
                                    {
                                        //String email,String phNumber,String s,String info,String amount,String coins,String packages
                                        callFromSelectPackages_show_pay_success_membership(email,phone,s,amount,coins,_package_,"success",service_amt,dis);

                                    }
                                    else{
                                        if (applied) {
                                            update_wallet(wallet_value + "");
                                        }
                                        if(applied)
                                            update_wallet(wallet_value+"");
                                        Intent intent = new Intent(CashFree_Activity.this, payment_status.class);
                                        intent.putExtra("amount", amount);
                                        intent.putExtra("email", email);
                                        intent.putExtra("balance", balance);
                                        intent.putExtra("sending_from_payment_gateway", "gateway");
                                        intent.putExtra("dis", dis);
                                        intent.putExtra("applied", applied);
                                        intent.putExtra("sending_shop_name", sending_shop_name);
                                        intent.putExtra("sending_savings", savings);
                                        intent.putExtra("sending_file_uri", selectedFileUri);

                                        intent.putExtra("sending_type", type);
                                        intent.putExtra("sending_wallet_amount", wallet_value);
                                        intent.putExtra("sending_content", content);
                                        intent.putExtra("amt_of_customers", total_charge);
                                        intent.putExtra("sending_city", city);
                                        intent.putExtra("sending_category", category);
                                        intent.putExtra("sending_reach", reach);
                                        intent.putExtra("sending_start_age", start_age);
                                        intent.putExtra("sending_end_age", end_age);
                                        intent.putExtra("num_of_customers", total_coustomer);

                                        intent.putExtra("orderId", s);
                                        intent.putExtra("isPromoApplied", isPromoApplied);
                                        intent.putExtra("promoCode", promoCodeString);
                                        intent.putExtra("phoneNumber", phone);
                                        intent.putExtra("transaction_status", 0);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }

                                @Override
                                public void onPaymentFailure(CFErrorResponse cfErrorResponse, String s) {
                                    if (change_Screen == 0  && from_mem_bill==null && !selectPackages.equals(unique.getStringExtra("checkMe"))) {
                                        Intent intent = new Intent(CashFree_Activity.this, PaymentStatus.class);
                                        intent.putExtra("amount", amount);
                                        intent.putExtra("orderId", s);
                                        intent.putExtra("isPromoApplied", isPromoApplied);
                                        intent.putExtra("promoCode", promoCodeString);
                                        intent.putExtra("phoneNumber", phone);
                                        intent.putExtra("transaction_status", 1);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                        Log.v(CashFree_Activity.class.getSimpleName(), "started new activity");
                                    }
                                    else if(selectPackages.equals(unique.getStringExtra("checkMe")))
                                    {
                                        //String email,String phNumber,String s,String info,String amount,String coins,String packages
                                        callFromSelectPackages_show_pay_success_membership(email,phone,s,amount,coins,_package_,"fail",service_amt,dis);
                                    }
                                    else if(from_mem_bill!=null){
                                        show_pay_success_membership(s,"f");
                                    }
                                    else{
                                        Intent intent = new Intent(CashFree_Activity.this, payment_status.class);
                                        intent.putExtra("amount", amount);
                                        intent.putExtra("orderId", s);
                                        intent.putExtra("balance", balance);
                                        intent.putExtra("dis", dis);
                                        intent.putExtra("sending_from_payment_gateway", "gateway");
                                        intent.putExtra("applied", applied);
                                        intent.putExtra("sending_shop_name", sending_shop_name);
                                        intent.putExtra("sending_savings", savings);
                                        intent.putExtra("sending_file_uri", selectedFileUri);

                                        intent.putExtra("sending_type", type);
                                        intent.putExtra("sending_wallet_amount", wallet_value);
                                        intent.putExtra("sending_content", content);
                                        intent.putExtra("amt_of_customers", total_charge);
                                        intent.putExtra("sending_city", city);
                                        intent.putExtra("sending_category", category);
                                        intent.putExtra("sending_reach", reach);
                                        intent.putExtra("sending_start_age", start_age);
                                        intent.putExtra("sending_end_age", end_age);
                                        intent.putExtra("num_of_customers", total_coustomer);

                                        intent.putExtra("isPromoApplied", isPromoApplied);
                                        intent.putExtra("promoCode", promoCodeString);
                                        intent.putExtra("phoneNumber", phone);
                                        intent.putExtra("transaction_status", 1);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                        Log.v(CashFree_Activity.class.getSimpleName(), "started new activity");
                                    }
                                }
                            });
                        } catch (CFException exception) {
                            exception.printStackTrace();
                        }

                        Log.e("response", token);
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
            public void retry(VolleyError error) {
            }
        });
        Log.d("string", stringRequest.toString());
        requestQueue.add(stringRequest);
    }

    public boolean checkApplication(String packageName) {
        PackageManager packageManager = getPackageManager();
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = packageManager.getApplicationInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (applicationInfo == null) {
            // not installed
            return false;
        } else {
            return true;
            // Installed
        }
    }
    private void update_wallet(String value) {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Merchant_data");
        //TODO: What if uid data is not present?
        reference.child(user.getUid()).child("Account_Information").child("walletPromotion").setValue(value);
    }
    public final boolean isAppUpiReady(@NotNull String packageName) {
        Intrinsics.checkNotNullParameter(packageName, "packageName");
        boolean appUpiReady = false;
        Intent upiIntent = new Intent("android.intent.action.VIEW", Uri.parse("upi://pay"));
        PackageManager pm = this.getPackageManager();
        List var10000 = pm.queryIntentActivities(upiIntent, 0);
        Intrinsics.checkNotNullExpressionValue(var10000, "pm.queryIntentActivities(upiIntent, 0)");
        List upiActivities = var10000;
        Iterator var7 = upiActivities.iterator();

        while (var7.hasNext()) {
            ResolveInfo a = (ResolveInfo) var7.next();
            if (Intrinsics.areEqual(a.activityInfo.packageName, packageName)) {
                appUpiReady = true;
            }
        }

        return appUpiReady;
    }
    private void show_pay_success_membership(String s,String info){
        Intent intent = new Intent(CashFree_Activity.this, pay_screen.class);
        intent.putExtra("amount", amount);
        intent.putExtra("orderId", s);
        intent.putExtra("txt_status", info);
        intent.putExtra("phoneNumber", phone);
        intent.putExtra("sending_plan", plan);
        intent.putExtra("sending_gst", dis);
        intent.putExtra("sending_service_amt", service_amt);
        intent.putExtra("transaction_status", 1);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }
    public static  void callFromSelectPackages_show_pay_success_membership(String email,String phNumber,String s,String amount,String coins,String packages,String statusSF,double serviceAmount,double dis){
//        Intent intent = new Intent(CashFree_Activity.this, pay_screen.class);
//        intent.putExtra("amount", amount);
//        intent.putExtra("orderId", s);
//        intent.putExtra("txt_status", info);
//        intent.putExtra("phoneNumber", phone);
//        intent.putExtra("sending_plan", plan);
//        intent.putExtra("sending_gst", dis);
//        intent.putExtra("sending_service_amt", service_amt);
//        intent.putExtra("transaction_status", 1);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        finish();
        Log.d("checKvalues", "callFromSelectPackages_show_pay_success_membership: "+statusSF);

        Intent intent=new Intent(context,pay_screen.class);
        intent.putExtra("email", email);
        intent.putExtra("phNumber", phNumber);
        intent.putExtra("s", s);
        intent.putExtra("amount", amount);
        intent.putExtra("coins", coins);
        intent.putExtra("packages", packages);
        intent.putExtra("status", statusSF);
        intent.putExtra("transaction_status", 1);
        intent.putExtra("serviceAmount", serviceAmount);
        intent.putExtra("gst", dis);
        intent.putExtra("transaction_status", 1);
        intent.putExtra("myValues", "callFromSelectPackages_show_pay_success_membership");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        //pay_screen.packagesData(email,phNumber,s,amount,coins,packages,dis,statusSF);
        context.startActivity(intent);




    }
}

