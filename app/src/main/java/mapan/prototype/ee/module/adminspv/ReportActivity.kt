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
import mapan.prototype.ee.databinding.ActivityReportBinding
import mapan.prototype.ee.module.core.BaseActivity
import mapan.prototype.ee.module.main.LoginActivity

class ReportActivity : BaseActivity() {
    lateinit var binding: ActivityReportBinding
    lateinit var fastAdapter: FastAdapter<AdapterMenu>
    lateinit var adapter: ItemAdapter<AdapterMenu>
    var email: String?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportBinding.inflate(layoutInflater)
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
        binding.toolbar.title.text = "Daftar Laporan"

        binding.list.layoutManager = LinearLayoutManager(this)
        binding.list.setAdapter(fastAdapter)
        binding.list.isNestedScrollingEnabled = false

        loadData()
        setListener()
    }

    override fun setListener() {

        binding.toolbar.buttonBack.setOnClickListener {
            onBackPressed()
        }

        fastAdapter.onClickListener = { view, adapter, item, position ->
            if(item.id == 1){
                var change = Intent(this@ReportActivity, ListActivity::class.java)
                change.putExtra(Constants.DATA_TYPE,"display")
                change.putExtra("email",email)
                startActivity(change)
            }else if(item.id == 2){
                var change = Intent(this@ReportActivity, ListActivity::class.java)
                change.putExtra(Constants.DATA_TYPE,"order")
                change.putExtra("email",email)
                startActivity(change)
            }else if(item.id == 3){
                var change = Intent(this@ReportActivity, ListActivity::class.java)
                change.putExtra(Constants.DATA_TYPE,"retur")
                change.putExtra("email",email)
                startActivity(change)
            }else if(item.id == 4){
                var change = Intent(this@ReportActivity, ListActivity::class.java)
                change.putExtra(Constants.DATA_TYPE,"history")
                change.putExtra("email",email)
                startActivity(change)
            }
            false
        }
    }

    override fun onBackPressed() {
        finish()
    }

    fun loadData(){
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
}
