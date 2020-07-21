package com.example.e_commerceapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Models.User;
import io.paperdb.Paper;
import prevalent.Prevalent;

public class LoginActivity extends AppCompatActivity {

private EditText mphonenumber, mpassword;
    private Button loginbtn;
    private ProgressDialog loadingbar;
    private CheckBox checkcbox;
    private TextView adminlink,notadminlink;
    private String ParentdbName="User";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );

        mphonenumber = findViewById( R.id.phonenumberlogin);
        mpassword =findViewById( R.id.passwordlogin);
        loginbtn =findViewById( R.id.loginactivityloginbtn);
        adminlink = findViewById( R.id.adminlogin);
        notadminlink = findViewById( R.id.notanadminlogin);
        loadingbar = new ProgressDialog( this );
        checkcbox = findViewById( R.id.checkboxrememberme);

        Paper.init(this);
        loginbtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUser();
            }
        } );
        adminlink.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginbtn.setText( "LOGIN ADMIN");
                adminlink.setVisibility( View.INVISIBLE);
                notadminlink.setVisibility( View.VISIBLE );

                ParentdbName ="Admin";
            }
        } );
        notadminlink.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginbtn.setText( "LOGIN" );

                adminlink.setVisibility( View.VISIBLE);
                notadminlink.setVisibility( View.INVISIBLE );

                ParentdbName ="User";
            }
        } );
    }


    private void LoginUser() {
         String  phonenumber =  mphonenumber.getText().toString().trim();
        String password =mpassword.getText().toString().trim();
        if(TextUtils.isEmpty(phonenumber)){
            Toast.makeText( this, "Please Enter Phone Number", Toast.LENGTH_SHORT ).show();
        }
        else if(phonenumber.length()!=10){
            Toast.makeText( this, "Please Enter Valid Phone Number", Toast.LENGTH_SHORT ).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText( this, "Please Enter Password", Toast.LENGTH_SHORT ).show();
        }
        else{
            loadingbar.setTitle( "Login Account" );
            loadingbar.setMessage( "Please wait ,while we are checking the credentials");
            loadingbar.setCanceledOnTouchOutside( false );
            loadingbar.show();

            AllowAccesstoAccoutn(phonenumber,password);

        }
    }


    private void AllowAccesstoAccoutn(final String phone, final String password)
    {
      if(checkcbox.isChecked()){
          Paper.book().write( Prevalent.Userphonekey ,phone);
          Paper.book().write( Prevalent.Userpasswordkey ,password);
      }

        final DatabaseReference Rootref;

        Rootref = FirebaseDatabase.getInstance().getReference();

        Rootref.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(ParentdbName).child(phone).exists()){
                    User userdata= dataSnapshot.child(ParentdbName).child(phone).getValue(User.class);

                    assert userdata != null;
                    if(userdata.getPhone().equals(phone))
                    {
                        if(userdata.getPassword().equals(password))
                        {
                            if(ParentdbName.equals("Admin"))
                            {
                                Toast.makeText( LoginActivity.this, "Welcome Admin!!", Toast.LENGTH_SHORT ).show();
                                loadingbar.dismiss();
                                Intent intent = new Intent( LoginActivity.this, AdminCategoryactivity.class);
                                startActivity(intent);
                            }
                            else if(ParentdbName.equals("User"))
                            {
                                Toast.makeText( LoginActivity.this, "Login Succsessful", Toast.LENGTH_SHORT ).show();
                                loadingbar.dismiss();

                                Intent intent = new Intent( LoginActivity.this, HomeActivty.class);
                                Prevalent.OnlineUser=userdata;
                                startActivity(intent);

                            }
                        }
                    }
                }
                else
                {
                    Toast.makeText( LoginActivity.this, "Account do not Exists", Toast.LENGTH_SHORT ).show();
                    loadingbar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }
}
