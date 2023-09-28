package com.cu.gastosmerchant1.registration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;

import com.cu.gastosmerchant1.R;
import com.cu.gastosmerchant1.details.Account_Information;
import com.cu.gastosmerchant1.details.Discount;
import com.cu.gastosmerchant1.details.Payment_Information;
import com.cu.gastosmerchant1.details.Shop_Information;
import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

public class Commission extends AppCompatActivity {

    //todo check the 1st amount if 0

    TextView margin, reMargin, commissionText, addmore, addmore2;
    Button comBtn;
    ImageView mPrev;
    private String shopName, shopAddress, shopCategory, location, latitude, longitude, shopImageUri, shopArea, shopState;
    Shop_Information shop_information;
    Account_Information account_information;
    Button cancelCommission2;
    Button cancelCommission3;

    int setMargin1, setMargin2;

    private CardView cardView1;
    private CardView cardView2;
    private CardView cardView3;

    int max = 50, min = 5;
    SeekBar seekBar, seekBar2, seekBar3;
    TextView mMin, mMax, mMin2, mMax2, mMin3, mMax3;
    EditText mDiscountDisplay, mDiscountDisplay2, mDiscountDisplay3;

    EditText mMinAmount, mMinAmount2, mMinAmount3;

    private long creationTimeStamp;
    private LinearLayout discount2, discount3;
    private String discount_2 = ""; 
    private String discount_3 = "";
    private LinearLayoutCompat linearLayoutCompat2, linearLayoutCompat3;

    private ImageView back;

