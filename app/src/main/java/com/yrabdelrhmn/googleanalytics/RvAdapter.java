package com.yrabdelrhmn.googleanalytics;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.MyViewHolder> {
List<Product> products;
Context context;
FirebaseAnalytics myFirebaseAnalytics;

    public RvAdapter(List<Product> products, Context context, FirebaseAnalytics myFirebaseAnalytics) {
        this.products = products;
        this.context = context;
        this.myFirebaseAnalytics = myFirebaseAnalytics;
    }

    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
        Product product = products.get(position);
       holder.tvName.setText(product.getProductDetails());
       String imageUrl = product.getProductImage();
        Picasso.get().load(R.drawable.labtop).into(holder.iv);
        Log.e("imageUrl",imageUrl);
    }

    @Override
    public int getItemCount() {
       return products.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView tvName;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.productImage);
            tvName = itemView.findViewById(R.id.productName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,DetailsOfProduct.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("DATE_PRODUCT",products.get(getAdapterPosition()));
                    intent.putExtras(bundle);

                    new Service().selectContent(
                            myFirebaseAnalytics,
                            String.valueOf(new Random().nextInt(10000)),
                            products.get(getAdapterPosition()).getProductName(),"click");
                    context.startActivity(intent);


                }
            });

            }
        }
    }

