package com.pratik.dailyexpense;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import java.util.Calendar;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.pratik.dailyexpense.connectivity.Expense_Request_Data;
import com.pratik.dailyexpense.sqlLite.DataBaseHelper;

public class Expense_Edit extends AppCompatActivity implements View.OnClickListener {

    EditText txtAmount;
    EditText txtReason;
    EditText submitionDate;
    Button btnSubmit;

    private StringBuilder date;
    String selectedDateForSubmission = "";
    String reason = "";
    String amount = "";
    String month_name = "";
    String listId;
    Spinner spinner2;

    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;

    private ProgressDialog progressDialog = null;

    private DataBaseHelper mydb;

    public String[] monthNameArray = new String[] {"Select Month","January", "February","March", "April",
            "May", "June", "July","August", "September",
            "October", "November", "December"};

    public int[] groupId = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};

    InterstitialAd mInterstitialAd;
    private AdView adView, adView1;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_edit);

        ShowBannerAds();
        mydb = new DataBaseHelper(this);

        spinner2 = (Spinner) findViewById(R.id.spinnerForMonth);
        txtReason = (EditText) findViewById(R.id.txtReason);
        txtAmount = (EditText) findViewById(R.id.txtAmount);
        submitionDate = (EditText) findViewById(R.id.txtSubmissionDate);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        Intent intent = getIntent();
        if (null != intent) {
            selectedDateForSubmission = intent.getStringExtra("date");
            reason = intent.getStringExtra("reason");
            amount = intent.getStringExtra("amount");
            listId = intent.getStringExtra("listId");
            month_name = intent.getStringExtra("month");
        }

        txtReason.setText(reason);
        txtAmount.setText(amount);
        submitionDate.setText(selectedDateForSubmission);

        btnSubmit.setOnClickListener(this);
        submitionDate.setOnClickListener(this);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, monthNameArray);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);
        if (month_name != null) {
            int spinnerPosition = dataAdapter.getPosition(month_name);
            spinner2.setSelection(spinnerPosition);
        }

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    month_name = parent.getItemAtPosition(position).toString();
                    Toast.makeText(Expense_Edit.this, month_name, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
                month_name = month_date.format(calendar.getTime());
            }
        });
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


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnSubmit) {
            reason = txtReason.getText().toString();
            amount = txtAmount.getText().toString();
            selectedDateForSubmission = submitionDate.getText().toString();
            if (selectedDateForSubmission == "") {
                Toast.makeText(this, "Please Select Date.", Toast.LENGTH_SHORT).show();
            } else if (reason.equals("")) {
                Toast.makeText(this, "Please Enter Description.", Toast.LENGTH_SHORT).show();
            } else if (amount.equals("")) {
                Toast.makeText(this, "Please Enter Heading.", Toast.LENGTH_SHORT).show();
            }else if(month_name.equals("Select Month") || month_name.equals("")){
                Toast.makeText(this, "Please Select Month.", Toast.LENGTH_SHORT).show();
            }
            else {

                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Please Wait..");
                progressDialog.show();
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);

                try {
                    Expense_Request_Data expenseReq = new Expense_Request_Data(Expense_Edit.this);
                    expenseReq.EditExpense(listId,amount,reason, selectedDateForSubmission,month_name,year,progressDialog);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ShowInterstitialAd();

            }
        }
        else if(v.getId() == R.id.txtSubmissionDate){

            DatePickerDialog mDatePicker = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                    // TODO Auto-generated method stub
                    /*      Your code   to get date and time    */
                    selectedDateForSubmission = String.valueOf(selectedyear) + "-" + String.valueOf(selectedmonth+1) + "-" + String.valueOf(selectedday);

                    //selectedDate = DateFormatter.ChangeDateFormat(selectedDate);

                    submitionDate.setText(selectedDateForSubmission);
                    // do what u want



                }
            }, year, month, day);
            mDatePicker.show();
        }

    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    private void ShowInterstitialAd() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }

            @Override
            public void onAdClosed() {
//                Toast.makeText(getApplicationContext(), "Ad is closed!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
//                Toast.makeText(getApplicationContext(), "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLeftApplication() {
//                Toast.makeText(getApplicationContext(), "Ad left application!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
//                Toast.makeText(getApplicationContext(), "Ad is opened!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
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

                Intent gotofeedback = new Intent(this,FeedBack.class);
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
