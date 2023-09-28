package com.cu.gastosmerchant1.settings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cu.gastosmerchant1.R;
import com.cu.gastosmerchant1.details.SettingItem;

import java.util.ArrayList;

public class RecyclerAdapterSettingMenu extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    ArrayList<SettingItem> settingItems;
    clickItem onClickItem;

    public RecyclerAdapterSettingMenu(ArrayList<SettingItem> settingItems, clickItem onClickItem) {
        this.settingItems = settingItems;
        this.onClickItem = onClickItem;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.setting_item_layout,parent,false);
        return new viewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        viewHolder itemView = (viewHolder) holder;

        itemView.mSettingImage.setImageResource(settingItems.get(position).getImage());
        itemView.mSettingTittle.setText(settingItems.get(position).getTittle());
//        itemView.mSettingTittle.setText("text");

        itemView.mSettingOption.setOnClickListener(view -> onClickItem.onSettingOptionClicked(settingItems.get(position).getTittle()));

        if (settingItems.size()-1==position){
            itemView.mBottomLine.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if (settingItems==null){
            return 0;
        }
        return settingItems.size();
    }

    public interface clickItem{
        void onSettingOptionClicked(String optionName);
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView mSettingImage;
        TextView mSettingTittle;
        LinearLayout mSettingOption;
        View mBottomLine;
        public viewHolder(@NonNull View itemView) {

            super(itemView);
            mSettingImage = itemView.findViewById(R.id.image_setting_item);
            mSettingTittle = itemView.findViewById(R.id.tittle_setting_item);
            mSettingOption = itemView.findViewById(R.id.linear_setting_item);
            mBottomLine = itemView.findViewById(R.id.line);
        }
    }
}
