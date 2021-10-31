package mapan.prototype.ee.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import mapan.prototype.ee.R
import mapan.prototype.ee.databinding.ItemAdapterHistoryBinding
import mapan.prototype.ee.databinding.ItemAdapterMenuBinding
import mapan.prototype.ee.model.MerchantDisplay
import mapan.prototype.ee.model.Transaction
import mapan.prototype.ee.module.adminspv.ListActivity
import mapan.prototype.ee.util.InitializerUi


open class AdapterHistory() : AbstractBindingItem<ItemAdapterHistoryBinding>(),
    InitializerUi {
    var id: Int? = null
    var date: String? = null
    var name: String? = null
    var status: String? = null
    lateinit var binding: ItemAdapterHistoryBinding

    /** defines the type defining this item. must be unique. preferably an id */
    override val type: Int
        get() = R.id.item_adapter_history

    override fun bindView(binding: ItemAdapterHistoryBinding, payloads: List<Any>) {
        this.binding = binding
        initConfig()
    }

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ItemAdapterHistoryBinding {
        return ItemAdapterHistoryBinding.inflate(inflater, parent, false)
    }

    override fun initConfig() {
        initUI()
    }

    override fun initUI() {
        binding.date.setText(date!!)
        binding.status.setText(status!!)
        setListener()

    }

    override fun setListener() {
    }


}