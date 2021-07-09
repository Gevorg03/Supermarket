package com.example.menu.activityes.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.core.app.ShareCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.menu.R;
import com.example.menu.activityes.AddressMyAddressActivity;
import com.example.menu.activityes.BaseActivity;
import com.example.menu.activityes.MainActivity;
import com.example.menu.activityes.RegistrationActivity;
import com.example.menu.adapters.HomeAdapter;
import com.example.menu.constructors.Contact;
import com.example.menu.constructors.ListItem;
import com.example.menu.databases.DatabaseHelperContacts;
import com.example.menu.retrofits.RetrofitClientDeleteAccount;
import com.example.menu.retrofits.RetrofitClientDeleteAllAddress;
import com.example.menu.retrofits.RetrofitClientDeleteAllProductCart;
import com.example.menu.retrofits.RetrofitClientDeleteMyOrders;
import com.example.menu.retrofits.RetrofitClientDeleteOrder;
import com.example.menu.services.APIServiceDeleteAccount;
import com.example.menu.services.APIServiceDeleteAllAddress;
import com.example.menu.services.APIServiceDeleteAllProductCart;
import com.example.menu.services.APIServiceDeleteMyOrders;
import com.example.menu.services.APIServiceDeleteOrder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class AccountFragment extends Fragment {
    Toolbar toolbar;
    TextView tv_userName,tv_error;
    ImageView img_not_wifi;
    Button btn_address,btn_log_out,btn_delete_account,btn_share;
    String user_name;
    DatabaseHelperContacts db;
    private static final String URL_DATA = "https://menuproject.000webhostapp.com/api/post/ReadContacts.php";
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_account, container, false);

        toolbar = root.findViewById(R.id.toolbar_account);
        toolbar.setTitle("Supermarket");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        //Toast.makeText(getContext(),"maladec",Toast.LENGTH_SHORT).show();
        db = new DatabaseHelperContacts(getContext());
        user_name = db.getName(1);
        img_not_wifi = root.findViewById(R.id.img_not_wifi_account);
        tv_error = root.findViewById(R.id.tv_error_account);
        tv_userName = root.findViewById(R.id.tv_account_userName);
        btn_address = root.findViewById(R.id.btn_address);
        btn_log_out = root.findViewById(R.id.btn_log_out);
        btn_delete_account = root.findViewById(R.id.btn_delete_account);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        tv_error.setText("");
                        img_not_wifi.setImageResource(R.drawable.ic_white);
                        try {
                            tv_userName.setText("Օգտատեր: " + db.getName(1));

                            btn_log_out.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    openDialogLogOut();
                                }
                            });

                            btn_delete_account.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    openDialogDeleteAccount();
                                }
                            });
                            btn_address.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getActivity(), AddressMyAddressActivity.class);
                                    startActivity(intent);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Drawable img = getContext().getResources().getDrawable(R.drawable.ic_white);
                        tv_userName.setText("");
                        tv_userName.setCompoundDrawables(img,null,null,null);
                        btn_address.setVisibility(View.INVISIBLE);
                        btn_log_out.setVisibility(View.INVISIBLE);
                        btn_delete_account.setVisibility(View.INVISIBLE);
                        tv_error.setText("Կապի խափանում");
                        img_not_wifi.setImageResource(R.drawable.ic_not_wifi);
                    }
                });
        if (mContext != null) {
            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            requestQueue.add(stringRequest);
        }


        /*btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT,"Sharing URL");
                intent.putExtra(Intent.EXTRA_TEXT,"http://www.url.com");
                startActivity(Intent.createChooser(intent, "Share URL"));

                ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/plain")
                        .setChooserTitle("Share URL")
                        .setText("http://www.url.com")
                        .startChooser();

            }
        });*/
        return root;
    }

    private void loadRecyclerViewDataLogOut(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray array = jsonObject.getJSONArray("data");
                            user_name = db.getName(1);
                            for(int i = 0; i< array.length(); i++){
                                JSONObject o = array.getJSONObject(i);
                                String name = o.getString("name");
                                String password = o.getString("pass");

                                if(password.equals("null") && name.equals(user_name)){
                                    db.deleteContact(new Contact(1, user_name,"null"));
                                }else if(!password.equals("null") && name.equals(user_name)){
                                    db.deleteContact(new Contact(1, user_name,password));
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
        if(mContext!=null) {
            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            requestQueue.add(stringRequest);
        }
    }

    private void loadRecyclerViewDataDeleteAccount(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray array = jsonObject.getJSONArray("data");

                            for(int i = 0; i< array.length(); i++){
                                JSONObject o = array.getJSONObject(i);
                                String contact_name = o.getString("name");
                                String password = o.getString("pass");

                                if(contact_name.equals(user_name)) {
                                    if (password.equals("null")) {
                                        db.deleteContact(new Contact(1, contact_name, "null"));
                                        deleteContact(contact_name);
                                        deleteOrder(contact_name);
                                        deleteProductCart(contact_name);
                                        deleteProductAllAddress(contact_name);
                                        deleteMyOrders(contact_name);
                                    } else if (!password.equals("null")) {
                                        db.deleteContact(new Contact(1, contact_name, password));
                                        deleteContact(contact_name);
                                        deleteOrder(contact_name);
                                        deleteProductCart(contact_name);
                                        deleteProductAllAddress(contact_name);
                                        deleteMyOrders(contact_name);
                                    }
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
        if(mContext != null) {
            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            requestQueue.add(stringRequest);
        }
    }

    public void deleteContact(String name) {
        Retrofit client = RetrofitClientDeleteAccount.getClient();
        final APIServiceDeleteAccount mAPIService = client.create(APIServiceDeleteAccount.class);
        mAPIService.deletePost(name).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    //Log.i("tag", "post submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Log.e("tag", "Unable to submit post to API.");
            }
        });
    }

    public void deleteOrder(String product_ordering_name) {
        Retrofit client = RetrofitClientDeleteOrder.getClient();
        final APIServiceDeleteOrder mAPIService = client.create(APIServiceDeleteOrder.class);
        mAPIService.deletePost(product_ordering_name).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    //Log.i("tag", "post submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Log.e("tag", "Unable to submit post to API.");
            }
        });
    }

    public void deleteProductCart(String productCart_user_name) {
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

    public void deleteProductAllAddress(String productAddress_name) {
        Retrofit client = RetrofitClientDeleteAllAddress.getClient();
        APIServiceDeleteAllAddress mAPIService = client.create(APIServiceDeleteAllAddress.class);
        mAPIService.deletePost(productAddress_name).enqueue(new Callback<ResponseBody>() {
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

    public void deleteMyOrders(String productMyOrders_name) {
        Retrofit client = RetrofitClientDeleteMyOrders.getClient();
        APIServiceDeleteMyOrders mAPIService = client.create(APIServiceDeleteMyOrders.class);
        mAPIService.deletePost(productMyOrders_name).enqueue(new Callback<ResponseBody>() {
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

    public void openDialogLogOut() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.user_dialog_for_correct, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);
        TextView textView = view.findViewById(R.id.dialog);
        Button btn_ok = view.findViewById(R.id.btn_dialog_ok);
        Button btn_cancel = view.findViewById(R.id.btn_dialog_cancel);
        textView.setText("Դուք վստահ ե՞ք");
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadRecyclerViewDataLogOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
                dialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void openDialogDeleteAccount() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.user_dialog_for_correct, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);
        TextView textView = view.findViewById(R.id.dialog);
        Button btn_ok = view.findViewById(R.id.btn_dialog_ok);
        Button btn_cancel = view.findViewById(R.id.btn_dialog_cancel);
        textView.setText("Դուք վստահ ե՞ք");
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadRecyclerViewDataDeleteAccount();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
                dialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
