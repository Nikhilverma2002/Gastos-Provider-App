package com.cu.gastosmerchant1.details;

public class SettingItem {
    String tittle;
    int image;

    public SettingItem(String tittle, int image) {
        this.tittle = tittle;
        this.image = image;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
