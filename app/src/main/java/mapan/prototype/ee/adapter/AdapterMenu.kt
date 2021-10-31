package mapan.prototype.ee.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import mapan.prototype.ee.R
import mapan.prototype.ee.databinding.ItemAdapterMenuBinding
import mapan.prototype.ee.util.InitializerUi


open class AdapterMenu() : AbstractBindingItem<ItemAdapterMenuBinding>(),
    InitializerUi {
    var id: Int? = null
    var icon: Int? = null
    var name: String? = null
    var isDelete: Boolean = false
    var isDetail: Boolean = true
    lateinit var binding: ItemAdapterMenuBinding

    /** defines the type defining this item. must be unique. preferably an id */
    override val type: Int
        get() = R.id.item_adapter_menu

    override fun bindView(binding: ItemAdapterMenuBinding, payloads: List<Any>) {
        this.binding = binding
        initConfig()
    }

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ItemAdapterMenuBinding {
        return ItemAdapterMenuBinding.inflate(inflater, parent, false)
    }

    override fun initConfig() {
        initUI()
    }

    override fun initUI() {
        binding.name.setText(name!!)
        binding.icon.setImageResource(icon!!)
        if(isDetail){
            binding.detail.visibility = View.VISIBLE
        }else{
            binding.detail.visibility = View.GONE
        }
        if(isDelete){
            binding.delete.visibility = View.VISIBLE
        }else{
            binding.delete.visibility = View.GONE
        }
        setListener()

    }

    override fun setListener() {
    }


}