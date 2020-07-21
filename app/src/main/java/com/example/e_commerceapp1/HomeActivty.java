package com.example.e_commerceapp1;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_commerceapp1.ViewHolder.ProductViewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import Models.products;
import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import prevalent.Prevalent;

import static com.example.e_commerceapp1.R.string.app_name;

public class HomeActivty extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private DatabaseReference productref;
    private RecyclerView recyclerview;
    RecyclerView.LayoutManager layoutManager;
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_home_activty );

        productref = FirebaseDatabase.getInstance().getReference().child( "Products" );
        recyclerview = findViewById( R.id.recycler_menu );
        recyclerview.setHasFixedSize( true );
        layoutManager = new LinearLayoutManager( this );
        recyclerview.setLayoutManager( layoutManager );

        Paper.init( this );
        DrawerLayout drawer = findViewById( R.id.drawer_layout );
        Toolbar toolbar = findViewById( R.id.toolbar );
        toolbar.setTitle( "Home" );
        setSupportActionBar( toolbar );
        actionBarDrawerToggle = new ActionBarDrawerToggle( this, drawer, toolbar, R.string.app_name, R.string.app_name );
        drawer.addDrawerListener( actionBarDrawerToggle );
        actionBarDrawerToggle.syncState();

        final NavigationView navigationView = findViewById( R.id.nav_view );

        navigationView.setNavigationItemSelectedListener( new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_cart) {

                } else if (id == R.id.nav_categories) {

                } else if (id == R.id.nav_order) {

                } else if (id == R.id.nav_settings)
                {
                    Intent intent = new Intent( HomeActivty.this, SettingsActivity.class );
                    startActivity( intent );
                }
                else if (id == R.id.nav_logout) {

                    Paper.book().destroy();
                    Intent intent = new Intent( HomeActivty.this, MainActivity.class );
                    intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
                    startActivity( intent );
                    finish();
                }
                return false;
            }
        } );


        View headerview = navigationView.getHeaderView( 0 );
        TextView usernametextview = headerview.findViewById( R.id.User_profilename );
        CircleImageView profileimageview = headerview.findViewById( R.id.User_profile_image );
        Picasso.get().load( Prevalent.OnlineUser.getImage()).placeholder( R.drawable.profile).into( profileimageview);
        usernametextview.setText( Prevalent.OnlineUser.getName() );
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<products> options =
                new FirebaseRecyclerOptions.Builder<products>()
                        .setQuery( productref, products.class )
                        .build();
        FirebaseRecyclerAdapter<products, ProductViewholder> adapter =
                new FirebaseRecyclerAdapter<products, ProductViewholder>( options ) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewholder holder, int position, @NonNull final products model) {
                        holder.txtproductname.setText( model.getProductName() );
                        holder.txtproductdescription.setText( model.getDescription() );
                        holder.txtproductprice.setText( "Price - Rs." + model.getPrice() );
                        Picasso.get().load( model.getImage() ).into( holder.imageview );

                        holder.itemView.setOnClickListener( new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent( HomeActivty.this,ProductdetailActibity.class );
                                intent.putExtra( "Pid",model.getPid());
                                startActivity( intent );
                            }
                        } );

                    }

                    @NonNull
                    @Override
                    public ProductViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.products_layouts, parent, false );
                        ProductViewholder holder = new ProductViewholder( view );
                        return holder;
                    }
                };

        recyclerview.setAdapter( adapter );
        adapter.startListening();
    }

}


