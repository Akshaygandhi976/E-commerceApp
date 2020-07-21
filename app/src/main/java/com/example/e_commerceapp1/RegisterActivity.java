package com.example.e_commerceapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
private EditText mname, mphonenumber, mpassword;
private Button mRegisterbutton;
private ProgressDialog loadingbar;

@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_register );
        mname = findViewById( R.id.NameRegister);
        mphonenumber =findViewById( R.id.phonenumberRegister );
        mpassword =findViewById( R.id.passwordRegister);
        mRegisterbutton=findViewById( R.id.Registerbutton);
        loadingbar = new ProgressDialog( this );

        mRegisterbutton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                createAccount();
            }
        } );
    }

    private void createAccount() {
        String name =mname.getText().toString().trim();
        String phonenumber =mphonenumber.getText().toString().trim();
        String password =mpassword.getText().toString().trim();
        if(TextUtils.isEmpty( name)){
            Toast.makeText( this, "Please Enter Name", Toast.LENGTH_SHORT ).show();
        }
        else if(TextUtils.isEmpty(phonenumber)){
            Toast.makeText( this, "Please Enter Phone Number", Toast.LENGTH_SHORT ).show();
        }

        else if(TextUtils.isEmpty(password)){
            Toast.makeText( this, "Please Enter Password", Toast.LENGTH_SHORT ).show();
        }
        else if(password.length()<6){
            Toast.makeText( this, "Please Enter Stronger Password", Toast.LENGTH_SHORT ).show();
        }
        else{
            loadingbar.setTitle( "Create Account" );
            loadingbar.setMessage( "Please wait ,while we are checking the credentials");
            loadingbar.setCanceledOnTouchOutside( false );
            loadingbar.show();
            
            Validatephonenumber(name,phonenumber, password);
        }
    }

    private void Validatephonenumber(final String name, final String phonenumber,final String password) {
        final DatabaseReference Rootref;
        Rootref = FirebaseDatabase.getInstance().getReference();

        Rootref.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child( "User" ).child( phonenumber ).exists())){
                    HashMap<String,Object>  userdataMap = new HashMap<>(  );
                    userdataMap.put("Phone",phonenumber);
                    userdataMap.put("Name",name);
                    userdataMap.put("Password",password);

                    Rootref.child( "User").child( phonenumber).updateChildren( userdataMap)
                            .addOnCompleteListener( new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText( RegisterActivity.this, "Account is created ", Toast.LENGTH_SHORT ).show();
                                            loadingbar.dismiss();

                                            Intent intent = new Intent(RegisterActivity.this,LoginActivity.class );
                                            startActivity(intent);
                                        }
                                        else{
                                            loadingbar.dismiss();
                                            Toast.makeText( RegisterActivity.this, "Please Try Again!", Toast.LENGTH_SHORT ).show();
                                        }
                                }
                            } );
                }
                else{
                    Toast.makeText( RegisterActivity.this, "This Phone Number already Exists", Toast.LENGTH_SHORT ).show();
                    loadingbar.dismiss();
                    Toast.makeText( RegisterActivity.this, "Please Enter using another Number", Toast.LENGTH_SHORT ).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

    }

}
