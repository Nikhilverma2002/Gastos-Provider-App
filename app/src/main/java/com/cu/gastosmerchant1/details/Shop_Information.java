package com.cu.gastosmerchant1.details;

import java.io.Serializable;
import java.util.ArrayList;

public class Shop_Information implements Serializable {
    String Category;
    String ShopDistrict;
    String ShopAddress;
    String ShopAddressLatitude;
    String ShopAddressLongitude;
    String ShopName;
    String ShopImageUri;
    long creationTimeStamp;
    ArrayList<Discount> discounts;
    String ShopImageUri1="";
    String ShopImageUri2="";
    String ShopImageUri3="";
    String ShopImageUri4="";
    String ShopState;
    String shopArea;
    boolean homeDelivery=false;
    boolean pickUp=false;

    public Shop_Information() {
    }

    public Shop_Information(String category, String shopDistrict, String shopAddress, String shopAddressLatitude, String shopAddressLongitude, String shopName, String shopImageUri, long creationTimeStamp, ArrayList<Discount> discounts, String shopState, String shopArea) {
        Category = category;
        ShopDistrict = shopDistrict;
        ShopAddress = shopAddress;
        ShopAddressLatitude = shopAddressLatitude;
        ShopAddressLongitude = shopAddressLongitude;
        ShopName = shopName;
        ShopImageUri = shopImageUri;
        this.creationTimeStamp = creationTimeStamp;
        this.discounts = discounts;
        ShopState = shopState;
        this.shopArea = shopArea;
    }

    public Shop_Information(String category, String shopDistrict, String shopAddress, String shopAddressLatitude, String shopAddressLongitude, String shopName, String shopImageUri, long creationTimeStamp, ArrayList<Discount> discounts, String shopImageUri1, String shopImageUri2, String shopImageUri3, String shopImageUri4, String shopState, String shopArea) {
        Category = category;
        ShopDistrict = shopDistrict;
        ShopAddress = shopAddress;
        ShopAddressLatitude = shopAddressLatitude;
        ShopAddressLongitude = shopAddressLongitude;
        ShopName = shopName;
        ShopImageUri = shopImageUri;
        this.creationTimeStamp = creationTimeStamp;
        this.discounts = discounts;
        ShopImageUri1 = shopImageUri1;
        ShopImageUri2 = shopImageUri2;
        ShopImageUri3 = shopImageUri3;
        ShopImageUri4 = shopImageUri4;
        ShopState = shopState;
        this.shopArea = shopArea;
    }

    public String getShopState() {
        return ShopState;
    }

    public void setShopState(String shopState) {
        ShopState = shopState;
    }

    public String getShopArea() {
        return shopArea;
    }

    public void setShopArea(String shopArea) {
        this.shopArea = shopArea;
    }

    public String getShopImageUri1() {
        return ShopImageUri1;
    }

    public void setShopImageUri1(String shopImageUri1) {
        ShopImageUri1 = shopImageUri1;
    }

    public String getShopImageUri2() {
        return ShopImageUri2;
    }

    public void setShopImageUri2(String shopImageUri2) {
        ShopImageUri2 = shopImageUri2;
    }

    public String getShopImageUri3() {
        return ShopImageUri3;
    }

    public void setShopImageUri3(String shopImageUri3) {
        ShopImageUri3 = shopImageUri3;
    }

    public String getShopImageUri4() {
        return ShopImageUri4;
    }

    public void setShopImageUri4(String shopImageUri4) {
        ShopImageUri4 = shopImageUri4;
    }

    public ArrayList<Discount> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(ArrayList<Discount> discounts) {
        this.discounts = discounts;
    }

    public long getCreationTimeStamp() {
        return creationTimeStamp;
    }

    public void setCreationTimeStamp(long creationTimeStamp) {
        this.creationTimeStamp = creationTimeStamp;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getShopDistrict() {
        return ShopDistrict;
    }

    public void setShopDistrict(String shopDistrict) {
        ShopDistrict = shopDistrict;
    }


    public String getShopAddress() {
        return ShopAddress;
    }

    public void setShopAddress(String shopAddress) {
        ShopAddress = shopAddress;
    }

    public String getShopAddressLatitude() {
        return ShopAddressLatitude;
    }

    public void setShopAddressLatitude(String shopAddressLatitude) {
        ShopAddressLatitude = shopAddressLatitude;
    }

    public String getShopAddressLongitude() {
        return ShopAddressLongitude;
    }

    public void setShopAddressLongitude(String shopAddressLongitude) {
        ShopAddressLongitude = shopAddressLongitude;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
    }

    public String getShopImageUri() {
        return ShopImageUri;
    }

    public void setShopImageUri(String shopImageUri) {
        ShopImageUri = shopImageUri;
    }

}
