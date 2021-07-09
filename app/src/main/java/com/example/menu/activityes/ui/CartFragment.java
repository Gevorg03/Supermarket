package com.example.menu.activityes.ui;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.menu.R;
import com.example.menu.activityes.AddressCartActivity;
import com.example.menu.activityes.BottomNavigationActivity;
import com.example.menu.activityes.MainActivity;
import com.example.menu.activityes.MenuActivity;
import com.example.menu.adapters.AddressCartAdapter;
import com.example.menu.adapters.CartAdapter;
import com.example.menu.constructors.ListItem;
import com.example.menu.constructors.Product;
import com.example.menu.databases.DatabaseHelperContacts;
import com.example.menu.retrofits.RetrofitClientAddMyOrders;
import com.example.menu.retrofits.RetrofitClientAddOrder;
import com.example.menu.retrofits.RetrofitClientAddProductCart;
import com.example.menu.retrofits.RetrofitClientAddProductCart1;
import com.example.menu.retrofits.RetrofitClientDeleteAllProductCart;
import com.example.menu.retrofits.RetrofitClientDeleteProductCart;
import com.example.menu.retrofits.RetrofitClientOrderProduct;
import com.example.menu.retrofits.RetrofitClientUpdateProductMarket;
import com.example.menu.services.APIServiceAddMyOrders;

import com.example.menu.services.APIServiceAddProductCart;

import com.example.menu.services.APIServiceDeleteAllProductCart;
import com.example.menu.services.APIServiceDeleteProductCart;
import com.example.menu.services.APIServiceOrderProduct;
import com.example.menu.services.APIServiceUpdateProductMarket;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class CartFragment extends Fragment implements CartAdapter.OnItemClickListener{
    Toolbar toolbar;
    private static final String URL_DATA_CART = "https://menuproject.000webhostapp.com/api/post/ReadCart.php";
    private static final String URL_DATA_MARKET = "https://menuproject.000webhostapp.com/api/post/ReadProductsMarket.php";
    private static List<ListItem> listItems;
    ImageView img_not_wifi;
    TextView tv_error;
    RecyclerView recyclerView;
    Button btn_order;
    int a = 0;
    static int b;
    Bundle bundle;
    DatabaseHelperContacts db;
    static CartAdapter cartAdapter;
    ProgressDialog progressDialog;
    private Context mContext;
    ConstraintLayout layout;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cart, container, false);
        img_not_wifi = root.findViewById(R.id.img_not_wifi_cart);
        tv_error = root.findViewById(R.id.tv_error_cart);
        btn_order = root.findViewById(R.id.btn_order_cart);
        layout = root.findViewById(R.id.cart_fragment);
        bundle = this.getArguments();

        toolbar = root.findViewById(R.id.toolbar_cart);
        toolbar.setTitle("Supermarket");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Տվյալների բեռնում");
        progressDialog.show();

        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(getContext());
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

                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject productsJsonObject = array.getJSONObject(i);
                                        String userName = productsJsonObject.getString("userName");
                                        if(userName.equals(db.getName(1))) {
                                           a = 1;
                                        }
                                    }

                                    if(a!=0){
                                        Intent intent = new Intent(getActivity(), AddressCartActivity.class);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(getContext(),"Զամբյուղը դատարկ է",Toast.LENGTH_SHORT).show();
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
                            }
                        });
                if(mContext != null){
                    RequestQueue requestQueue = Volley.newRequestQueue(mContext);
                    requestQueue.add(stringRequest);
                }
            }
        });

        db = new DatabaseHelperContacts(getContext());
        recyclerView = root.findViewById(R.id.recyclerview_cart);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        listItems = new ArrayList<>();
        loadRecyclerViewData();

        return root;
    }

    private void loadRecyclerViewData(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA_CART,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    tv_error.setText("");
                    img_not_wifi.setImageResource(R.drawable.ic_white);
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        JSONArray array = jsonObject.getJSONArray("data");
                        listItems.clear();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject productsJsonObject = array.getJSONObject(i);
                            String userName = productsJsonObject.getString("userName");
                            if(userName.equals(db.getName(1))) {
                                String id = productsJsonObject.getString("id");
                                String name = productsJsonObject.getString("name");
                                String count = productsJsonObject.getString("count");
                                String price = productsJsonObject.getString("price");
                                String img = "https://menuproject.000webhostapp.com/uploads/" + productsJsonObject.getString("img");
                                String img1 = productsJsonObject.getString("img");
                                ListItem item = new ListItem(id,name, count, price, img,img1);
                                listItems.add(item);
                            }
                        }
                        cartAdapter = new CartAdapter(getContext(), listItems);
                        cartAdapter.setOnItemClickListener(CartFragment.this);
                        recyclerView.setAdapter(cartAdapter);
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
        if(mContext!= null) {
            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            requestQueue.add(stringRequest);
        }
    }

    @Override
    public void onDeleteClick(int position, int size, String count) {
        b = size;
        final String countCart = count;
        final ListItem listItem = listItems.get(position);
        deletePostCart(Integer.parseInt(listItem.getId()));
        final String name = listItem.getName();

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
                                String nameMarket = productsJsonObject.getString("name");
                                String price = productsJsonObject.getString("price");
                                String countMarket = productsJsonObject.getString("count");
                                String img = productsJsonObject.getString("img");
                                String description = productsJsonObject.getString("description");
                                //String img_update = productsJsonObject.getString("img_update");
                                int countInt = Integer.parseInt(countCart) + Integer.parseInt(countMarket);
                                if(name.equals(nameMarket)){
                                    updateProductMarket(Long.parseLong(id),nameMarket,
                                            Double.parseDouble(price),countInt,img,description);
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
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);

        refresh(1000);
        /*DatabaseHelperProducts db = new DatabaseHelperProducts(mContext);
        List<Product> products = db.getAllProducts();
        for (Product p : products) {
            String log = "ID:" + p.getId() + ", NAME: " + p.getName() +
                    ", COUNT: " + p.getCount() +"\n";

            if(p.equals(name)){

            }
        }*/

        /*Snackbar snackbar = Snackbar.make(layout,name + " Removed",Snackbar.LENGTH_LONG);
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartAdapter.restoreItem(listItem,position);
                refresh(1000);
            }
        });
        snackbar.setActionTextColor(Color.GREEN);
        snackbar.show();*/

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

    public void deletePostCart(int id) {
        Retrofit client = RetrofitClientDeleteProductCart.getClient();
        APIServiceDeleteProductCart mAPIService = client.create(APIServiceDeleteProductCart.class);
        mAPIService.deletePost(id).enqueue(new Callback<ResponseBody>() {
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
                loadRecyclerViewData();
            }
        };
        handler.postDelayed(runnable,milliseconds);
    }
}
