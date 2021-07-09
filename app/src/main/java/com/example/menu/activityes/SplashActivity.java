package com.example.menu.activityes;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.example.menu.R;
import com.example.menu.activityes.ui.CartFragment;
import com.example.menu.activityes.ui.HomeFragment;
import com.example.menu.databases.DatabaseHelperContacts;

public class SplashActivity extends AppCompatActivity {
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        toolbar = findViewById(R.id.toolbar_splash);
        toolbar.setTitle("Supermarket");
        setSupportActionBar(toolbar);

        final DatabaseHelperContacts db = new DatabaseHelperContacts(this);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    String name = db.getName(1);

                    if(!name.equals("") && name != null) {
                        /*HomeFragment homeFragment = new HomeFragment();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.splash_activity,homeFragment).commit();
                        finish();*/
                        startActivity(new Intent(SplashActivity.this, BottomNavigationActivity.class));
                        finish();
                    }
                    else{
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }

                    } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
