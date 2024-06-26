package com.example.Mobile.maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadUrl {

    public String retrieveUrl(String url) throws IOException {
        String urlData = "";
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try {
            URL getUrl = new URL(url);
            httpURLConnection = (HttpURLConnection) getUrl.openConnection();
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();

            String Line = "";

            while ((Line = bufferedReader.readLine()) != null) {

                stringBuffer.append(Line);
            }

            urlData = stringBuffer.toString();
            bufferedReader.close();
        } catch (Exception e) {
        } finally {
            inputStream.close();
            httpURLConnection.disconnect();
        }
        return urlData;
    }
}
