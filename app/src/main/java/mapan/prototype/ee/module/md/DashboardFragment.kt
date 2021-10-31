package mapan.prototype.ee.module.md

import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import mapan.prototype.ee.R
import mapan.prototype.ee.databinding.FragmentDashboardBinding
import mapan.prototype.ee.util.InitializerUi
import mapan.prototype.ee.util.Log

class DashboardFragment : Fragment(), InitializerUi {

    private var _binding: FragmentDashboardBinding?= null
    private val binding get() = _binding!!
    private lateinit var main: MainActivity
    var mFusedLocationClient: FusedLocationProviderClient? = null
    var loc : Location?= null
    var isNotif = false

    companion object {
        fun newInstance(): Fragment = DashboardFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val view = binding.root

        main = getActivity() as MainActivity

        initConfig()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }

    override fun onResume() {
        super.onResume()
    }

    override fun initConfig() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(main)
        initUI()
    }

    fun loadData(){
        if(main!!.timeActivity.equals("breakout")){
            main.isBreak = true
        }
//                    main.loadData()
        main.onResumeMain()
    }

    override fun initUI() {

        mFusedLocationClient!!.lastLocation
            .addOnSuccessListener { location : Location? ->
                loc = location
            }
        var name = "Pegawai 1"
        if(main!!.email!!.contains("admin")){
            name = "Admin"
        }else if(main!!.email!!.contains("spv")){
            name = "Suprevisor 1"
        }
        binding.name.setText(name)
        Log.d("OkCheck",main!!.timeActivity)
        if(main!!.timeActivity.equals("")||main!!.timeActivity.toLowerCase().equals("go home")){
            binding.status.setText("Status : Belum Hadir")

            binding.iconCheck.visibility = View.GONE
            binding.iconOff.visibility = View.VISIBLE

            binding.textIcon.setText("Presensi")
            binding.relativeButton.visibility = View.GONE
        }else{
            if(main!!.timeActivity.toLowerCase().equals("work")) {
                binding.status.setText("Status : Kerja")
                binding.textIcon.setText("Cek Toko")
            }else if(main!!.timeActivity.toLowerCase().equals("check out")){
                binding.status.setText("Status : Check Out")
                binding.textIcon.setText("Cek Toko")
                binding.relativeButton.visibility = View.VISIBLE
                if(main.isBreak!!){
                    binding.buttonText.setText("Presensi Pulang")
                }else{
                    binding.buttonText.setText("Break In")
                }
            }else if(main!!.timeActivity.toLowerCase().equals("break in")){
                binding.linearIcon.visibility = View.GONE
                binding.status.setText("Status : Break In")
                binding.buttonText.setText("Break Out")
                binding.relativeButton.visibility = View.VISIBLE
            }else if(main!!.timeActivity.toLowerCase().equals("break out")){
                binding.linearIcon.visibility = View.VISIBLE
                binding.status.setText("Status : Break Out")
                binding.textIcon.setText("Cek Toko")
                binding. buttonText.setText("Presensi Pulang")
                binding.relativeButton.visibility = View.VISIBLE
            }


            binding.iconCheck.visibility = View.VISIBLE
            binding.iconOff.visibility = View.GONE

        }

        setListener()
    }

    override fun setListener() {
        binding.linearIcon.setOnClickListener {
            if(areThereMockPermissionApps()){
//                var message = "Harap Uninstall Aplikasi Fake GPS dan sejenisnya"
//                main.showAlertDialog(getString(R.string.text_alert), message,
//                        View.OnClickListener {
//                            main.dialog.dismiss()
//                        }, false)
            }else{
                if(main!!.timeActivity.equals("") || main!!.timeActivity.equals("go home")){
                    main!!.timeActivity = "work"
                    loadData()
                }else{
//                    var change = Intent(main,ListMerchantActivity::class.java)
//                    startActivity(change)
                }
            }

        }

        binding.relativeButton.setOnClickListener {
            if(areThereMockPermissionApps()){
//                var message = "Harap Matikan Fake GPS dan sejenisnya"
//                main.showAlertDialog(getString(R.string.text_alert), message,
//                        View.OnClickListener {
//                            main.dialog.dismiss()
//                        }, false)
            }else{
                if(main.timeActivity.toLowerCase().equals("check out") && !main.isBreak!!){
                    main.timeActivity = "break in"
                }else if(main.timeActivity.toLowerCase().equals("break in")){
                    main.timeActivity = "break out"
                }else if((main.timeActivity.toLowerCase().equals("check out") && main.isBreak!!)||
                    main.timeActivity.toLowerCase().equals("break out")){
                    main.timeActivity = "go home"
                }
                loadData()
            }
        }
    }

    fun isMockLocation(location:Location):Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && location != null && location.isFromMockProvider()
    }
    fun areThereMockPermissionApps():Boolean{
        var count = 0
        var pm = main.packageManager
        var packages = pm.getInstalledPackages(PackageManager.GET_META_DATA)

        var name = ""
        packages.forEach {
            try{
                var applicationInfo = it
                var packageInfo = pm.getPackageInfo(it.packageName, PackageManager.GET_PERMISSIONS)
                var requestedPermissions = packageInfo.requestedPermissions
                if (requestedPermissions != null) {
                    requestedPermissions.forEach {
                        if(it.equals("android.permission.ACCESS_MOCK_LOCATION") &&
                            !applicationInfo.packageName.equals(main.packageName)){
                            var notAvailable = true
                            var listPackageNext = arrayListOf<String>()
                            listPackageNext.add("com.android.calendar")
                            listPackageNext.add("com.samsung.android.messaging")

                            listPackageNext.forEach {
                                if(applicationInfo.packageName.equals(it)){
                                    notAvailable = false
                                }
                            }
                            if(notAvailable){
                                count++
                                name+=""+applicationInfo.packageName+", "
                            }
                        }
                    }
                }
            }catch (e: PackageManager.NameNotFoundException){

            }
        }
        if (count > 0){

            var message = "Daftar aplikasi sejenis FakeGPS : "+name+" Harap dihapus."

            main.showAlertDialog(getString(R.string.text_alert), message,
                View.OnClickListener {
                    main.dialog.dismiss()
                }, false)
            return true
        }
        return false
    }
}
