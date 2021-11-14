package mapan.prototype.ee.module.adminspv

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import mapan.prototype.ee.R
import mapan.prototype.ee.adapter.AdapterMenu
import mapan.prototype.ee.adapter.MarkerWindowAdapter
import mapan.prototype.ee.config.Constants
import mapan.prototype.ee.databinding.ActivityFormMerchantBinding
import mapan.prototype.ee.databinding.ActivityLocationMerchantBinding
import mapan.prototype.ee.databinding.ActivityPickLocationBinding
import mapan.prototype.ee.model.Place
import mapan.prototype.ee.module.core.BaseActivity
import mapan.prototype.ee.util.PermissionHelper
import mapan.prototype.ee.util.Util
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

@SuppressLint("ByteOrderMark")
class PickLocationActivity : BaseActivity(),OnMapReadyCallback {
    lateinit var binding: ActivityPickLocationBinding
    var email: String?= null
    var name: String?= null

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location

    var locationRequest: LocationRequest? = null
    var callbackMap: LocationCallback? = null
    private lateinit var mMap: GoogleMap

    var latitude: Double? = null
    var longitude: Double? = null
    var selectedAddress: String? = null
    var selectedLocation: LatLng? = null

    private val LOCATION_REQUEST_CODE = 101
    private val PLACE_AUTOCOMPLETE_REQUEST_CODE = 1001
    var currentLatLng: LatLng? = null

    companion object {
        open val FORM_LOCATION: Int = 110
        open val LOCATION_NAME = "LocationName"
        open val LOCATION_LATLNG = "LocationLatLng"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPickLocationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initConfig()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out)
    }

    override fun initConfig() {
        email = intent.getStringExtra("email")
        name = intent.getStringExtra(Constants.DATA_NAME)

        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out)
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this@PickLocationActivity)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        locationRequest = LocationRequest.create()
        locationRequest?.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        callbackMap = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
//                super.onLocationResult(p0)

//                location = net.gumcode.hadir.data.model.Location()
//                location!!.lat = locationResult?.getLastLocation()?.getLatitude()
//                location!!.lng = locationResult?.getLastLocation()?.getLongitude()
//                Log.d("mapLocLatLong",locationResult?.getLastLocation()?.getLatitude().toString()
//                        +":"+locationResult?.getLastLocation()?.getLongitude().toString())

                latitude = locationResult?.getLastLocation()?.getLatitude()
                longitude = locationResult?.getLastLocation()?.getLongitude()

            }
        }
        initUI()
    }

    override fun initUI() {
        binding.toolbar.title.text = getString(R.string.text_pick_location)
    }

    override fun setListener() {
        binding.toolbar.buttonBack.setOnClickListener {
            onBackPressed()
        }
        binding.next.setOnClickListener {
            var limit: Double = 25.0
            try {
                var radius = CoordDistance(selectedLocation!!.latitude, selectedLocation!!.longitude, currentLatLng!!.latitude, currentLatLng!!.longitude)

                if (radius <= limit) {
                    val intent = Intent()
                    intent.putExtra(LOCATION_NAME, selectedAddress);
                    intent.putExtra(LOCATION_LATLNG, selectedLocation);
                    setResult(Activity.RESULT_OK, intent);
                    finish()
                } else {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17f))
                }
            } catch (e: NullPointerException) {
                showAlertDialog("", getString(R.string.error_cant_get_current_location), View.OnClickListener {
                    dialog.dismiss()
                }, false)
            }
        }

        binding.buttonAutoComplete.setOnClickListener ( View.OnClickListener {
            val filter: AutocompleteFilter = AutocompleteFilter.Builder().setCountry("ID").build()
//                    var bounds:LatLngBounds = LatLngBounds(
//                            LatLng(-33.880490, 151.184363),
//                            LatLng(-33.858754, 151.229596))

            var bounds: LatLngBounds = LatLngBounds(
                    LatLng(currentLatLng!!.latitude, currentLatLng!!.longitude),
                    LatLng(currentLatLng!!.latitude, currentLatLng!!.longitude))

            try {
                val intent:Intent = PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                        .setFilter(filter)
                        .setBoundsBias(bounds)
                        .build(this);
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE)


            }catch (e: Exception){
            }
        } )
    }

    fun CoordDistance(latitude1: Double, longitude1: Double, latitude2: Double, longitude2: Double): Double {
        return 6371 * Math.acos(
                Math.sin(latitude1) * Math.sin(latitude2) + Math.cos(latitude1) * Math.cos(latitude2) * Math.cos(longitude2 - longitude1))
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!

        if (mMap != null) {
            mMap.getUiSettings().setZoomControlsEnabled(true)
            setUpMap()
            setupMapOnCameraChange()
        } else {
//            requestPermission(
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    LOCATION_REQUEST_CODE)
        }

    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
            return
        }

        mMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                lastLocation = location
                currentLatLng = LatLng(location.latitude, location.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17f))
                binding.address.text = getAddress(currentLatLng!!)
                binding.address.visibility = View.VISIBLE

            }
        }

    }

    private fun getAddress(latLng: LatLng): String {

        val geocoder = Geocoder(this)
        val addresses: List<Address>?
        val address: Address?
        var addressText = ""
//        Log.d("PICK 2a","" + addressText)

        try {

            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

            if (null != addresses && !addresses.isEmpty()) {
                address = addresses[0]
//                for (i in 0 until address.maxAddressLineIndex) {
//                    addressText += if (i == 0) address.getAddressLine(i) else "\n" + address.getAddressLine(i)
//                }
                addressText += address.getAddressLine(0)
            }
//            Log.d("PICK 2b","" + addressText)
        } catch (e: IOException) {
        }





        return addressText
    }

    private fun setupMapOnCameraChange() {
        mMap.setOnCameraIdleListener(GoogleMap.OnCameraIdleListener {
            val center = mMap.getCameraPosition().target
            val rotate = AnimationUtils.loadAnimation(this, R.anim.rotate)
            val getAddress = Thread(Runnable {
                try {

                    this.runOnUiThread(Runnable {
                        binding.next.clearAnimation()
                        var fin_address = getAddress(center)
                        binding.address.text = fin_address
                        binding.address.visibility = View.VISIBLE
                        selectedAddress = fin_address
                        selectedLocation = center

                    })


                } catch (e: IOException) {
                    this.runOnUiThread(Runnable {
                        binding.address.visibility = View.GONE
                        binding.address.text = ""
                    })
                    e.printStackTrace()
                }
            })

            val animate = Thread(Runnable {
                try {
                    Thread.sleep(700)
                    this.runOnUiThread(Runnable {
                        if (getAddress.isAlive) {
                            binding.next.startAnimation(rotate)
                        } else {
                            binding.next.clearAnimation()
                        }
                    })

                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            })

            animate.start()
            getAddress.start()


        })

        mMap.setOnCameraMoveListener {
            binding.address.visibility = View.GONE
            binding.address.text = ""
            binding.next.clearAnimation()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val place = PlaceAutocomplete.getPlace(this, data)
                binding.autoCompleteText.text = place.address
                selectedAddress = "" + place.address
                selectedLocation = place.latLng
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(selectedLocation, 17f))


            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                val status = PlaceAutocomplete.getStatus(this, data)

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
}
