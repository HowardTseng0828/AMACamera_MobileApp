package com.example.Mobile.activitys;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.Mobile.R;
import com.example.Mobile.fragments.HomeFragment;
import com.example.Mobile.fragments.MapsFragment;
import com.example.Mobile.fragments.ProfileFragment;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class MenuActivity extends AppCompatActivity {

    private SmoothBottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        redirect(new HomeFragment());
        bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelect(int i) {
                switch (i) {
                    case 0:
                        redirect(new HomeFragment());
                        break;
                    case 1:
                        redirect(new MapsFragment());
                        break;
                    case 2:
                        redirect(new ProfileFragment());
                        break;
                }
            }
        });
    }

    private void redirect(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, fragment)
                .commit();
    }
}
