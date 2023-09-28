package com.cu.gastosmerchant1.details;

import java.io.Serializable;

public class Account_Information implements Serializable {
    private String EmailAddress;
    private String OwnerName;
    private String PhoneNumber;
    private String Pin;
    private String version="2.0";
    private String SalesCode = "";
    private String WalletPromotion = "0";
    private String WalletBranding = "0";
    private String Platform = "Android";


    public String getWalletPromotion() {
        return WalletPromotion;
    }

    public void setWalletPromotion(String walletPromotion) {
        WalletPromotion = walletPromotion;
    }

    public String getWalletBranding() {
        return WalletBranding;
    }

    public void setWalletBranding(String walletBranding) {
        WalletBranding = walletBranding;
    }

    boolean isRegistrationPaymentDone;


    public Account_Information(String emailAddress, String ownerName, String phoneNumber, String pin, String salesCode) {
        EmailAddress = emailAddress;
        OwnerName = ownerName;
        PhoneNumber = phoneNumber;
        Pin = pin;
        SalesCode = salesCode;
    }

    //for pushing data in intern node
    public Account_Information(String emailAddress, String ownerName, String phoneNumber) {
        EmailAddress = emailAddress;
        OwnerName = ownerName;
        PhoneNumber = phoneNumber;
        SalesCode = null;
        WalletPromotion = null;
        WalletBranding = null;
    }

    public boolean isRegistrationPaymentDone() {
        return isRegistrationPaymentDone;
    }

    public void setRegistrationPaymentDone(boolean registrationPaymentDone) {
        isRegistrationPaymentDone = registrationPaymentDone;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSalesCode() {
        return SalesCode;
    }

    public void setSalesCode(String salesCode) {
        SalesCode = salesCode;
    }

    public Account_Information() {
    }


    public Account_Information(String EmailAddress, String OwnerName, String PhoneNumber, String Pin) {
        this.EmailAddress = EmailAddress;
        this.OwnerName = OwnerName;
        this.PhoneNumber = PhoneNumber;
        this.Pin = Pin;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String EmailAddress) {
        this.EmailAddress = EmailAddress;
    }

    public String getOwnerName() {
        return OwnerName;
    }

    public void setOwnerName(String ownerName) {
        this.OwnerName = ownerName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.PhoneNumber = phoneNumber;
    }

    public String getPin() {
        return Pin;
    }

    public void setPin(String pin) {
        this.Pin = pin;
    }
}
