package com.doby.maps

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.internal.ViewUtils.dpToPx
import java.io.IOException

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var searchView: SearchView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchView = findViewById(R.id.sv_location)
        mapFragment = SupportMapFragment.newInstance()
        supportFragmentManager.beginTransaction().add(R.id.myMap, mapFragment).commit()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                var location: String? = searchView.query.toString()

                var addressList: List<Address>? = null

                if (location != null || !location.equals("")) {
                    var geocoder: Geocoder = Geocoder(this@MainActivity)
                    try {
                        addressList = geocoder.getFromLocationName(location, 1)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }

                var address = addressList?.get(0)
                var latLng = LatLng(address!!.latitude, address.longitude)
                map.addMarker(MarkerOptions().position(latLng).title(location))
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                TODO("Not yet implemented")
            }

        })
        mapFragment.getMapAsync(this)
        //fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this)
        //fetchLocation()
    }

    /*private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), persmissionCode
            )
            return
        }
        val task = fusedLocationProvider.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = location
                Toast.makeText(
                    this,
                    currentLocation.latitude.toString() + " " + currentLocation.longitude.toString(),
                    Toast.LENGTH_SHORT
                ).show()

                val supportMapFragment = (supportFragmentManager.findFragmentById(R.id.myMap) as
                        SupportMapFragment)!!
                supportMapFragment.getMapAsync(this)
            }
        }
    }*/


    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.setPadding(0, 1900, 0, 0)

        googleMap.uiSettings.isCompassEnabled = true
        googleMap.uiSettings.isZoomGesturesEnabled = true
        googleMap.uiSettings.isScrollGesturesEnabled = true
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        googleMap.isMyLocationEnabled = true
        googleMap.setOnMapClickListener {
            googleMap.clear()
            googleMap.addMarker(MarkerOptions().position(it))
        }
    }

    /*override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            persmissionCode -> if (grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation()
            }
        }
    }*/
}