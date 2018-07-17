package com.example.angelika.sunrisesunsetdemo;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class AbstractWeatherActivity extends AppCompatActivity {
  protected Geocoder geocoder;
  protected TextView textResult;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    geocoder = new Geocoder(getApplicationContext());
  }

  public class DownloadWeatherTask extends AsyncTask<Address, Void, String> {
    private Address address;

    @Override
    protected String doInBackground(Address... addresses) {
      address = addresses[0];

      String result = "";

      try {
        URL url = new URL("https://api.sunrise-sunset.org/json?lat=" + address.getLatitude() + "&lng=" + address.getLongitude() + "&formatted=0");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        InputStream inputStream = urlConnection.getInputStream();
        InputStreamReader reader = new InputStreamReader(inputStream);
        int data = reader.read();

        while (data != -1) {
          char current = (char) data;
          result += current;
          data = reader.read();
        }

        return result;
      } catch (Exception e) {
        Toast.makeText(getApplicationContext(), "Something going wrong.", Toast.LENGTH_LONG).show();
      }

      return null;
    }

    @Override
    protected void onPostExecute(String result) {
      super.onPostExecute(result);
      try {
        String message = "";
        JSONObject jsonObject = new JSONObject(result);
        JSONObject jsonResult = jsonObject.getJSONObject("results");

        String sunrise = jsonResult.getString("sunrise");
        String sunset = jsonResult.getString("sunset");

        String city = address.getLocality();
        String country = address.getCountryName();

        if (city == null) {
          city = "";
        }

        if (country == null) {
          country = "";
        }

        if (sunrise != null && sunset != null) {
          message = city + "\r\n" + country + "\r\n" + "Sunrise " + sunrise + "\r\n" + "Sunset  " + sunset;
        }

        if (!message.isEmpty()) {
          textResult.setText(message);
        } else {
          Toast.makeText(getApplicationContext(), "Weather is not available for current location.", Toast.LENGTH_LONG).show();
        }
      } catch (JSONException e) {
        Toast.makeText(getApplicationContext(), "Weather is not available for current location.", Toast.LENGTH_LONG).show();
      }
    }
  }
}
