package com.example.menu.activityes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

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
import com.example.menu.databases.DatabaseHelperContacts;
import com.example.menu.retrofits.RetrofitClientAddAddress;
import com.example.menu.services.APIServiceAddAddress;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class AddressAddCartActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText ed_address,ed_phone;
    ImageView img_not_wifi;
    TextView tv_error;
    DatabaseHelperContacts db = new DatabaseHelperContacts(this);
    private static final String URL_DATA = "https://menuproject.000webhostapp.com/api/post/ReadAddress.php";
    int a = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        toolbar = findViewById(R.id.toolbar_add_address);
        toolbar.setTitle("Supermarket");
        setSupportActionBar(toolbar);

        img_not_wifi = findViewById(R.id.img_not_wifi_add_address);
        tv_error = findViewById(R.id.tv_error_add_address);
        ed_address = findViewById(R.id.ed_add_address_address);
        ed_phone = findViewById(R.id.ed_add_address_phone);
        ed_phone.setText("+374");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ok, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_ok: {
                a = 0;
                final String name = db.getName(1);
                final String address = ed_address.getText().toString();
                final String phone = ed_phone.getText().toString();
                if(!name.equals("")){
                    if(!address.equals("")){
                        char b = address.charAt(0);
                        if(!String.valueOf(b).equals(" ")){
                            if(!phone.equals("")) {
                                if(phone.length() == 12) {
                                    if (String.valueOf(phone.charAt(0)).equals("+") &&
                                            String.valueOf(phone.charAt(1)).equals("3") &&
                                            String.valueOf(phone.charAt(2)).equals("7") &&
                                            String.valueOf(phone.charAt(3)).equals("4")) {
                                        final ProgressDialog progressDialog = new ProgressDialog(this);
                                        progressDialog.setMessage("Տվյալների բեռնում");
                                        progressDialog.show();
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
                                                            for (int i = 0; i < array.length(); i++) {
                                                                JSONObject productsJsonObject = array.getJSONObject(i);
                                                                String name = productsJsonObject.getString("name");
                                                                String address1 = productsJsonObject.getString("address");
                                                                String phone1 = productsJsonObject.getString("phone");
                                                                if (address.equals(address1) && phone.equals(phone1) && name.equals(db.getName(1))) {
                                                                    a = 1;
                                                                }
                                                            }
                                                            if (a != 1) {
                                                                sendPostAddress(name, address, phone);
                                                                Intent intent = new Intent(AddressAddCartActivity.this, AddressCartActivity.class);
                                                                startActivity(intent);
                                                                finish();
                                                            } else {
                                                                Toast.makeText(getApplicationContext(), "Տվյալներն արդեն առկա են", Toast.LENGTH_SHORT).show();
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
                                                        tv_error.setText("Կապի խափանում");
                                                        img_not_wifi.setImageResource(R.drawable.ic_not_wifi);
                                                    }
                                                });
                                        RequestQueue requestQueue = Volley.newRequestQueue(this);
                                        requestQueue.add(stringRequest);
                                    }else {
                                        Toast.makeText(getApplicationContext(), "Հեռախոսահամարը պետք է սկսվի +374-ով", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(getApplicationContext(), "ՈՒղղել հեռախոսահամարը", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(getApplicationContext(), "Լրացնել հեռախոսահամարը", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(),"Հասցեն չի կարող սկսվել բացատ նշանով",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"Լրացնել հասցեն",Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void sendPostAddress(String name,String address,String phone) {
        Retrofit client = RetrofitClientAddAddress.getClient();
        APIServiceAddAddress mAPIService = client.create(APIServiceAddAddress.class);
        mAPIService.savePost(name,address,phone).enqueue(new Callback<ResponseBody>() {
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
}
