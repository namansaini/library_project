package com.namannikhil.studentlibrary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        Button availableBooks=(Button)findViewById(R.id.available_books);
        Button issuedBooks=(Button)findViewById(R.id.issued_books);
        Button payFine=(Button)findViewById(R.id.pay_fine);
        availableBooks.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
               newScreen();
            }
        });
    }
    void newScreen()
    {
        Intent intent =new Intent(this,AvailBooks.class);
        startActivity(intent);
    }
}
