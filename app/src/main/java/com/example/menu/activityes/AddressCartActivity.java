package com.example.menu.activityes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.menu.R;
import com.example.menu.activityes.ui.AccountFragment;
import com.example.menu.activityes.ui.CartFragment;
import com.example.menu.activityes.ui.HomeFragment;
import com.example.menu.activityes.ui.MyOrdersFragment;
import com.example.menu.adapters.AddressCartAdapter;
import com.example.menu.adapters.MenuAdapter;
import com.example.menu.constructors.ListItem;
import com.example.menu.databases.DatabaseHelperContacts;
import com.example.menu.retrofits.RetrofitClientAddMyOrders;
import com.example.menu.retrofits.RetrofitClientDeleteAllProductCart;
import com.example.menu.retrofits.RetrofitClientOrderProduct;
import com.example.menu.services.APIServiceAddMyOrders;
import com.example.menu.services.APIServiceDeleteAllProductCart;
import com.example.menu.services.APIServiceOrderProduct;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class AddressCartActivity extends AppCompatActivity implements AddressCartAdapter.OnItemClickListener{
    Toolbar toolbar;
    DatabaseHelperContacts db = new DatabaseHelperContacts(this);
    private static List<ListItem> listItems,listItems1;
    RecyclerView recyclerView;
    static AddressCartAdapter addressAdapter;
    ProgressDialog progressDialog;
    double price1 = 0;
    private static final String URL_DATA_ADDRESS = "https://menuproject.000webhostapp.com/api/post/ReadAddress.php";
    private static final String URL_DATA_CART = "https://menuproject.000webhostapp.com/api/post/ReadCart.php";
    ImageView img_not_wifi;
    TextView tv_error,tv_toolbar_text;
    ImageButton btn_add;
    static String a = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_cart);

        btn_add = findViewById(R.id.btn_add_address_cart);
        tv_toolbar_text = findViewById(R.id.tv_toolbar_address_cart_text);
        toolbar = findViewById(R.id.toolbar_address_cart);
        tv_toolbar_text.setText("Supermarket");
        setSupportActionBar(toolbar);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddressCartActivity.this, AddressAddCartActivity.class));
            }
        });

        img_not_wifi = findViewById(R.id.img_not_wifi_address_cart);
        tv_error = findViewById(R.id.tv_error_address_cart);
        recyclerView = findViewById(R.id.recyclerview_address_cart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        listItems = new ArrayList<>();
        listItems1 = new ArrayList<>();
        loadRecyclerViewData();
        loadRecyclerViewDataUpdate();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Տվյալների բեռնում");
        progressDialog.show();
    }

    private void loadRecyclerViewData() {
        //refresh(1000);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA_ADDRESS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        tv_error.setText("");
                        img_not_wifi.setImageResource(R.drawable.ic_white);
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray array = jsonObject.getJSONArray("data");
                            String user_name = db.getName(1);
                            listItems.clear();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject productsJsonObject = array.getJSONObject(i);
                                String name = productsJsonObject.getString("name");
                                if(user_name.equals(name)) {
                                    String address = productsJsonObject.getString("address");
                                    String phone = productsJsonObject.getString("phone");
                                    ListItem item = new ListItem(address,phone);
                                    listItems.add(item);
                                }
                            }
                            addressAdapter = new AddressCartAdapter(getApplicationContext(), listItems);
                            addressAdapter.setOnItemClickListener(AddressCartActivity.this);
                            recyclerView.setAdapter(addressAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.dismiss();
                        listItems.clear();
                        tv_error.setText("Կապի խափանում");
                        img_not_wifi.setImageResource(R.drawable.ic_not_wifi);
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void loadRecyclerViewDataUpdate() {
        refresh(1000);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA_ADDRESS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        tv_error.setText("");
                        img_not_wifi.setImageResource(R.drawable.ic_white);
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray array = jsonObject.getJSONArray("data");
                            String user_name = db.getName(1);
                            listItems1.clear();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject productsJsonObject = array.getJSONObject(i);
                                String name = productsJsonObject.getString("name");
                                if(user_name.equals(name)) {
                                    String address = productsJsonObject.getString("address");
                                    String phone = productsJsonObject.getString("phone");
                                    ListItem item = new ListItem(address,phone);
                                    listItems1.add(item);
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
                        progressDialog.dismiss();
                        listItems1.clear();
                        tv_error.setText("Կապի խափանում");
                        img_not_wifi.setImageResource(R.drawable.ic_not_wifi);
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        if(listItems.size() < listItems1.size()){
            loadRecyclerViewData();
        }
    }

    @Override
    public void onItemClick(int position) {
        final ListItem listItem = listItems.get(position);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Տվյալների բեռնում");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA_CART,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray array = jsonObject.getJSONArray("data");
                            String user_name = db.getName(1);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject productsJsonObject = array.getJSONObject(i);
                                String user_name1 = productsJsonObject.getString("userName");
                                String name = productsJsonObject.getString("name");
                                String count = productsJsonObject.getString("count");
                                String price = productsJsonObject.getString("price");
                                String img = productsJsonObject.getString("img");
                                if(user_name.equals(user_name1)) {
                                    price1 = price1 + Integer.parseInt(price) * Integer.parseInt(count);
                                    sendPostOrder(name,listItem.getAddress(),Integer.parseInt(count),
                                            Double.parseDouble(price) * Integer.parseInt(count),
                                            db.getName(1),listItem.getPhone(), img);
                                    deletePostCart(user_name);
                                }
                            }
                            if(price1 != 0) {
                                sendPostMyOrders(user_name, price1,listItem.getAddress(),listItem.getPhone());
                            }
                            Toast.makeText(getApplicationContext(),"Պատվերն ընդունված է",Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(AddressCartActivity.this, BottomNavigationActivity.class);
                            startActivity(intent1);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Կապի խափանում",
                                Toast.LENGTH_SHORT).show();

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void sendPostOrder(String name,String address,int count,double price,String ordering_name,
                         String ordering_phone,String img) {
        Retrofit client = RetrofitClientOrderProduct.getClient();
        APIServiceOrderProduct mAPIService = client.create(APIServiceOrderProduct.class);
        mAPIService.savePost(name,address,count,price,ordering_name,ordering_phone,img).enqueue(new Callback<ResponseBody>() {
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

    public void deletePostCart(String productCart_user_name) {
        Retrofit client = RetrofitClientDeleteAllProductCart.getClient();
        APIServiceDeleteAllProductCart mAPIService = client.create(APIServiceDeleteAllProductCart.class);
        mAPIService.deletePost(productCart_user_name).enqueue(new Callback<ResponseBody>() {
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



    public void sendPostMyOrders(String name,double price,String ordering_address,String ordering_phone) {
        Retrofit client = RetrofitClientAddMyOrders.getClient();
        APIServiceAddMyOrders mAPIService = client.create(APIServiceAddMyOrders.class);
        mAPIService.savePost(name,price,ordering_address,ordering_phone).enqueue(new Callback<ResponseBody>() {
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

    public void refresh(int milliseconds){
        final Handler handler = new Handler();

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                loadRecyclerViewDataUpdate();
            }
        };
        handler.postDelayed(runnable,milliseconds);
    }
}
