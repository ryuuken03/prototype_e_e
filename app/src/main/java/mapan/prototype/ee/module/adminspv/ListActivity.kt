package mapan.prototype.ee.module.adminspv

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
import mapan.prototype.ee.adapter.AdapterMenu
import mapan.prototype.ee.config.Constants
import mapan.prototype.ee.databinding.ActivityDashboardBinding
import mapan.prototype.ee.databinding.ActivityListBinding
import mapan.prototype.ee.databinding.ItemAdapterDataBinding
import mapan.prototype.ee.model.Merchant
import mapan.prototype.ee.model.Transaction
import mapan.prototype.ee.module.core.BaseActivity
import mapan.prototype.ee.module.main.LoginActivity
import java.util.*

class ListActivity : BaseActivity() {
    lateinit var binding: ActivityListBinding
    lateinit var fastAdapter: FastAdapter<AdapterData>
    lateinit var adapter: ItemAdapter<AdapterData>
    var email: String?= null
    var type: String?= null
    var isMD: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initConfig()
    }

    override fun initConfig() {
        type = intent.getStringExtra(Constants.DATA_TYPE)
        email = intent.getStringExtra("email")
        adapter = ItemAdapter<AdapterData>()
        fastAdapter = FastAdapter.with(adapter)
        initUI()
    }

    override fun initUI() {
        var title = ""

        if(type.equals("user")){
            isMD = intent.getBooleanExtra(Constants.DATA_ISMD,false)
            if(isMD){
                title = "Daftar MD"
            }else{
                title = "Daftar Supervisor"
            }
        }else if(type.equals("merchant")){
            title = "Daftar Toko"
        }else if(type.equals("product")){
            title = "Daftar Produk"
        }else if(type.equals("display")){
            title = "Daftar Foto Display"
        }else if(type.equals("order")){
            title = "Daftar Order"
        }else if(type.equals("retur")){
            title = "Daftar Retur"
        }else if(type.equals("history")){
            title = "Daftar History MD"
        }
        binding.toolbar.title.text = title

        binding.list.layoutManager = LinearLayoutManager(this)
        binding.list.setAdapter(fastAdapter)
        binding.list.isNestedScrollingEnabled = false

        loadDataByType()

        if(!email!!.contains("md")){
            if(type.equals("user")||type.equals("merchant")||type.equals("product")){
                if(type.equals("user")){
                    if(isMD){
                        binding.buttonAdd.setText("Tambah MD Baru")
                    }else{
                        binding.buttonAdd.setText("Tambah SPV Baru")
                    }
                }else if(type.equals("merchant")){
                    binding.buttonAdd.setText("Tambah Toko Baru")
                }else if(type.equals("product")){
                    binding.buttonAdd.setText("Tambah Produk Baru")
                }
                binding.buttonAdd.visibility = View.VISIBLE
            }
        }

        setListener()
    }

    override fun setListener() {

        binding.toolbar.buttonBack.setOnClickListener {
            onBackPressed()
        }

        fastAdapter.onClickListener = { view, adapter, item, position ->
            var type = item.typeData
            if(type.equals("user")){
                var change =  Intent(this@ListActivity, FormUserActivity::class.java)
                change.putExtra(Constants.DATA_ID,item.id.toString())
                var type = 1
                if(!isMD!!){
                    type = 2
                }
                change.putExtra(Constants.DATA_TYPE,type)
                change.putExtra("email",email)
                startActivity(change)
            }else if(type.equals("merchant")){
                var change =  Intent(this@ListActivity, FormMerchantActivity::class.java)
                change.putExtra(Constants.DATA_ID,item.id.toString())
                change.putExtra("email",email)
                startActivity(change)
            }else if(type.equals("product")){
                var change =  Intent(this@ListActivity, FormProductActivity::class.java)
                change.putExtra(Constants.DATA_ID,item.id.toString())
                change.putExtra("email",email)
                startActivity(change)
            }else if(type.equals("display")){
                var change =  Intent(this@ListActivity, FormDisplayActivity::class.java)
                change.putExtra(Constants.DATA_ID,item.id.toString())
                change.putExtra("email",email)
                change.putExtra(Constants.DATA_ISMD,false)
                startActivity(change)
            }else if(type.equals("order")){
                var change =  Intent(this@ListActivity, FormTransactionActivity::class.java)
                change.putExtra(Constants.DATA_ID,item.id.toString())
                change.putExtra("email",email)
                change.putExtra(Constants.DATA_ISMD,false)
                var storeName = item.name!!.replace("Order ","")
                change.putExtra(Constants.DATA_NAME,storeName)
                change.putExtra(Constants.DATA_TYPE,0)
                startActivity(change)
            }else if(type.equals("retur")){
                var change =  Intent(this@ListActivity, FormTransactionActivity::class.java)
                change.putExtra(Constants.DATA_ID,item.id.toString())
                change.putExtra("email",email)
                change.putExtra(Constants.DATA_ISMD,false)
                var storeName = item.name!!.replace("Retur ","")
                change.putExtra(Constants.DATA_NAME,storeName)
                change.putExtra(Constants.DATA_TYPE,1)
                startActivity(change)
            }else if(type.equals("history")){
                var change =  Intent(this@ListActivity, HistoryActivity::class.java)
                change.putExtra(Constants.DATA_ID,item.id.toString())
                change.putExtra(Constants.DATA_NAME,item.name)
                startActivity(change)
            }
            false
        }
        fastAdapter.addEventHook(object : ClickEventHook<AdapterData>(){
            override fun onBind(viewHolder: RecyclerView.ViewHolder): View? {
                return if (viewHolder is BindingViewHolder<*> && viewHolder.binding is ItemAdapterDataBinding) {
                    (viewHolder.binding as ItemAdapterDataBinding).delete
                }else{
                    null
                }
            }
            override fun onClick(v: View, position: Int, fastAdapter: FastAdapter<AdapterData>, item: AdapterData) {
                adapter.remove(position)
                fastAdapter.notifyAdapterDataSetChanged()
            }
        })
        binding.buttonAdd.setOnClickListener {
            if(type.equals("user")){
                var change =  Intent(this@ListActivity,FormUserActivity::class.java)
                change.putExtra(Constants.DATA_ID,"-1")
                var type = 1
                if(!isMD!!){
                    type = 2
                }
                change.putExtra(Constants.DATA_TYPE,type)
                change.putExtra("email",email)
                startActivity(change)
            }else if(type.equals("merchant")){
                var change =  Intent(this@ListActivity, FormMerchantActivity::class.java)
                change.putExtra("email",email)
                change.putExtra(Constants.DATA_ID,"-1")
                startActivity(change)
            }else if(type.equals("product")){
                var change =  Intent(this@ListActivity, FormProductActivity::class.java)
                change.putExtra(Constants.DATA_ID,"-1")
                change.putExtra("email",email)
                startActivity(change)
            }
        }
    }

    override fun onBackPressed() {
        finish()
    }

    fun loadDataByType(){
        if(type.equals("user")){
            loadData()
        }else if(type.equals("merchant")){
            loadDataMerchant()
        }else if(type.equals("product")){
            loadDataProduct()
        }else if(type.equals("display")){
            loadDataDisplay()
        }else if(type.equals("order")){
            loadDataTransaction(0)
        }else if(type.equals("retur")){
            loadDataTransaction(1)
        }else if(type.equals("history")){
            loadData()
        }
    }

    fun loadData(){
        for(i in 0 .. 9){
            var item = AdapterData()
            item.main = this@ListActivity
            item.id = i+1
            item.name = (if (isMD) "Pegawai " else "Supervisor ") + (i+1).toString()
            item.icon = R.drawable.ic_person_black_24dp
            if(type.equals("user")){
                item.isDetail = false
                item.isDelete = true
                item.isMD = isMD
                item.typeData = "user"
            }else{
                item.isDetail = true
                item.isDelete = false
                item.isMD = true
                item.typeData = "history"
            }
            adapter.add(item)
        }

    }

    fun loadDataMerchant(){
        for(i in 0 .. 9){
            var item = AdapterData()
            item.main = this@ListActivity
            item.id = i+1
            item.name = "Toko "+ (i+1).toString()
            item.icon = R.drawable.ic_front_store
            item.isDetail = false
            item.isDelete = true
            item.typeData = "merchant"
            adapter.add(item)
        }
    }

    fun loadDataProduct(){
        for(i in 0 .. 9){
            var item = AdapterData()
            item.main = this@ListActivity
            item.id = i+1
            item.name = "Produk "+ (i+1).toString()
            item.icon = R.drawable.ic_shopping_cart_black_24dp
            item.isDetail = false
            item.isDelete = true
            item.typeData = "product"
            adapter.add(item)
        }
    }

    fun loadDataDisplay(){
        for(i in 0 .. 9){
            var item = AdapterData()
            item.main = this@ListActivity
            item.id = i+1
            item.name = "Foto Display toko  "+ (i+1).toString()
            item.icon = R.drawable.ic_add_a_photo_black_24dp
            item.isDetail = false
            item.isDelete = true
            item.typeData = "display"
            adapter.add(item)
        }
    }

    fun loadDataTransaction(typeInd:Int){
        for(i in 0 .. 9){
            var name = ""
            var icon = 0
            if(typeInd == 0){
                name = "Order Toko "
                icon = R.drawable.ic_add_shopping_cart_black_24dp
            }else{
                name = "Retur Toko "
                icon = R.drawable.ic_add_shopping_cart_black_24dp
            }
            var random = Random().nextInt(10)
            var item = AdapterData()
            item.main = this@ListActivity
            item.id = i+1
            item.name = name+(random+1).toString()
            item.icon = icon
            item.isDetail = false
            item.isDelete = true

            var trans = Transaction()
            trans.id = (i+1).toString()
            trans.id_merchant = (random+1).toString()
            var merchant = Merchant()
            merchant.id = (random+1).toString()
            merchant.name = "Toko "+(random+1).toString()
            trans.merchant = merchant

            item.itemTransaction = trans
            if(typeInd == 0){
                item.typeData = "order"
            }else{
                item.typeData = "retur"
            }
            adapter.add(item)
        }
    }
}
