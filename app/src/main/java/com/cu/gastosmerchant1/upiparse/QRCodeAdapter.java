package com.cu.gastosmerchant1.upiparse;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.cu.gastosmerchant1.R;
import com.cu.gastosmerchant1.details.Payment_Information;
import com.cu.gastosmerchant1.registration.AddQR;

import java.util.ArrayList;

public class QRCodeAdapter extends RecyclerView.Adapter<QRCodeAdapter.QRViewHolder> {
    private Context context;
    private ArrayList<UpiView> upiViewArrayList;
    private ArrayList<Payment_Information> upiArrayList;
    private static ImageView containerImage;
    private int mode;

    public QRCodeAdapter(Context context, ArrayList<UpiView> upiViewArrayList, ArrayList<Payment_Information> upiArrayList, int mode) {
        this.context = context;
        this.upiViewArrayList = upiViewArrayList;
        this.upiArrayList = upiArrayList;
        this.mode = mode;
    }

    @NonNull
    @Override
    public QRViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.qr_item, parent, false);
        return new QRViewHolder(view);
    }

    void alertBox(int position) {
        class CustomPrimaryDialog extends Dialog {
            CardView setPrimary;
            ImageView upiImage;

            public CustomPrimaryDialog(@NonNull Context context) {
                super(context);
            }

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                requestWindowFeature(Window.FEATURE_NO_TITLE);
                setContentView(R.layout.alertdialog_setprimary);
                upiImage = findViewById(R.id.qrItemImage);
                setPrimary = findViewById(R.id.set_primary);

                String upiName = upiViewArrayList.get(position).getUpiName();
                if (upiName.equals("BharatPe UPI")) {
                    upiImage.setImageResource(R.drawable.bharatpe_mini_qr);
                } else if (upiName.equals("GooglePay UPI")) {
                    upiImage.setImageResource(R.drawable.googlepay_mini_qr);
                } else if (upiName.equals("PayTm UPI")) {
                    upiImage.setImageResource(R.drawable.paytm_mini_qr);
                } else if (upiName.equals("BHIM UPI")) {
                    upiImage.setImageResource(R.drawable.bhim_upi_mini_qr);
                } else if (upiName.equals("PhonePe UPI")) {
                    upiImage.setImageResource(R.drawable.phonepe_mini_qr);
                } else if (upiName.equals("Amazon UPI")) {
                    upiImage.setImageResource(R.drawable.amazon_upi_mini_qr);
                } else if (upiName.equals("Airtel UPI")) {
                    upiImage.setImageResource(R.drawable.airtel_mini_qr);
                } else {
                    upiImage.setImageResource(R.drawable.other_upi_mini_qr);
                }
            }
        }
        CustomPrimaryDialog customPrimaryDialog = new CustomPrimaryDialog(context);
        customPrimaryDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customPrimaryDialog.show();
        customPrimaryDialog.setPrimary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (UpiView upiView : upiViewArrayList) {
                    if (upiView.isPrimary()) {
                        upiView.setPrimary(false);
                    }
                }
                for (Payment_Information payment_information : upiArrayList) {
                    if (payment_information.isPrimary()) {
                        payment_information.setPrimary(false);
                    }
                }
                containerImage.setVisibility(View.INVISIBLE);
                upiViewArrayList.get(position).setPrimary(true);
                upiArrayList.get(position).setPrimary(true);

                if (mode == 0) {
                    AddQR.mTitlePrimary.setVisibility(View.VISIBLE);
                    AddQR.mNamePrimary.setText(upiArrayList.get(position).getUpiName());
                }

                //TODO PRIMARY QR SET PRIMARY
                notifyDataSetChanged();
                customPrimaryDialog.dismiss();
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull QRViewHolder holder, int position) {
        int temp = position;
        UpiView upiView = upiViewArrayList.get(position);
        holder.upiImageView.setImageResource(upiView.getResourceId());
        if (upiView.isPrimary()) {
            holder.primaryUpi.setVisibility(View.VISIBLE);
            containerImage = holder.primaryUpi;
            if (mode == 0) {
                AddQR.mNamePrimary.setText(upiView.getUpiName());
                AddQR.mTitlePrimary.setVisibility(View.VISIBLE);
            }
            //TODO QR IS PRIMARY
        }
        holder.upiImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertBox(temp);
            }
        });
    }

    @Override
    public int getItemCount() {
        return upiViewArrayList.size();
    }

    class QRViewHolder extends RecyclerView.ViewHolder {
        private ImageView upiImageView;
        private ImageView primaryUpi;

        public QRViewHolder(@NonNull View itemView) {
            super(itemView);
            upiImageView = itemView.findViewById(R.id.qrItemImage);
            primaryUpi = itemView.findViewById(R.id.primaryUpiImage);
        }
    }
}
