package com.example.Mobile.viewmodels;

import android.location.Location;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.LatLng;

public class MapsViewModel extends ViewModel {

    private MutableLiveData<LatLng> currentLocation = new MutableLiveData<>();
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    public LiveData<LatLng> getCurrentLocation() {
        return currentLocation;
    }

    public void setFusedLocationProviderClient(FusedLocationProviderClient client) {
        this.fusedLocationProviderClient = client;
        setUpLocationUpdate();
    }

    private void setUpLocationUpdate() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    for (Location location : locationResult.getLocations()) {
                        currentLocation.setValue(new LatLng(location.getLatitude(), location.getLongitude()));
                    }
                }
            }
        };
    }

    public void startLocationUpdates() {
        if (fusedLocationProviderClient != null) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        }
    }

    public void stopLocationUpdates() {
        if (fusedLocationProviderClient != null) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
    }

    public void getLastLocation() {
        if (fusedLocationProviderClient != null) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    currentLocation.setValue(new LatLng(location.getLatitude(), location.getLongitude()));
                }
            });
        }
    }
}
