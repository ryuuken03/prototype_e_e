package mapan.prototype.ee.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import mapan.prototype.ee.R
import mapan.prototype.ee.config.Constants
import mapan.prototype.ee.databinding.ItemAdapterMenuBinding
import mapan.prototype.ee.databinding.ItemAdapterProductDisplayBinding
import mapan.prototype.ee.model.MerchantDisplay
import mapan.prototype.ee.model.Transaction
import mapan.prototype.ee.module.adminspv.FormDisplayActivity
import mapan.prototype.ee.module.adminspv.ListActivity
import mapan.prototype.ee.util.InitializerUi
import mapan.prototype.ee.util.Util
import java.util.*


open class AdapterProductDisplay() : AbstractBindingItem<ItemAdapterProductDisplayBinding>(),
    InitializerUi {
    var main: FormDisplayActivity? = null
    var id: Int? = null
    var name: String? = null
    var merchant: Int? = null
    var werehause: Int? = null
    var expired: String? = null
    var calendar: Date? = null
    var isCheck : Boolean? =  null
    var isEnable : Boolean? =  null
    lateinit var binding: ItemAdapterProductDisplayBinding

    /** defines the type defining this item. must be unique. preferably an id */
    override val type: Int
        get() = R.id.item_adapter_product_display

    override fun bindView(binding: ItemAdapterProductDisplayBinding, payloads: List<Any>) {
        this.binding = binding
        initConfig()
    }

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ItemAdapterProductDisplayBinding {
        return ItemAdapterProductDisplayBinding.inflate(inflater, parent, false)
    }

    override fun initConfig() {
        initUI()
    }

    override fun initUI() {

        if(calendar !=null){
            expired = Util.formatDate(calendar!!, Constants.DATE_OUT_FORMAT_DEF3)
            binding.exp.setText(Util.formatDate(calendar!!,Constants.DATE_OUT_FORMAT_DEF))
        }else{
            expired = ""
            binding.exp.setText("")
        }
        binding.textProduct.setText(name)
        binding.textMerchant.setText(merchant.toString())
        binding.textWerehouse.setText(werehause.toString())
        binding.textProduct.setText(name)
        binding.textProduct.isEnabled = false
        binding.check.isEnabled = isEnable!!
        binding.check.isChecked = isCheck!!
        binding.textMerchant.isEnabled =  isCheck!!
        binding.textWerehouse.isEnabled =  isCheck!!

        setListener()

    }

    override fun setListener() {
        var position = id!!-1
        binding.check.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.textMerchant.isEnabled = isChecked
            binding.textWerehouse.isEnabled = isChecked
            isCheck = isChecked
        }

        binding.textMerchant.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(sentence: CharSequence?, start: Int, before: Int, count: Int) {
                binding.textMerchant.removeTextChangedListener(this)
                if(!sentence.toString().equals(merchant.toString())){
                    if(!sentence.toString().equals("")){
                        merchant = sentence.toString().toInt()
                    }else{
                        merchant = 0
                    }
                    binding.textMerchant.setText(merchant.toString())
                    binding.textMerchant.setSelection(merchant!!.toString().length)
                }
                binding.textMerchant.addTextChangedListener(this)
            }
        })
        binding.textWerehouse.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(sentence: CharSequence?, start: Int, before: Int, count: Int) {
                binding.textWerehouse.removeTextChangedListener(this)
                if(!sentence.toString().equals(werehause.toString())){
                    if(!sentence.toString().equals("")){
                        werehause = sentence.toString().toInt()
                    }else{
                        werehause = 0
                    }
                    binding.textWerehouse.setText(werehause.toString())
                    binding.textWerehouse.setSelection(werehause!!.toString().length)
                }
                binding.textWerehouse.addTextChangedListener(this)
            }
        })

        binding.linearExp.setOnClickListener {
            if(isCheck!!){
                main!!.pickExpDate(position)
            }
        }
    }


}