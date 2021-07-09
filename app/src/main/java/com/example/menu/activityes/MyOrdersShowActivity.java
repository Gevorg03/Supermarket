package com.example.menu.activityes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
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
import com.example.menu.adapters.MenuAdapter;
import com.example.menu.adapters.MyOrdersShowAdapter;
import com.example.menu.constructors.ListItem;
import com.example.menu.databases.DatabaseHelperContacts;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MyOrdersShowActivity extends AppCompatActivity {
    Toolbar toolbar;
    private static List<ListItem> listItems;
    RecyclerView recyclerView;
    ImageView img_not_wifi;
    TextView tv_error;
    ProgressDialog progressDialog;
    List<ListItem> outputList;
    static MyOrdersShowAdapter myOrdersShowAdapter;
    private static final String URL_DATA = "https://menuproject.000webhostapp.com/api/post/Read.php";
    DatabaseHelperContacts db = new DatabaseHelperContacts(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders_show);

        toolbar = findViewById(R.id.toolbar_my_orders_show);
        toolbar.setTitle("Supermarket");
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Տվյալների բեռնում");
        progressDialog.show();

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navigation_cart:{
                        CartFragment cartFragment = new CartFragment();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.my_orders_show_activity,cartFragment).commit();
                        return true;
                    }case R.id.navigation_home:{
                        HomeFragment homeFragment = new HomeFragment();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.my_orders_show_activity,homeFragment).commit();
                        return true;
                    }case R.id.navigation_orders:{
                        MyOrdersFragment myOrdersFragment = new MyOrdersFragment();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.my_orders_show_activity,myOrdersFragment).commit();
                        return true;
                    }case R.id.navigation_account:{
                        AccountFragment accountFragment = new AccountFragment();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.my_orders_show_activity,accountFragment).commit();
                        return true;
                    }
                }
                return false;
            }
        });

        Intent intent = getIntent();
        Type listOfMyClassObject = new TypeToken<ArrayList<ListItem>>() {}.getType();
        Gson gson = new Gson();
        outputList = gson.fromJson(intent.getStringExtra("list"), listOfMyClassObject);

        img_not_wifi = findViewById(R.id.img_not_wifi_my_orders_show);
        tv_error = findViewById(R.id.tv_error_my_orders_show);
        recyclerView = findViewById(R.id.recyclerview_my_orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        listItems = new ArrayList<>();
        loadRecyclerViewData();

    }

    private void loadRecyclerViewData() {
        //refresh(1000);
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
                                String name = productsJsonObject.getString("ordering_name");
                                String datetime = productsJsonObject.getString("datetime");
                                String date = "";
                                String date1 = "";
                                for(int k = 0; k < 16;k++){
                                    char b = datetime.charAt(k);
                                    String d = String.valueOf(b);
                                    date +=d;
                                }
                                Intent intent = getIntent();
                                String a = intent.getStringExtra("position");
                                int position = Integer.parseInt(a);
                                if(a != null && !a.equals("")) {
                                    ListItem listItem = outputList.get(position);
                                    String datetime1 = listItem.getDatetime();
                                    for(int j = 0; j < 16;j++){
                                        char b1 = datetime1.charAt(j);
                                        String d1 = String.valueOf(b1);
                                        date1 +=d1;
                                    }
                                    if (user_name.equals(name) && date.equals(date1)) {
                                        String price = productsJsonObject.getString("price");
                                        String count = productsJsonObject.getString("count");
                                        String img = "https://menuproject.000webhostapp.com/uploads/" + productsJsonObject.getString("img");
                                        String price1 = String.valueOf(Integer.parseInt(price) / Integer.parseInt(count));
                                        ListItem item = new ListItem(count, price1, price, img);
                                        listItems.add(item);
                                    }
                                }
                            }
                            myOrdersShowAdapter = new MyOrdersShowAdapter(getApplicationContext(), listItems);
                            recyclerView.setAdapter(myOrdersShowAdapter);

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
