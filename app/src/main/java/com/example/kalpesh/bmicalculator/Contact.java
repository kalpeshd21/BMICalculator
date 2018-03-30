package com.example.kalpesh.bmicalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Contact extends AppCompatActivity {
    TextView tvContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        tvContact=(TextView)findViewById(R.id.tvContact);
        tvContact.setText("Kalpesh:9867741694 \n Shubham:7045530569");
    }
}
