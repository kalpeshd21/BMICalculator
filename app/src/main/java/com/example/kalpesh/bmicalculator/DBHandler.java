package com.example.kalpesh.bmicalculator;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


public class DBHandler extends SQLiteOpenHelper {


    Context context;
    SQLiteDatabase db;
    public DBHandler(Context context) {
        super(context, "BMIDB", null, 1);
        this.context=context;
        db=this.getWritableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      String sql="create table BMI(date text,bmi text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    public void addBMI(String date,String bmi)
    {
        ContentValues cv= new ContentValues();
        cv.put("date",date);
        cv.put("bmi",bmi);
        long rid=db.insert("BMI",null,cv);
        if(rid<0)
        {
            Toast.makeText(context, "Insert issue", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(context, "1 Record Inserted", Toast.LENGTH_SHORT).show();
        }

    }
    public String viewBMI()
    {
        StringBuffer sb=new StringBuffer();
        Cursor cursor=db.query("BMI",null,null,null,null,null,null);
        cursor.moveToFirst();
        if (cursor.getCount()>0)
        {
            do {
                String date=cursor.getString(0);
                String bmi=cursor.getString(1);
                sb.append("date: "+date+" BMI:"+bmi +"\n" );
            }while (cursor.moveToNext());

        }
        return sb.toString();
    }
}
