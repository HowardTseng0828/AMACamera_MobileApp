package com.example.Mobile.maps;

import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class FetchData extends AsyncTask<Object, String, String> {

    private String googleNearbyPlacesData;
    private GoogleMap mMap;
    private String Url;


    @Override
    protected void onPostExecute(String s) {

        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                JSONObject getLocation = jsonObject1.getJSONObject("geometry")
                        .getJSONObject("location");

                String lat = getLocation.getString("lat");
                String lng = getLocation.getString("lng");

                JSONObject getData = jsonArray.getJSONObject(i);
                String name = getData.getString("name");

                LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title(name);
                markerOptions.position(latLng);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                mMap.addMarker(markerOptions);


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected String doInBackground(Object... objects) {
        try {
            mMap = (GoogleMap) objects[0];
            Url = (String) objects[1];
            DownloadUrl downloadUrl = new DownloadUrl();
            googleNearbyPlacesData = downloadUrl.retrieveUrl(Url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return googleNearbyPlacesData;
    }
}
