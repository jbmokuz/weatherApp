package com.example.weatherapp;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class Networking {

    private URL url = null;
    public String title = "";
    public int woeid = 0;
    HttpsURLConnection connection = null;

    public Networking() {

    }

    public String getJson(URL url) throws IOException, JSONException {

        // From https://developer.android.com/training/basics/network-ops/connecting

        connection = (HttpsURLConnection) url.openConnection();

        // Timeout for reading InputStream arbitrarily set to 3000ms.
        connection.setReadTimeout(3000);

        // Timeout for connection.connect() arbitrarily set to 3000ms.
        connection.setConnectTimeout(3000);

        // For this use case, set HTTP method to GET.
        connection.setRequestMethod("GET");

        // Already true by default but setting just in case; needs to be true since this request
        // is carrying an input (response) body.
        connection.setDoInput(true);

        // Open communications link (network traffic occurs here).
        connection.connect();

        // @TODO check incase we dont have connection to the server or something
        int responseCode = connection.getResponseCode();

        InputStream stream = connection.getInputStream();

        Scanner scan = new Scanner(stream);
        StringBuilder sb = new StringBuilder();
        while(scan.hasNext()){
            sb.append(scan.next());
        }

        connection.disconnect();

        return sb.toString();

    }

    public int updateLocation(double latitude, double longitude) throws IOException, JSONException {

        Uri uri = Uri.parse("https://www.metaweather.com/api/location/search").buildUpon().appendQueryParameter("lattlong",String.valueOf(latitude)+","+String.valueOf(longitude)).build();
        url = new URL(uri.toString());

        JSONArray arr = new JSONArray(getJson(url));
        JSONObject json = (JSONObject) arr.get(0);

        System.out.println(json.toString());

        title = (String) json.get("title");
        woeid = (int) json.get("woeid");

        return woeid;

    }

    public void updateWeather(int woeid) throws IOException, JSONException {

        url = new URL("https://www.metaweather.com/api/location/"+String.valueOf(woeid));

        JSONObject json = new JSONObject(getJson(url));

        System.out.println(json.toString());

    }

}