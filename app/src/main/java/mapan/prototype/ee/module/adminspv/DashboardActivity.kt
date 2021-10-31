package mapan.prototype.ee.module.adminspv

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import mapan.prototype.ee.R
import mapan.prototype.ee.adapter.AdapterMenu
import mapan.prototype.ee.config.Constants
import mapan.prototype.ee.databinding.ActivityDashboardBinding
import mapan.prototype.ee.module.core.BaseActivity
import mapan.prototype.ee.module.main.LoginActivity

class DashboardActivity : BaseActivity() {
    lateinit var binding: ActivityDashboardBinding
    lateinit var fastAdapter: FastAdapter<AdapterMenu>
    lateinit var adapter: ItemAdapter<AdapterMenu>
    var email: String?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initConfig()
    }

    override fun initConfig() {
        email = intent.getStringExtra("email")
        adapter = ItemAdapter<AdapterMenu>()
        fastAdapter = FastAdapter.with(adapter)
        initUI()
    }

    override fun initUI() {
        binding.toolbar.title.text = getString(R.string.app_name_prototype)
        binding.toolbar.buttonBack.visibility = View.GONE

        binding.list.layoutManager = LinearLayoutManager(this)
        binding.list.setAdapter(fastAdapter)
        binding.list.isNestedScrollingEnabled = false
        var name = "Admin"
        var role = "Admin Jakarta"
        if(email!!.contains("spv")){
            name = "Supervisor"
            role = "Supervisor Gresik"
        }

        binding.name.setText(name)
        binding.role.setText(role)

        loadData()
        setListener()
    }

    override fun setListener() {

        fastAdapter.onClickListener = { view, adapter, item, position ->
            if(item.id == 1){
                var change = Intent(this@DashboardActivity, ListActivity::class.java)
                change.putExtra(Constants.DATA_ISMD,true)
                change.putExtra(Constants.DATA_TYPE,"user")
                change.putExtra("email",email)
                startActivity(change)
            }else if(item.id == 2){
                var change = Intent(this@DashboardActivity, ListActivity::class.java)
                change.putExtra(Constants.DATA_ISMD,false)
                change.putExtra(Constants.DATA_TYPE,"user")
                change.putExtra("email",email)
                startActivity(change)
            }else if(item.id == 3){
                var change = Intent(this@DashboardActivity, ListActivity::class.java)
                change.putExtra(Constants.DATA_TYPE,"product")
                change.putExtra("email",email)
                startActivity(change)
            }else if(item.id == 4){
                var change = Intent(this@DashboardActivity, ListActivity::class.java)
                change.putExtra(Constants.DATA_TYPE,"merchant")
                change.putExtra("email",email)
                startActivity(change)
            }else if(item.id == 5){
                var change =  Intent(this@DashboardActivity,ReportActivity::class.java)
                change.putExtra("email",email)
                startActivity(change)
            }else if(item.id == 6){
                var change =  Intent(this@DashboardActivity,FormUserActivity::class.java)
                change.putExtra(Constants.DATA_ID,"1")
                var type = 0
                change.putExtra(Constants.DATA_TYPE,type)
                change.putExtra("email",email)
                startActivity(change)
            }else if(item.id == 7){
                showPromptDialog(getString(R.string.text_confirmation), getString(R.string.text_ask_logout),
                    getString(R.string.action_ok), View.OnClickListener {
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                        dialog.dismiss()
                    }, false)
            }
            false
        }
    }

    override fun onBackPressed() {
        showPromptDialog(getString(R.string.text_confirmation), getString(R.string.text_are_you_sure_want_to_exit),
            getString(R.string.action_ok), View.OnClickListener {
                finish()
                dialog.dismiss()
            }, false)
    }

    fun loadData(){

        var md = AdapterMenu()
        md.id = 1
        md.name = "Daftar MD"
        md.icon = R.drawable.ic_person_black_24dp
        adapter.add(md)
        var spv = AdapterMenu()
        if(email!!.contains("admin")){
            spv.id = 2
            spv.name = "Daftar SPV"
            spv.icon = R.drawable.ic_person_black_24dp
            adapter.add(spv)
        }

        var product = AdapterMenu()
        product.id = 3
        product.name = "Daftar Produk"
        product.icon = R.drawable.ic_shopping_cart_black_24dp
        adapter.add(product)
        var merchant = AdapterMenu()
        merchant.id = 4
        merchant.name = "Daftar Toko"
        merchant.icon = R.drawable.ic_front_store
        adapter.add(merchant)
        var report = AdapterMenu()
        report.id = 5
        report.name = "Daftar Laporan"
        report.icon = R.drawable.ic_assignment_black_24dp
        adapter.add(report)
        var profile = AdapterMenu()
        profile.id = 6
        profile.name = "Profil"
        profile.icon = R.drawable.ic_person_black_24dp
        adapter.add(profile)
        var logout = AdapterMenu()
        logout.id = 7
        logout.name = "Keluar"
        logout.icon = R.drawable.ic_logout
        adapter.add(logout)

    }
}
