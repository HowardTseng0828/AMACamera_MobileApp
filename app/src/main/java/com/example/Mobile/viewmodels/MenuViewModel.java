package com.example.Mobile.viewmodels;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.Mobile.fragments.HomeFragment;
import com.example.Mobile.fragments.MapsFragment;
import com.example.Mobile.fragments.ProfileFragment;

public class MenuViewModel extends ViewModel {

    private final MutableLiveData<Fragment> selectedFragment = new MutableLiveData<>();

    public MenuViewModel() {
        selectedFragment.setValue(new HomeFragment());
    }

    public LiveData<Fragment> getSelectedFragment() {
        return selectedFragment;
    }

    public void onItemSelected(int index) {
        switch (index) {
            case 0:
                selectedFragment.setValue(new HomeFragment());
                break;
            case 1:
                selectedFragment.setValue(new MapsFragment());
                break;
            case 2:
                selectedFragment.setValue(new ProfileFragment());
                break;
        }
    }
}
