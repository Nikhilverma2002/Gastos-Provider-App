package com.cu.gastosmerchant1.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.cu.gastosmerchant1.Dashboard.Home;
import com.cu.gastosmerchant1.R;

public class Adapter_class extends PagerAdapter {

    Context context;
    public Adapter_class(Context context)
    {
        this.context=context;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position)
    {

        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_pager, container, false);

       TextView amount,packs,content,month,serviceCharges,discount,gst,grandTotal;
       CardView materialCardView;
       ImageView back;



        amount = view.findViewById(R.id.amount);
        packs = view.findViewById(R.id.packs);
        content = view.findViewById(R.id.content);
        month = view.findViewById(R.id.months);
        serviceCharges = view.findViewById(R.id.serviceCharges);
        discount = view.findViewById(R.id.discount);
        gst= view.findViewById(R.id.gst);
        grandTotal = view.findViewById(R.id.grandTotal);
       // back = view.findViewById(R.id.back);
        materialCardView = view.findViewById(R.id.materialCardView);
        Log.d("poss", "instantiateItem: "+position);

//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i=new Intent(context, Home.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(i);
//
//            }
//        });


//        windchain.setBackgroundResource(mainimage[position]);
//        text.setImageResource(bodyimage[position]);

        if(position==0)
        {
            amount.setText("3000");
            packs.setText("Premium Package");
            content.setText("Perfect for large businesses with sizeable operation,\nproviding higher wallet balance & other features.");
            month.setText("3 months marketplace membership FREE");
            serviceCharges.setText("₹1599");
            discount.setText("-₹400");
            gst.setText("₹215.82");
            grandTotal.setText("₹1414.82");
            materialCardView.setCardBackgroundColor(Color.parseColor("#FBC87B"));



        }
        if(position==1)
        {
            amount.setText("1400");
            packs.setText("Standard Package");
           content.setText("Fits most use-cases with a medium sized operation.\n");
            month.setText("1 months marketplace membership FREE");
            serviceCharges.setText("₹799");
            discount.setText("-₹200");
            gst.setText("₹107.82");
            grandTotal.setText("₹706.82");
            materialCardView.setCardBackgroundColor(Color.parseColor("#60A5FA"));
        }
        if(position==2)
        {

            amount.setText("600");
            packs.setText("Basic Package");
            content.setText("Perfect for Shopkeepers with basic needs.\n");
            //month.setText(" months marketplace membership FREE");
            month.setVisibility(View.INVISIBLE);
            serviceCharges.setText("₹399.00");
            discount.setText("-₹100.00");
            gst.setText("₹53.82");
            grandTotal.setText("₹352.82");
        }




        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((View) object);

    }
}