    private ArrayList<Payment_Information> upiArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commission);
        back = findViewById(R.id.prev);
        comBtn = findViewById(R.id.confirm_button);
        margin = findViewById(R.id.setmar);
        reMargin = findViewById(R.id.resetmar);
        commissionText = findViewById(R.id.commissionText);
        seekBar = findViewById(R.id.seekBar);
        seekBar2 = findViewById(R.id.seekBar2);
        seekBar3 = findViewById(R.id.seekBar3);
        addmore = findViewById(R.id.addmore);
        addmore2 = findViewById(R.id.addmore2);
        discount2 = findViewById(R.id.view_discount2);
        discount3 = findViewById(R.id.view_discount3);
        linearLayoutCompat2 = findViewById(R.id.linearLayoutCompat2);
        linearLayoutCompat3 = findViewById(R.id.linearLayoutCompat3);
        cardView1 = findViewById(R.id.cardview1);
        cardView2 = findViewById(R.id.cardview2);
        cardView3 = findViewById(R.id.cardView3);
        mMin = findViewById(R.id.text_min);
        mMax = findViewById(R.id.text_max);
        mMin2 = findViewById(R.id.text_min2);
        mMax2 = findViewById(R.id.text_max2);
        mMin3 = findViewById(R.id.text_min3);
        mMax3 = findViewById(R.id.text_max3);
        mDiscountDisplay = findViewById(R.id.et_discount);
        mDiscountDisplay2 = findViewById(R.id.et_discount2);
        mDiscountDisplay3 = findViewById(R.id.et_discount3);
        mPrev = findViewById(R.id.prev);
        mMinAmount = findViewById(R.id.et_min_amount);
        mMinAmount2 = findViewById(R.id.et_min_amount2);
        mMinAmount3 = findViewById(R.id.et_min_amount3);
        cancelCommission2 = findViewById(R.id.discount_2_cancelBt);
        cancelCommission3 = findViewById(R.id.discount_3_cancelBt);

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
        upiArrayList = (ArrayList<Payment_Information>) getIntent().getSerializableExtra("upiArrayList");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        switch (shopCategory) {
            case "Food & Beverages":
//                commissionText.setText("Set the value in between 5-40%");
                min = 5;
                max = 40;
                break;
            case "Fashion":
//                commissionText.setText("Set the value in between 5-50%");
                min = 5;
                max = 50;
                break;
            case "Salon & spa":
//                commissionText.setText("Set the value in between 5-50%");
                min = 5;
                max = 50;
                break;
            case "Stores":
//                commissionText.setText("Set the value in between 0-30%");
                min = 0;
                max = 30;
                break;
            case "Medical":
//                commissionText.setText("Set the value in between 1-30%");
                min = 1;
                max = 30;
                break;
            case "others":
//                commissionText.setText("Set the value in between 5-50%");
                min = 5;
                max = 50;
                break;
            default:
//                commissionText.setText("Set the value in between 5-50%");
                min = 5;
                max = 50;
                break;
        }

        mMin.setText(min + "");
        mMax.setText(max + "");
        mMin2.setText(min + "");
        mMax2.setText(max + "");
        mMin3.setText(min + "");
        mMax3.setText(max + "");

        seekBar.setMax(max - min);
        seekBar.setProgress(20 - min);
        seekBar2.setMax(max - min);
        seekBar2.setProgress(20 - min);
        seekBar3.setMax(max - min);
        seekBar3.setProgress(20 - min);
            /*
            mDiscountDisplay.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }
                @Override
                public void afterTextChanged(Editable editable) {
                    if(editable.toString()!=null) {
                        int temp_value = Integer.parseInt(editable.toString());
                        if (temp_value > max | temp_value < min) {
                            Toast.makeText(Commission.this, "Set Value Between " + min + "-" + max + "%", Toast.LENGTH_SHORT).show();
//                            mDiscountDisplay.setText(seekBar.getProgress() + min+"");
                        } else {
                            seekBar.setProgress(Integer.parseInt(editable.toString()) - min);
                        }
                    }
                }
            });*/

        addmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMinAmount.getText().toString().matches("")) {
                    Toast.makeText(Commission.this, "enter proper amount", Toast.LENGTH_SHORT).show();
                    return;
                }
                discount_2 = "2";
                addmore.setVisibility(View.GONE);
                addmore2.setVisibility(View.VISIBLE);
                setMargin1 = Integer.parseInt(mDiscountDisplay.getText().toString().trim()) + 1;
                cancelCommission2.setVisibility(View.VISIBLE);
                seekBar2.setProgress(setMargin1);
                mDiscountDisplay2.setText(String.valueOf(setMargin1));
                mMinAmount2.setText(String.valueOf(Integer.parseInt(mMinAmount.getText().toString()) + 1));
                cardView2.setVisibility(View.VISIBLE);
            }
        });

        addmore2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                discount_3 = "3";
                setMargin2 = Integer.parseInt(mDiscountDisplay2.getText().toString().trim()) + 1;
                cancelCommission2.setVisibility(View.GONE);
                cancelCommission3.setVisibility(View.VISIBLE);
                seekBar3.setProgress(setMargin2);
                mDiscountDisplay3.setText(String.valueOf(setMargin2));
                mMinAmount3.setText(String.valueOf(Integer.parseInt(mMinAmount2.getText().toString()) + 1));
                addmore2.setVisibility(View.GONE);
                cardView3.setVisibility(View.VISIBLE);
            }
        });

        mMinAmount2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    Toast.makeText(Commission.this, "here", Toast.LENGTH_SHORT).show();
                }
                int temp_value = Integer.parseInt(mMinAmount2.getText().toString());
                if (b && temp_value <= Integer.parseInt(mMinAmount.getText().toString())) {
                    mMinAmount2.setText(Integer.parseInt(mMinAmount.getText().toString()) + 1);
                    Toast.makeText(Commission.this, "this amount must be greater than 1st amount", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mMinAmount3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                int temp_value = Integer.parseInt(mMinAmount3.getText().toString());
                if (b && temp_value <= Integer.parseInt(mMinAmount2.getText().toString())) {
                    mMinAmount3.setText(Integer.parseInt(mMinAmount2.getText().toString()) + 1);
                    Toast.makeText(Commission.this, "this amount must be greater than 2nd amount", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mDiscountDisplay.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                int temp_value = Integer.parseInt(mDiscountDisplay.getText().toString());
                if (temp_value > max | temp_value < min) {
                    Toast.makeText(Commission.this, "Set Value Between " + min + "-" + max + "%", Toast.LENGTH_SHORT).show();
//                             mDiscountDisplay.setText(seekBar.getProgress() + min+"");

                } else {
                    seekBar.setProgress(Integer.parseInt(mDiscountDisplay.getText().toString()) - min);
                }
            }
        });

        mDiscountDisplay2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                int temp_value = Integer.parseInt(mDiscountDisplay2.getText().toString());
//                int prev = Integer.parseInt(mDiscountDisplay.getText().toString());
//                if (!b && temp_value < prev) {
//                    Toast.makeText(Commission.this, "value can not be less than 1st discount value", Toast.LENGTH_SHORT).show();
//                    seekBar2.setProgress(prev);
//                    mDiscountDisplay2.setText(String.valueOf(prev));
//                    return;
//                }
                if (temp_value > max | temp_value < min) {
                    Toast.makeText(Commission.this, "Set Value Between " + min + "-" + max + "%", Toast.LENGTH_SHORT).show();
//                            mDiscountDisplay.setText(seekBar.getProgress() + min+"");

                } else {
                    seekBar2.setProgress(Integer.parseInt(mDiscountDisplay2.getText().toString()) - min);
                }
            }
        });

        mDiscountDisplay3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                int temp_value = Integer.parseInt(mDiscountDisplay3.getText().toString());
