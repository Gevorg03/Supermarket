package com.example.menu.activityes.ui;

import android.Manifest;
import android.accounts.Account;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
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
import com.example.menu.activityes.MenuActivity;
import com.example.menu.adapters.HomeAdapter;
import com.example.menu.constructors.ListItem;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements HomeAdapter.OnItemClickListener,
        GoogleApiClient.OnConnectionFailedListener {
    Toolbar toolbar;
    private static final String URL_DATA_TYPE = "https://menuproject.000webhostapp.com/api/post/ReadProductsType.php";
    private static final int REQUEST_CALL = 1;
    RecyclerView recyclerView;
    static HomeAdapter homeAdapter;
    private static List<ListItem> listItems;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;
    private Context mContext;
    ProgressDialog progressDialog;
    ImageView img_not_wifi;
    TextView tv_toolbar_text,tv_error;
    Button btn_call;

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
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        img_not_wifi = root.findViewById(R.id.img_not_wifi_home);
        tv_error = root.findViewById(R.id.tv_error_home);
        tv_toolbar_text = root.findViewById(R.id.tv_toolbar_text);
        btn_call = root.findViewById(R.id.btn_call);

        toolbar = root.findViewById(R.id.toolbar_home);
        tv_toolbar_text.setText("Supermarket");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Տվյալների բեռնում");
        progressDialog.show();

        recyclerView = root.findViewById(R.id.recyclerview_home);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        listItems = new ArrayList<>();
        loadRecyclerViewData();

        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                requestEmail().build();
        try {
            googleApiClient = new GoogleApiClient.Builder(getContext())
                    .enableAutoManage(getActivity(), this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        } catch (IllegalStateException exc) {
            //Toast.makeText(getContext(),exc.getMessage(),Toast.LENGTH_SHORT).show();
        }

        setHasOptionsMenu(true);
        return root;
    }

    private void makePhoneCall(){
        String number = "+37477544727";

        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) !=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CALL_PHONE},
                    REQUEST_CALL);
        }else{
            String dial = "tel:" + number;
            startActivity(new Intent(Intent.ACTION_CALL,Uri.parse(dial)));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CALL){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                makePhoneCall();
            }else{
                //Toast.makeText(getContext(),"Permission DENIED",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();

            Account account1 = account.getAccount();
            String a = String.valueOf(account1);
            /*Toast.makeText(getContext(),account.getDisplayName(),Toast.LENGTH_SHORT).show();
            Toast.makeText(getContext(),a,Toast.LENGTH_SHORT).show();*/
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi
                    .silentSignIn(googleApiClient);

            if (opr.isDone()) {
                GoogleSignInResult result = opr.get();
                handleSignInResult(result);
            } else {
                opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                    @Override
                    public void onResult(@NonNull GoogleSignInResult result) {
                        handleSignInResult(result);
                    }
                });
            }
        } catch (Exception exc) {}
    }

    private void loadRecyclerViewData() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA_TYPE,
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
                                String description = productsJsonObject.getString("description");
                                String img = "https://menuproject.000webhostapp.com/uploads/" + productsJsonObject.getString("img");
                                ListItem item = new ListItem(description, img,1.0);
                                listItems.add(item);
                            }
                            homeAdapter = new HomeAdapter(getContext(), listItems);
                            homeAdapter.setOnItemClickListener(HomeFragment.this);
                            recyclerView.setAdapter(homeAdapter);

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
        if (mContext != null) {
            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            requestQueue.add(stringRequest);
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), MenuActivity.class);
        ListItem listItem = listItems.get(position);
        String description = listItem.getDescription();
        intent.putExtra("description", description);
        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    void refresh(int milliseconds){
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
