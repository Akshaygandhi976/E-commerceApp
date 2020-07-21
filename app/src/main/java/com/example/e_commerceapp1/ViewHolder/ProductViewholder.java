package com.example.e_commerceapp1.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerceapp1.Interface.ItemClickListener;
import com.example.e_commerceapp1.R;

public class ProductViewholder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtproductname,txtproductdescription,txtproductprice;
    public ImageView imageview;
    public ItemClickListener listener;

    public ProductViewholder(@NonNull View itemView) {
        super( itemView );
        imageview = itemView.findViewById( R.id.product_Image );
        txtproductname = itemView.findViewById( R.id.product_name );
        txtproductdescription = itemView.findViewById( R.id.product_description );
        txtproductprice = itemView.findViewById( R.id.product_Price );
    }
    public void setItemClickListener(ItemClickListener listener)
    {
        this.listener= listener;
    }

    @Override
    public void onClick(View view)
    {
         listener.onClick( view,getAdapterPosition(),false);
    }

}
