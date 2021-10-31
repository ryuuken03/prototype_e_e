package mapan.prototype.ee.module.md

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import mapan.prototype.ee.R
import mapan.prototype.ee.adapter.AdapterMenu
import mapan.prototype.ee.adapter.MarkerWindowAdapter
import mapan.prototype.ee.config.Constants
import mapan.prototype.ee.databinding.ActivityFormMerchantBinding
import mapan.prototype.ee.databinding.ActivityLocationMerchantBinding
import mapan.prototype.ee.model.Place
import mapan.prototype.ee.module.core.BaseActivity
import mapan.prototype.ee.util.PermissionHelper
import mapan.prototype.ee.util.Util
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

class LocationMerchantActivity : BaseActivity() {
    lateinit var binding: ActivityLocationMerchantBinding
    private lateinit var mMap : GoogleMap
    private var mapFragment: SupportMapFragment? = null
    var fusedLocationClient: FusedLocationProviderClient? = null
    var email: String?= null
    var lat = 0.0
    var lng = 0.0
    var name: String?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationMerchantBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initConfig()
    }

    override fun initConfig() {
        email = intent.getStringExtra("email")
        name = intent.getStringExtra("name")
        lat = intent.getDoubleExtra(Constants.DATA_LAT,-1.0)
        lng = intent.getDoubleExtra(Constants.DATA_LNG,-1.0)
        initUI()
    }

    override fun initUI() {
        binding.toolbar.title.text = "Lokasi "+name
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance()
            mapFragment!!.getMapAsync(object : OnMapReadyCallback {
                override fun onMapReady(googleMap: GoogleMap) {
                    mMap = googleMap
                    mMap.getUiSettings().setZoomControlsEnabled(true)
                    setUpMap()
                }
            })
            getSupportFragmentManager().beginTransaction().replace(R.id.map, mapFragment!!).commit();
        }
        setListener()
    }
    @SuppressLint("MissingPermission")
    private fun setUpMap() {

        mMap.isMyLocationEnabled = true

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient?.lastLocation?.addOnSuccessListener(this) { location ->
            if (location != null) {

                var markerPin = (getSystemService(Context.LAYOUT_INFLATER_SERVICE)as LayoutInflater)
                        .inflate(R.layout.item_marker, null)

                var latLng = LatLng(lat,lng)

                val customInfoWindow = MarkerWindowAdapter(this@LocationMerchantActivity)
                mMap.setInfoWindowAdapter(customInfoWindow)

                mMap.addMarker(MarkerOptions().position(latLng)
                        .title(name)
                        .icon(BitmapDescriptorFactory.fromBitmap(
                                createDrawableFromView(this@LocationMerchantActivity, markerPin))))
                        .showInfoWindow()

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
            }
        }
    }

    private fun createDrawableFromView(context: Context, view:View):Bitmap{
        var displayMetrics = DisplayMetrics();
        (context as LocationMerchantActivity).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        var bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        var canvas = Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    override fun setListener() {
        binding.toolbar.buttonBack.setOnClickListener {
            onBackPressed()
        }
    }
}
