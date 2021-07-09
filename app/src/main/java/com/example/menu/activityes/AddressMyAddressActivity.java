package com.example.menu.activityes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.example.menu.adapters.AddressMyAddressAdapter;
import com.example.menu.constructors.ListItem;
import com.example.menu.databases.DatabaseHelperContacts;
import com.example.menu.retrofits.RetrofitClientDeleteAddress;
import com.example.menu.services.APIServiceDeleteAddress;
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

public class AddressMyAddressActivity extends AppCompatActivity implements AddressMyAddressAdapter.OnItemClickListener{
    Toolbar toolbar;
    DatabaseHelperContacts db = new DatabaseHelperContacts(this);
    private static List<ListItem> listItems,listItems1;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    static AddressMyAddressAdapter addressMyAddressAdapter;
    private static final String URL_DATA = "https://menuproject.000webhostapp.com/api/post/ReadAddress.php";
    ImageView img_not_wifi;
    TextView tv_error,tv_toolbar_text;
    ImageButton btn_add;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        btn_add = findViewById(R.id.btn_add_address);
        tv_toolbar_text = findViewById(R.id.tv_toolbar_address_text);

        toolbar = findViewById(R.id.toolbar_address);
        tv_toolbar_text.setText("Supermarket");
        setSupportActionBar(toolbar);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddressMyAddressActivity.this,AddressAddMyAddressActivity.class);
                intent.putExtra("add_address","add");
                startActivity(intent);
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.nav_view_address);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navigation_cart:{
                        CartFragment cartFragment = new CartFragment();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.address_activity,cartFragment).commit();
                        return true;
                    }case R.id.navigation_home:{
                        HomeFragment homeFragment = new HomeFragment();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.address_activity,homeFragment).commit();
                        return true;
                    }case R.id.navigation_orders:{
                        MyOrdersFragment myOrdersFragment = new MyOrdersFragment();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.address_activity,myOrdersFragment).commit();
                        return true;
                    }case R.id.navigation_account:{
                        AccountFragment accountFragment = new AccountFragment();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.address_activity,accountFragment).commit();
                        return true;
                    }
                }
                return false;
            }
        });

        img_not_wifi = findViewById(R.id.img_not_wifi_address);
        tv_error = findViewById(R.id.tv_error_address);
        recyclerView = findViewById(R.id.recyclerview_address);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        listItems = new ArrayList<>();
        listItems1 = new ArrayList<>();
        refresh1(1000);
        loadRecyclerViewData();
        loadRecyclerViewDataUpdate();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Տվյալների բեռնում");
        progressDialog.show();
    }

    private void loadRecyclerViewData() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA,
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
                                    String id = productsJsonObject.getString("id");
                                    String address = productsJsonObject.getString("address");
                                    String phone = productsJsonObject.getString("phone");
                                    ListItem item = new ListItem(id,address,phone);
                                    listItems.add(item);
                                }
                            }
                            addressMyAddressAdapter = new AddressMyAddressAdapter(getApplicationContext(), listItems);
                            addressMyAddressAdapter.setOnItemClickListener(AddressMyAddressActivity.this);
                            recyclerView.setAdapter(addressMyAddressAdapter);
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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA,
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
                                    String id = productsJsonObject.getString("id");
                                    String address = productsJsonObject.getString("address");
                                    String phone = productsJsonObject.getString("phone");
                                    ListItem item = new ListItem(id,address,phone);
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

    public void openDialog(final ListItem listItem){
        View view = LayoutInflater.from(this).inflate(R.layout.user_dialog_for_correct, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        final DatabaseHelperContacts db = new DatabaseHelperContacts(this);
        Button btn_ok = view.findViewById(R.id.btn_dialog_ok);
        Button btn_cancel = view.findViewById(R.id.btn_dialog_cancel);
        TextView tv_name = view.findViewById(R.id.dialog);
        tv_name.setText("Ջնջել հասցե՞ն");
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePostAddress(Integer.parseInt(listItem.getId1()));
                dialog.dismiss();
                refresh1(1000);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void deletePostAddress(int id) {
        Retrofit client = RetrofitClientDeleteAddress.getClient();
        APIServiceDeleteAddress mAPIService = client.create(APIServiceDeleteAddress.class);
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
                loadRecyclerViewDataUpdate();
            }
        };
        handler.postDelayed(runnable,milliseconds);
    }

    public void refresh1(int milliseconds){
        final Handler handler = new Handler();

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                loadRecyclerViewData();
            }
        };
        handler.postDelayed(runnable,milliseconds);
    }

    @Override
    public void onDeleteClick(int position) {
        openDialog(listItems.get(position));
    }

    @Override
    public void onEditClick(int position) {
        ListItem listItem = listItems.get(position);
        Intent intent = new Intent(AddressMyAddressActivity.this,AddressAddMyAddressActivity.class);
        intent.putExtra("add_address","edit");
        intent.putExtra("address",listItem.getAddress());
        intent.putExtra("phone",listItem.getPhone());
        intent.putExtra("id11",String.valueOf(listItem.getId1()));
        startActivity(intent);
    }
}
