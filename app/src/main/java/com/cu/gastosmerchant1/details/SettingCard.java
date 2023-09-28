package com.cu.gastosmerchant1.details;

import java.util.ArrayList;

public class SettingCard {
    private String Tittle;
    private ArrayList<SettingItem> settingItems;

    public SettingCard(String tittle) {
        Tittle = tittle;
    }

    public SettingCard(String tittle, ArrayList<SettingItem> settingItems) {
        Tittle = tittle;
        this.settingItems = settingItems;
    }

    public String getTittle() {
        return Tittle;
    }

    public void setTittle(String tittle) {
        Tittle = tittle;
    }

    public ArrayList<SettingItem> getSettingItems() {
        return settingItems;
    }

    public void setSettingItems(ArrayList<SettingItem> settingItems) {
        this.settingItems = settingItems;
    }
}
