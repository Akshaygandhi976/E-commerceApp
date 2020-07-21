package com.example.e_commerceapp1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class AdminaddnewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_adminaddnew );

        Toast.makeText( this, "Welcome Admin", Toast.LENGTH_SHORT ).show();
    }
}
