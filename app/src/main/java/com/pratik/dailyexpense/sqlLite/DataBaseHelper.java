package com.pratik.dailyexpense.sqlLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class DataBaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "DailyExpense";


    //NEWS Details
    public static final String TABLE_EXPENSE = "ExpenseDetails";
    public static final String EXPENSE_PK_ID = "Id"; //primary key
    public static final String AMOUNT = "amount";
    public static final String REASON = "reason";
    public static final String DATE = "date";
    public static final String MONTH = "month";
    public static final String YEAR = "year";


    private HashMap hp;

   //String CREATE_TABLE_EXPENSE ="CREATE TABLE " + TABLE_EXPENSE + "("+ EXPENSE_PK_ID + " INTEGER PRIMARY KEY,"+ AMOUNT +" TEXT ,"+ REASON +" TEXT,"+ DATE +"TEXT,"+MONTH+"TEXT"+ ")";
   String CREATE_TABLE_EXPENSE ="create table ExpenseDetails(Id INTEGER PRIMARY KEY AUTOINCREMENT,amount varchar(50),reason varchar(500),date varchar(50),month varchar(50),year varchar(50))";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
//        super(context, Environment.getExternalStorageDirectory()+"/StudentCares/Database".toString()+DATABASE_NAME, null, DATABASE_VERSION);
//        SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory()+"/StudentCares/Database".toString()+DATABASE_NAME,null);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_EXPENSE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSE);
        onCreate(db);
    }

    public JSONArray getMonthListExpense() throws JSONException{
        //String selectQuery = "SELECT  * FROM " + TABLE_EXPENSE;
        String selectQuery = "SELECT sum(amount) as amount,month from "+TABLE_EXPENSE+" GROUP BY month";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery(selectQuery, null);
        res.moveToFirst();
        JSONObject Root = new JSONObject();
        JSONArray detailsArray = new JSONArray();
        int i = 0;
        while(res.isAfterLast() == false){
            JSONObject details = new JSONObject();
            details.put(MONTH, res.getString(res.getColumnIndex(MONTH)));
            details.put(AMOUNT, res.getString(res.getColumnIndex(AMOUNT)));

            res.moveToNext();
            detailsArray.put(i, details);
            i++;
        }
        res.close();
        return detailsArray;
    }

    public JSONArray getMonthWiseExpense(String month) throws JSONException {

        String selectQuery = "SELECT * FROM "+TABLE_EXPENSE+" WHERE month = '"+month+"' ORDER BY "+EXPENSE_PK_ID+" DESC ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery(selectQuery, null);
        res.moveToFirst();
        JSONObject Root = new JSONObject();
        JSONArray detailsArray = new JSONArray();
        int i = 0;
        while(res.isAfterLast() == false){
            JSONObject details = new JSONObject();
            details.put(MONTH, res.getString(res.getColumnIndex(MONTH)));
            details.put(DATE, res.getString(res.getColumnIndex(DATE)));
            details.put(REASON, res.getString(res.getColumnIndex(REASON)));
            details.put(AMOUNT, res.getString(res.getColumnIndex(AMOUNT)));
            details.put(EXPENSE_PK_ID, res.getString(res.getColumnIndex(EXPENSE_PK_ID)));

            res.moveToNext();
            detailsArray.put(i, details);
            i++;
        }
        res.close();
        return detailsArray;
    }
    public JSONArray getDateWiseExpense(String month,String selectedDate) throws JSONException {

        String selectQuery = "SELECT * FROM "+TABLE_EXPENSE+" WHERE month = '"+month+"' AND "+DATE+" = '"+selectedDate+"' ORDER BY "+EXPENSE_PK_ID+" DESC ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery(selectQuery, null);
        res.moveToFirst();
        JSONObject Root = new JSONObject();
        JSONArray detailsArray = new JSONArray();
        int i = 0;
        while(res.isAfterLast() == false){
            JSONObject details = new JSONObject();
            details.put(MONTH, res.getString(res.getColumnIndex(MONTH)));
            details.put(DATE, res.getString(res.getColumnIndex(DATE)));
            details.put(REASON, res.getString(res.getColumnIndex(REASON)));
            details.put(AMOUNT, res.getString(res.getColumnIndex(AMOUNT)));
            details.put(EXPENSE_PK_ID, res.getString(res.getColumnIndex(EXPENSE_PK_ID)));

            res.moveToNext();
            detailsArray.put(i, details);
            i++;
        }
        res.close();
        return detailsArray;
    }

    public boolean AddExpense (String amount , String reason, String date, String month, int year) {


//        //String selectQuery = "SELECT * FROM "+ TABLE_EXPENSE + " WHERE "+MONTH+" = "+month+" and "+REASON+" = "+reason+"  ";
//        String selectQuery = "SELECT * FROM ExpenseDetails where month = "+month+" And  Reason = "+reason+"";
//       // String selectQuery2 = "SELECT * FROM "+ TABLE_EXPENSE +" WHERE "+ MONTH +" = "+ month +" ";
//        SQLiteDatabase db2 = this.getReadableDatabase();
//        Cursor cursor = db2.rawQuery(selectQuery, null);
//        int count =  cursor.getCount();
//        cursor.close();
//
//        if(count == 0){


            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(AMOUNT, amount);
            contentValues.put(REASON, reason);
            contentValues.put(DATE, date);
            contentValues.put(MONTH, month);
            contentValues.put(YEAR, year);
            db.insert(TABLE_EXPENSE, null, contentValues);
            db.close();
            return true;
//        }
//        else{
//            return false;
//        }

    }
    public boolean EditExpense (String id,String amount, String reason, String date, String month, int year) {

        String selectQuery = "SELECT * FROM "+ TABLE_EXPENSE +" WHERE "+ EXPENSE_PK_ID +" = "+id+" ";
        SQLiteDatabase db2 = this.getReadableDatabase();
        Cursor cursor = db2.rawQuery(selectQuery, null);
        int count =  cursor.getCount();
        cursor.close();

        if(count == 1){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(AMOUNT, amount);
            contentValues.put(REASON, reason);
            contentValues.put(DATE, date);
            contentValues.put(MONTH, month);
            contentValues.put(YEAR, year);
            db.update(TABLE_EXPENSE, contentValues, EXPENSE_PK_ID + " = ?",new String[]{id});
            db.close();
            return true;
        }
        else{
            return false;
        }

    }
    public boolean DeleteExpense (String id) {

        String selectQuery = "SELECT * FROM "+ TABLE_EXPENSE +" WHERE "+ EXPENSE_PK_ID +" = "+id+" ";
        SQLiteDatabase db2 = this.getReadableDatabase();
        Cursor cursor = db2.rawQuery(selectQuery, null);
        int count =  cursor.getCount();
        cursor.close();

        if(count == 1){
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_EXPENSE, EXPENSE_PK_ID + " = ?",new String[]{id});
            db.close();
            return true;
        }
        else{
            return false;
        }

    }


}
