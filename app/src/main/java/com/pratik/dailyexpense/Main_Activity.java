package com.pratik.dailyexpense;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.UpdateClickListener;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.google.android.gms.ads.AdListener;
import com.pratik.dailyexpense.adapter.Expense_Month_List_Adapter;
import com.pratik.dailyexpense.connectivity.Expense_Request_Data;
import com.pratik.dailyexpense.model.Expense_Items;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.toptas.fancyshowcase.FancyShowCaseQueue;
import me.toptas.fancyshowcase.FancyShowCaseView;

import com.github.javiersantos.appupdater.enums.Display;
import com.pratik.dailyexpense.sessionManager.SessionManager;
import com.rampo.updatechecker.UpdateChecker;
import com.rampo.updatechecker.UpdateCheckerResult;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


import android.net.Uri;

public class Main_Activity extends AppCompatActivity {

    private ProgressDialog progressDialog = null;

    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.RecyclerView)
    RecyclerView recyclerView;

    public Integer[] mThumbIds;
    public String[] title;
    public int[] groupId;

    public List<Expense_Items> listItems = new ArrayList<Expense_Items>();

    RecyclerView.Adapter reviewAdapter;
    LinearLayoutManager linearLayoutManager;
    GridLayoutManager gridLayoutManager;
    TextView txtTotalAmount;

    private String packageName, imFrom = "MainActivity";
    private URL url;
    private FancyShowCaseQueue fancyShowCaseQueue;

    private AdView adView, adView1;

    SessionManager sessionManager;
    String token,deviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

        sessionManager = new SessionManager(this);
        HashMap<String, String> userToken = sessionManager.getUserFirebaseNotificationToken();
        token = userToken.get(SessionManager.KEY_TOKEN);
        deviceId = userToken.get(SessionManager.KEY_DEVICEID);

//        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
//        Toast.makeText(this, "" + currentDateTimeString, Toast.LENGTH_SHORT).show();
//
//
//        Intent intent = new Intent(this, Expense_Add.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this);
//
//        notificationBuilder.setSmallIcon(R.drawable.app_icon);
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//            notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.app_icon));
//        } else {
//            notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.app_icon));
//        }
//        notificationBuilder.setContentTitle("Add Expense");
//        notificationBuilder.setContentText("You forget to add your expense.");
//        notificationBuilder.setPriority(2);
//        notificationBuilder.setAutoCancel(true);
//        notificationBuilder.setSound(defaultSoundUri);
//        notificationBuilder.setLights(Color.GREEN, 1000, 1000);
//        notificationBuilder.setContentIntent(pendingIntent);
//
//
//        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        manager.notify(0, notificationBuilder.build());


        ShowBannerAds();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Month List");

        fab = (FloatingActionButton) findViewById(R.id.fab);
        txtTotalAmount = (TextView) findViewById(R.id.txtTotalAmount);
        recyclerView = (RecyclerView) findViewById(R.id.RecyclerView);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoForm = new Intent(Main_Activity.this, Expense_Add.class);
                gotoForm.putExtra("imFrom", imFrom);
                startActivity(gotoForm);
            }
        });

        SharedPreferences wmbPreference = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirstRun = wmbPreference.getBoolean("FIRSTRUN", true);
        if (isFirstRun) {
            // Code to run once
            final FancyShowCaseView fancyShowCaseView1 = new FancyShowCaseView.Builder(this)
                    .title("Click here to add your expense,also click on month to check expense details")
                    .focusOn(fab)
                    .build();

            fancyShowCaseQueue = new FancyShowCaseQueue()
                    .add(fancyShowCaseView1);

            fancyShowCaseQueue.show();

            SharedPreferences.Editor editor = wmbPreference.edit();
            editor.putBoolean("FIRSTRUN", false);
            editor.commit();
        }

        title = new String[]{"January", "February", "March", "April",
                "May", "June", "July", "August", "September",
                "October", "November", "December"};
        groupId = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};

        recyclerView.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.smoothScrollToPosition(0);
        reviewAdapter = new Expense_Month_List_Adapter(listItems);
        recyclerView.setAdapter(reviewAdapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        getList();


//        new AppUpdater(this)
//                .setUpdateFrom(UpdateFrom.GOOGLE_PLAY)
//                .setDisplay(Display.DIALOG)
//                .setTitleOnUpdateAvailable("Update available")
//                .setContentOnUpdateAvailable("Check out the latest version available of my app!")
//                //.showAppUpdated(true)
//                .start();
//        CheckIsAppUpdated();

    }

    private void ShowBannerAds() {
        adView = (AdView) findViewById(R.id.adView);
        adView1 = (AdView) findViewById(R.id.adView1);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView1.loadAd(adRequest);

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        });
        adView1.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        });
    }


    private void CheckIsAppUpdated() {
        packageName = getPackageName();

        try {
            url = new URL("https://play.google.com/store/apps/details?id=com.pratik.dailyexpense");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        new AppUpdater(this)
                .setDisplay(Display.DIALOG)
                .setUpdateFrom(UpdateFrom.GOOGLE_PLAY)
                .setTitleOnUpdateAvailable("Update available")
                .setContentOnUpdateAvailable("Check out the latest version available of my app!")
                .setTitleOnUpdateNotAvailable("Update not available")
                .setContentOnUpdateNotAvailable("No update available. Check for updates again later!")
                .setButtonUpdate("Update now?")
                .setButtonUpdateClickListener(new UpdateClickListener(this, UpdateFrom.GOOGLE_PLAY, url))
                .setButtonDismiss(null)
                .setButtonDoNotShowAgain(null)
                .setCancelable(false)
                .showAppUpdated(true)
                .start();


    }

    private void getList() {
        try {
            Expense_Request_Data getMonthList = new Expense_Request_Data(this);
            getMonthList.GetMonthList(listItems, recyclerView, reviewAdapter, title, progressDialog);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Daily Expense  is an amazing app to deal with the spending in this swamped life. \n\n https://play.google.com/store/apps/details?id=com.pratik.dailyexpense");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return true;
            case R.id.feedback:

                Intent gotofeedback = new Intent(this, FeedBack.class);
                startActivity(gotofeedback);
                return true;

            case R.id.checkUpdate:
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
