package com.example.e_commerceapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Models.User;
import io.paperdb.Paper;
import prevalent.Prevalent;

public class MainActivity extends AppCompatActivity {
private Button joinnowb, loginhere;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        joinnowb = findViewById( R.id.joinnowbutton);
        loginhere =findViewById( R.id.loginbutton);
        loadingbar = new ProgressDialog( this );
        Paper.init(this);

        loginhere.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class );
                startActivity(intent);
            }
        } );

        joinnowb.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class );
                startActivity(intent);
            }
        } );

        String Userphonekey=Paper.book().read( Prevalent.Userphonekey);
        String Userpasswordkey=Paper.book().read( Prevalent.Userpasswordkey);

        if(Userphonekey !="" && Userpasswordkey!="")
        {
            if(!TextUtils.isEmpty( Userphonekey ) && !TextUtils.isEmpty( Userpasswordkey))
            {
                Allowaccess(Userphonekey,Userpasswordkey);


                loadingbar.setTitle( "Already Login" );
                loadingbar.setMessage( "Please wait ,while we are checking the credentials");
                loadingbar.setCanceledOnTouchOutside(false);
                loadingbar.show();
            }
        }
    }

    private void Allowaccess(final String phone,final String password) {
        final DatabaseReference Rootref;

        Rootref = FirebaseDatabase.getInstance().getReference();

        Rootref.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("User").child(phone).exists()){
                    User userdata= dataSnapshot.child("User").child( phone).getValue( User.class);

                    if(userdata.getPhone().equals(phone))
                    {
                        if(userdata.getPassword().equals(password)){
                            Toast.makeText( MainActivity.this, "Login Succsessful", Toast.LENGTH_SHORT ).show();
                            loadingbar.dismiss();

                            Intent intent = new Intent( MainActivity.this, HomeActivty.class);
                            Prevalent.OnlineUser=userdata;
                            startActivity(intent);
                        }
                    }
                }
                else
                {
                    Toast.makeText( MainActivity.this, "Account do not Exists", Toast.LENGTH_SHORT ).show();
                    loadingbar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }
}
