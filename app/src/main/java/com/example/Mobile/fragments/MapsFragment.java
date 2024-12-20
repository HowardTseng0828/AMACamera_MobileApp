package com.example.Mobile.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.Mobile.R;
import com.example.Mobile.databinding.FragmentMapsBinding;
import com.example.Mobile.maps.FetchData;
import com.example.Mobile.viewmodels.MapsViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private static final int Request_code = 123;
    private GoogleMap mMap;
    private double lat, lng;
    private MapsViewModel mapsViewModel;
    private FragmentMapsBinding binding;
    private FusedLocationProviderClient fusedLocationProviderClient;

    public MapsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 使用 ViewBinding
        binding = FragmentMapsBinding.inflate(inflater, container, false);
        mapsViewModel = new ViewModelProvider(this).get(MapsViewModel.class);

        // 初始化 FusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());

        // 將 FusedLocationProviderClient 傳遞給 ViewModel
        mapsViewModel.setFusedLocationProviderClient(fusedLocationProviderClient);

        // 觀察地理位置
        mapsViewModel.getCurrentLocation().observe(getViewLifecycleOwner(), latLng -> {
            if (latLng != null) {
                lat = latLng.latitude;
                lng = latLng.longitude;
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            }
        });

        // 設置按鈕點擊事件
        binding.btnHospital.setOnClickListener(view -> {
            mMap.clear();
            StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            stringBuilder.append("location=" + lat + "," + lng);
            stringBuilder.append("&radius=1000");
            stringBuilder.append("&type=hospital");
            stringBuilder.append("&sensor=true");
            stringBuilder.append("&language=zh-TW");
            stringBuilder.append("&key=YOUR_API_KEY");
            String url = stringBuilder.toString();
            Object dataFetch[] = new Object[2];
            dataFetch[0] = mMap;
            dataFetch[1] = url;
            FetchData fetchData = new FetchData();
            fetchData.execute(dataFetch);
            mapsViewModel.getLastLocation();
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.GoogleMap);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        setUpGoogleMap();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Request_code) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setUpGoogleMap();
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, Request_code);
            }
        }
    }

    private void setUpGoogleMap() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, Request_code);
            return;
        }

        mMap.setMyLocationEnabled(true);
        mapsViewModel.startLocationUpdates();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 清理 ViewBinding
        binding = null;
    }
}
