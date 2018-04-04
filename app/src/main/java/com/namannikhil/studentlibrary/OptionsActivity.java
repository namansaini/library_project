package com.namannikhil.studentlibrary;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OptionsActivity extends AppCompatActivity {
private int sId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        Intent intent=getIntent();
        sId=intent.getIntExtra("sId",1);
        Button availableBooks=(Button)findViewById(R.id.available_books);
        Button issuedBooks=(Button)findViewById(R.id.issued_books);
        Button payFine=(Button)findViewById(R.id.pay_fine);
        availableBooks.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
               newScreen1();
            }
        });
        issuedBooks.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                newScreen2();
            }
        });
        payFine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newScreen3();
            }
        });
    }
    void newScreen1()
    {
        Intent intent =new Intent(this,AvailBooks.class);
        intent.putExtra("sId",sId);
        startActivity(intent);
    }
    void newScreen2()
    {
        Intent intent=new Intent(this,IssuedBooks.class);
        intent.putExtra("sId",sId);
        startActivity(intent);
    }
    void newScreen3()
    {
        Intent intent =new Intent(this,Fine.class);
        intent.putExtra("sId",sId);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to exit?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }

                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(dialogInterface!=null)
                            dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog=builder.create();
                alertDialog.show();
    }
}
