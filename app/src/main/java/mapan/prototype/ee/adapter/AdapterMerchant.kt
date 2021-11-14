package mapan.prototype.ee.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import mapan.prototype.ee.R
import mapan.prototype.ee.databinding.ItemAdapterMenuBinding
import mapan.prototype.ee.databinding.ItemAdapterMerchantBinding
import mapan.prototype.ee.model.MerchantDisplay
import mapan.prototype.ee.model.Transaction
import mapan.prototype.ee.module.adminspv.ListActivity
import mapan.prototype.ee.util.InitializerUi


open class AdapterMerchant() : AbstractBindingItem<ItemAdapterMerchantBinding>(),
    InitializerUi {
    var id: Int? = null
    var name: String? = null
    var address: String? = null
    lateinit var binding: ItemAdapterMerchantBinding

    /** defines the type defining this item. must be unique. preferably an id */
    override val type: Int
        get() = R.id.item_adapter_merchant

    override fun bindView(binding: ItemAdapterMerchantBinding, payloads: List<Any>) {
        this.binding = binding
        initConfig()
    }

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ItemAdapterMerchantBinding {
        return ItemAdapterMerchantBinding.inflate(inflater, parent, false)
    }

    override fun initConfig() {
        initUI()
    }

    override fun initUI() {
        binding.nameMerchant.setText(name!!)
        binding.addressMerchant.setText(address!!)
        setListener()

    }

    override fun setListener() {
    }


}