package com.example.kalpesh.bmicalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ViewHistory extends AppCompatActivity {
    TextView tvHistory;
    DBHandler dbH;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history);

        tvHistory=(TextView)findViewById(R.id.tvHistory);
        dbH=new DBHandler(this);

        tvHistory.setText(dbH.viewBMI());
    }
}
