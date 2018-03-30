package com.example.kalpesh.bmicalculator;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView tvTitle;
    EditText etName,etPhone,etAge;
    RadioGroup rgGender;
    RadioButton rbMale,rbFemale;
    Button btnRegister;
    SharedPreferences sp1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvTitle= (TextView) findViewById(R.id.tvTitle);
        etName= (EditText) findViewById(R.id.etName);
        etPhone=(EditText)findViewById(R.id.etPhone);
        etAge=(EditText)findViewById(R.id.etAge);
        rgGender=(RadioGroup)findViewById(R.id.rgGender);
        rbMale=(RadioButton)findViewById(R.id.rbMale);
        rbFemale=(RadioButton)findViewById(R.id.rbFemale);
        btnRegister=(Button)findViewById(R.id.btnRegister);
        sp1=getSharedPreferences("MyP1",MODE_PRIVATE);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name,phone,age;
                name=etName.getText().toString();
                phone=etPhone.getText().toString();
                age=etAge.getText().toString();

                if(name.length()==0)
                {
                    etName.setError("Please Enter Name");
                    etName.setText("");
                    etName.requestFocus();
                    return;
                }
                if(phone.length()==0)
                {
                    etPhone.setError("Please Enter Phone Number");
                    etPhone.setText("");
                    etPhone.requestFocus();
                    return;
                }
                if(age.length()==0)
                {
                    etAge.setError("Please Enter Age");
                    etAge.setText("");
                    etAge.requestFocus();
                    return;
                }

                int s1=rgGender.getCheckedRadioButtonId();
                RadioButton rbGender=(RadioButton)findViewById(s1);
                String gender = rbGender.getText().toString();

                SharedPreferences.Editor editor=sp1.edit();
                editor.putString("name",name);
                editor.putString("phone",phone);
                editor.putString("age",age);
                editor.putString("gender",gender);
                editor.commit();
                Toast.makeText(MainActivity.this, "User Created", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(MainActivity.this,Calculator.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        String name = sp1.getString("name","");
        String phone = sp1.getString("phone","");
        String age = sp1.getString("age","");
        String gender = sp1.getString("gender","");

        if(name != "" && phone !="" && age != "" && gender != "" )
        {
            Intent i = new Intent(MainActivity.this,Calculator.class);
            i.putExtra("name",name);
            i.putExtra("phone",phone);
            i.putExtra("age",age);
            i.putExtra("gender",gender);
            startActivity(i);
            finish();
        }
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to close this application?");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                finish();

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });


        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.setTitle("Exit");
        alert.show();

    }
}
