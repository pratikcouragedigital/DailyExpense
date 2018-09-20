package com.pratik.dailyexpense.model;

public class Expense_Items {


    public String reason;
    public String amount;
    public String date;
    public String month;
    public String ListId;
    public String totalAmount;


    public String title;
    public int gridId;
    public int firstImagePath;


    public Expense_Items() {
    }

    public Expense_Items(String ListId, String month, String date, String reason, String amount, String totalAmount,String title, int firstImagePath, int gridId) {

        this.month = month;
        this.ListId = ListId;
        this.reason = reason;
        this.amount = amount;
        this.date = date;
        this.totalAmount = totalAmount;

        this.gridId = gridId;
        this.title = title;
        this.firstImagePath = firstImagePath;
    }

    public String getmonth() {
        return month;
    }

    public void setmonth(String month) {
        this.month = month;
    }

    public String getListId() {
        return ListId;
    }

    public void setListId(String ListId) {
        this.ListId = ListId;
    }

    public String getdate() {
        return date;
    }

    public void setdate(String date) {
        this.date = date;
    }

    public String getreason() {
        return reason;
    }

    public void setreason(String reason) {
        this.reason = reason;
    }

    public String getamount() {
        return amount;
    }

    public void setamount(String amount) {
        this.amount = amount;
    }

    public String gettotalAmount() {
        return totalAmount;
    }

    public void settotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(Integer firstImagePath) {
        this.firstImagePath = firstImagePath;
    }


    public String gettitle() {
        return title;
    }

    public void settitle(String title) {
        this.title = title;
    }

    public int getgridId() {
        return gridId;
    }

    public void setgridId(int gridId) {
        this.gridId = gridId;
    }


}