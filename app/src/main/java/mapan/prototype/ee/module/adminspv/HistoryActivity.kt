package mapan.prototype.ee.module.adminspv

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import mapan.prototype.ee.R
import mapan.prototype.ee.adapter.AdapterData
import mapan.prototype.ee.adapter.AdapterHistory
import mapan.prototype.ee.adapter.AdapterMenu
import mapan.prototype.ee.config.Constants
import mapan.prototype.ee.databinding.ActivityDashboardBinding
import mapan.prototype.ee.databinding.ActivityHistoryBinding
import mapan.prototype.ee.databinding.ActivityListBinding
import mapan.prototype.ee.model.Merchant
import mapan.prototype.ee.model.Presence
import mapan.prototype.ee.model.Transaction
import mapan.prototype.ee.module.core.BaseActivity
import mapan.prototype.ee.module.main.LoginActivity
import mapan.prototype.ee.module.md.LocationMerchantActivity
import mapan.prototype.ee.util.Util
import java.util.*

class HistoryActivity : BaseActivity() {
    lateinit var binding: ActivityHistoryBinding
    lateinit var fastAdapter: FastAdapter<AdapterHistory>
    lateinit var adapter: ItemAdapter<AdapterHistory>
    var listData = ArrayList<Presence>()
    var email: String?= null
    var type: String?= null
    var id : String ?= null
    var name : String ?= null
    var isMD: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initConfig()
    }

    override fun initConfig() {
        type = intent.getStringExtra(Constants.DATA_TYPE)
        email = intent.getStringExtra("email")
        id = intent.getStringExtra(Constants.DATA_ID)
        name = intent.getStringExtra(Constants.DATA_NAME)
        adapter = ItemAdapter<AdapterHistory>()
        fastAdapter = FastAdapter.with(adapter)
        initUI()
    }

    override fun initUI() {
        binding.toolbar.title.text = name

        binding.list.layoutManager = LinearLayoutManager(this)
        binding.list.setAdapter(fastAdapter)
        binding.list.isNestedScrollingEnabled = false

        loadDataHistory()

        setListener()
    }

    override fun setListener() {

        binding.toolbar.buttonBack.setOnClickListener {
            onBackPressed()
        }
        fastAdapter.onClickListener = { view, adapter, item, position ->
            var change = Intent(this@HistoryActivity, LocationMerchantActivity::class.java)
            change.putExtra("email",email)
            change.putExtra(Constants.DATA_NAME,item.name)
            change.putExtra(Constants.DATA_LAT,listData!!.get(position)!!.latitude)
            change.putExtra(Constants.DATA_LNG,listData!!.get(position)!!.longitude)
            startActivity(change)
            false
        }
    }

    override fun onBackPressed() {
        finish()
    }

    fun loadDataHistory(){
        for(i in 0 .. 9){
            var data = Presence()
            data.id_merchant = (i+1).toString()
            data.merchant = "Toko "+data.id_merchant
            data.latitude = -7.1451104
            data.longitude = 112.6339195
            data.activity = if(i%2 == 0)"Check In" else "CheckOut"
            data.created_at = "2021-10-02T09:10:20.000Z"
            listData.add(data)
        }
        for(data in listData){
            var item = AdapterHistory()
            var merchant = " di toko : "+data.merchant!!
            item.name = data.merchant!!
            item.status = data.activity + merchant
            item.date = Util.convertDate(data.created_at!!,Constants.DATE_OUT_FORMAT_DEF5,Constants.DATE_OUT_FORMAT_DEF_FULL)
            adapter.add(item)
        }
        if(adapter.adapterItemCount > 0){
            binding.empty.visibility = View.GONE
            binding.list.visibility = View.VISIBLE
        }else{
            binding.empty.visibility = View.VISIBLE
            binding.list.visibility = View.GONE
        }
    }

}
