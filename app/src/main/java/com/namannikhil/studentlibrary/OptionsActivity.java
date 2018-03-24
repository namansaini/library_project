package com.namannikhil.studentlibrary;

import android.content.Intent;
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
    }
    void newScreen1()
    {
        Intent intent =new Intent(this,AvailBooks.class);
        intent.putExtra("sId",sId);
        intent.putExtra("ButtonNo",2);
        startActivity(intent);
    }
    void newScreen2()
    {
        Intent intent=new Intent(this,AvailBooks.class);
        intent.putExtra("sId",sId);
        intent.putExtra("ButtonNo",1);
        startActivity(intent);
    }
}
