package com.cu.gastosmerchant1.selectPackage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.cu.gastosmerchant1.Adapters.Adapter_class;
import com.cu.gastosmerchant1.Dashboard.Home;
import com.cu.gastosmerchant1.Dashboard.membership.Bill_payment;
import com.cu.gastosmerchant1.R;
import com.cu.gastosmerchant1.registration.CashFree_Activity;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class selectPackage extends AppCompatActivity {
    TextView get_started;
    private int currentApiVersion;
    TabLayout tabIndicator;
    ViewPager viewPager;
    FirebaseAuth auth;
    FirebaseUser user;
    ImageView back;

   // String[] text ={"1","1","1","1","1","1","1","1","2","2","2","2","2","2","2","2","3","3","3","3","3","3","3","3"};
    //int bodyimage[] = {R.drawable.ic_title, R.drawable.ic_security_text, R.drawable.ic_convenient};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentApiVersion = Build.VERSION.SDK_INT;


        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        // This work only for android 4.4+
        if(currentApiVersion >= Build.VERSION_CODES.KITKAT)
        {

            getWindow().getDecorView().setSystemUiVisibility(flags);

            // Code below is to handle presses of Volume up or Volume down.
            // Without this, after pressing volume buttons, the navigation bar will
            // show up and won't hide
            final View decorView = getWindow().getDecorView();
            decorView
                    .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
                    {

                        @Override
                        public void onSystemUiVisibilityChange(int visibility)
                        {
                            if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
                            {
                                decorView.setSystemUiVisibility(flags);
                            }
                        }
                    });
        }

    }


    @SuppressLint("NewApi")
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        if(currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus)
        {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        setContentView(R.layout.select_package);
        viewPager = findViewById(R.id.view_pager);;
        tabIndicator = findViewById(R.id.tabIndicator);

        Adapter_class adapter = new Adapter_class(getApplicationContext());
        viewPager.setAdapter(adapter);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        tabIndicator.setupWithViewPager(viewPager);
        get_started = findViewById(R.id.started_btn);
        back = findViewById(R.id.back);
        get_started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               // Toast.makeText(selectPackage.this, ""+viewPager.getCurrentItem(), Toast.LENGTH_SHORT).show();

                if(viewPager.getCurrentItem()==0)
                {
                    callDataBase("1414.82",215.82,1599,"3000","Premium");
                }
                else if(viewPager.getCurrentItem()==1)
                {
                    callDataBase("706.82",107.82,799,"1400","Standard");
                }
                else
                {
                    callDataBase("352.82",53.82,399,"600","Basic");
                }



            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), Home.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

            }
        });



    }
    void callDataBase(String amount,double gst,double service_amount,String coins,String _package_)
    {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Merchant_data");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String email=snapshot.child(user.getUid()).child("Account_Information").child("emailAddress").getValue(String.class);
                String phone=snapshot.child(user.getUid()).child("Account_Information").child("phoneNumber").getValue(String.class);

                Intent intent = new Intent(getApplicationContext(), CashFree_Activity.class);
                intent.putExtra("amount", String.valueOf(amount));
                //intent.putExtra("amount", "1");
                intent.putExtra("checkMe","selectPackages");
                intent.putExtra("phoneNumber", phone);
                intent.putExtra("sending_package", _package_);
                intent.putExtra("sending_discount", gst);
                intent.putExtra("coins", coins);
                intent.putExtra("sending_service_amt", service_amount);
                intent.putExtra("email", email);
                //intent to cash free
                //String email,String phNumber,String gst,String s,String info,String amount,String coins,String packages
               // CashFree_Activity.callFromSelectPackages_show_pay_success_membership(email,phone,gst,,);
                startActivity(intent);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error", "onCancelled: "+error);
            }
        });
    }

}
