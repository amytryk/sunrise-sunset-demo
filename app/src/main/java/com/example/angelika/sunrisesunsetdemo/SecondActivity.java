package com.example.angelika.sunrisesunsetdemo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class SecondActivity extends AbstractWeatherActivity {
  private LocationManager locationManager;
  private LocationListener locationListener;

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
      if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
      }
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_second);

    textResult = findViewById(R.id.resultText);
    Button backToSearch = findViewById(R.id.backToSearch);

    backToSearch.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
      }
    });

    locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    locationListener = new LocationListener() {
      @Override
      public void onLocationChanged(Location location) {

        try {
          List<Address> searchResult = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

          if (searchResult != null && !searchResult.isEmpty()) {
            Address currentLocation = searchResult.get(0);
            Log.i("Place info", currentLocation.toString());

            DownloadWeatherTask task = new DownloadWeatherTask();
            task.execute(currentLocation);
          } else {
            Toast.makeText(getApplicationContext(), "Could not find a city", Toast.LENGTH_LONG).show();
          }
        } catch (IOException e) {
          Toast.makeText(getApplicationContext(), "Network is unavailable.", Toast.LENGTH_LONG).show();
          Log.e("Error", "Network is unavailable.", e);
        }
      }

      @Override
      public void onStatusChanged(String provider, int status, Bundle extras) {

      }

      @Override
      public void onProviderEnabled(String provider) {

      }

      @Override
      public void onProviderDisabled(String provider) {

      }
    };

    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    } else {
      locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

  }


}

