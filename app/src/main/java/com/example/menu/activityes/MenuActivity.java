package com.example.menu.activityes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
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
import com.example.menu.adapters.MenuAdapter;
import com.example.menu.constructors.ListItem;
import com.example.menu.databases.DatabaseHelperContacts;
import com.example.menu.retrofits.RetrofitClientAddProductCart;
import com.example.menu.retrofits.RetrofitClientOrderProduct;
import com.example.menu.services.APIServiceAddProductCart;
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

public class MenuActivity extends AppCompatActivity {
    Toolbar toolbar;
    private static List<ListItem> listItems;
    RecyclerView recyclerView;
    static MenuAdapter menuAdapter;
    private static final String URL_DATA = "https://menuproject.000webhostapp.com/api/post/ReadProductsMarket.php";
    ProgressDialog progressDialog;
    ImageView img_not_wifi;
    int count1 = 0;
    TextView tv_error,tv_toolbar_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        img_not_wifi = findViewById(R.id.img_not_wifi_menu);
        tv_error = findViewById(R.id.tv_error_menu);
        tv_toolbar_text = findViewById(R.id.tv_toolbar_menu_text);
        toolbar = findViewById(R.id.toolbar_menu);
        tv_toolbar_text.setText("Supermarket");
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Տվյալների բեռնում");
        progressDialog.show();

        SearchView searchView = findViewById(R.id.btn_search);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                menuAdapter.getFilter().filter(newText);
                return false;
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView
                .OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navigation_cart:{
                        CartFragment cartFragment = new CartFragment();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.menu_activity,cartFragment).commit();
                        return true;
                    }case R.id.navigation_home:{
                        HomeFragment homeFragment = new HomeFragment();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.menu_activity,homeFragment).commit();
                        return true;
                    }case R.id.navigation_orders:{
                        MyOrdersFragment myOrdersFragment = new MyOrdersFragment();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.menu_activity,myOrdersFragment).commit();
                        return true;
                    }case R.id.navigation_account:{
                        AccountFragment accountFragment = new AccountFragment();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.menu_activity,accountFragment).commit();
                        return true;
                    }
                }
                return false;
            }
        });

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        listItems = new ArrayList<>();
        loadRecyclerViewData();
        //loadRecyclerViewDataUpdate();
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
                            Intent intent = getIntent();
                            String description1 = intent.getStringExtra("description");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject productsJsonObject = array.getJSONObject(i);
                                String description = productsJsonObject.getString("description");
                                if(description.equals(description1)) {
                                    String id = productsJsonObject.getString("id");
                                    String name = productsJsonObject.getString("name");
                                    String price = productsJsonObject.getString("price");
                                    String count = productsJsonObject.getString("count");
                                    String img = "https://menuproject.000webhostapp.com/uploads/" +
                                            productsJsonObject.getString("img");
                                    String img1 = productsJsonObject.getString("img");
                                    count1 = Integer.parseInt(count);
                                    int gin = Integer.parseInt(price);
                                    ListItem item = new ListItem(id,name, gin,count,img,img1,description);
                                    listItems.add(item);
                                }
                            }
                            menuAdapter = new MenuAdapter(getApplicationContext(), listItems);
                            recyclerView.setAdapter(menuAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.dismiss();
                        tv_error.setText("Կապի խափանում");
                        img_not_wifi.setImageResource(R.drawable.ic_not_wifi);

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void loadRecyclerViewDataUpdate() {
        refresh(1000);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray array = jsonObject.getJSONArray("data");
                            Intent intent = getIntent();
                            String description1 = intent.getStringExtra("description");
                            listItems.clear();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject productsJsonObject = array.getJSONObject(i);
                                String description = productsJsonObject.getString("description");
                                //if(description.equals(description1)) {
                                    String id = productsJsonObject.getString("id");
                                    String name = productsJsonObject.getString("name");
                                    String price = productsJsonObject.getString("price");
                                    String count = productsJsonObject.getString("count");
                                    String img = "https://menuproject.000webhostapp.com/uploads/" +
                                            productsJsonObject.getString("img");
                                    String img1 = productsJsonObject.getString("img");
                                    int gin = Integer.parseInt(price);
                                    if(Integer.parseInt(count) != count1)
                                        refresh(1000);
                                //}
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search,menu);
        MenuItem searchItem = menu.findItem(R.id.item_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                menuAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }*/

}
