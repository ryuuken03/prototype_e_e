package mapan.prototype.ee.module.adminspv

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import mapan.prototype.ee.R
import mapan.prototype.ee.adapter.AdapterData
import mapan.prototype.ee.adapter.AdapterMenu
import mapan.prototype.ee.adapter.AdapterProductTransaction
import mapan.prototype.ee.config.Constants
import mapan.prototype.ee.databinding.ActivityDashboardBinding
import mapan.prototype.ee.databinding.ActivityFormTransactionBinding
import mapan.prototype.ee.databinding.ActivityFormUserBinding
import mapan.prototype.ee.model.Place
import mapan.prototype.ee.model.Product
import mapan.prototype.ee.model.Transaction
import mapan.prototype.ee.module.core.BaseActivity
import mapan.prototype.ee.module.main.LoginActivity
import mapan.prototype.ee.util.Log
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class FormTransactionActivity : BaseActivity() {
    lateinit var binding: ActivityFormTransactionBinding
    lateinit var fastAdapter: FastAdapter<AdapterProductTransaction>
    lateinit var adapter: ItemAdapter<AdapterProductTransaction>
    var listProduct = ArrayList<Product>()
    var id: String?= null
    var email: String?= null
    var type: Int?= null
    var isMD: Boolean = true
    var total : Long= 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormTransactionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initConfig()
    }

    override fun initConfig() {
        email = intent.getStringExtra("email")
        id = intent.getStringExtra(Constants.DATA_ID)
        type = intent.getIntExtra(Constants.DATA_TYPE,-1)
        isMD = intent.getBooleanExtra(Constants.DATA_ISMD,false)
        adapter = ItemAdapter<AdapterProductTransaction>()
        fastAdapter = FastAdapter.with(adapter)
        initUI()
    }

    override fun initUI() {
        var title = ""
        if(type == 0) {
            title = "Order Produk"
        } else{
            title = "Retur Produk"
        }
        binding.toolbar.title.text = title

        var layoutManager = LinearLayoutManager(this)
        binding.list.layoutManager = layoutManager
        binding.list.setAdapter(fastAdapter)
        binding.list.addItemDecoration(DividerItemDecoration(this, layoutManager.getOrientation()))
        binding.list.isNestedScrollingEnabled = false
        loadDataProduct()
        if(isMD!!){
            loadData()
        }else{
            loadData()
        }

        setListener()
    }

    override fun setListener() {
        binding.toolbar.buttonBack.setOnClickListener {
            onBackPressed()
        }

        binding.buttonSave.setOnClickListener {

        }
    }

    fun changeTotal(){
        total = 0
        for(item in adapter.adapterItems){
            total += (item.price!!*item.stock!!)
        }
        var formatter = NumberFormat.getNumberInstance(Locale.ITALIAN)
        binding.totalpayment.setText("Rp. " +formatter.format(total))
    }

    fun loadData(){
        binding.nameMerchant.setText(intent.getStringExtra(Constants.DATA_NAME))
        binding.totalpayment.setText(total.toString())

        if(type == 1){
            binding.icon1.visibility = View.GONE
            binding.label1.visibility = View.GONE
            binding.totalpayment.visibility = View.GONE
        }
        if(!isMD!!){
            binding.buttonAdd.visibility = View.GONE
            binding.buttonSave.visibility = View.GONE
            for(i in 0 .. 9) {
                var item = AdapterProductTransaction()
                item.id = i+1
                item.main = this
                item.productId = (Random().nextInt(10)+1).toString()
                item.productName = "Produk "+item.productId
                item.stock = 100
                item.price = 10000
                item.note = "Catatan Produk"+item.productId
                item.typeData = type
                item.isEditable = false
                adapter.add(item)
            }

            changeTotal()
        }else{
            binding.buttonSave.visibility = View.VISIBLE

            if(adapter.adapterItemCount == 0){
                addProduct()
            }


            binding.buttonAdd.setOnClickListener {
                addProduct()
                adapter.clear()
//                adapter.notifyDataSetChanged()
            }

            binding.buttonSave.setOnClickListener {

            }
        }
    }

    fun loadDataProduct(){
        for(i in 0 .. 9){
            var item = Product()
            item.id = (i+1).toString()
            item.name = "Produk "+ (i+1).toString()
            item.price = "10000"
            item.count = "100"
            item.note = "Catatan Produk "+ (i+1).toString()
            item.description = "Catatan Produk "+ (i+1).toString()
            item.image = "https://disparbud.gresikkab.go.id/wp-content/uploads/2020/05/pudak-@ndari_adhiansyah-fix.png"
            item.created_at = "2021-10-02T09:10:20.000Z"
            item.updated_at = "2021-10-02T09:10:20.000Z"
            item.status = "Aktif"
            listProduct.add(item)
        }
    }

    fun addProduct(){
        var item = AdapterProductTransaction()
        item.id = adapter.adapterItemCount+1
        item.main = this
        item.productId = ""
        item.stock = 0
        item.price = 0
        item.note = ""
        item.typeData = type
        item.isEditable = true
        adapter.add(item)
    }


    override fun onBackPressed() {
        finish()
    }
}
