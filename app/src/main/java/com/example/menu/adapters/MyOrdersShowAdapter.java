package com.example.menu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.menu.R;
import com.example.menu.constructors.ListItem;

import java.util.List;

public class MyOrdersShowAdapter extends RecyclerView.Adapter<MyOrdersShowAdapter.MyOrdersShowViewHolder> {
    Context context;
    private List<ListItem> listItems;

    public MyOrdersShowAdapter(Context ct, List<ListItem>  listItems){
        context = ct;
        this.listItems = listItems;
    }

    @NonNull
    @Override
    public MyOrdersShowAdapter.MyOrdersShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_my_orders,parent,false);
        return new MyOrdersShowAdapter.MyOrdersShowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrdersShowAdapter.MyOrdersShowViewHolder holder, int position) {
        ListItem listItem = listItems.get(position);
        holder.tv_count.setText("Քանակ: " + listItem.getCount3() + " հատ");
        holder.tv_price.setText("Արժեք: " + listItem.getPrice3() + " AMD");
        holder.tv_total_price.setText("Ընդհանուր գումար: " + listItem.getTotalPrice3() + " AMD");

        Glide
                .with(context)
                .load(listItem.getImg3())
                .centerCrop()
                //.placeholder(R.drawable.imagesign)
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class MyOrdersShowViewHolder extends RecyclerView.ViewHolder {
        TextView tv_count,tv_price,tv_total_price;
        ImageView img;
        public MyOrdersShowViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_my_orders);
            tv_count = itemView.findViewById(R.id.tv_count_my_orders);
            tv_price = itemView.findViewById(R.id.tv_price_my_orders);
            tv_total_price = itemView.findViewById(R.id.tv_total_price_my_orders);
        }
    }
}
