package com.codingwithme.notesapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.codingwithme.notesapp.Model.APIResponse
import com.codingwithme.notesapp.Remote.Api
import com.google.android.gms.location.*
import android.content.SharedPreferences
import android.view.MenuItem
import com.codingwithme.notesapp.Common.Common
import com.codingwithme.notesapp.Model.User
import com.codingwithme.notesapp.Model.modelReport
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.report.*
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    internal lateinit var mService: Api
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var mMap: GoogleMap
    private val LOCATION_PERMISSION_REQUEST = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    lateinit var myMarkers: MutableList<Marker>
    lateinit var otherMarkers : MutableList<Marker>
    lateinit var identificacao : String

    private fun getLocationAccess() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
            getLocationUpdates()
            startLocationUpdates()

        }
        else
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST)
    }


    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {

                mMap.isMyLocationEnabled = true

            }
            else {
                Toast.makeText(this, "O utilizador n??o permitiu o acesso a sua localiza????o", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        identificacao= intent.getStringExtra("id").toString()
        val Reportar = findViewById<FloatingActionButton>(R.id.fab2)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mService = Common.api

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        Reportar.setOnClickListener{

            val intent6 = Intent(this@MapsActivity, report::class.java)
            intent6.putExtra("id", identificacao)
            Log.d("ze", identificacao)
            startActivity(intent6)

        }

        val sair = findViewById<Button>(R.id.logout)

        sair.setOnClickListener {
            val sharedPref : SharedPreferences = getSharedPreferences(getString(R.string.preference_key), Context.MODE_PRIVATE)
            with(sharedPref.edit()){
                clear()
                commit()
                apply()
                val intent = Intent(this@MapsActivity, login::class.java)
                startActivity(intent)
                finishAffinity()
            }
        }
    }



    private fun getLocationUpdates() {
        locationRequest = LocationRequest()
        locationRequest.interval = 30000
        locationRequest.fastestInterval = 20000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (locationResult.locations.isNotEmpty()) {
                    val location = locationResult.lastLocation
                    //val id = sharedPreferences.getString(getString(R.string.id_user), "0")
                    if (location != null) {

                        val latLng = LatLng(location.latitude, location.longitude)
                        val markerOptions = MarkerOptions().position(latLng)
                        mMap.addMarker(markerOptions)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                        val address = getAddress(location.latitude, location.longitude)
                        val sharedpreferences = getSharedPreferences("localizacao", Context.MODE_PRIVATE).edit()
                        sharedpreferences.putString("id", R.string.id_user.toString())
                        sharedpreferences.putString("latitude", location.latitude.toString())
                        sharedpreferences.putString("longitude", location.longitude.toString())
                        sharedpreferences.putString("morada", address)
                        sharedpreferences.apply()

                    }
                }
            }
        }
    }


    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    override fun onMarkerClick(p0: Marker?) = false

    private fun getAddress(lat: Double, lng: Double): String{
        val geocoder = Geocoder(this)
        val list = geocoder.getFromLocation(lat,lng,1)
        return list[0].getAddressLine(0)
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        getLocationAccess()
        mMap.setOnMarkerClickListener(this)




        mMap.setOnInfoWindowClickListener { marker ->
            if (marker.isInfoWindowShown) {
                marker.hideInfoWindow()
            } else {
                marker.showInfoWindow(  )
            }
            true
        }



        val call = mService.pontosMapa()
        call.enqueue(object: retrofit2.Callback<List<modelReport>> {
            override fun onFailure(call: Call<List<modelReport>>, t: Throwable) {
                Toast.makeText(this@MapsActivity,"Falhou!", Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(call: Call<List<modelReport>>, response: Response<List<modelReport>>) {

                response.body()!!.forEach {
                    val latLng = LatLng(it.latitude.toDouble(), it.longitude.toDouble())
                    val markerOptions = MarkerOptions().position(latLng)
                    mMap.addMarker(markerOptions)
                }

            }

        })




    }



}