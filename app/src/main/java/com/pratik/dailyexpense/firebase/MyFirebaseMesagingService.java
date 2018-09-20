package com.pratik.dailyexpense.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pratik.dailyexpense.Expense_Add;
import com.pratik.dailyexpense.R;


public class MyFirebaseMesagingService extends FirebaseMessagingService {

    Bitmap bitmap;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        showNotification(remoteMessage);
    }

    private void showNotification(RemoteMessage remoteMessage) {
        String message = remoteMessage.getData().get("NOTIFICATION_TYPE");
        Intent intent = new Intent(this, Expense_Add.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this);

        notificationBuilder.setSmallIcon(R.drawable.app_icon);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.app_icon));
        } else {
            notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.app_icon));
        }
        notificationBuilder.setContentTitle("Add Expense");
        notificationBuilder.setContentText("You forget to add your expense.");
        notificationBuilder.setPriority(2);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSound(defaultSoundUri);
        notificationBuilder.setLights(Color.GREEN, 1000, 1000);
        notificationBuilder.setContentIntent(pendingIntent);


        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0, notificationBuilder.build());
    }
}