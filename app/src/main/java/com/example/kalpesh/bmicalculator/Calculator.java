package com.example.kalpesh.bmicalculator;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.Task;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

public class Calculator extends AppCompatActivity
implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener

{
    TextView tvLocTemp,tvName,tvHeight,tvFeet,tvInch,tvWeight;
    Spinner spnFeet,spnInch;
    EditText etWeight;
    Button btnCalculate,btnViewHistory;
    GoogleApiClient gac;
    Location loc;
    String add;
    SharedPreferences sp2;
    DBHandler dbH;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        tvLocTemp=(TextView)findViewById(R.id.tvLocTemp);
        tvName=(TextView)findViewById(R.id.tvName);
        tvHeight=(TextView)findViewById(R.id.tvHeight);
        tvFeet=(TextView)findViewById(R.id.tvFeet);
        tvInch=(TextView)findViewById(R.id.tvInch);
        tvWeight=(TextView)findViewById(R.id.tvWeight);
        spnFeet=(Spinner)findViewById(R.id.spnFeet);
        spnInch=(Spinner)findViewById(R.id.spnInch);
        etWeight=(EditText)findViewById(R.id.etWeight);
        btnCalculate=(Button)findViewById(R.id.btnCalculate);
        btnViewHistory=(Button)findViewById(R.id.btnViewHistory);
        sp2=getSharedPreferences("MyP2",MODE_PRIVATE);
        dbH=new DBHandler(this);
        GoogleApiClient.Builder builder=new GoogleApiClient.Builder(this);
        builder.addApi(LocationServices.API);
        builder.addConnectionCallbacks(this);
        builder.addOnConnectionFailedListener(this);
        gac=builder.build();

        Intent i= getIntent();
        String name = i.getStringExtra("name");
        tvName.setText("Welcome " +name );

        final Task1 t = new Task1();
        t.execute("http://api.openweathermap.org/data/2.5/weather?units=metric" + "&q="+sp2.getString("add","")+ "&appid=" + "c6e315d09197cec231495138183954bd");


        final Integer feet2[]=new Integer[9];
        int feet1=0;
        for(int k=0;k<=8;k++)
        {
            feet2[k] = feet1;
            feet1++;
        }
        ArrayAdapter <Integer> dataAdapter = new ArrayAdapter<Integer>( this,android.R.layout.simple_spinner_item,feet2 );
        spnFeet.setAdapter(dataAdapter);


        final Integer Inch2[]=new Integer[12];
        int Inch1=0;
        for(int k=0;k<=11;k++)
        {
            Inch2[k] = Inch1;
            Inch1++;
        }
        ArrayAdapter <Integer> dataAdapter2 = new ArrayAdapter<Integer>( this,android.R.layout.simple_spinner_item,Inch2 );
        spnInch.setAdapter(dataAdapter2);


        btnCalculate.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 String weight=etWeight.getText().toString();
                 if(weight.length()==0)
                 {
                     etWeight.setError("Please enter weight");
                     etWeight.setText("");
                     etWeight.requestFocus();
                     return;
                 }

                 int k=spnFeet.getSelectedItemPosition();
                 int s=feet2[k];

                 int v=spnInch.getSelectedItemPosition();
                 int r=Inch2[v];

                 Double p=(s*0.305)+(r*0.0254);
                 String hm=Double.toString(p);
                 Intent i=new Intent(Calculator.this,BmiResult.class);
                 i.putExtra("weight",weight);
                 i.putExtra("hm",hm);
                 startActivity(i);
             }
         });

        btnViewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent k=new Intent(Calculator.this,ViewHistory.class);
                startActivity(k);
            }
        });


    }


    class Task1 extends AsyncTask<String, Void,Double>
    {
        double temprature;
        @Override
        protected Double doInBackground(String... strings) {
            String json="",line="";

            try {
                URL url = new URL(strings[0]);
                HttpURLConnection con=  (HttpURLConnection)url.openConnection();
                con.connect();

                InputStreamReader isr = new InputStreamReader(con.getInputStream());
                BufferedReader br =new BufferedReader(isr);

                while((line=br.readLine())!=null)
                {
                    json = json + line + "\n";

                }

                if(json !=null)
                {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONObject info = jsonObject.getJSONObject("main");
                    temprature = info.getDouble("temp");
                }
            }
            catch (Exception e) { }

            return temprature;
        }

        @Override
        protected void onPostExecute(Double aDouble) {
            super.onPostExecute(aDouble);
            tvLocTemp.setText(sp2.getString("add","")+ " " +aDouble);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(gac !=null)
            gac.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(gac!= null)
        {
            gac.disconnect();

        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        loc=LocationServices.FusedLocationApi.getLastLocation(gac);
        if(loc!=null)
        {
            double lat =loc.getLatitude();
            double lon= loc.getLongitude();
            Geocoder g= new Geocoder(Calculator.this,Locale.ENGLISH);
            try
            {
                List<Address> addresseList = g.getFromLocation(lat,lon,1);
                Address address=addresseList.get(0);
                add=address.getSubAdminArea();
                tvLocTemp.setText(add);
                SharedPreferences.Editor editor = sp2.edit();
                editor.putString("add",add);
                editor.commit();



             }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(this, "Please Enable Gps/Come In Open Area", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connectinon Suspended", Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Failed", Toast.LENGTH_SHORT).show();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.m1, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.web){

            Intent i =new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://www.google.com"));
            startActivity(i);


        }
        if(item.getItemId() == R.id.contact)
        {
            Intent i=new Intent(getApplicationContext(),Contact.class);
            startActivity(i);
        }
        if(item.getItemId() == R.id.about)
        {
            Intent i=new Intent(getApplicationContext(),About.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
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

