package mapan.prototype.ee.module.md

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import mapan.prototype.ee.adapter.AdapterHistory
import mapan.prototype.ee.config.Constants
import mapan.prototype.ee.databinding.FragmentHistoryBinding
import mapan.prototype.ee.model.Presence
import mapan.prototype.ee.util.InitializerUi
import mapan.prototype.ee.util.Util

class HistoryFragment : Fragment(), InitializerUi {

    private var _binding: FragmentHistoryBinding?= null
    private val binding get() = _binding!!
    private lateinit var main: MainActivity
    lateinit var fastAdapter: FastAdapter<AdapterHistory>
    lateinit var adapter: ItemAdapter<AdapterHistory>
    var listData = java.util.ArrayList<Presence>()

    companion object {
        fun newInstance(): Fragment = HistoryFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val view = binding.root

        main = getActivity() as MainActivity

        initConfig()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }

    override fun onResume() {
        super.onResume()
    }

    override fun initConfig() {
        adapter = ItemAdapter<AdapterHistory>()
        fastAdapter = FastAdapter.with(adapter)
        initUI()
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
            item.date = Util.convertDate(data.created_at!!,
                Constants.DATE_OUT_FORMAT_DEF5,
                Constants.DATE_OUT_FORMAT_DEF_FULL)
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

    override fun initUI() {

        binding.list.layoutManager = LinearLayoutManager(main)
        binding.list.setAdapter(fastAdapter)
        binding.list.isNestedScrollingEnabled = false

        loadDataHistory()
        setListener()
    }

    override fun setListener() {

        fastAdapter.onClickListener = { view, adapter, item, position ->

            var change = Intent(activity, LocationMerchantActivity::class.java)
            change.putExtra("email", main.email)
            change.putExtra(Constants.DATA_NAME,item.name)
            change.putExtra(Constants.DATA_LAT,listData!!.get(position)!!.latitude)
            change.putExtra(Constants.DATA_LNG,listData!!.get(position)!!.longitude)
            startActivity(change)
            false
        }
    }
}
