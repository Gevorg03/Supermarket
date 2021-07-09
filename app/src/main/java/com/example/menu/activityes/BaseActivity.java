package com.example.menu.activityes;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.menu.R;
import com.example.menu.constructors.Contact;
import com.example.menu.constructors.Product;
import com.example.menu.databases.DatabaseHelperContacts;
import com.example.menu.databases.DatabaseHelperProducts;

import java.util.List;

public class BaseActivity extends AppCompatActivity {
    Toolbar toolbar;
    String text = "";
    TextView tv_baza;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        toolbar = findViewById(R.id.toolbar_base);
        toolbar.setTitle("Supermarket");
        setSupportActionBar(toolbar);

        tv_baza = findViewById(R.id.tv_base);

        DatabaseHelperContacts db = new DatabaseHelperContacts(this);
        List<Contact> contacts = db.getAllContacts();
        for (Contact c : contacts) {
            String log = "ID:" + c.getId() + ", NAME: " + c.getName() +
                    ", PASSWORD: " + c.getPassword() + "\n";
            text += log;
        }
        tv_baza.setText(text);

        /*DatabaseHelperProducts db = new DatabaseHelperProducts(this);
        List<Product> products = db.getAllProducts();
        for (Product p : products) {
            String log = "ID:" + p.getId() + ", NAME: " + p.getName() +
                    ", COUNT: " + p.getCount() +"\n";
            text += log;
        }
        tv_baza.setText(text);*/
    }
}
