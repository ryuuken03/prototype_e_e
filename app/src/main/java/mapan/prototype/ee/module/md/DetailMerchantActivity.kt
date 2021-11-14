package mapan.prototype.ee.module.md

import android.app.Activity
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import mapan.prototype.ee.R
import mapan.prototype.ee.adapter.AdapterMenu
import mapan.prototype.ee.config.Constants
import mapan.prototype.ee.databinding.ActivityDashboardBinding
import mapan.prototype.ee.databinding.ActivityDetailMerchantBinding
import mapan.prototype.ee.databinding.ActivityReportBinding
import mapan.prototype.ee.module.core.BaseActivity
import mapan.prototype.ee.module.main.LoginActivity
import mapan.prototype.ee.util.Util

class DetailMerchantActivity : BaseActivity() {
    lateinit var binding: ActivityDetailMerchantBinding
    var email: String?= null
    var id: Int?= null
    var mFusedLocationClient: FusedLocationProviderClient? = null
    var loc : Location?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMerchantBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

    }

    override fun onResume() {
        super.onResume()
        initConfig()
    }

    override fun initConfig() {
        email = intent.getStringExtra("email")
        id = intent.getIntExtra(Constants.DATA_ID,-1)
        if(email!!.contains("md",true)){
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        }
        initUI()
    }

    override fun initUI() {
        binding.toolbar.title.text = "Detail Toko"
        if(!email!!.contains("md",true)){
            binding.buttonCheckIn.visibility = View.GONE
        }else{
            mFusedLocationClient!!.lastLocation
                    .addOnSuccessListener { location : Location? ->
                        loc = location
                    }
        }
        var image = "https://arsitagx-master-article.s3-ap-southeast-1.amazonaws.com/article-photo/241/48768e32aa84accaff381167e95285cb.jpg"
        Util.loadImage(this,image,binding.imageMerchant)
        binding.nameMerchant.setText("Toko "+id.toString())
        binding.ownerMerchant.setText("Owner Toko "+id.toString())
        binding.phoneMerchant.setText("0848748484848")
        binding.addressMerchant.setText("Detail alamat Toko "+id.toString())
        setListener()
    }

    override fun setListener() {

        binding.toolbar.buttonBack.setOnClickListener {
            onBackPressed()
        }
        binding.buttonCheckIn.setOnClickListener {
//            presence("checkin")
            var intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            finish()


        }

        binding.buttonLocation.setOnClickListener {
            var change = Intent(this,LocationMerchantActivity::class.java)
            change.putExtra(Constants.DATA_NAME,"Toko 1")
            change.putExtra(Constants.DATA_LAT,-7.1451104)
            change.putExtra(Constants.DATA_LNG,112.6339195)
            startActivity(change)
        }

    }

    override fun onBackPressed() {
        finish()
    }

}
