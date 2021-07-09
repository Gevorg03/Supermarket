package com.example.menu.activityes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.menu.R;
import com.example.menu.activityes.ui.AccountFragment;
import com.example.menu.constructors.Contact;
import com.example.menu.databases.DatabaseHelperContacts;
import com.example.menu.retrofits.RetrofitClientAddAccount;
import com.example.menu.services.APIServiceAddAccount;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    Toolbar toolbar;
    DatabaseHelperContacts db = new DatabaseHelperContacts(this);
    Button btn_log,btn_reg;
    EditText ed_name;
    int a = 1,v=0,c=0,m = 0;
    long id;
    static String email,name,pass;
    SignInButton signInButton;
    private GoogleApiClient googleApiClient;
    private static final int SIGN_IN = 1;
    private static String fullName;
    CallbackManager callbackManager;
    static EditText ed_pass;
    CheckBox ch_pass;
    private static final String URL_DATA = "https://menuproject.000webhostapp.com/api/post/ReadContacts.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar_main);
        toolbar.setTitle("Supermarket");
        setSupportActionBar(toolbar);

        btn_log = findViewById(R.id.log_btn);
        btn_reg = findViewById(R.id.btn_reg);
        ed_name = findViewById(R.id.ed_name_login);
        ed_pass = findViewById(R.id.ed_pass_login);
        ch_pass = findViewById(R.id.checkbox_pass_login);
        signInButton = findViewById(R.id.google_btn);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,SIGN_IN);
            }
        });

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handelFacebookToken(loginResult.getAccessToken());
                if(fullName != null && !fullName.equals("")) {
                    db.addContact(new Contact(1,fullName));

                    /*final ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
                    progressDialog.setMessage("Տվյալների բեռնում");
                    progressDialog.show();*/
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    //progressDialog.dismiss();
                                    try {
                                        JSONObject jsonObject = new JSONObject(s);
                                        JSONArray array = jsonObject.getJSONArray("data");

                                        for(int i = 0; i< array.length(); i++){
                                            JSONObject o = array.getJSONObject(i);
                                            String name = o.getString("name");

                                            if(name.equals(fullName)){
                                                m = 1;
                                            }
                                        }

                                        if(m!=1){
                                            sendPost(fullName,"null");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    //progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(),"Կապի խափանում",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);

                    startActivity(new Intent(MainActivity.this, BottomNavigationActivity.class));
                    finish();
                }
            }

            @Override
            public void onCancel() {
                //Toast.makeText(getApplicationContext(),"Canceled",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                //Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        ch_pass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    ed_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                else
                    ed_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });


        btn_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadRecyclerViewData();
            }
        });

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,RegistrationActivity.class));
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

    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();

            String email1 = account.getEmail();
            String name1 = account.getDisplayName();
            if(email1 != null)
                email = email1;
            else
                email = name1;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi
                .silentSignIn(googleApiClient);

        if(opr.isDone()){
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        }else{
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult result) {
                    handleSignInResult(result);
                }
            });
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi
                .silentSignIn(googleApiClient);

        if(opr.isDone()){
            //Toast.makeText(getApplicationContext(),"if",Toast.LENGTH_SHORT).show();
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        }else{
            //Toast.makeText(getApplicationContext(),"else",Toast.LENGTH_SHORT).show();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult result) {
                    handleSignInResult(result);
                }
            });
        }

        if(requestCode == SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if(result.isSuccess()){
                db.addContact(new Contact(email));
                /*final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Տվյալների բեռնում");
                progressDialog.show();*/
                StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                //progressDialog.dismiss();
                                try {
                                    JSONObject jsonObject = new JSONObject(s);
                                    JSONArray array = jsonObject.getJSONArray("data");

                                    for(int i = 0; i< array.length(); i++){
                                        JSONObject o = array.getJSONObject(i);
                                        String name = o.getString("name");

                                        if(name.equals(email)){
                                            v = 1;
                                        }
                                    }
                                    if(v!=1){
                                        sendPost(email,"null");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                //progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"Կապի խափանում",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);

                startActivity(new Intent(MainActivity.this,BottomNavigationActivity.class));
                finish();
            }else{
                //Toast.makeText(getApplicationContext(),"Login failed",Toast.LENGTH_SHORT).show();
            }
        }
    }

    GraphRequest request1;

    private void handelFacebookToken(AccessToken accessToken){
        Profile profile = Profile.getCurrentProfile();

        if(profile != null){
            String facebook_id = profile.getId();
            String f_name = profile.getFirstName();
            String m_name = profile.getMiddleName();
            String l_name = profile.getLastName();
            String full_name = profile.getName();
            fullName = full_name;

            request1 = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    try{
                        String email_id = object.getString("email");
                        String gender = object.getString("gender");
                        String profile_name = object.getString("name");
                        long fb_id = object.getLong("id");
                    }catch (JSONException e){ }
                }
            });
            request1.executeAsync();
        }
    }

    private void loadRecyclerViewData(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Տվյալների բեռնում");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        progressDialog.dismiss();
                        name = ed_name.getText().toString();
                        pass = ed_pass.getText().toString();
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray array = jsonObject.getJSONArray("data");

                            for(int i = 0; i< array.length(); i++){
                                JSONObject o = array.getJSONObject(i);
                                String name1 = o.getString("name");
                                String password1 = o.getString("pass");

                                if(name.equals(name1) && password1.equals(pass)){
                                    db.addContact(new Contact(name1,password1));
                                    a = 0;
                                    id = o.getLong("id");
                                }
                            }
                            if(a == 0){
                                Intent intent = new Intent(MainActivity.this,
                                        BottomNavigationActivity.class);
                                String d = String.valueOf(id);
                                intent.putExtra("id",d);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(getApplicationContext(),"Այդպիսի օգտատեր գոյություն չունի",
                                        Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getApplicationContext(),"Կապի խափանում",
                                Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
