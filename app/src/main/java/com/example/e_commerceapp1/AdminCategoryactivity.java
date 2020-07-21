package com.example.e_commerceapp1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminCategoryactivity extends AppCompatActivity {

    private Button Addnewproduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_admin_categoryactivity );

        Addnewproduct = findViewById( R.id.slogancategory);

        Addnewproduct.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( getApplicationContext(),AdminAddnewproductActivity.class );
                startActivity(intent);
            }
        } );

    }
}
