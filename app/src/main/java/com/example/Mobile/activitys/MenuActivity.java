package com.example.Mobile.activitys;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.Mobile.R;
import com.example.Mobile.viewmodels.MenuViewModel;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class MenuActivity extends AppCompatActivity {

    private SmoothBottomBar bottomBar;
    private MenuViewModel menuViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);

        menuViewModel.getSelectedFragment().observe(this, this::redirect);

        bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelect(int i) {
                menuViewModel.onItemSelected(i);
            }
        });
    }

    private void redirect(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, fragment)
                .commit();
    }
}
