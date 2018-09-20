package com.pratik.dailyexpense.firebase;

import android.provider.Settings;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.pratik.dailyexpense.sessionManager.SessionManager;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String REG_TOKEN ="REG_TOKEN";
    SessionManager sessionManager;
    String deviceId;
    String token;

    @Override
    public void onTokenRefresh() {
        token = FirebaseInstanceId.getInstance().getToken();
        Log.d(REG_TOKEN,"Your Token No:-"+ token);

        createDeviceId();
    }

    public void createDeviceId() {
        deviceId = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);

        sessionManager = new SessionManager(this);
        sessionManager.createUserFirebaseNotificationToken(token, deviceId);
    }
}
