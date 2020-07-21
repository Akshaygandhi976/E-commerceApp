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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddnewproductActivity extends AppCompatActivity {

    private String categoryname,Description,price,pname,savecurrentdate,savecurrenttime;
    private Button AddnewproductBtn;
    private ImageView InputproductImage;
    private EditText productname,productdescription, productprice;
    private static final int GalleryPick=1;
    private Uri Imageuri;
    private String productrandomkey,downloadimageurl;
    private StorageReference ProductImagesref;
    private DatabaseReference ProductsRef;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_admin_addnewproduct );

        ProductImagesref = FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductsRef =FirebaseDatabase.getInstance().getReference().child( "Products" );
        AddnewproductBtn = findViewById( R.id.newproductbtn);
        InputproductImage = findViewById( R.id.selectproductimage);
        productname=findViewById( R.id.productname);
        productdescription=findViewById( R.id.descrption);
        productprice =findViewById( R.id.productprice);
        loadingbar = new ProgressDialog( this );


        InputproductImage.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Opengallery();
            }
        } );
        AddnewproductBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
             validateproductdata();
            }
        } );
    }

    private void validateproductdata()
    {
        Description = productdescription.getText().toString().trim();
        pname = productname.getText().toString().trim();
        price = productprice.getText().toString().trim();


        if(TextUtils.isEmpty( Description))
        {
            Toast.makeText( this, "Please Write Product Descrption", Toast.LENGTH_SHORT ).show();
        }
        else if(TextUtils.isEmpty(pname))
        {
            Toast.makeText( this, "Please Write Product Descrption", Toast.LENGTH_SHORT ).show();
        }
        else if(TextUtils.isEmpty(price))
        {
            Toast.makeText( this, "Please Write Product Descrption", Toast.LENGTH_SHORT ).show();
        }
        else
            {
                StoreproductInformation();
        }

    }

    private void StoreproductInformation()
    {
        loadingbar.setTitle( "Add new Product" );
        loadingbar.setMessage( "Please wait ,while we are adding product");
        loadingbar.setCanceledOnTouchOutside( false );
        loadingbar.show();

        Calendar calender = Calendar.getInstance();
        SimpleDateFormat currentdate = new SimpleDateFormat( "MM-dd,yyyy" );
        savecurrentdate = currentdate.format( calender.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat( "HH:mm:ss a" );
        savecurrenttime = currentTime.format( calender.getTime());

        productrandomkey =savecurrentdate +"  "+savecurrenttime;

        final StorageReference filepath=ProductImagesref.child( Imageuri.getLastPathSegment() + productrandomkey +".jpg");
        final UploadTask uploadTask =filepath.putFile( Imageuri );

        uploadTask.addOnFailureListener( new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message=e.toString();
                Toast.makeText( AdminAddnewproductActivity.this, "Error:"+ message, Toast.LENGTH_SHORT ).show();
                loadingbar.dismiss();
            }
        } ).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText( AdminAddnewproductActivity.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT ).show();
                Task<Uri> urlTask=  uploadTask.continueWithTask( new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                       if(!task.isSuccessful())
                       {
                           throw task.getException();

                       }
                        downloadimageurl =filepath.getDownloadUrl().toString();
                       return filepath.getDownloadUrl();
                    }
                } ).addOnCompleteListener( new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            downloadimageurl=task.getResult().toString();
                            Toast.makeText( AdminAddnewproductActivity.this, "Product Image saved successfully", Toast.LENGTH_SHORT ).show();
                            saveproductinfoTodatabase();
                        }
                    }
                } );
            }
        } );


    }

    private void saveproductinfoTodatabase()
    {
        HashMap<String ,Object> productMap = new HashMap<>( );
        productMap.put( "Pid",productrandomkey);
        productMap.put( "Date",savecurrentdate);
        productMap.put( "Time",savecurrenttime);
        productMap.put( "Descrption",Description);
        productMap.put( "Image",downloadimageurl);
        productMap.put( "Category",categoryname);
        productMap.put( "Price",price);
        productMap.put( "ProductName",pname);

        ProductsRef.child(productrandomkey).updateChildren( productMap )
                .addOnCompleteListener( new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            Intent intent = new Intent( getApplicationContext(),AdminCategoryactivity.class );
                            startActivity( intent);
                            Toast.makeText( AdminAddnewproductActivity.this, "Product is added Successfully", Toast.LENGTH_SHORT ).show();
                            loadingbar.dismiss();
                        }
                        else{
                            Toast.makeText( AdminAddnewproductActivity.this, "Error occured", Toast.LENGTH_SHORT ).show();
                            loadingbar.dismiss();
                        }
                    }
                } );

    }

    private void Opengallery() {
        Intent galleryIntent = new Intent( );
        galleryIntent.setAction( Intent.ACTION_GET_CONTENT);
        galleryIntent.setType( "image/*" );
        startActivityForResult(galleryIntent,GalleryPick);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null)
        {
            Imageuri =data.getData();
            InputproductImage.setImageURI(Imageuri);
        }
    }
}
