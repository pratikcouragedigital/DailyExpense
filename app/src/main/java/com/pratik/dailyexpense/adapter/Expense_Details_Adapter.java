package com.pratik.dailyexpense.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.pratik.dailyexpense.Expense_Details;
import com.pratik.dailyexpense.R;
import com.pratik.dailyexpense.connectivity.Expense_Request_Data;
import com.pratik.dailyexpense.model.Expense_Items;
import com.pratik.dailyexpense.sessionManager.SessionManager;

import java.util.HashMap;
import java.util.List;

public class Expense_Details_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<Expense_Items> listItems;
    View v;
    RecyclerView.ViewHolder viewHolder;
    TextView txtTotalAmount;
    int calTotalMarks = 0;
    SessionManager sessionManager;
    RecyclerView recyclerView;


    public Expense_Details_Adapter(Expense_Details expense_details, List<Expense_Items> items, TextView txtTotalAmount, RecyclerView rv) {
        this.listItems = items;
        this.txtTotalAmount = txtTotalAmount;
        this.recyclerView = rv;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.expense_details_items, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ViewHolder) {
            ViewHolder vHolder = (ViewHolder) viewHolder;
            Expense_Items itemOflist = listItems.get(position);
            vHolder.bindListDetails(itemOflist );
        }

        sessionManager = new SessionManager(v.getContext());
        HashMap<String, String> typeOfUser = sessionManager.GetTotalAmount();
        String total = typeOfUser.get(SessionManager.KEY_TOTAL_AMOUNT);
        txtTotalAmount.setText(total+" Rs.");
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView date;
        public TextView amount;
        public TextView reason;

        public View cardView;
        Expense_Request_Data expenseReq;

        Expense_Items listItems = new Expense_Items();

        public ViewHolder(View itemView) {
            super(itemView);

            expenseReq = new Expense_Request_Data(v.getContext());

            date = (TextView) itemView.findViewById(R.id.txtDate);
            amount = (TextView) itemView.findViewById(R.id.txtAmount);
            reason = (TextView) itemView.findViewById(R.id.txtReason);

            cardView = itemView;
        }

        public void bindListDetails(final Expense_Items listItems) {
            this.listItems = listItems;


            date.setText(listItems.getdate());
            reason.setText(listItems.getreason());
            amount.setText(listItems.getamount()+" Rs.");

            //txtTotalAmount.setText(listItems.gettotalAmount()+" Rs.");
        }

        @Override
        public void onClick(final View v) {

        }
    }
}