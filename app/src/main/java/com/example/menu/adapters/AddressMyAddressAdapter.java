package com.example.menu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.menu.R;
import com.example.menu.constructors.ListItem;

import java.util.List;

public class AddressMyAddressAdapter extends RecyclerView.Adapter<AddressMyAddressAdapter.AddressMyAddressViewHolder> {
    Context context;
    private List<ListItem> listItems;
    private AddressMyAddressAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onDeleteClick(int position);
        void onEditClick(int position);
    }

    public void setOnItemClickListener(AddressMyAddressAdapter.OnItemClickListener listener){
        mListener = listener;
    }

    public AddressMyAddressAdapter(Context ct, List<ListItem>  listItems){
        context = ct;
        this.listItems = listItems;
    }

    @NonNull
    @Override
    public AddressMyAddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_address,parent,false);
        return new AddressMyAddressAdapter.AddressMyAddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressMyAddressViewHolder holder, int position) {
        ListItem listItem = listItems.get(position);
        holder.tv_address.setText("Հասցե: " + listItem.getAddress());
        holder.tv_phone.setText("Հեռախոսահամար: " + listItem.getPhone());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class AddressMyAddressViewHolder extends RecyclerView.ViewHolder {
        TextView tv_address,tv_phone;
        ImageView img_delete,img_edit;
        public AddressMyAddressViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_address = itemView.findViewById(R.id.tv_address_address);
            tv_phone = itemView.findViewById(R.id.tv_address_phone);
            img_delete = itemView.findViewById(R.id.img_address_delete);
            img_edit = itemView.findViewById(R.id.img_address_edit);

            img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener!=null){
                        int position = getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION){
                            mListener.onDeleteClick(position);
                        }
                    }
                }
            });
            img_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener!=null){
                        int position = getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION){
                            mListener.onEditClick(position);
                        }
                    }
                }
            });
        }
    }
}
