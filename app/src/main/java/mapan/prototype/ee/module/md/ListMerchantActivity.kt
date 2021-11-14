package mapan.prototype.ee.module.md

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.binding.BindingViewHolder
import com.mikepenz.fastadapter.listeners.ClickEventHook
import mapan.prototype.ee.R
import mapan.prototype.ee.adapter.AdapterData
import mapan.prototype.ee.adapter.AdapterMerchant
import mapan.prototype.ee.config.Constants
import mapan.prototype.ee.databinding.ActivityListBinding
import mapan.prototype.ee.databinding.ActivityListMerchantBinding
import mapan.prototype.ee.databinding.ItemAdapterDataBinding
import mapan.prototype.ee.model.Merchant
import mapan.prototype.ee.model.Transaction
import mapan.prototype.ee.module.core.BaseActivity
import mapan.prototype.ee.module.md.FormProductActivity
import mapan.prototype.ee.module.md.FormTransactionActivity
import java.util.*

class ListMerchantActivity : BaseActivity() {
    lateinit var binding: ActivityListMerchantBinding
    lateinit var fastAdapter: FastAdapter<AdapterMerchant>
    lateinit var adapter: ItemAdapter<AdapterMerchant>
    var email: String?= null
    var type: String?= null
    var isMD: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListMerchantBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initConfig()
    }

    override fun initConfig() {
        type = intent.getStringExtra(Constants.DATA_TYPE)
        email = intent.getStringExtra("email")
        adapter = ItemAdapter<AdapterMerchant>()
        fastAdapter = FastAdapter.with(adapter)
        initUI()
    }

    override fun initUI() {
        binding.toolbar.title.text = "Daftar Toko"

        binding.list.layoutManager = LinearLayoutManager(this)
        binding.list.setAdapter(fastAdapter)
        binding.list.isNestedScrollingEnabled = false

        binding.empty.visibility = View.GONE
        binding.list.visibility = View.VISIBLE
        loadData()

        setListener()
    }

    fun loadData(){
        for(i in 0 .. 10){
            var item = AdapterMerchant()
            item.id = i+1
            item.name = "Toko "+item.id.toString()
            item.address = "Detail Alamat Toko "+item.id.toString()
            adapter.add(item)
        }
    }

    override fun setListener() {

        binding.toolbar.buttonBack.setOnClickListener {
            onBackPressed()
        }

        fastAdapter.onClickListener = { view, adapter, item, position ->
            var change = Intent(this@ListMerchantActivity,DetailMerchantActivity::class.java)
            change.putExtra(Constants.DATA_ID,item.id)
            change.putExtra("email",email)
            startActivityForResult(change, Constants.check)
            false
        }
    }

    override fun onBackPressed() {
        finish()
    }

}
