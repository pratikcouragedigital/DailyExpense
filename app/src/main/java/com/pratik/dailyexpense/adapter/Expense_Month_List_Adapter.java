package com.pratik.dailyexpense.adapter;


import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pratik.dailyexpense.Expense_Add;
import com.pratik.dailyexpense.Expense_Details;
import com.pratik.dailyexpense.R;
import com.pratik.dailyexpense.model.Expense_Items;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import me.toptas.fancyshowcase.FancyShowCaseQueue;
import me.toptas.fancyshowcase.FancyShowCaseView;

public class Expense_Month_List_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Expense_Items> listItems;
    View v;
    RecyclerView.ViewHolder viewHolder;

    private static final int VIEW_TYPE_EMPTY = 1;
    private Calendar calendar;
    private int year, month, day;
    String currMonth,currMonthName;


    public Expense_Month_List_Adapter(List<Expense_Items> items) {
        this.listItems = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.expense_month_list_items, viewGroup, false);
        viewHolder = new Expense_Month_List_Adapter.ViewHolder(v);

//        calendar = Calendar.getInstance();
//        year = calendar.get(Calendar.YEAR);
//        month = calendar.get(Calendar.MONTH);
//        day = calendar.get(Calendar.DAY_OF_MONTH);
//        currMonth = String.valueOf(month + 1);
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM");
//        currMonthName = simpleDateFormat.format(calendar.getTime());

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof Expense_Month_List_Adapter.ViewHolder) {
            Expense_Month_List_Adapter.ViewHolder vHolder = (Expense_Month_List_Adapter.ViewHolder) viewHolder;
            Expense_Items itemOflist = listItems.get(position);
            vHolder.bindListDetails(itemOflist);
        }
    }

    @Override
    public int getItemCount() {

        return listItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtmonth, txtTotalAmount;
        public View cardView;
        Expense_Items listItems = new Expense_Items();
        private FancyShowCaseQueue fancyShowCaseQueue;

        public ViewHolder(View itemView) {
            super(itemView);

            txtmonth = (TextView) itemView.findViewById(R.id.txtmonth);
            txtTotalAmount = (TextView) itemView.findViewById(R.id.txtTotalAmount);

            cardView = itemView;
            cardView.setOnClickListener(this);

//            final FancyShowCaseView fancyShowCaseView2 = new FancyShowCaseView.Builder(this)
//                    .title("Click on month to check expense details")
//                    .focusOn(cardView)
//                    .build();
//            fancyShowCaseQueue = new FancyShowCaseQueue()
//                    .add(fancyShowCaseView2);
//
//            fancyShowCaseQueue.show();
        }

        public void bindListDetails(Expense_Items listItems) {
            this.listItems = listItems;

            txtmonth.setText(listItems.getmonth());
            String monthlyExpAmt = listItems.gettotalAmount();
            if(monthlyExpAmt.equals("") || monthlyExpAmt.equals("0") || monthlyExpAmt == null){
                txtTotalAmount.setText("Expense not added yet.");

            }else{
                txtTotalAmount.setText("Total Expense "+monthlyExpAmt+" Rs.");
                txtTotalAmount.setTextColor(v.getResources().getColor(R.color.blue500));
            }

        }

        @Override
        public void onClick(View v) {


            if (this.listItems != null) {
                String expMonthName =  this.listItems.getmonth();
                String expMonthAmount =  this.listItems.gettotalAmount();

                String imFrom = "MainActivity";
                if(expMonthAmount.equals("") || expMonthAmount.equals("0")|| expMonthAmount == null){
                    Toast.makeText(v.getContext(), "Expense Not Added To This Month", Toast.LENGTH_SHORT).show();
                    Intent gotoForm = new Intent(v.getContext(), Expense_Add.class);
                    gotoForm.putExtra("month", listItems.getmonth());
                    gotoForm.putExtra("imFrom", imFrom);
                    v.getContext().startActivity(gotoForm);

                }else{
                    Intent gotoDetails = new Intent(v.getContext(), Expense_Details.class);
                    gotoDetails.putExtra("month", listItems.getmonth());
//                    gotoDetails.putExtra("expMonthAmount", listItems.gettotalAmount());
                    v.getContext().startActivity(gotoDetails);
                }
            }

        }
    }

}
