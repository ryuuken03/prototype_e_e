package mapan.prototype.ee.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import mapan.prototype.ee.R
import mapan.prototype.ee.databinding.ItemAdapterMenuBinding
import mapan.prototype.ee.databinding.ItemAdapterProductTransactionBinding
import mapan.prototype.ee.module.adminspv.FormTransactionActivity
import mapan.prototype.ee.util.InitializerUi
import java.text.NumberFormat
import java.util.*


open class AdapterProductTransaction() : AbstractBindingItem<ItemAdapterProductTransactionBinding>(),
    InitializerUi {
    var main: FormTransactionActivity? = null
    var id: Int? = null
    var productId: String? = null
    var productName: String? = null
    var stock: Int? = null
    var price: Long? = null
    var note: String? = null
    var typeData: Int? = null
    var isEditable: Boolean? = null
    var indexProduct = -1
    lateinit var binding: ItemAdapterProductTransactionBinding

    /** defines the type defining this item. must be unique. preferably an id */
    override val type: Int
        get() = R.id.item_adapter_product_transaction

    override fun bindView(binding: ItemAdapterProductTransactionBinding, payloads: List<Any>) {
        this.binding = binding
        initConfig()
    }

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ItemAdapterProductTransactionBinding {
        return ItemAdapterProductTransactionBinding.inflate(inflater, parent, false)
    }

    override fun initConfig() {
        initUI()
    }

    override fun initUI() {
        if(productName == null){
            var listProdcut = ArrayList<String>()
            listProdcut.add(main!!.getString(R.string.text_choose_product).toUpperCase())

            var index = 0
            main!!.listProduct!!.forEach {
                if(productId.equals(it.id!!)){
                    indexProduct = index
                }
                listProdcut.add(it.name!!)
                index++
            }
            val spinnerAdapter = ArrayAdapter(
                main!!,
                android.R.layout.simple_spinner_dropdown_item, listProdcut)
            binding.product.adapter = spinnerAdapter
            if(main!!.adapter.adapterItemCount == 1){

                binding.delete.visibility = View.GONE
            }else{

                binding.delete.visibility = View.VISIBLE
            }
        }else{
            binding.product.visibility = View.GONE
            binding.productText.visibility = View.VISIBLE
            binding.productText.setText(productName!!)
            var formatter = NumberFormat.getNumberInstance(Locale.ITALIAN)
            binding.textTotalPrice.setText("Rp. " +formatter.format(price!!*stock!!))
        }

        var formatter = NumberFormat.getNumberInstance(Locale.ITALIAN)
        binding.textPrice.setText("Rp. "+formatter.format(price!!*stock!!))
        binding.textPrice.isEnabled = false
        binding.textTotalPrice.isEnabled = false
        binding.textStock.setText(stock.toString())
        binding.textStock.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(sentence: CharSequence?, start: Int, before: Int, count: Int) {
                binding.textStock.removeTextChangedListener(this)
                if(!sentence.toString().equals(stock.toString())){
                    if(!sentence.toString().equals("")){
                        stock = sentence.toString().toInt()
                    }else{
                        stock = 0
                    }
                    binding.textStock.setText(stock.toString())
                    binding.textStock.setSelection(stock!!.toString().length)
                    var formatter = NumberFormat.getNumberInstance(Locale.ITALIAN)
                    binding.textTotalPrice.setText("Rp. "+formatter.format(price!!*stock!!))
                    main!!.changeTotal()
                }
                binding.textStock.addTextChangedListener(this)
            }
        })
        binding.textNote.setText(note)
        binding.textNote.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(sentence: CharSequence?, start: Int, before: Int, count: Int) {
                binding.textNote.removeTextChangedListener(this)
                if(!sentence.toString().equals(note.toString())){
                    note = sentence.toString()
                    binding.textNote.setText(note.toString())
                    binding.textNote.setSelection(note!!.length)
                }
                binding.textNote.addTextChangedListener(this)
            }
        })

        if(type == 1){
            binding.inputTextPrice.visibility = View.GONE
            binding.inputTextTotalPrice.visibility = View.GONE
        }

        if(!isEditable!!){
            binding.textNote.isEnabled = false
            binding.textStock.isEnabled = false
            binding.delete.visibility = View.GONE
        }

        binding.delete.setOnClickListener {
//            main!!.deleteProduct(position)
        }

        setListener()

    }

    override fun setListener() {

        var position = id!!-1
        binding.product.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                var isSelect = true
                for(i in 0 .. main!!.adapter.adapterItemCount-1){
                    if (position != i && pos > 0){
                        var dataLog1 = position.toString()+"!="+i.toString()+"&&"+pos.toString()+">0"
//                                                Log.d("indexChooseCheck"+position.toString()+"3",main!!.listItemProduct!!.get(i).productId)
                        var itemOther = main!!.adapter.getAdapterItem(i)
                        if(main!!.listProduct!!.get(pos-1).id.equals(itemOther.productId)){
                            //                                        if(i != pos){
                            var message = main!!.getString(R.string.error_product_same)

                            main!!.showAlertDialog(main!!.getString(R.string.text_alert),message ,
                                View.OnClickListener {
                                    main!!.dialog.dismiss()
                                },false)
                            isSelect = false
                            binding.product.setSelection(0)
                            productId = ""
                            break
                        }
                    }
                }

                if(isSelect){
                    if(pos > 0){
                        productId = main!!.listProduct!!.get(pos-1).id
                        price = main!!.listProduct!!.get(pos-1).price!!.toLong()
                        var formatter = NumberFormat.getNumberInstance(Locale.ITALIAN)
                        binding.textPrice.setText("Rp. " +formatter.format(price!!))
                        binding.textTotalPrice.setText("Rp. " +formatter.format(price!!*stock!!))
                        main!!.changeTotal()
                    }else{
                        if(indexProduct>-1){
                            binding.product.setSelection(indexProduct+1)
                        }else{
                            productId = ""
                            price = 0
                            var formatter = NumberFormat.getNumberInstance(Locale.ITALIAN)
                            binding.textPrice.setText("Rp. " +formatter.format(price!!))
                            binding.textTotalPrice.setText("Rp. " +formatter.format(price!!*stock!!))
                            main!!.changeTotal()
                        }
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        })
    }


}