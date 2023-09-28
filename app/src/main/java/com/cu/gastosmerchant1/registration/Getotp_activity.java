package com.cu.gastosmerchant1.registration;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cu.gastosmerchant1.Dashboard.Home;
import com.cu.gastosmerchant1.R;
import com.cu.gastosmerchant1.Utils.OtpEditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class  Getotp_activity extends AppCompatActivity {

    TextView phone_num;
  //  OtpEditText otpEditText;
    public String phonenumber_value;
    private FirebaseAuth mAuth;
    Button btnVerify;
    ImageView back;
    ProgressBar pBar;
    private String verificationId;
    private PhoneAuthProvider.ForceResendingToken resendOTPtoken;
    private  TextView timer,resendOTPTV;
    static ProgressDialog dialog;
    EditText[] editText = new EditText[6];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getotp);

//        dialog = new ProgressDialog(Getotp_activity.this);
//        dialog.setMessage("Sending OTP...");
//        dialog.setCancelable(false);
//        dialog.show();
        back=findViewById(R.id.prev);
        pBar=findViewById(R.id.pBar);
        timer=findViewById(R.id.timer);
        resendOTPTV = findViewById(R.id.resend_otp);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        countTimer();

        editText[0] = (EditText) findViewById(R.id.edit_text_1);
        editText[1] = (EditText) findViewById(R.id.edit_text_2);
        editText[2] = (EditText) findViewById(R.id.edit_text_3);
        editText[3] = (EditText) findViewById(R.id.edit_text_4);
        editText[4] = (EditText) findViewById(R.id.edit_text_5);
        editText[5] = (EditText) findViewById(R.id.edit_text_6);
        editText[0].requestFocus();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            editText[0].addOnUnhandledKeyEventListener((v, event) -> {
                Log.i("UnhandledKeyEvent", "" + event);
                if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                    if (editText[0].isFocused()) {
                        Log.i("et[1]", "Focused");
                    }
                    if (editText[1].isFocused()) {
                        editText[1].clearFocus();
                        editText[0].requestFocus();
                        editText[0].setSelection(editText[0].getText().length());
                    }
                    if (editText[2].isFocused()) {
                        editText[2].clearFocus();
                        editText[1].requestFocus();
                        editText[1].setSelection(editText[1].getText().length());
                    }
                    if (editText[3].isFocused()) {
                        editText[3].clearFocus();
                        editText[2].requestFocus();
                        editText[2].setSelection(editText[2].getText().length());
                    }
                    if (editText[4].isFocused()) {
                        editText[4].clearFocus();
                        editText[3].requestFocus();
                        editText[3].setSelection(editText[3].getText().length());
                    }
                    if (editText[5].isFocused()) {
                        editText[5].clearFocus();
                        editText[4].requestFocus();
                        editText[4].setSelection(editText[4].getText().length());
                    }
                }
                return false;
            });
        } else {
            editText[0].setOnKeyListener((v, keyCode, event) -> {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                    Log.i("Hardware", "Event detected " + event);
                    Log.i("Hardware", "Key detected " + keyCode);
                }
                return false;
            });

            editText[1].setOnKeyListener((v, keyCode, event) -> {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                    Log.i("Hardware", "Event detected " + event);
                    Log.i("Hardware", "Key detected " + keyCode);
                    editText[1].clearFocus();
                    editText[0].requestFocus();
                    editText[0].setSelection(editText[0].getText().length());
                }
                return false;
            });

            editText[2].setOnKeyListener((v, keyCode, event) -> {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                    Log.i("Hardware", "Event detected " + event);
                    Log.i("Hardware", "Key detected " + keyCode);
                    editText[2].clearFocus();
                    editText[1].requestFocus();
                    editText[1].setSelection(editText[1].getText().length());
                }
                return false;
            });

            editText[3].setOnKeyListener((v, keyCode, event) -> {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                    Log.i("Hardware", "Event detected " + event);
                    Log.i("Hardware", "Key detected " + keyCode);
                    editText[3].clearFocus();
                    editText[2].requestFocus();
                    editText[2].setSelection(editText[2].getText().length());
                }
                return false;
            });

            editText[4].setOnKeyListener((v, keyCode, event) -> {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                    Log.i("Hardware", "Event detected " + event);
                    Log.i("Hardware", "Key detected " + keyCode);
                    editText[4].clearFocus();
                    editText[3].requestFocus();
                    editText[3].setSelection(editText[3].getText().length());
                }
                return false;
            });

            editText[5].setOnKeyListener((v, keyCode, event) -> {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                    Log.i("Hardware", "Event detected " + event);
                    Log.i("Hardware", "Key detected " + keyCode);
                    editText[5].clearFocus();
                    editText[4].requestFocus();
                    editText[4].setSelection(editText[4].getText().length());
                }
                return false;
            });
        }


        editText[0].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.i("Char", "editText[0] " + s);
                Log.i("start", "editText[0] " + start);
                Log.i("count", "editText[0] " + count);
                Log.i("after", "editText[0] " + after);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    editText[0].clearFocus();
                    editText[1].requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i("Char", "editText[0] After " + s);
            }
        });
        editText[1].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.i("Char", "editText[1] " + s);
                Log.i("start", "editText[1] " + start);
                Log.i("count", "editText[1] " + count);
                Log.i("after", "editText[1] " + after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    editText[1].clearFocus();
                    editText[2].requestFocus();

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        editText[2].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.i("Char length", "editText[2] " + s.length());
                Log.i("Char", "editText[2] " + s);
                Log.i("start", "editText[2] " + start);
                Log.i("count", "editText[2] " + count);
                Log.i("after", "editText[2] " + after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("Char length", "editText[2] " + s.length());
                if (count > 0) {
                    editText[2].clearFocus();
                    editText[3].requestFocus();

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i("Char length", "editText[2] " + s.length());
            }
        });
        editText[3].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.i("Char", "editText[3] " + s);
                Log.i("start", "editText[3] " + start);
                Log.i("count", "editText[3] " + count);
                Log.i("after", "editText[3] " + after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    editText[3].clearFocus();
                    editText[4].requestFocus();

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        editText[4].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.i("Char", "editText[4] " + s);
                Log.i("start", "editText[4] " + start);
                Log.i("count", "editText[4] " + count);
                Log.i("after", "editText[4] " + after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    editText[4].clearFocus();
                    editText[5].requestFocus();

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        editText[5].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.i("Char", "editText[5] " + s);
                Log.i("start", "editText[5] " + start);
                Log.i("count", "editText[5] " + count);
                Log.i("after", "editText[5] " + after);
            }

            @Override
            public void onTextChanged(CharSequence s, final int start, int before, int count) {
                if (count > 0) {
                    // editText[5].clearFocus();
                    //Intent to Signup Fragment here

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        Log.i("kk", "error");

        //getting phone number
        phone_num = findViewById(R.id.pho_number);
     //   otpEditText = findViewById(R.id.et_otp);

        phonenumber_value = getIntent().getStringExtra("mobile");
        phone_num.setText(phonenumber_value);
        mAuth = FirebaseAuth.getInstance();
        phonenumber_value = "+91" + phonenumber_value;

        btnVerify = findViewById(R.id.otp_btn1);

        sendVerificationCode(phonenumber_value);

        // displaying keyboard
     //   otpEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        btnVerify.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                ArrayList<String> arrayList=new ArrayList<>();
                //testing purpose----------------------**********
//                String otp = otpa.getText().toString() + otpb.getText().toString() + otpc.getText().toString() + otpd.getText().toString() + otpe.getText().toString() + otpf.getText().toString();
//
//                if(otp.length() == 6)
//                    verifyCode(otp);
//                else
//                    Toast.makeText(Getotp_activity.this, "Enter Correct OTP", Toast.LENGTH_SHORT).show();


              //  String code = otpEditText.getText().toString();

                if(editText[0].getText().toString().length()!=0 &&
                        editText[1].getText().length()!=0 &&
                        editText[2].getText().length()!=0 &&
                        editText[3].getText().length()!=0 &&
                        editText[4].getText().length()!=0 &&
                        editText[5].getText().length()!=0)
                {
                    for(int i=0;i<editText.length;i++)
                    {
                      //  Toast.makeText(getApplicationContext(), ""+editText[i].getText().toString(), Toast.LENGTH_SHORT).show();

                        arrayList.add(editText[i].getText().toString());
                    }

                    String one=arrayList.get(0);
                    String two=arrayList.get(1);
                    String three=arrayList.get(2);
                    String four=arrayList.get(3);
                    String five=arrayList.get(4);
                    String six=arrayList.get(5);
                    String code=one+two+three+four+five+six;
                    Log.d("tempCode", "onClick: "+code);


                    pBar.setVisibility(View.VISIBLE);
                    btnVerify.setVisibility(View.GONE);
                    verifyCode(code);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please Enter Valid OTP", Toast.LENGTH_SHORT).show();
                }


//                if (code.isEmpty()){
//                    Toast.makeText(Getotp_activity.this, "Please Enter the OTP", Toast.LENGTH_SHORT).show();
//                }
//                else if (code.length()<6) {
//                    Toast.makeText(Getotp_activity.this, "Please Enter the correct OTP", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    verifyCode(code);
//                }
            }
        });
        resendOTPTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendOTPTV.setVisibility(View.GONE);
                timer.setVisibility(View.VISIBLE);
                resendVerificationCode(phonenumber_value, resendOTPtoken);
                countTimer();
            }
        });
    }

    private void countTimer()
    {
        new CountDownTimer(15000, 1000)
        {
            public void onTick(long millisUntilFinished) {

                NumberFormat f = new DecimalFormat("00");
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                timer.setText("Retry after - "+f.format(min) + ":" + f.format(sec));
            }
            // When the task is over it will print 00:00:00 there
            public void onFinish() {
                resendOTPTV.setVisibility(View.VISIBLE);
                timer.setVisibility(View.GONE);
               // btnVerify.setEnabled(true);
            }
        }.start();
    }

    private void verifyCode(String code)
    {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    /*private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallBack,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
*/
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallBack)
                        .setForceResendingToken(token)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {


                           // dialog.hide();
                            // todo check if the pin is already set or not

                          //  Toast.makeText(Getotp_activity.this, "nnnn", Toast.LENGTH_SHORT).show();
                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

                            mDatabase.child("Merchant_data").child(Objects.requireNonNull(mAuth.getUid())).child("Account_Information").child("emailAddress").get()
                                    .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                                            if (!task.isSuccessful())
                                            {
                                                Log.e("Firebase","There is error to get the data");
                                                btnVerify.setVisibility(View.VISIBLE);
                                                pBar.setVisibility(View.GONE);
                                                Toast.makeText(getApplicationContext(), "Failed to get registration", Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                if (task.getResult().exists())
                                                {

                                                    mDatabase.child("Merchant_data").child(Objects.requireNonNull(mAuth.getUid())).child("Account_Information").child("registrationPaymentDone").addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            Intent intent=new Intent(getApplicationContext(), Home.class);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);
                                                            finish();
//                                                            boolean isPaid=snapshot.getValue(Boolean.class);
//                                                            if(isPaid)
//                                                            {
//                                                                Intent intent=new Intent(getApplicationContext(), Home.class);
//                                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                                                startActivity(intent);
//                                                                finish();
//                                                            }
//                                                            else
//                                                            {
//                                                                Intent intent=new Intent(getApplicationContext(), RegistrationPayActivity.class);
//                                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                                                startActivity(intent);
//                                                                finish();
//                                                            }
                                                        }
                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
//                                                    startActivity(new Intent(getApplicationContext(), Home.class));
                                                }
                                                else {
                                                    startActivity(new Intent(getApplicationContext(), BasicDetails.class));
                                                }
                                                finish();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e)
                                {
                                    btnVerify.setVisibility(View.VISIBLE);
                                    pBar.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            });
                            //Toast.makeText(VerifyOTPActivity.this, "User verified..", Toast.LENGTH_SHORT).show();
                            Toast.makeText(Getotp_activity.this, "User verified", Toast.LENGTH_SHORT).show();
                            btnVerify.setVisibility(View.VISIBLE);
                            pBar.setVisibility(View.GONE);
                            btnVerify.setEnabled(false);
                        }
                        else
                        {
                            btnVerify.setVisibility(View.VISIBLE);
                            pBar.setVisibility(View.GONE);
                            Toast.makeText(Getotp_activity.this, "Fail to verify the user..", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendVerificationCode(String number) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallBack)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
       // Toast.makeText(Getotp_activity.this, "idher", Toast.LENGTH_SHORT).show();




    }

    /*private void sendVerificationCode(String number) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                getotp_activity.this,
                mCallBack
        );
    }*/


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken)
        {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
            resendOTPtoken = forceResendingToken;
            btnVerify.setEnabled(true);

        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            final String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Log.e("TAG", "ERROR IS " + e.getMessage());
            Toast.makeText(Getotp_activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

}
