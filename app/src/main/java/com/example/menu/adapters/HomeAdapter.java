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
import com.example.menu.constructors.ListItem;
import com.example.menu.R;

import java.io.File;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MenuViewHolder> {
    private OnItemClickListener mListener;
    Context context;
    private List<ListItem> listItems;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public HomeAdapter(Context ct, List<ListItem> listItems){
        context = ct;
        this.listItems = listItems;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_type_product,parent,false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        ListItem listItem = listItems.get(position);
        holder.tv_title.setText(listItem.getDescription());

        Glide
                .with(context)
                .load(listItem.getImg1())
                .centerCrop()
                //.placeholder(R.drawable.imagesign)
                .into(holder.img);
    }


    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView tv_title;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title_product_type);
            img = itemView.findViewById(R.id.img_type);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener!=null){
                        int position = getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

}
