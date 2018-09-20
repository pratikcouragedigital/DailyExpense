package com.pratik.dailyexpense.connectivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.pratik.dailyexpense.Expense_Details;
import com.pratik.dailyexpense.Expense_Add;
import com.pratik.dailyexpense.Expense_Edit;
import com.pratik.dailyexpense.Main_Activity;
import com.pratik.dailyexpense.R;
import com.pratik.dailyexpense.model.Expense_Items;
import com.pratik.dailyexpense.sessionManager.SessionManager;
import com.pratik.dailyexpense.sqlLite.DataBaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Expense_Request_Data {

    private static int currentPage;
    private static ProgressDialog progressDialogBox;

    private static RecyclerView.Adapter adapterForAsyncTask;
    private static RecyclerView recyclerViewForAsyncTask;
    private static List<Expense_Items> ItemsArrayForAsyncTask;
    private static DataBaseHelper mydb;


    private static Context context;
    private static String ResponseResult;
    private static String webMethName;
    private static String amount;
    private static String reason;
    private static String userId;
    private static String date;
    private static String month;
    private static String selectedDate;
    private static String listId;
    private static String imFrom;
    public String[] title;
    private static ProgressDialog progressDialog;
    JSONArray listArray = null;
    int year;
    SessionManager sessionManager;
    String total;

    public Expense_Request_Data(Main_Activity expenseList) {
        context = expenseList;
    }

    public Expense_Request_Data(Expense_Add expense_add) {
        context = expense_add;
    }

    public Expense_Request_Data(Expense_Details expense_details) {
        context = expense_details;
    }

    public Expense_Request_Data(Expense_Edit expense_edit) {
        context = expense_edit;
    }

    public Expense_Request_Data(Context deleteExpense) {
        context = deleteExpense;
    }

    public void AddExpense(String imFromPage, String amt, String rsn, String selectedDateForSubmission, String monthName, int yr, ProgressDialog pd) {
        imFrom = imFromPage;
        amount = amt;
        reason = rsn;
        date = selectedDateForSubmission;
        month = monthName;
        year = yr;
        progressDialog = pd;

        // AddExpense();
        AddExpenseOffline();
    }

    public void EditExpense(String id, String amt, String rsn, String selectedDateForSubmission, String monthName, int yr, ProgressDialog pd) {
        listId = id;
        amount = amt;
        reason = rsn;
        date = selectedDateForSubmission;
        month = monthName;
        year = yr;
        progressDialog = pd;

        // AddExpense();
        EditExpenseOffline();
    }

    public void DeleteExpense(String id, ProgressDialog pd) {
        listId = id;
        progressDialog = pd;

        // AddExpense();
        DeleteExpenseOffline();
    }

    private void DeleteExpenseOffline() {
        boolean isInserted = false;
        mydb = new DataBaseHelper(context);
        isInserted = mydb.DeleteExpense(listId);

        if (isInserted == true) {
            progressDialog.dismiss();
            Toast.makeText(context, "Expense Deleted.", Toast.LENGTH_SHORT).show();
            Intent gotoMonthList = new Intent(context, Main_Activity.class);
            context.startActivity(gotoMonthList);
        } else {
            progressDialog.dismiss();
            progressDialog.dismiss();
            Toast.makeText(context, "Expense Not Deleted.Please Try Again Later", Toast.LENGTH_SHORT).show();
        }
    }

    private void AddExpenseOffline() {

        boolean isInserted = false;
        mydb = new DataBaseHelper(context);
        isInserted = mydb.AddExpense(amount, reason, date, month, year);

        if (isInserted == true) {
            progressDialog.dismiss();
            Toast.makeText(context, "Expense Added.", Toast.LENGTH_SHORT).show();

        } else {
            progressDialog.dismiss();
            Toast.makeText(context, "Expense Already Added.", Toast.LENGTH_SHORT).show();

        }

        if (imFrom.equals("ExpenseDetails")) {
            Intent gotoDetails = new Intent(context, Expense_Details.class);
            gotoDetails.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            gotoDetails.putExtra("month", month);
            context.startActivity(gotoDetails);
        } else {
            Intent gotoMonthList = new Intent(context, Main_Activity.class);
            gotoMonthList.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(gotoMonthList);
        }
    }

    private void EditExpenseOffline() {

        boolean isInserted = false;
        mydb = new DataBaseHelper(context);
        isInserted = mydb.EditExpense(listId, amount, reason, date, month, year);

        if (isInserted == true) {
            progressDialog.dismiss();
            Toast.makeText(context, "Expense Updated.", Toast.LENGTH_SHORT).show();
            Intent gotoMonthList = new Intent(context, Main_Activity.class);
            gotoMonthList.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(gotoMonthList);
        } else {
            progressDialog.dismiss();
            Toast.makeText(context, "Expense Not Updated.Please Try Again Later", Toast.LENGTH_SHORT).show();
        }


    }

    public void ExpenseDetailsList(List<Expense_Items> newsItems, RecyclerView recyclerView, RecyclerView.Adapter reviewAdapter, String monthName, ProgressDialog progressDialog) {
        progressDialogBox = progressDialog;
        adapterForAsyncTask = reviewAdapter;
        recyclerViewForAsyncTask = recyclerView;
        ItemsArrayForAsyncTask = newsItems;
        month = monthName;

        //ExpenseList();
        mydb = new DataBaseHelper(context);
        ExpenseListOffline();
    }
    public void DateWiseExpenseDetailsList(List<Expense_Items> newsItems, RecyclerView recyclerView, RecyclerView.Adapter reviewAdapter, String monthName, String date,ProgressDialog progressDialog) {
        progressDialogBox = progressDialog;
        adapterForAsyncTask = reviewAdapter;
        recyclerViewForAsyncTask = recyclerView;
        ItemsArrayForAsyncTask = newsItems;
        month = monthName;
        selectedDate = date;

        //ExpenseList();
        mydb = new DataBaseHelper(context);
        ExpenseListDateWiseOffline();
    }


    private void ExpenseListDateWiseOffline() {

        try {
            listArray = mydb.getDateWiseExpense(month,selectedDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        int dataCount = listArray.length();

        if (dataCount == 0) {
            progressDialogBox.dismiss();
            Toast.makeText(context, "Expense Not Available For This Date.", Toast.LENGTH_SHORT).show();
        } else {

            int calTotalMarks = 0;
            ItemsArrayForAsyncTask.clear();

            sessionManager = new SessionManager(context);

            for (int i = 0; i < listArray.length(); i++) {
                try {
                    JSONObject obj = listArray.getJSONObject(i);

                    calTotalMarks = calTotalMarks + Integer.parseInt(obj.getString("amount"));
                    total = String.valueOf(calTotalMarks);

                    Expense_Items expense_Items = new Expense_Items();
                    expense_Items.setListId(obj.getString("Id"));
                    expense_Items.setmonth(obj.getString("month"));
                    expense_Items.setamount(obj.getString("amount"));
                    expense_Items.setreason(obj.getString("reason"));
                    expense_Items.setdate(obj.getString("date"));

                    expense_Items.settotalAmount(total);

                    ItemsArrayForAsyncTask.add(expense_Items);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            sessionManager.SetTotalAmount(total);
            adapterForAsyncTask.notifyDataSetChanged();
            progressDialogBox.dismiss();
        }
    }

    private void ExpenseListOffline() {

        try {
            listArray = mydb.getMonthWiseExpense(month);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        int dataCount = listArray.length();

        if (dataCount == 0) {
            progressDialogBox.dismiss();
            Toast.makeText(context, "Expense Month Not Available", Toast.LENGTH_SHORT).show();
        } else {

            int calTotalMarks = 0;
            ItemsArrayForAsyncTask.clear();

            sessionManager = new SessionManager(context);

            for (int i = 0; i < listArray.length(); i++) {
                try {
                    JSONObject obj = listArray.getJSONObject(i);

                    calTotalMarks = calTotalMarks + Integer.parseInt(obj.getString("amount"));
                    total = String.valueOf(calTotalMarks);

                    Expense_Items expense_Items = new Expense_Items();
                    expense_Items.setListId(obj.getString("Id"));
                    expense_Items.setmonth(obj.getString("month"));
                    expense_Items.setamount(obj.getString("amount"));
                    expense_Items.setreason(obj.getString("reason"));
                    expense_Items.setdate(obj.getString("date"));

                    expense_Items.settotalAmount(total);

                    ItemsArrayForAsyncTask.add(expense_Items);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            sessionManager.SetTotalAmount(total);
            adapterForAsyncTask.notifyDataSetChanged();
            progressDialogBox.dismiss();
        }
    }

    private void ExpenseList() {
        webMethName = "Main_Activity";
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("userId", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = context.getString(R.string.url) + webMethName;

        AndroidNetworking.post(url)
                .addJSONObjectBody(jsonObject)
                .setTag(webMethName)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialogBox.dismiss();
                            ItemsArrayForAsyncTask.clear();
                            adapterForAsyncTask.notifyDataSetChanged();
                            String res = response.getString("responseDetails");
                            if (res.equals("Not found News..!!")) {
                                Toast.makeText(context, "Expense Details Not Available", Toast.LENGTH_SHORT).show();
                            } else {
                                try {
                                    JSONArray jArr = response.getJSONArray("responseDetails");
                                    for (int count = 0; count < jArr.length(); count++) {
                                        JSONObject obj = jArr.getJSONObject(count);
                                        Expense_Items expense_Items = new Expense_Items();
                                        expense_Items.setListId(obj.getString("Id"));
                                        expense_Items.setamount(obj.getString("amount"));
                                        expense_Items.setdate(obj.getString("date"));
                                        expense_Items.setmonth(obj.getString("month"));
                                        expense_Items.setreason(obj.getString("reason"));

//                                        mydb = new DataBaseHelper(context);
//                                        mydb.AddExpense((obj.getString("amount")), (obj.getString("date")), (obj.getString("reason")), (obj.getString("month")));

                                        ItemsArrayForAsyncTask.add(expense_Items);
                                    }
                                    adapterForAsyncTask.notifyDataSetChanged();
                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                            progressDialogBox.dismiss();
                            e.getMessage();
                            Toast.makeText(context, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        progressDialogBox.dismiss();
                        error.getErrorDetail();
                        Toast.makeText(context, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void AddExpense() {
        webMethName = "Expense_Add";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("User_Id", userId);
            jsonObject.put("Added_Date", date);
            jsonObject.put("amount", amount);
            jsonObject.put("reason", reason);
            jsonObject.put("year", year);
            jsonObject.put("month", month);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = context.getString(R.string.url) + webMethName;
        AndroidNetworking.post(url)
                .addJSONObjectBody(jsonObject)
                .setTag(webMethName)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialogBox.dismiss();
                            String res = response.getString("responseDetails");
                            if (res.equals("Successfully Expense Added.")) {

                                Intent gotoHomeList = new Intent(context, Main_Activity.class);
                                gotoHomeList.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(gotoHomeList);

                            } else {
                                Toast.makeText(context, " " + res, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            progressDialogBox.dismiss();
                            e.getMessage();
                            Toast.makeText(context, "Exception" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        progressDialogBox.dismiss();
                        error.getErrorDetail();
                        Toast.makeText(context, "Error" + error.getErrorDetail(), Toast.LENGTH_LONG).show();
                    }
                });
    }


    public void GetMonthList(List<Expense_Items> newsItems, RecyclerView recyclerView, RecyclerView.Adapter reviewAdapter, String[] monthName, ProgressDialog progressDialog) {
        progressDialogBox = progressDialog;
        adapterForAsyncTask = reviewAdapter;
        recyclerViewForAsyncTask = recyclerView;
        ItemsArrayForAsyncTask = newsItems;
        title = monthName;

        mydb = new DataBaseHelper(context);
        ExpenseMonthListOffline();
    }

    private void ExpenseMonthListOffline() {
        try {
            listArray = mydb.getMonthListExpense();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        int dataCount = listArray.length();

        if (dataCount == 0) {

            for (int i = 0; i < title.length; i++) {

                Expense_Items expense_Items = new Expense_Items();
                expense_Items.setmonth(title[i]);
                expense_Items.settotalAmount("");
                ItemsArrayForAsyncTask.add(expense_Items);
            }
            progressDialogBox.dismiss();
            adapterForAsyncTask.notifyDataSetChanged();

        } else {
            for (int i = 0; i < title.length; i++) {
                try {
                    Expense_Items expense_Items = new Expense_Items();
                    if (listArray.toString().contains(title[i])) {
                        expense_Items.setmonth(title[i]);
                        for (int j = 0; j < listArray.length(); j++) {
                            JSONObject obj = listArray.getJSONObject(j);
                            String month = obj.getString("month");
                            String amount = obj.getString("amount");
                            if (title[i].contains(month)) {
                                expense_Items.settotalAmount(amount);
                            }
                        }
                    } else {
                        expense_Items.setmonth(title[i]);
                        expense_Items.settotalAmount("");
                    }
                    ItemsArrayForAsyncTask.add(expense_Items);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            progressDialogBox.dismiss();
            adapterForAsyncTask.notifyDataSetChanged();

        }

//        if (dataCount == 0) {
//
//            for (int i = 0; i < title.length; i++) {
//
//                Expense_Items expense_Items = new Expense_Items();
//                expense_Items.settitle(title[i]);
//                expense_Items.settotalAmount("");
//                ItemsArrayForAsyncTask.add(expense_Items);
//            }
//            progressDialogBox.dismiss();
//            adapterForAsyncTask.notifyDataSetChanged();
//
//        } else {
//            for (int i = 0; i < title.length; i++) {
//                int size = listArray.length();
//
//                try {
//                    Expense_Items expense_Items = new Expense_Items();
//                    expense_Items.settitle(title[i]);
//
//                    if (i < size) {
//                        JSONObject obj = listArray.getJSONObject(i);
//                        String month = obj.getString("month");
//                        String amount = obj.getString("amount");
//
//                        if (month.equals("") || month == null) {
//                            expense_Items.settotalAmount("");
//                            expense_Items.setmonth("");
//
//                        } else {
//                            expense_Items.setmonth(obj.getString("month"));
//                            expense_Items.settotalAmount(obj.getString("amount"));
//                        }
//                    } else {
//                        expense_Items.settotalAmount("");
//                        expense_Items.setmonth("");
//                    }
//                    ItemsArrayForAsyncTask.add(expense_Items);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            progressDialogBox.dismiss();
//            adapterForAsyncTask.notifyDataSetChanged();
//
//        }


    }
}
