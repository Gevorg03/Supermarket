package com.example.menu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.menu.R;
import com.example.menu.constructors.ListItem;

import java.util.List;

public class AddressCartAdapter extends RecyclerView.Adapter<AddressCartAdapter.AddressViewHolder> {
    Context context;
    private List<ListItem> listItems;
    private AddressCartAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(AddressCartAdapter.OnItemClickListener listener){
        mListener = listener;
    }

    public AddressCartAdapter(Context ct, List<ListItem>  listItems){
        context = ct;
        this.listItems = listItems;
    }


    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_address_cart,parent,false);
        return new AddressCartAdapter.AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        ListItem listItem = listItems.get(position);
        holder.tv_address.setText("Հասցե: " + listItem.getAddress());
        holder.tv_phone.setText("Հեռախոսահամար: " + listItem.getPhone());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }


    public class AddressViewHolder extends RecyclerView.ViewHolder{
        TextView tv_address,tv_phone;

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_address = itemView.findViewById(R.id.tv_address_address_cart);
            tv_phone = itemView.findViewById(R.id.tv_address_phone_cart);

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
