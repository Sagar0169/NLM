package com.nlm.services

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*


class LocationService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Define the location callback
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    Log.d("LocationService", "Location Updated: $latitude, $longitude")

                    val intent = Intent("LOCATION_UPDATED")
                    intent.putExtra("latitude", latitude)
                    intent.putExtra("longitude", longitude)
                    sendBroadcast(intent)
                }
            }
        }
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startLocationUpdates()
        return START_STICKY
    }

    private fun startLocationUpdates() {
        // Check for permissions before requesting location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Request a single location update
            fusedLocationClient.getCurrentLocation(
                LocationRequest.PRIORITY_HIGH_ACCURACY,
                null // CancellationToken is optional
            ).addOnSuccessListener { location ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    Log.d("LocationService", "Single Location Fetched: $latitude, $longitude")

                    // Broadcast the location
                    val intent = Intent("LOCATION_UPDATED")
                    intent.putExtra("latitude", latitude)
                    intent.putExtra("longitude", longitude)
                    sendBroadcast(intent)
                } else {
                    Log.d("LocationService", "Failed to fetch location.")
                }
            }.addOnFailureListener { exception ->
                Log.e("LocationService", "Error fetching location: ${exception.message}")
            }
        } else {
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show()
            stopSelf()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
