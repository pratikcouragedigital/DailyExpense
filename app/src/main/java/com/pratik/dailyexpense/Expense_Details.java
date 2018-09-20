package com.pratik.dailyexpense;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.pratik.dailyexpense.adapter.Expense_Details_Adapter;
import com.pratik.dailyexpense.connectivity.Expense_Request_Data;
import com.pratik.dailyexpense.model.Expense_Items;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.toptas.fancyshowcase.FancyShowCaseQueue;
import me.toptas.fancyshowcase.FancyShowCaseView;
import me.toptas.fancyshowcase.OnViewInflateListener;

public class Expense_Details extends AppCompatActivity implements View.OnClickListener{

    private ProgressDialog progressDialog = null;

    public List<Expense_Items> listItems = new ArrayList<Expense_Items>();
    RecyclerView recyclerView;
    RecyclerView.Adapter reviewAdapter;
    LinearLayoutManager linearLayoutManager;
    TextView txtTotalAmount;
    String monthName,expMonthAmount,imFrom = "ExpenseDetails",selectedDate="";
    FloatingActionButton fab;
    SwipeController swipeController = null;
    private FancyShowCaseQueue fancyShowCaseQueue;

    InterstitialAd mInterstitialAd;

    private Context mContext;

    EditText submitionDate;
    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_details);

        Calendar mcurrentDate = Calendar.getInstance();
        year = mcurrentDate.get(Calendar.YEAR);
        month = mcurrentDate.get(Calendar.MONTH);
        day = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        submitionDate = (EditText) findViewById(R.id.txtSubmissionDate);
        submitionDate.setOnClickListener(this);
    
        mContext = this;
        ShowInterstitialAd();

        Intent intent = getIntent();
        if (null != intent) {
            monthName = intent.getStringExtra("month");
//            expMonthAmount = intent.getStringExtra("expMonthAmount");
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoForm = new Intent(Expense_Details.this,Expense_Add.class);
                gotoForm.putExtra("imFrom", imFrom);
                gotoForm.putExtra("month", monthName);
                startActivity(gotoForm);
            }
        });

        txtTotalAmount = (TextView) findViewById(R.id.txtTotalAmount);
//        txtTotalAmount.setText(expMonthAmount);
        recyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.smoothScrollToPosition(0);
        reviewAdapter = new Expense_Details_Adapter(this,listItems, txtTotalAmount, recyclerView);
        recyclerView.setAdapter(reviewAdapter);

        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onLeftClicked(int position) {
                super.onLeftClicked(position);
                String amount = listItems.get(position).getamount();
                String reason = listItems.get(position).getreason();
                String date = listItems.get(position).getdate();
                String id = listItems.get(position).getListId();
                String monthName = listItems.get(position).getmonth();

                Intent i = new Intent(Expense_Details.this, Expense_Edit.class);
                i.putExtra("date", date);
                i.putExtra("reason", reason);
                i.putExtra("amount", amount);
                i.putExtra("listId", id);
                i.putExtra("month", monthName);
                startActivity(i);

            }

            @Override
            public void onRightClicked(int position) {

                final String expenseListId = listItems.get(position).getListId();
                AlertDialog.Builder builder = new AlertDialog.Builder(Expense_Details.this);
                builder.setTitle("Confirmation");
                builder.setMessage("Do you really want to delete this entry?");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        progressDialog.setMessage("Please Wait..");
                        progressDialog.show();
                        progressDialog.setCancelable(false);
                        progressDialog.setCanceledOnTouchOutside(false);
                        try {

                            Expense_Request_Data expenseReq = new Expense_Request_Data(Expense_Details.this);
                            expenseReq.DeleteExpense(expenseListId, progressDialog);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        getList();
        
        SharedPreferences wmbPreference = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isSwipeDetailsRun = wmbPreference.getBoolean("SwipeRun", true);
        if (isSwipeDetailsRun) {
            // Code to run once
            final FancyShowCaseView fancyShowCaseView1 = new FancyShowCaseView.Builder(this)
//                .title("Swipe Left to Delete")

                    .customView(R.layout.layout_my_custom_view, new OnViewInflateListener() {
                        @Override
                        public void onViewInflated(@NonNull View view) {
                            view.findViewById(R.id.btnLeftClose).setOnClickListener(mClickListener);
                            view.findViewById(R.id.leftLayout).setVisibility(View.GONE);
                        }
                    })
                    .closeOnTouch(true)
                    .build();

            final FancyShowCaseView fancyShowCaseView3 = new FancyShowCaseView.Builder(this)
//                .title("Swipe Right to Edit")

                    .customView(R.layout.layout_my_custom_view, new OnViewInflateListener() {
                        @Override
                        public void onViewInflated(@NonNull View view) {
                            view.findViewById(R.id.btnRightClose).setOnClickListener(mClickListener);
                            view.findViewById(R.id.rightLayout).setVisibility(View.GONE);
                        }
                    })
                    .closeOnTouch(true)
                    .build();

            fancyShowCaseQueue = new FancyShowCaseQueue()
                    .add(fancyShowCaseView1)
                    .add(fancyShowCaseView3);

            fancyShowCaseQueue.show();

        }

        SharedPreferences.Editor editor = wmbPreference.edit();
        editor.putBoolean("SwipeRun", false);
        editor.commit();
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

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d("", "onClick: ");

          if(view.getId() == R.id.rightLayout){
              view.findViewById(R.id.rightLayout).setVisibility(View.GONE);
          }
          else if(view.getId() == R.id.leftLayout){
              view.findViewById(R.id.leftLayout).setVisibility(View.GONE);
          }
        }
    };

    private void getList() {
        try {
            Expense_Request_Data expenseDetailsList = new Expense_Request_Data(this);
            expenseDetailsList.ExpenseDetailsList(listItems, recyclerView, reviewAdapter, monthName, progressDialog);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void getDateWiseList() {
        try {
            Expense_Request_Data expenseDetailsList = new Expense_Request_Data(this);
            expenseDetailsList.DateWiseExpenseDetailsList(listItems, recyclerView, reviewAdapter, monthName,selectedDate, progressDialog);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,Main_Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
       super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.txtSubmissionDate){

            DatePickerDialog mDatePicker = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                    // TODO Auto-generated method stub
                    /*      Your code   to get date and time    */
                    selectedDate = String.valueOf(selectedyear) + "-" + String.valueOf(selectedmonth+1) + "-" + String.valueOf(selectedday);

                    //selectedDate = DateFormatter.ChangeDateFormat(selectedDate);

                    submitionDate.setText(selectedDate);
                    // do what u want
                    getDateWiseList();
                }
            }, year, month, day);
            mDatePicker.show();
        }
    }
}
