package com.example.pokemonandroid

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.hussein.pockemonandroid.Pockemon

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        checkPermission()
        LoadPockemon()
    }

        var Accesslocation = 123
        fun checkPermission() {
            if(Build.VERSION.SDK_INT>=23){
                if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)
                !=PackageManager.PERMISSION_GRANTED){
                    requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),Accesslocation)
                    return
                }
            }
            GetUserLocation()
        }
        @SuppressLint("MissingPermission")
        fun GetUserLocation(){
            Toast.makeText(this, "User Location access on",Toast.LENGTH_LONG).show()

            var myLocation= MyocationListener()
            var locationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3,3f,myLocation)
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3,3f,myLocation)

            var mythread=myThread()
            mythread.start()
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) { Accesslocation ->
            {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    GetUserLocation()
                } else {
                    Toast.makeText(this, "de cannont to your location", Toast.LENGTH_LONG).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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

        // Add a marker in Sydney and move the camera
        val mario = LatLng(18.85, -97.1)
        mMap.addMarker(MarkerOptions().position(mario).title("Mario").snippet("Esta es mi ubicacion")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mario)))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mario))

        val  bulbasaur = LatLng(20.85, -100.1)
        mMap.addMarker(MarkerOptions().position(bulbasaur).title("bulbasaur").snippet("Esta es mi ubicacion")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.bulbasaur)))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(bulbasaur))

        val  squirtle = LatLng(19.85, -99.1)
        mMap.addMarker(MarkerOptions().position(squirtle).title("squirtle").snippet("Esta es mi ubicacion")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.squirtle)))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(squirtle))

        val  charmander = LatLng(17.0, -96.1)
        mMap.addMarker(MarkerOptions().position(charmander).title("charmander").snippet("Esta es mi ubicacion")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.charmander)))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(charmander))
    }
    var location:Location?=null

    inner class MyocationListener:LocationListener {
        var location:Location?=null

        constructor(){
            location = Location("Start")
            location!!.longitude=0.0
            location!!.longitude=0.0
        }

        override fun onLocationChanged(p0: Location?) {
            this.location=p0
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderEnabled(provider: String?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderDisabled(provider: String?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    var oldLocation:Location?=null
    inner class myThread:Thread{

        constructor():super(){
            oldLocation= Location("Start")
            oldLocation!!.longitude=0.0
            oldLocation!!.longitude=0.0
        }

        override fun run(){

            while (true){

                try {

                    if(oldLocation!!.distanceTo(location)==0f){
                        continue
                    }

                    oldLocation=location


                    runOnUiThread {


                        mMap!!.clear()

                        // show me
                        val sydney = LatLng(location!!.latitude, location!!.longitude)
                        mMap!!.addMarker(MarkerOptions()
                            .position(sydney)
                            .title("Me")
                            .snippet(" here is my location")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mario)))
                        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14f))

                        // show pockemons

                        for(i in 0..listPockemons.size-1){

                            var newPockemon=listPockemons[i]

                            if(newPockemon.IsCatch==false){

                                val pockemonLoc = LatLng(newPockemon.location!!.latitude, newPockemon.location!!.longitude)
                                mMap!!.addMarker(MarkerOptions()
                                    .position(pockemonLoc)
                                    .title(newPockemon.name!!)
                                    .snippet(newPockemon.des!! +", power:"+ newPockemon!!.power)
                                    .icon(BitmapDescriptorFactory.fromResource(newPockemon.image!!)))


                                if (location!!.distanceTo(newPockemon.location)<2){
                                    newPockemon.IsCatch=true
                                    listPockemons[i]=newPockemon
                                    playerPower+=newPockemon.power!!
                                    Toast.makeText(applicationContext,
                                        "You catch new pockemon your new pwoer is " + playerPower,
                                        Toast.LENGTH_LONG).show()

                                }

                            }
                        }





                    }

                    Thread.sleep(1000)

                }catch (ex:Exception){}


            }

        }

    }


    var playerPower=0.0
    var listPockemons=ArrayList<Pockemon>()

    fun  LoadPockemon(){


        listPockemons.add(Pockemon(R.drawable.charmander,
            "Charmander", "Charmander living in japan", 55.0, 37.7789994893035, -122.401846647263))
        listPockemons.add(Pockemon(R.drawable.bulbasaur,
            "Bulbasaur", "Bulbasaur living in usa", 90.5, 37.7949568502667, -122.410494089127))
        listPockemons.add(Pockemon(R.drawable.squirtle,
            "Squirtle", "Squirtle living in iraq", 33.5, 37.7816621152613, -122.41225361824))

    }

}
