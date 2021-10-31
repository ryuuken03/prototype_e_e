package mapan.prototype.ee.module.adminspv

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import mapan.prototype.ee.R
import mapan.prototype.ee.adapter.AdapterMenu
import mapan.prototype.ee.config.Constants
import mapan.prototype.ee.databinding.ActivityDashboardBinding
import mapan.prototype.ee.databinding.ActivityFormUserBinding
import mapan.prototype.ee.model.Place
import mapan.prototype.ee.module.core.BaseActivity
import mapan.prototype.ee.module.main.LoginActivity
import mapan.prototype.ee.util.Log

class FormUserActivity : BaseActivity() {
    lateinit var binding: ActivityFormUserBinding
    var id: String?= null
    var email: String?= null
    var type: Int?= null
    var listArea = ArrayList<Place>()
    var idProvince = 0
    var idCity = 0
    var first = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormUserBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initConfig()
    }

    override fun initConfig() {
        email = intent.getStringExtra("email")
        id = intent.getStringExtra(Constants.DATA_ID)
        type = intent.getIntExtra(Constants.DATA_TYPE,-1)
        initUI()
    }

    override fun initUI() {
        var title = ""
        if(type!!.equals(0)){
            title = "Data Profil"
        }else if(type!!.equals(1)){
            title = "Data MD"
        }else if(type!!.equals(2)){
            title = "Data SPV"
        }
        binding.toolbar.title.text = title
        if(type!!.equals(0)){
            if(email!!.contains("admin")){
                binding.linearProvince.visibility = View.GONE
                binding.linearCity.visibility = View.GONE
                loadData()
            }else{
                loadArea()
            }
        }else{
            loadArea()
        }

        setListener()
    }

    override fun setListener() {
        binding.toolbar.buttonBack.setOnClickListener {
            onBackPressed()
        }

        binding.province.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                if(position > 0 ){
                    idProvince = position
                    if(first && !id.equals("-1")|| id.equals("-1")){
                        idCity = 0
                        var listCity = ArrayList<String>()
                        listCity.add(getString(R.string.text_choose_city).toUpperCase())
                        listArea.get(position-1).city!!.forEach {
                            listCity.add(it.name!!)
                        }
                        val spinnerAdapter2 = ArrayAdapter(this@FormUserActivity,
                            android.R.layout.simple_spinner_dropdown_item, listCity)
                        binding.city.adapter = spinnerAdapter2
                    }
                }
            }

        }
        if(!id.equals("-1")){
            binding.city.setSelection(idCity)
        }

        binding.city.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position > 0){
                    idCity = position
                }
                first = true
            }

        }

        binding.textPassword.setOnTouchListener(View.OnTouchListener { v, event ->

            var DRAWABLE_RIGHT = 2
            if(event.action == MotionEvent.ACTION_UP){
                if(event.rawX >= (binding.textPassword.right - binding.textPassword.compoundDrawables[DRAWABLE_RIGHT].bounds.width())){

                    if(binding.textPassword.transformationMethod == null){
                        binding.textPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                    }else{
                        binding.textPassword.transformationMethod = null
                    }

                    true
                }
            }

            false
        })

        binding.buttonSave.setOnClickListener {

            if(!binding.textPassword.text.toString().equals("")){
                var message = "Password akan diganti jika diisi , Apakah anda teteap ingin lanjut?"
                showPromptDialog(getString(R.string.text_alert), message,
                    getString(R.string.action_ok), View.OnClickListener {
                        dialog.dismiss()
//                        if(validate()){
//                            var user = User()
//                            user.name = textName.text.toString()
//                            user.email = textEmail.text.toString()
//                            user.phone = textPhone.text.toString()
//                            if(id.equals("-1")){
//                                user.password = textPassword.text.toString()
//                            }else{
//                                user.id = id
//                                if(!textPassword.text.toString().equals("")){
//                                    user.password = textPassword.text.toString()
//                                }
//                                user.role = person!!.role
//                                user.status = "1"
//                            }
//                            if(type!! != 0){
//                                user.area = listArea!!.get(idProvince-1).city!!.get(idCity-1).id
//                            }else {
//                                if(!person!!.role!!.equals("ADMIN")){
//                                    user.area = listArea!!.get(idProvince-1).city!!.get(idCity-1).id
//                                }
//                            }
//
//
//                            postData(user)
//                        }
                    }, false)
            }else{
//                if(validate()){
//                    var user = User()
//                    user.name = textName.text.toString()
//                    user.email = textEmail.text.toString()
//                    user.phone = textPhone.text.toString()
//                    if(id.equals("-1")){
//                        user.password = textPassword.text.toString()
//                    }else{
//                        user.id = id
//                        if(!textPassword.text.toString().equals("")){
//                            user.password = textPassword.text.toString()
//                        }
//                        user.role = person!!.role
//                        user.status = "1"
//                    }
//                    if(type!! != 0){
//                        user.area = listArea!!.get(idProvince-1).city!!.get(idCity-1).id
//                    }else {
//                        if(!person!!.role!!.equals("ADMIN")){
//                            user.area = listArea!!.get(idProvince-1).city!!.get(idCity-1).id
//                        }
//                    }
//                    postData(user)
//                }
            }
        }
    }

    fun loadData(){
        if(!id.equals("-1")){
            var name = "Admin"
            if(email!!.contains("spv")){
                name = "Supervisor"
            }
            var emailData = email!!
            var phone = "0808080808"
            binding.textName.setText(name)
            binding.textEmail.setText(emailData)
            binding.textPhone.setText(phone)
            Log.d("OkCheckID",id)
            Log.d("OkCheckType",type.toString())
            Log.d("OkCheckEmail",email)
            if((type == 0 &&!email!!.contains("admin")) || type!! > 0){
                idProvince = 1
                binding.province.setSelection(idProvince)
                var listCity = ArrayList<String>()
                listCity.add(getString(R.string.text_choose_city).toUpperCase())
//                listArea.get(idProvince-1).city!!.forEach {
//                    listCity.add(it.name!!)
//                }
                listArea.get(idProvince-1).city!!.forEach {
                    listCity.add(it.name!!)
                }
                idCity = 1
                val spinnerAdapter2 = ArrayAdapter(this@FormUserActivity,
                    android.R.layout.simple_spinner_dropdown_item, listCity)
                binding.city.adapter = spinnerAdapter2
                binding.city.setSelection(1)

            }
        }
    }

    fun loadArea(){
        var area1 = Place()
        area1.id = "1"
        area1.name = "DKI Jakarta"
        area1.city = ArrayList()
        for(i in 0 .. 9){
            var city = Place()
            city.id = area1.id +(i+1).toString()
            city.name = "Kota Jakarta "+(i+1).toString()
            city.province_id = area1.id
            area1.city!!.add(city)
        }
        listArea.add(area1)
        var area2 = Place()
        area2.id = "2"
        area2.name = "Jawa Barat"
        area2.city = ArrayList()
        for(i in 0 .. 9){
            var city = Place()
            city.id = area2.id +(i+1).toString()
            city.name = "Kota Jawa Barat "+(i+1).toString()
            city.province_id = area2.id
            area2.city!!.add(city)
        }
        listArea.add(area2)
        var area3 = Place()
        area3.id = "3"
        area3.name = "Jawa Tengah"
        area3.city = ArrayList()
        for(i in 0 .. 9){
            var city = Place()
            city.id = area3.id +(i+1).toString()
            city.name = "Kota Jawa Tengah "+(i+1).toString()
            city.province_id = area3.id
            area3.city!!.add(city)
        }
        listArea.add(area3)
        var area4 = Place()
        area4.id = "4"
        area4.name = "Jawa Timur"
        area4.city = ArrayList()
        for(i in 0 .. 9){
            var city = Place()
            city.id = area4.id +(i+1).toString()
            city.name = "Kota Jawa Timur "+(i+1).toString()
            city.province_id = area4.id
            area4.city!!.add(city)
        }
        listArea.add(area4)
        var area5 = Place()
        area5.id = "5"
        area5.name = "Bali"
        area5.city = ArrayList()
        for(i in 0 .. 9){
            var city = Place()
            city.id = area5.id +(i+1).toString()
            city.name = "Kota Bali "+(i+1).toString()
            city.province_id = area5.id
            area5.city!!.add(city)
        }
        listArea.add(area5)

        var listProvince = ArrayList<String>()
        listProvince.add(getString(R.string.text_choose_province).toUpperCase())
        listArea!!.forEach {
            listProvince.add(it.name!!)
        }
        val spinnerAdapter = ArrayAdapter(this@FormUserActivity,
            android.R.layout.simple_spinner_dropdown_item, listProvince)
        binding.province.adapter = spinnerAdapter

        var listCity = ArrayList<String>()
        listCity.add(getString(R.string.text_choose_city).toUpperCase())
        val spinnerAdapter2 = ArrayAdapter(this@FormUserActivity,
            android.R.layout.simple_spinner_dropdown_item, listCity)
        binding.city.adapter = spinnerAdapter2

        if(id.equals("-1")){
            loadData()
        }else{
            loadData()
        }
    }

    override fun onBackPressed() {
        finish()
    }
}
