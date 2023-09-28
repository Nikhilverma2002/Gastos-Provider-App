package com.cu.gastosmerchant1.upiparse;

import android.util.Log;
import android.widget.Toast;

public class FindUpi {
    private static int BHARAT_PE = 1;//
    private static int PAYTM = 2;//
    private static int AMAZON_PAY = 3;//
    private static int G_PAY = 4;//
    private static int PHONE_PE = 5;//
    private static int AIRTEL_UPI = 6;//
    private static int WHATSAPP_PAY = 7;//
    private static int BHIM_UPI = 8;
    private static int BANK_UPI = 9;
    private static int OTHER_UPI = 10;

    public static int findUpi(String rawString) {
        int index = rawString.indexOf("@");
        String rawString2 = rawString.substring(index);
        int index1 = rawString2.indexOf("&");
        String upi = rawString2.substring(0, index1);
        if (upi.contains("ok")) {
            return G_PAY;
        }
        else if (upi.contains("@ybl"))
            return PHONE_PE;
        else if (upi.contains("@ibl"))
            return PHONE_PE;
        else if (upi.contains("@axl"))
            return PHONE_PE;
        else if (upi.contains("@apl"))
            return AMAZON_PAY;
        else if (upi.contains("@paytm"))
            return PAYTM;
        else if (upi.contains("BHARATPE"))// todo need to verify
            return BHARAT_PE;
        else if (upi.contains("@airtel"))
            return AIRTEL_UPI;
        else
            return OTHER_UPI;
    }

    public static String findUpiId(String rawString) {
        try {
            int index = rawString.indexOf("pa=");
            String rawString2 = rawString.substring(index);
            int index1 = rawString2.indexOf("&");
            String upi = rawString2.substring(3, index1);
            return upi;
        } catch (Exception e) {
            Log.e("The Error occurred is;", e.toString());
            return "error";
        }
    }
}
