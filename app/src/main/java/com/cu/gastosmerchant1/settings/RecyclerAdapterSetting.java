package com.cu.gastosmerchant1.settings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cu.gastosmerchant1.R;
import com.cu.gastosmerchant1.details.SettingCard;

import java.util.ArrayList;

public class RecyclerAdapterSetting extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    ArrayList<SettingCard> settingCardData;
    RecyclerAdapterSettingMenu.clickItem onClickItem;

    public RecyclerAdapterSetting(ArrayList<SettingCard> settingCardData, RecyclerAdapterSettingMenu.clickItem onClickItem) {
        this.settingCardData = settingCardData;
        this.onClickItem = onClickItem;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.setting_card_layour,parent,false);
        return new viewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        viewHolder itemViewHolder = (viewHolder) holder;

        itemViewHolder.mTextTittle.setText(settingCardData.get(position).getTittle());

        RecyclerAdapterSettingMenu adapterSettingMenu = new RecyclerAdapterSettingMenu(settingCardData.get(position).getSettingItems(),onClickItem);
        itemViewHolder.mRecyclerCard.setAdapter(adapterSettingMenu);
        itemViewHolder.mRecyclerCard.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));


    }

    @Override
    public int getItemCount() {
        return settingCardData.size();
    }

    private class viewHolder extends RecyclerView.ViewHolder{
        TextView mTextTittle;
        RecyclerView mRecyclerCard;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            mTextTittle = itemView.findViewById(R.id.tittle_menu);
            mRecyclerCard = itemView.findViewById(R.id.recycler_list);

        }
    }
}
