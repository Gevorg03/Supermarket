package com.example.menu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.menu.R;
import com.example.menu.constructors.ListItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.MyOrdersViewHolder> {
    Context context;
    private List<ListItem> listItems;
    private MyOrdersAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(MyOrdersAdapter.OnItemClickListener listener){
        mListener = listener;
    }

    public MyOrdersAdapter(Context ct, List<ListItem>  listItems){
        context = ct;
        this.listItems = listItems;
    }

    @NonNull
    @Override
    public MyOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_my_orders_datetime,parent,false);
        return new MyOrdersAdapter.MyOrdersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrdersViewHolder holder, int position) {
        final ListItem listItem = listItems.get(position);
        String dtc = "2014-04-02T07:59:02.111Z";
        String a = "";
        for(int i = 0; i < 10;i++){
            String c = listItem.getDatetime();
            char b = c.charAt(i);
            String d = String.valueOf(b);
            a +=d;
        }
        a += "T";
        for(int i = 11;i<19;i++){
            String c = listItem.getDatetime();
            char b = c.charAt(i);
            String d = String.valueOf(b);
            a +=d;
        }
        a+=".111Z";
        SimpleDateFormat readDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        readDate.setTimeZone(TimeZone.getTimeZone("GMT")); // missing line
        Date date = null;
        try {
            date = readDate.parse(a);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat writeDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        writeDate.setTimeZone(TimeZone.getTimeZone("GMT+04:00"));
        String s = writeDate.format(date);
        holder.tv_datetime.setText("Ժամանակ: " + s);
        holder.tv_price.setText("Գումարը: " + listItem.getPrice2() + " AMD");
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class MyOrdersViewHolder extends RecyclerView.ViewHolder {
        TextView tv_datetime,tv_price;
        ImageView img;
        public MyOrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_datetime = itemView.findViewById(R.id.tv_datetime);
            tv_price = itemView.findViewById(R.id.tv_spent_money);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
