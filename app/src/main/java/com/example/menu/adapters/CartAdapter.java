package com.example.menu.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.menu.R;
import com.example.menu.constructors.ListItem;
import com.example.menu.databases.DatabaseHelperContacts;
import com.example.menu.retrofits.RetrofitClientUpdateProductCart;
import com.example.menu.retrofits.RetrofitClientUpdateProductMarket;
import com.example.menu.services.APIServiceUpdateProductCart;
import com.example.menu.services.APIServiceUpdateProductMarket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MenuViewHolder> {
    Context context;
    private List<ListItem> listItems;
    private OnItemClickListener mListener;
    int count,price,count1;
    private static final String URL_DATA_CART = "https://menuproject.000webhostapp.com/api/post/ReadCart.php";
    private static final String URL_DATA_MARKET = "https://menuproject.000webhostapp.com/api/post/ReadProductsMarket.php";

    public interface OnItemClickListener{
        void onDeleteClick(int position,int size,String count);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public CartAdapter(Context ct, List<ListItem>  listItems){
        context = ct;
        this.listItems = listItems;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_order_menu,parent,false);
        return new CartAdapter.MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MenuViewHolder holder, final int position) {
        final ListItem listItem = listItems.get(position);
        holder.tv_name.setText(listItem.getName());
        if((holder.tv_count.getText().toString()).equals("0")){
            count = Integer.parseInt(listItem.getCount());
            holder.tv_count.setText(listItem.getCount());
        }
        if((holder.tv_price.getText().toString()).equals("0")) {
            price = Integer.parseInt(listItem.getPrice1());
            int a = price * count;
            holder.tv_price.setText(String.valueOf(a));
        }

        Glide
                .with(context)
                .load(listItem.getImg())
                .centerCrop()
                //.placeholder(R.drawable.imagesign)
                .into(holder.img_order);

        holder.btn_decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!holder.tv_count.getText().toString().equals("1")) {
                    StringRequest stringRequest1 = new StringRequest(Request.Method.GET, URL_DATA_MARKET,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(s);
                                        JSONArray array = jsonObject.getJSONArray("data");
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject productsJsonObject = array.getJSONObject(i);
                                            String id = productsJsonObject.getString("id");
                                            String name = productsJsonObject.getString("name");
                                            String price1 = productsJsonObject.getString("price");
                                            String count = productsJsonObject.getString("count");
                                            String img = "https://menuproject.000webhostapp.com/uploads/" + productsJsonObject.getString("img");
                                            String img1 = productsJsonObject.getString("img");
                                            String description = productsJsonObject.getString("description");
                                            if(listItem.getName().equals(name)) {
                                                updateProductMarket(Long.parseLong(id), name, Double.parseDouble(price1),
                                                        Integer.parseInt(count) + 1, img1, description);

                                                final DatabaseHelperContacts db = new DatabaseHelperContacts(context);
                                                StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA_CART,
                                                        new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String s) {
                                                                try {
                                                                    JSONObject jsonObject = new JSONObject(s);
                                                                    JSONArray array = jsonObject.getJSONArray("data");

                                                                    for (int j = 0; j < array.length(); j++) {
                                                                        JSONObject productsJsonObject = array.getJSONObject(j);
                                                                        String id = productsJsonObject.getString("id");
                                                                        String name = productsJsonObject.getString("name");
                                                                        String user_name = productsJsonObject.getString("userName");
                                                                        String price1 = productsJsonObject.getString("price");
                                                                        String count = productsJsonObject.getString("count");
                                                                        String img = "https://menuproject.000webhostapp.com/uploads/" + productsJsonObject.getString("img");
                                                                        String img1 = productsJsonObject.getString("img");
                                                                        if(name.equals(listItem.getName()) && db.getName(1).equals(user_name)){
                                                                            count1 = Integer.parseInt(holder.tv_count.getText().toString());
                                                                            price = Integer.parseInt(holder.tv_price.getText().toString());
                                                                            count1--;
                                                                            updateProductCart(Long.parseLong(id),user_name,name,count1,Double.parseDouble(price1),img1);
                                                                            holder.tv_count.setText(String.valueOf(count1));
                                                                            int a = price - Integer.parseInt(listItem.getPrice1());
                                                                            holder.tv_price.setText(String.valueOf(a));
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
                    RequestQueue requestQueue1 = Volley.newRequestQueue(context);
                    requestQueue1.add(stringRequest1);



                } else {
                    Toast.makeText(context,"Քանակը չի կարող մեկից փոքր լինել",Toast.LENGTH_SHORT).show();
                }
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
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject productsJsonObject = array.getJSONObject(i);
                                        String id = productsJsonObject.getString("id");
                                        String name = productsJsonObject.getString("name");
                                        String price1 = productsJsonObject.getString("price");
                                        String count = productsJsonObject.getString("count");
                                        String img = "https://menuproject.000webhostapp.com/uploads/" + productsJsonObject.getString("img");
                                        String img1 = productsJsonObject.getString("img");
                                        String description = productsJsonObject.getString("description");

                                        if(listItem.getName().equals(name)) {
                                            if(Integer.parseInt(count) >= 1) {
                                                updateProductMarket(Long.parseLong(id), name, Double.parseDouble(price1),
                                                        Integer.parseInt(count) - 1, img1, description);

                                                final DatabaseHelperContacts db = new DatabaseHelperContacts(context);
                                                StringRequest stringRequest1 = new StringRequest(Request.Method.GET, URL_DATA_CART,
                                                        new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String s) {
                                                                try {
                                                                    JSONObject jsonObject = new JSONObject(s);
                                                                    JSONArray array = jsonObject.getJSONArray("data");

                                                                    for (int j = 0; j < array.length(); j++) {
                                                                        JSONObject productsJsonObject = array.getJSONObject(j);
                                                                        String id = productsJsonObject.getString("id");
                                                                        String name = productsJsonObject.getString("name");
                                                                        String user_name = productsJsonObject.getString("userName");
                                                                        String price1 = productsJsonObject.getString("price");
                                                                        String count = productsJsonObject.getString("count");
                                                                        String img = "https://menuproject.000webhostapp.com/uploads/" + productsJsonObject.getString("img");
                                                                        String img1 = productsJsonObject.getString("img");
                                                                        if(name.equals(listItem.getName()) && db.getName(1).equals(user_name)){
                                                                            count1 = Integer.parseInt(holder.tv_count.getText().toString());
                                                                            price = Integer.parseInt(holder.tv_price.getText().toString());
                                                                            count1++;
                                                                            updateProductCart(Long.parseLong(id),user_name,name,count1,Double.parseDouble(price1),img1);
                                                                            holder.tv_count.setText(String.valueOf(count1));
                                                                            int a = price + Integer.parseInt(listItem.getPrice1());
                                                                            holder.tv_price.setText(String.valueOf(a));
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
                                                RequestQueue requestQueue1 = Volley.newRequestQueue(context);
                                                requestQueue1.add(stringRequest1);
                                            }else{
                                                Toast.makeText(context, "Այդքան ապրանք առկա չէ", Toast.LENGTH_SHORT).show();
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

    public void updateProductCart(long id,String user_name,String name,int count,double price,
                                  String img){
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

    @Override
    public int getItemCount() {
         return listItems.size();
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder{
        TextView tv_name,tv_count,tv_price;
        ImageView img_order,img_delete;
        Button btn_decrement,btn_increment;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name_order_cart);
            tv_count = itemView.findViewById(R.id.tv_count_order_cart);
            tv_price = itemView.findViewById(R.id.tv_price_order_cart);
            img_order = itemView.findViewById(R.id.img_order_cart);
            img_delete = itemView.findViewById(R.id.img_delete);
            btn_decrement = itemView.findViewById(R.id.btn_decrement_cart);
            btn_increment = itemView.findViewById(R.id.btn_increment_cart);

            img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener!=null){
                        int position = getAdapterPosition();
                        int size = listItems.size();
                        if(position!= RecyclerView.NO_POSITION){
                            mListener.onDeleteClick(position,size,tv_count.getText().toString());
                        }
                    }
                }
            });
        }
    }

    public void restoreItem(ListItem listItem,int position){
        listItems.add(position,listItem);
        notifyItemInserted(position);
    }
}
