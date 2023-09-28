
package com.cu.gastosmerchant1.registration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetStates {

    @SerializedName("states")
    @Expose
    private ArrayList<State> states = null;

    public ArrayList<State> getStates() {
        return states;
    }

    public void setStates(ArrayList<State> states) {
        this.states = states;
    }

}
