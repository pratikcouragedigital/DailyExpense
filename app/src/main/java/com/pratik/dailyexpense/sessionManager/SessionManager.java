package com.pratik.dailyexpense.sessionManager;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;      // Shared pref mode
    SessionManager sessionManager;

    // Sharedpref file name
    private static final String PREF_NAME = "Preference";

    // All Shared Preferences Keys
    public static final String KEY_TOTAL_AMOUNT = "totalAmount";

    public static final String KEY_TOKEN = "token";
    public static final String KEY_DEVICEID = "deviceId";

    String userType;

    public SessionManager(Context c) {
        this.context = c;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void SetTotalAmount(String totalAmount ) {

        editor.putString(KEY_TOTAL_AMOUNT, totalAmount);
        editor.commit();
    }

    public HashMap<String, String> GetTotalAmount() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_TOTAL_AMOUNT, pref.getString(KEY_TOTAL_AMOUNT, null));
        return user;
    }

    public void createUserFirebaseNotificationToken(String token, String deviceId) {
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_DEVICEID, deviceId);
        editor.commit();
    }

    public HashMap<String, String> getUserFirebaseNotificationToken() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_TOKEN, pref.getString(KEY_TOKEN, null));
        user.put(KEY_DEVICEID, pref.getString(KEY_DEVICEID, null));

        return user;
    }


}
