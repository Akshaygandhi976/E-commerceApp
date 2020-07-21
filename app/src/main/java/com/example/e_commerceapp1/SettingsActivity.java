package com.example.e_commerceapp1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import io.grpc.Context;
import prevalent.Prevalent;

public class SettingsActivity extends AppCompatActivity {
    private CircleImageView profileImageview;
    private EditText nameeditext, phonenumberedittext, addressedittext;
    private TextView profilechangeetxtbtn, closeTextbtn,saveTextButton;

    private Uri imageuri;
    private String  myUrl ="";
    private StorageReference storageprofilepictureRef;
    private  String checker="";
    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_settings );

        storageprofilepictureRef= FirebaseStorage.getInstance().getReference().child(   "Profile pictures");

        profileImageview =findViewById( R.id.profile_image_settings);
        nameeditext=findViewById( R.id.settings_fullname);
        phonenumberedittext =findViewById( R.id.settings_phoneNumber);
        addressedittext =findViewById( R.id.settings_address);
        profilechangeetxtbtn =findViewById( R.id.profileimagechange_settingsbtn);
        closeTextbtn =findViewById( R.id.close_settings);
        saveTextButton = findViewById( R.id.update_settings);

        updateinfoDisplay(profileImageview,nameeditext,phonenumberedittext,addressedittext);
                
            closeTextbtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            } );
            saveTextButton.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(checker.equals( "clicked" ))
                    {
                        userinfosaved();
                    }
                    else{
                        updateOnlyUserInfo();
                    }
                }
            } );
            profilechangeetxtbtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checker="clicked";
                    CropImage.activity(imageuri)
                            .start(SettingsActivity.this);
                }
            } );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null){
            CropImage.ActivityResult result= CropImage.getActivityResult( data);
            imageuri =result.getUri();

            profileImageview.setImageURI( imageuri);
            
        }
        else{
            Toast.makeText( this, "Error Try Again", Toast.LENGTH_SHORT ).show();
            startActivity( new Intent( getApplicationContext(),SettingsActivity.class));
            finish();
        }
    }

    private void updateOnlyUserInfo() {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child( "User" );
        HashMap<String,Object> userMap= new HashMap<>( );
        userMap.put( "Name",nameeditext.getText().toString());
        userMap.put( "Address",addressedittext.getText().toString());
        userMap.put( "PhoneOreder",phonenumberedittext.getText().toString());
        ref.child( Prevalent.OnlineUser.getPhone()).updateChildren( userMap);

        startActivity( new Intent( getApplicationContext(),LoginActivity.class));
        finish();

    }

    private void userinfosaved()
    {
        if(TextUtils.isEmpty( nameeditext.getText().toString() )){
            Toast.makeText( this, "Name is Mandatory", Toast.LENGTH_SHORT ).show();
        }
        else if(TextUtils.isEmpty( addressedittext.getText().toString() )){
            Toast.makeText( this, "Address is Mandatory", Toast.LENGTH_SHORT ).show();
        }
        else if(TextUtils.isEmpty( phonenumberedittext.getText().toString() )){
            Toast.makeText( this, "Phone Number is Mandatory", Toast.LENGTH_SHORT ).show();
        }
       else if(checker.equals( "clicked" )){
           uploadImage();
        }
    }

    private void uploadImage()
    {
        final ProgressDialog progressdialog= new ProgressDialog( this );
        progressdialog.setTitle( "Update Profile" );
        progressdialog.setMessage( "Please wait while we are updating your Information" );
        progressdialog.setCanceledOnTouchOutside( false);
        progressdialog.show();

        if(imageuri!=null){
            final StorageReference fileRef =storageprofilepictureRef
                    .child( Prevalent.OnlineUser.getPhone() + ".jpg");

            uploadTask = fileRef.putFile( imageuri);
            uploadTask.continueWithTask( new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();

                }
            } )
            .addOnCompleteListener( new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUrl= task.getResult();
                        myUrl  = downloadUrl.toString();

                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child( "User" );
                        HashMap<String,Object> userMap= new HashMap<>( );
                        userMap.put( "Name",nameeditext.getText().toString());
                        userMap.put( "Address",addressedittext.getText().toString());
                        userMap.put( "PhoneOreder",phonenumberedittext.getText().toString());
                        userMap.put( "image",myUrl);
                        ref.child( Prevalent.OnlineUser.getPhone()).updateChildren( userMap);

                        progressdialog.dismiss();
                        startActivity( new Intent( getApplicationContext(),LoginActivity.class));
                        finish();
                    }
                }
            } );

        }
    }

    private void updateinfoDisplay(final CircleImageView profileImageview, final EditText nameeditext, final EditText phonenumberedittext, final EditText addressedittext)
    {
        DatabaseReference UserRef= FirebaseDatabase.getInstance().getReference().child( "User").child( Prevalent.OnlineUser.getPhone());
        UserRef.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.child( "image" ).exists()){
                        String image=dataSnapshot.child( "image" ).getValue().toString();
                        String name=dataSnapshot.child( "Name" ).getValue().toString();
                        String phone=dataSnapshot.child( "Phone" ).getValue().toString();
                        String address=dataSnapshot.child( "Address" ).getValue().toString();

                        Picasso.get().load(image).into(profileImageview);
                        nameeditext.setText( name);
                        phonenumberedittext.setText(phone);
                        addressedittext.setText(address);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }
}
