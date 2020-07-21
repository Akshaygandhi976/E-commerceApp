package com.example.e_commerceapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import Models.products;

public class ProductdetailActibity extends AppCompatActivity {

  private   FloatingActionButton addtocart;
    private ImageView imageView;
    private ElegantNumberButton numberButton;
    private TextView price,name, description;
    private  String productID="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_productdetail_actibity );

        productID = getIntent().getStringExtra( "Pid");
//        addtocart = findViewById( R.id.productdetails_cartbtn);
        numberButton = findViewById( R.id.productdetails_numberbtn);
        price=findViewById( R.id.productdetail_price);
        description=findViewById( R.id.productdetail_descrtion );
        name=findViewById( R.id.productdetail_productname );

        getproductdetails(productID);
    }

    private void getproductdetails(String productID) {

        DatabaseReference productref= FirebaseDatabase.getInstance().getReference().child( "Products" );
        productref.child(productID).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists()){
                    products product=dataSnapshot.getValue(products.class);

                    name.setText( product.getProductName());
                    description.setText( product.getDescription());
                    price.setText( product.getPrice());
                    Picasso.get().load( product.getImage() ).into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }
}
