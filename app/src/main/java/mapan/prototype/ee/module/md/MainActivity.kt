package mapan.prototype.ee.module.md

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import mapan.prototype.ee.R
import mapan.prototype.ee.config.Constants
import mapan.prototype.ee.databinding.ActivityMainBinding
import mapan.prototype.ee.module.core.BaseActivity
import mapan.prototype.ee.module.main.LoginActivity
import mapan.prototype.ee.util.PermissionHelper
import mapan.prototype.ee.util.Util

class MainActivity : BaseActivity() , BottomNavigationView.OnNavigationItemSelectedListener{

    lateinit var binding: ActivityMainBinding

    private var dashboardFragment: Fragment? = null
    private var checkInFragment: Fragment? = null
    private var historyFragment: Fragment? = null

    var isBreak = false
    var idMerchant = -1
    var email : String?= null
    var timeActivity = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

    }

    override fun onBackPressed() {
        finish()
    }

    override fun onResume() {
        super.onResume()
        checkPermission()
    }

    fun onResumeMain(){
        onResume()
    }

    fun checkPermission(){
        email = intent.getStringExtra("email")
        if (PermissionHelper.checkLocationPermission(this)) {
            initConfig()
        }else{
            checkPermission()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.chooseLocationRequestCode){
            if(grantResults[0] == PackageManager.PERMISSION_DENIED) {
                checkPermission()
            }
        }
    }
    override fun initConfig() {
        initUI()
    }

    override fun initUI() {

        binding.toolbar.title.text = getString(R.string.app_name_prototype)
        binding.toolbar.buttonBack.visibility = View.GONE

//
        if(timeActivity.equals("Check In")){
            if (checkInFragment == null) {
                checkInFragment = CheckInFragment.newInstance()
            }
            changeFragment(checkInFragment!!)
        }else{
//            if (dashboardFragment == null) {
            dashboardFragment = DashboardFragment.newInstance()
//            }
            changeFragment(dashboardFragment!!)
        }
        setListener()
    }

    override fun setListener() {
        binding.navigationMenu.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_dashboard -> {

                if(timeActivity.equals("Check In")){
                    if (checkInFragment == null) {
                        checkInFragment = CheckInFragment.newInstance()
                    }
                    changeFragment(checkInFragment!!)
                }else{
//                    if (dashboardFragment == null) {
                    dashboardFragment = DashboardFragment.newInstance()
//                    }
                    changeFragment(dashboardFragment!!)
                }
            }
            R.id.action_history -> {
                if (historyFragment == null) {
                    historyFragment = HistoryFragment.newInstance()
                }
                changeFragment(historyFragment!!)
            }
            R.id.action_logout -> {

                binding.navigationMenu.selectedItemId = R.id.action_dashboard
                if(timeActivity.toLowerCase().equals("break in")){
                    var message = "Anda tidak dapat keluar silahkan selesaikan istirahat anda terlebih dahulu"
                    showAlertDialog(getString(R.string.text_alert), message,
                            View.OnClickListener {
                                dialog.dismiss()
                            }, false)
                }else{
                    showPromptDialog(getString(R.string.text_alert), getString(R.string.text_ask_logout),
                            getString(R.string.action_ok), View.OnClickListener {
                        logout()
                        dialog.dismiss()
                    }, false)
                }
            }
        }
        return true
    }
    fun logout(){
        showPromptDialog(getString(R.string.text_confirmation), getString(R.string.text_ask_logout),
                getString(R.string.action_ok), View.OnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            dialog.dismiss()
        }, false)
    }
    open fun changeFragment(newFragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, newFragment).commit()
    }
}
