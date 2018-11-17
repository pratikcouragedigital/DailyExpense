package com.pratik.dailyexpense;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.pratik.dailyexpense.sessionManager.SessionManager;

public class Change_Year extends AppCompatActivity {

    RadioButton rd2018, rd2019;
    RadioGroup rdgYear;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_year);

        sessionManager = new SessionManager(this);
        rdgYear = (RadioGroup) findViewById(R.id.rdgYear);

        rdgYear.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
                boolean isChecked = checkedRadioButton.isChecked();
                String rdSelectedYear = String.valueOf(checkedRadioButton.getText());

                sessionManager.SetSelectdYear(rdSelectedYear);

                Intent gotohome = new Intent(Change_Year.this,Main_Activity.class);
                startActivity(gotohome);

            }
        });

    }


}
