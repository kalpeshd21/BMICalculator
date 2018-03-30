package com.example.kalpesh.bmicalculator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class BmiResult extends AppCompatActivity {


    TextView tvResult,tvUW,tvNW,tvOW,tvObW;
    Button btnSave,btnShare,btnBack;
    DBHandler dbH;
    SharedPreferences sp3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi_result);

        tvResult=(TextView)findViewById(R.id.tvResult);
        tvUW=(TextView)findViewById(R.id.tvUW);
        tvNW=(TextView)findViewById(R.id.tvNW);
        tvOW=(TextView)findViewById(R.id.tvOW);
        tvObW=(TextView)findViewById(R.id.tvObW);
        btnBack=(Button)findViewById(R.id.btnBack);
        btnShare=(Button)findViewById(R.id.btnShare);
        btnSave=(Button)findViewById(R.id.btnSave);
        dbH=new DBHandler(this);
        sp3=getSharedPreferences("MyP1",MODE_PRIVATE);
        Intent i=getIntent();
        String weight=i.getStringExtra("weight");
        String hm =i.getStringExtra("hm");
        Double weight1=Double.parseDouble(weight);
        Double hm1=Double.parseDouble(hm);
        Double BMI= weight1/(hm1*hm1);
        final String BMI1=String.format("%.2f",BMI);
        String str = null;



        Date d=new Date();
        final String date= String.valueOf(d);


        String Underweight="Below 18.5 is Underweight";
        String Normal="Between 18.5 to 25 is Normal";
        String Overweight="Between 25 to 30 is Overweight ";
        String Obese= "More Than 30 is Obese";


        tvUW.setText(Underweight);
        tvNW.setText(Normal);
        tvOW.setText(Overweight);
        tvObW.setText(Obese);


        if(BMI<18.5)
        {
            tvResult.setText("Your BMI is "+BMI1+ " and You Are Underweight");
            tvUW.setTextColor(Color.RED);
            str= "You Are Underweight";
        }
        if(BMI>18.5&&BMI<25)
        {
            tvResult.setText("Your BMI is "+BMI1+ " and You Are Normal");
            tvNW.setTextColor(Color.RED);
            str= "You Are Normal";
        }
        if(BMI>25&&BMI<30)
        {
            tvResult.setText("Your BMI is "+BMI1+ " and You Are Overweight");
            tvOW.setTextColor(Color.RED);
            str= "You Are Overweight";
        }
        if(BMI>30)
        {
            tvResult.setText("Your BMI is "+BMI1+ " and You Are Obese");
            tvObW.setTextColor(Color.RED);
            str= "You Are Obese";
        }


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent j=new Intent(BmiResult.this,Calculator.class);
                startActivity(j);
                finish();
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbH.addBMI(date,BMI1);
            }
        });


        final String name = sp3.getString("name","");
        final String phone = sp3.getString("phone","");
        final String age = sp3.getString("age","");
        final String gender = sp3.getString("gender","");


        final String finalStr = str;
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent (Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT,"Name: "+name + "\n Age: "+age+"\n Gender: "+gender+" \n Phone: "+phone+ "\n BMI: "+BMI1+ "\n "+ finalStr);
                startActivity(i);

            }
        });

    }


}
