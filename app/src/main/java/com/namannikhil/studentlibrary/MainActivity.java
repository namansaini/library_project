package com.namannikhil.studentlibrary;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText mUsername;
    private EditText mPassword;
    private SQLiteDatabase mDb;
    DbHelper helper=new DbHelper(this);

    private int run=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(true) {
            mDb = helper.getWritableDatabase();
           // BookUtility.insertFakeData(mDb, getApplicationContext());
            Utility.insertFakeData(mDb);
            run=0;
        }
        mUsername=(EditText) findViewById(R.id.username_edit_box);
        mPassword=(EditText) findViewById(R.id.password_edit_box);
        Button button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                addToStudent(v);
            }
        });

    }
    public void addToStudent(View view)
    {
        if(mUsername.getText().toString().length()==0 || mPassword.getText().toString().length()==0)
        {
            Toast.makeText(this, "Username or Password Field Empty", Toast.LENGTH_LONG).show();
            return;
        }
        Cursor cursor;

             cursor= search();
             if(!cursor.moveToPosition(0)) {
                 Toast.makeText(this, "No user with this username exists!", Toast.LENGTH_LONG).show();
                 return;
             }
             else {
                 String password = cursor.getString(cursor.getColumnIndex(StudentContract.StudentEntry.COLUMN_PASSWORD));
                 String Fname=cursor.getString(cursor.getColumnIndex(StudentContract.StudentEntry.COLUMN_FNAME));
                 String Lname=cursor.getString(cursor.getColumnIndex(StudentContract.StudentEntry.COLUMN_LNAME));
                 if (mPassword.getText().toString().equals(password)) {
                     Toast.makeText(this, "Welcome " + Fname +" "+ Lname, Toast.LENGTH_LONG).show();
                     Intent intent =new Intent(this,OptionsActivity.class);
                     startActivity(intent);
                 } else {
                     Toast.makeText(this, "Password Incorrect", Toast.LENGTH_LONG).show();
                 }
             }



    }
    private Cursor search()
    {
        String[] projection={StudentContract.StudentEntry.COLUMN_USERNAME, StudentContract.StudentEntry.COLUMN_PASSWORD, StudentContract.StudentEntry.COLUMN_FNAME, StudentContract.StudentEntry.COLUMN_LNAME};
        String selection="username=?";
        String[] selectionArgs={mUsername.getText().toString()};
        mDb=helper.getReadableDatabase();
        return mDb.query(StudentContract.StudentEntry.TABLE_NAME,projection ,selection,selectionArgs,null,null,null);
    }

}
