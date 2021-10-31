package mapan.prototype.ee.module.md

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import mapan.prototype.ee.R
import mapan.prototype.ee.adapter.AdapterMenu
import mapan.prototype.ee.config.Constants
import mapan.prototype.ee.databinding.FragmentCheckinBinding
import mapan.prototype.ee.model.Merchant
import mapan.prototype.ee.module.adminspv.FormDisplayActivity
import mapan.prototype.ee.module.adminspv.FormTransactionActivity
import mapan.prototype.ee.util.InitializerUi

class CheckInFragment : Fragment(), InitializerUi {

    private var _binding: FragmentCheckinBinding?= null
    private val binding get() = _binding!!
    private lateinit var main: MainActivity
    lateinit var fastAdapter: FastAdapter<AdapterMenu>
    lateinit var adapter: ItemAdapter<AdapterMenu>
    var mFusedLocationClient: FusedLocationProviderClient? = null
    var loc : Location?= null
    var isNotif = false
    var detailMerchant : Merchant?= null

    companion object {
        fun newInstance(): Fragment = CheckInFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCheckinBinding.inflate(inflater, container, false)
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
        adapter = ItemAdapter<AdapterMenu>()
        fastAdapter = FastAdapter.with(adapter)
        initUI()
    }
    fun loadDataMenu(){
        var display = AdapterMenu()
        display.id = 1
        display.name = "Daftar Foto Display"
        display.icon = R.drawable.ic_add_a_photo_black_24dp
        adapter.add(display)
        var order = AdapterMenu()
        order.id = 2
        order.name = "Daftar Order Produk"
        order.icon = R.drawable.ic_add_shopping_cart_black_24dp
        adapter.add(order)
        var retur = AdapterMenu()
        retur.id = 3
        retur.name = "Daftar Retur Produk"
        retur.icon = R.drawable.ic_add_shopping_cart_black_24dp
        adapter.add(retur)
        var history = AdapterMenu()
        history.id = 4
        history.name = "Daftar Hitory MD"
        history.icon = R.drawable.ic_history_black_24dp
        adapter.add(history)

    }

    fun loadData(){
        if(main!!.timeActivity.equals("breakout")){
            main.isBreak = true
        }
//                    main.loadData()
        main.onResumeMain()
    }

    override fun initUI() {
        detailMerchant = Merchant()
        detailMerchant!!.id = "1"
        detailMerchant!!.name = "Toko 1"
        detailMerchant!!.address = "Alamat Toko 1"
        detailMerchant!!.latitude = -7.1451104
        detailMerchant!!.longitude = 112.6339195


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
        binding.status.setText("Check In di Toko "+detailMerchant!!.name)

        var layoutManager = LinearLayoutManager(main)
        binding.list.layoutManager = layoutManager
        binding.list.setAdapter(fastAdapter)
        binding.list.addItemDecoration(DividerItemDecoration(main, layoutManager.getOrientation()))
        binding.list.isNestedScrollingEnabled = false
        loadDataMenu()

        setListener()
    }

    override fun setListener() {
        fastAdapter.onClickListener = { view, adapter, item, position ->
            if(item.id == 1){
                var change =  Intent(main, FormDisplayActivity::class.java)
                change.putExtra(Constants.DATA_ID,item.id.toString())
                change.putExtra("email",main.email)
                change.putExtra(Constants.DATA_ISMD,false)
                startActivity(change)
            }else if(item.id == 2){
                var change =  Intent(main, FormTransactionActivity::class.java)
                change.putExtra(Constants.DATA_ID,item.id.toString())
                change.putExtra("email",main.email)
                change.putExtra(Constants.DATA_ISMD,false)
                change.putExtra(Constants.DATA_NAME,"Toko 1")
                change.putExtra(Constants.DATA_TYPE,0)
            }else if(item.id == 3){
                var change =  Intent(main, FormTransactionActivity::class.java)
                change.putExtra(Constants.DATA_ID,item.id.toString())
                change.putExtra("email",main.email)
                change.putExtra(Constants.DATA_ISMD,false)
                change.putExtra(Constants.DATA_NAME,"Toko 1")
                change.putExtra(Constants.DATA_TYPE,0)
            }
            false
        }
        binding.buttonCheckout.setOnClickListener {
//            if(areThereMockPermissionApps()){
//                var message = "Harap Uninstall Aplikasi Fake GPS dan sejenisnya"
//                main.showAlertDialog(getString(R.string.text_alert), message,
//                        View.OnClickListener {
//                            main.dialog.dismiss()
//                        }, false)
//            }else{
                main.timeActivity = "check out"
                loadData()
//            }
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
