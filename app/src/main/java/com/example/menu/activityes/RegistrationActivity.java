package com.example.menu.activityes;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.menu.R;
import com.example.menu.constructors.Contact;
import com.example.menu.databases.DatabaseHelperContacts;
import com.example.menu.retrofits.RetrofitClientAddAccount;
import com.example.menu.services.APIServiceAddAccount;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class RegistrationActivity extends AppCompatActivity {
    Toolbar toolbar;
    static EditText ed_pass;
    EditText ed_name,ed_pass2;
    CheckBox ch_pass,ch_pass2;
    Button btn_sb;
    static String name, password,v = "";
    static int b;
    String password2;
    private static final String URL_DATA = "https://menuproject.000webhostapp.com/api/post/ReadContacts.php";
    DatabaseHelperContacts db = new DatabaseHelperContacts(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        toolbar = findViewById(R.id.toolbar_registration);
        toolbar.setTitle("Supermarket");
        setSupportActionBar(toolbar);

        ed_pass = findViewById(R.id.ed_pass_rg);
        ed_name = findViewById(R.id.ed_name_rg);
        ch_pass = findViewById(R.id.checkbox_pass);
        ed_pass2 = findViewById(R.id.ed_pass2_rg);
        ch_pass2 = findViewById(R.id.checkbox_pass2);
        btn_sb = findViewById(R.id.submit);

        ch_pass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    ed_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                else
                    ed_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });
        ch_pass2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    ed_pass2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                else
                    ed_pass2.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

        btn_sb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = ed_name.getText().toString();
                password = ed_pass.getText().toString();
                password2 = ed_pass2.getText().toString();
                if (!name.equals("") && name.length() >= 6) {
                    char a = name.charAt(0);
                    if(!String.valueOf(a).equals(" ")) {
                        if (!password.equals("") && password.length() >= 8) {
                            char x = password.charAt(0);
                            if(!String.valueOf(x).equals(" ")) {
                                if (password.equals(password2)) {
                                    final ProgressDialog progressDialog = new ProgressDialog(RegistrationActivity.this);
                                    progressDialog.setMessage("Տվյալների բեռնում");
                                    progressDialog.show();

                                    StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String s) {
                                                    progressDialog.dismiss();
                                                    try {
                                                        JSONObject jsonObject = new JSONObject(s);
                                                        JSONArray array = jsonObject.getJSONArray("data");
                                                        b = 1;
                                                        for (int i = 0; i < array.length(); i++) {
                                                            JSONObject o = array.getJSONObject(i);
                                                            String name1 = o.getString("name");
                                                            if (!name1.equals(name)) {
                                                                b = 2;
                                                            } else {
                                                                Toast.makeText(getApplicationContext(), "Օգտանունը օգտագործված է",
                                                                        Toast.LENGTH_SHORT).show();
                                                                return;
                                                            }
                                                        }
                                                        if (b == 2) {
                                                            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) {
                                                                Toast.makeText(getApplicationContext(), "hasav", Toast.LENGTH_SHORT).show();
                                                                sendPost(name, password);
                                                                db.addContact(new Contact(name,password));
                                                            }
                                                            Toast.makeText(getApplicationContext(),
                                                                    "Դուք հաջողությամբ գրանցվեցիք", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(
                                                                    RegistrationActivity.this, BottomNavigationActivity.class);
                                                            startActivity(intent);
                                                            finish();
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
                                                    Toast.makeText(getApplicationContext(), "Կապի խափանում",
                                                            Toast.LENGTH_SHORT).show();

                                                }
                                            });

                                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                    requestQueue.add(stringRequest);

                                } else {
                                    Toast.makeText(getApplicationContext(), "ՈՒղղել գաղտնաբառի կրկնությունը", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(getApplicationContext(), "Գաղտնաբառը չի կարող սկսվել բացատ նշանով", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Գաղտնաբառը նվազագույնը 8 նիշ", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Օգտանունը չի կարող սկսվել բացատ նշանով", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Օգտանունը նվազագույնը 6 նիշ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void sendPost(String name, String password) {
        Retrofit client = RetrofitClientAddAccount.getClient();
        APIServiceAddAccount mAPIService = client.create(APIServiceAddAccount.class);
        mAPIService.savePost(name, password).enqueue(new Callback<ResponseBody>() {
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