//                int prev = Integer.parseInt(mDiscountDisplay2.getText().toString());
//                if (!b && temp_value < prev) {
//                    Toast.makeText(Commission.this, "value can not be less than 2nd discount value", Toast.LENGTH_SHORT).show();
//                    seekBar3.setProgress(prev);
//                    mDiscountDisplay3.setText(String.valueOf(prev));
//                    return;
//                }
                if (temp_value > max | temp_value < min) {
                    Toast.makeText(Commission.this, "Set Value Between " + min + "-" + max + "%", Toast.LENGTH_SHORT).show();
//                            mDiscountDisplay.setText(seekBar.getProgress() + min+"");

                } else {
                    seekBar3.setProgress(Integer.parseInt(mDiscountDisplay3.getText().toString()) - min);
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int value = min + i;
                mDiscountDisplay.setText(value + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int value = min + i;
                if (value < Integer.parseInt(mDiscountDisplay.getText().toString().trim())) {
                    Toast.makeText(Commission.this, "value can not be less than 1st discount value", Toast.LENGTH_SHORT).show();
                    value = Integer.parseInt(mDiscountDisplay.getText().toString().trim());
                    seekBar2.setProgress(value);
                }
                mDiscountDisplay2.setText(value + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int value = min + i;
                if (value < Integer.parseInt(mDiscountDisplay2.getText().toString().trim())) {
                    Toast.makeText(Commission.this, "Amount can not be less than 2nd discount", Toast.LENGTH_SHORT).show();
                    value = Integer.parseInt(mDiscountDisplay2.getText().toString().trim());
                    seekBar3.setProgress(value);
                }
                mDiscountDisplay3.setText(value + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        cancelCommission2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardView2.setVisibility(View.GONE);
                discount_2 = "removed";
                addmore.setVisibility(View.VISIBLE);
                addmore2.setVisibility(View.GONE);
            }
        });

        cancelCommission3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelCommission2.setVisibility(View.VISIBLE);
                cardView3.setVisibility(View.GONE);
                discount_3 = "removed";
                addmore2.setVisibility(View.VISIBLE);
            }
        });


        comBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (viewEmpty()) {
//                    return;
//                }

//                if (!margin.getText().toString().trim().equals(reMargin.getText().toString().trim())) {
//                    Toast.makeText(Commission.this, "margin does not matches", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                int marginInt = Integer.parseInt(margin.getText().toString().trim());

                ArrayList<Discount> discounts = new ArrayList<>();
                int minAmount, minAmount2, minAmount3;
                String marginString = mDiscountDisplay.getText().toString();

                //1st discount
                int marginInt = Integer.parseInt(mDiscountDisplay.getText().toString().trim());
                if (marginInt < min || marginInt > max) {
                    Toast.makeText(Commission.this, "margin must be in correct range", Toast.LENGTH_SHORT).show();
                } else {
                    if (mMinAmount.getText().toString().isEmpty()) minAmount = 0;
                    else minAmount = Integer.parseInt(mMinAmount.getText().toString().trim());
                    discounts.add(new Discount(minAmount, Integer.parseInt(marginString)));
                }

                //2nd discount
                if (discount_2.equals("2")) {
                    int marginInt2 = Integer.parseInt(mDiscountDisplay2.getText().toString().trim());
                    if (marginInt2 < min || marginInt2 > max) {
                        Toast.makeText(Commission.this, "margin must be in correct range", Toast.LENGTH_SHORT).show();
                    } else {
                        discount_2 = mDiscountDisplay2.getText().toString();
                        if (mMinAmount2.getText().toString().isEmpty()) minAmount2 = 0;
                        else minAmount2 = Integer.parseInt(mMinAmount2.getText().toString().trim());
                        discounts.add(new Discount(minAmount2, Integer.parseInt(discount_2)));
                    }
                }

                // 3rd discount
                if (discount_3.equals("3")) {
                    int marginInt3 = Integer.parseInt(mDiscountDisplay3.getText().toString().trim());
                    if (marginInt3 < min || marginInt3 > max) {
                        Toast.makeText(Commission.this, "margin must be in correct range", Toast.LENGTH_SHORT).show();
                    } else {
                        discount_3 = mDiscountDisplay3.getText().toString();
                        if (mMinAmount3.getText().toString().isEmpty()) minAmount3 = 0;
                        else minAmount3 = Integer.parseInt(mMinAmount3.getText().toString().trim());
                        discounts.add(new Discount(minAmount3, Integer.parseInt(discount_3)));
                    }
                }

                shop_information = new Shop_Information(shopCategory, location, shopAddress, latitude, longitude, shopName, shopImageUri, creationTimeStamp, discounts, shopState, shopArea);
                //alertDialog();
                Intent intent = new Intent(Commission.this, UserAgreement.class);
                intent.putExtra("shop_information", shop_information);
                intent.putExtra("account_information", account_information);
                intent.putExtra("upiArrayList", upiArrayList);
                startActivity(intent);
            }
        });

        mPrev.setOnClickListener(view -> finish());

    }

    private boolean viewEmpty() {
        if (margin.getText().toString().isEmpty() || reMargin.getText().toString().isEmpty()) {
            Toast.makeText(Commission.this, "views are empty", Toast.LENGTH_SHORT).show();
            return true;//todo write name for the view
        }
        return false;
    }
}
