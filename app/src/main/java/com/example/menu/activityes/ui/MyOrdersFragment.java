package com.example.menu.activityes.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.annotation.NonNull;
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
import com.example.menu.activityes.MyOrdersShowActivity;
import com.example.menu.adapters.MyOrdersAdapter;
import com.example.menu.constructors.ListItem;
import com.example.menu.databases.DatabaseHelperContacts;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyOrdersFragment extends Fragment implements MyOrdersAdapter.OnItemClickListener{
    Toolbar toolbar;
    private static final String URL_DATA = "https://menuproject.000webhostapp.com/api/post/ReadMyOrders.php";
    private static List<ListItem> listItems;
    RecyclerView recyclerView;
    DatabaseHelperContacts db;
    static MyOrdersAdapter myOrdersAdapter;
    ImageView img_not_wifi;
    TextView tv_error;
    ProgressDialog progressDialog;
    private Context mContext;

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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_orders, container, false);
        img_not_wifi = root.findViewById(R.id.img_not_wifi_my_orders);
        tv_error = root.findViewById(R.id.tv_error_my_orders);

        toolbar = root.findViewById(R.id.toolbar_my_orders);
        toolbar.setTitle("Supermarket");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Տվյալների բեռնում");
        progressDialog.show();

        db = new DatabaseHelperContacts(getContext());
        recyclerView = root.findViewById(R.id.recyclerview_my_orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        listItems = new ArrayList<>();
        loadRecyclerViewData();

        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.empty, menu);
        super.onCreateOptionsMenu(menu, inflater);
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
                                String name = productsJsonObject.getString("name");
                                if(name.equals(user_name)) {
                                    String price = productsJsonObject.getString("price");
                                    String datetime = productsJsonObject.getString("datetime");
                                    ListItem item = new ListItem(price,datetime,1);
                                    listItems.add(item);
                                }
                            }
                            myOrdersAdapter = new MyOrdersAdapter(getContext(), listItems);
                            myOrdersAdapter.setOnItemClickListener(MyOrdersFragment.this);
                            recyclerView.setAdapter(myOrdersAdapter);
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
        if(mContext != null) {
            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            requestQueue.add(stringRequest);
        }
    }

    @Override
    public void onItemClick(int position) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(listItems);
        Intent intent = new Intent(getActivity(), MyOrdersShowActivity.class);
        intent.putExtra("position",String.valueOf(position));
        intent.putExtra("list", jsonString);
        startActivity(intent);
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
