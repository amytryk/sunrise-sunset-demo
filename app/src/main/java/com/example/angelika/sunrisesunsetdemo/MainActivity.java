package com.example.angelika.sunrisesunsetdemo;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MainActivity extends AbstractWeatherActivity {
  private EditText cityName;

  public void findCurrentLocation(View view) {
    Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
    startActivity(intent);
  }

  public void findCity(View view) {
    try {
      List<Address> searchResult = geocoder.getFromLocationName(cityName.getText().toString(), 1);

      if (searchResult != null && !searchResult.isEmpty()) {
        Address address = searchResult.get(0);
        Log.i("Address ", address.toString());

        DownloadWeatherTask task = new DownloadWeatherTask();
        task.execute(address);
      } else {
        Toast.makeText(getApplicationContext(), "Could not find a city", Toast.LENGTH_LONG).show();
      }
    } catch (IOException e) {
      Toast.makeText(getApplicationContext(), "Network is unavailable.", Toast.LENGTH_LONG).show();
      Log.e("Error", "Network is unavailable.", e);
    }

    InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    manager.hideSoftInputFromWindow(cityName.getWindowToken(), 0);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    cityName = findViewById(R.id.cityName);
    textResult = findViewById(R.id.resultTextView);
  }

}
