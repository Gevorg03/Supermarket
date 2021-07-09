package com.example.menu.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.menu.activityes.AddressCartActivity;
import com.example.menu.activityes.BaseActivity;
import com.example.menu.activityes.MenuActivity;
import com.example.menu.activityes.ui.CartFragment;
import com.example.menu.constructors.ListItem;
import com.example.menu.R;
import com.example.menu.constructors.Product;
import com.example.menu.databases.DatabaseHelperContacts;
import com.example.menu.retrofits.RetrofitClientAddAccount;
import com.example.menu.retrofits.RetrofitClientAddProduct;
import com.example.menu.retrofits.RetrofitClientAddProductCart;
import com.example.menu.retrofits.RetrofitClientDeleteAllProductCart;
import com.example.menu.retrofits.RetrofitClientDeleteOrder;
import com.example.menu.retrofits.RetrofitClientDeleteProductCart;
import com.example.menu.retrofits.RetrofitClientDeleteProductCartName;
import com.example.menu.retrofits.RetrofitClientDeleteProductName;
import com.example.menu.retrofits.RetrofitClientUpdateProductCart;
import com.example.menu.retrofits.RetrofitClientUpdateProductMarket;

import com.example.menu.services.APIServiceAddProductCart;
import com.example.menu.services.APIServiceDeleteAllProductCart;
import com.example.menu.services.APIServiceDeleteOrder;
import com.example.menu.services.APIServiceDeleteProductCart;
import com.example.menu.services.APIServiceUpdateProductCart;
import com.example.menu.services.APIServiceUpdateProductMarket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> implements Filterable {
    private Context context;
    private List<ListItem> listItems;
    private List<ListItem> listItems1;
    DatabaseHelperContacts db;
    MenuActivity activity = new MenuActivity();
    static int count = 1;
    int d = 0,z = 0;
    private static final String URL_DATA_CART = "https://menuproject.000webhostapp.com/api/post/ReadCart.php";
    private static final String URL_DATA_MARKET = "https://menuproject.000webhostapp.com/api/post/ReadProductsMarket.php";

    public MenuAdapter(Context contextt,List<ListItem>  listItems){
        context = contextt;
        this.listItems = listItems;
        listItems1 = new ArrayList<>(listItems);
        db = new DatabaseHelperContacts(context);
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_menu,parent,false);
        return new MenuViewHolder(view);
    }
    int count1[] = new int[2];
    int count2[] = new int[2];
    @Override
    public void onBindViewHolder(@NonNull final MenuViewHolder holder, final int position) {
        final ListItem listItem = listItems.get(position);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA_MARKET,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray array = jsonObject.getJSONArray("data");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject productsJsonObject = array.getJSONObject(i);

                                String name = productsJsonObject.getString("name");
                                String count = productsJsonObject.getString("count");
                                if(listItem.getName().equals(name)) {
                                    count1[0] = Integer.parseInt(count);
                                    if(count1[0] > 0) {
                                        holder.tv_arkayutyun.setText("առկա է");
                                        holder.tv_arkayutyun.setTextColor(Color.parseColor("#7FFF00"));
                                    }else{
                                        holder.tv_arkayutyun.setTextColor(Color.parseColor("#FF0000"));
                                        holder.tv_arkayutyun.setText("առկա չէ");
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

        holder.tv_title.setText(listItem.getName());
        if((holder.tv_price.getText().toString()).equals("0")) {
            holder.tv_price.setText(String.valueOf(listItem.getPrice()));
        }

        Glide
                .with(context)
                .load(listItem.getImg())
                .centerCrop()
                //.placeholder(R.drawable.imagesign)
                .into(holder.img);

        holder.btn_add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest2 = new StringRequest(Request.Method.GET, URL_DATA_MARKET,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                try {
                                    JSONObject jsonObject = new JSONObject(s);
                                    JSONArray array = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject productsJsonObject = array.getJSONObject(i);

                                        String name = productsJsonObject.getString("name");
                                        String count = productsJsonObject.getString("count");
                                        if(listItem.getName().equals(name)) {
                                            count2[0] = Integer.parseInt(count);
                                            if(count2[0] > 0) {
                                                if(count2[0] >=
                                                        Integer.parseInt(holder.tv_count.getText().toString())) {
                                                    StringRequest stringRequest1 = new StringRequest(Request.Method.GET, URL_DATA_CART,
                                                            new Response.Listener<String>() {
                                                                @Override
                                                                public void onResponse(String s) {
                                                                    try {
                                                                        d = 0;
                                                                        JSONObject jsonObject = new JSONObject(s);
                                                                        JSONArray array = jsonObject.getJSONArray("data");
                                                                        for (int i = 0; i < array.length(); i++) {
                                                                            JSONObject productsJsonObject = array.getJSONObject(i);
                                                                            String id = productsJsonObject.getString("id");
                                                                            String userName = productsJsonObject.getString("userName");
                                                                            String img = productsJsonObject.getString("img");
                                                                            String name = productsJsonObject.getString("name");
                                                                            if(userName.equals(db.getName(1)) && listItem.getName().equals(name)){
                                                                                String a = String.valueOf(listItem.getPrice());
                                                                                double b = Double.parseDouble(a);
                                                                                String count1 = productsJsonObject.getString("count");
                                                                                int count2 = Integer.parseInt(count1) +
                                                                                        Integer.parseInt(holder.tv_count.getText().toString());
                                                                                updateProductCart(Long.parseLong(id),db.getName(1), listItem.getName(),count2,b,img);
                                                                                /*databaseHelperProducts.addProduct(new Product(listItem.getName(),
                                                                                   listItem.getCount2()));*/
                                                                                d = 1;
                                                                                //Toast.makeText(context, "Ավելացված է", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }
                                                                        if(d != 1){
                                                                            String a = String.valueOf(listItem.getPrice());
                                                                            double b = Double.parseDouble(a);
                                                                            sendPostCart(db.getName(1), listItem.getName(),
                                                                                    Integer.parseInt(holder.tv_count.getText().toString()), b,
                                                                                    listItem.getImg4());
                                                                            //sendPostCart("a", "a",1,1000,"a");
                                                                        }
                                                                    } catch (JSONException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            },
                                                            new Response.ErrorListener() {
                                                                @Override
                                                                public void onErrorResponse(VolleyError volleyError) {
                                                                }
                                                            });
                                                    RequestQueue requestQueue1 = Volley.newRequestQueue(context);
                                                    requestQueue1.add(stringRequest1);

                                                    String a = String.valueOf(listItem.getPrice());
                                                    double b = Double.parseDouble(a);
                                                    int count3 = count2[0] - Integer.parseInt(holder.tv_count.getText().toString());
                                                    if(count3 >= 0) {
                                                        updateProductMarket(Long.parseLong(listItem.getId2()), listItem.getName(), b, count3,
                                                                listItem.getImg4(), listItem.getDescription1());
                                                        //Toast.makeText(context, "Ավելացված է", Toast.LENGTH_SHORT).show();
                                                    }else {
                                                        Toast.makeText(context, "Այդքան ապրանք առկա չէ", Toast.LENGTH_SHORT).show();
                                                    }
                                                    Toast.makeText(context, "Ավելացված է", Toast.LENGTH_SHORT).show();
                                                }else{
                                                    Toast.makeText(context, "Այդքան ապրանք առկա չէ", Toast.LENGTH_SHORT).show();
                                                }
                                            }else{
                                                if(count2[0] > 0) {
                                                    holder.tv_arkayutyun.setText("առկա է");
                                                    holder.tv_arkayutyun.setTextColor(Color.parseColor("#7FFF00"));
                                                }else{
                                                    holder.tv_arkayutyun.setTextColor(Color.parseColor("#FF0000"));
                                                    holder.tv_arkayutyun.setText("առկա չէ");
                                                }
                                                Toast.makeText(context, "Ապրանքն առկա չէ", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                            }
                        });
                RequestQueue requestQueue2 = Volley.newRequestQueue(context);
                requestQueue2.add(stringRequest2);
            }
        });

        holder.btn_decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count != 1) {
                    count = Integer.parseInt(holder.tv_count.getText().toString());
                    count--;
                    String a = String.valueOf(count);
                    holder.tv_count.setText(String.valueOf(count));
                    int price = (int) (Integer.parseInt(holder.tv_price.getText().toString())
                            - listItem.getPrice() );
                    holder.tv_price.setText(String.valueOf(price));
                }else
                    Toast.makeText(context,"Քանակը չի կարող մեկից փոքր լինել",Toast.LENGTH_SHORT).show();
            }
        });

        holder.btn_increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA_MARKET,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                try {
                                    JSONObject jsonObject = new JSONObject(s);
                                    JSONArray array = jsonObject.getJSONArray("data");
                                    z = 0;
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject productsJsonObject = array.getJSONObject(i);
                                        String count1 = productsJsonObject.getString("count");
                                        String name = productsJsonObject.getString("name");
                                        if(Integer.parseInt(count1) > Integer.parseInt(holder.tv_count
                                            .getText().toString()) && listItem.getName().equals(name)) {
                                            count = Integer.parseInt(holder.tv_count.getText().toString());
                                            count++;
                                            z = 1;
                                            String a = String.valueOf(count);
                                            holder.tv_count.setText(String.valueOf(count));
                                            int price = (int) (listItem.getPrice() *
                                                    Integer.parseInt(holder.tv_count.getText().toString()));
                                            holder.tv_price.setText(String.valueOf(price));
                                        }
                                    }
                                    if(z != 1){
                                        Toast.makeText(context, "Այդքան ապրանք առկա չէ", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                            }
                        });
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(stringRequest);
            }
        });
    }

    public void updateProductMarket(long id,String name,double price,int count,String img,String type){
        Retrofit client = RetrofitClientUpdateProductMarket.getClient();
        APIServiceUpdateProductMarket mAPIService = client.create(APIServiceUpdateProductMarket.class);
        mAPIService.savePost(id,name,price,count,img,type,"dont").enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    //Log.i(TAG, "post submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Log.e("tag", "Unable to submit post to API.");
            }
        });
    }

    public void updateProductCart(long id,String user_name,String name,int count,double price,String img){
        Retrofit client = RetrofitClientUpdateProductCart.getClient();
        APIServiceUpdateProductCart mAPIService = client.create(APIServiceUpdateProductCart.class);
        mAPIService.savePost(id,user_name,name,count,price,img).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    //Log.i(TAG, "post submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Log.e("tag", "Unable to submit post to API.");
            }
        });
    }

    public void sendPostCart(String user_name,String name,int count,double price,String img) {
        Retrofit client = RetrofitClientAddAccount.getClient();
        APIServiceAddProductCart mAPIService = client.create(APIServiceAddProductCart.class);
        mAPIService.savePost(user_name,name,count,price,img).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    //Log.i(TAG, "post submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Log.e("tag", "Unable to submit post to API.");
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder{
        TextView tv_title,tv_price,tv_count,tv_arkayutyun;
        ImageView img;
        Button btn_add_cart,btn_decrement,btn_increment;
        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_count = itemView.findViewById(R.id.tv_count);
            tv_arkayutyun = itemView.findViewById(R.id.tv_arkayutyun);
            img = itemView.findViewById(R.id.img);
            btn_add_cart = itemView.findViewById(R.id.btn_add_cart);
            btn_decrement = itemView.findViewById(R.id.btn_decrement);
            btn_increment = itemView.findViewById(R.id.btn_increment);
        }
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ListItem> filteredList = new ArrayList<>();
            if(constraint== null || constraint.length() == 0){
                filteredList.addAll(listItems1);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(ListItem item: listItems1){
                    if(item.getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listItems.clear();
            listItems.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}

