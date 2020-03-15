package com.example.online_quize_app.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.online_quize_app.R;

import com.example.online_quize_app.Interface.ItemClickListener;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView category_name;
    public ImageView category_image;

    private ItemClickListener itemClickListener;
    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        category_image=itemView.findViewById(R.id.category_image);
        category_name=itemView.findViewById(R.id.category_name);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);

    }
}
