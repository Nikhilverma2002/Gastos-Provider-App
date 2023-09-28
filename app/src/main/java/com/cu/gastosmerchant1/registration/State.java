
package com.cu.gastosmerchant1.registration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class State {

    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("districts")
    @Expose
    private ArrayList<String> districts = null;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public ArrayList<String> getDistricts() {
        return districts;
    }

    public void setDistricts(ArrayList<String> districts) {
        this.districts = districts;
    }

}
