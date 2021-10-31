package mapan.prototype.ee.module.adminspv

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.method.PasswordTransformationMethod
import android.util.Base64
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import mapan.prototype.ee.R
import mapan.prototype.ee.adapter.*
import mapan.prototype.ee.config.Constants
import mapan.prototype.ee.databinding.ActivityDashboardBinding
import mapan.prototype.ee.databinding.ActivityFormDisplayBinding
import mapan.prototype.ee.databinding.ActivityFormTransactionBinding
import mapan.prototype.ee.databinding.ActivityFormUserBinding
import mapan.prototype.ee.model.Place
import mapan.prototype.ee.model.Transaction
import mapan.prototype.ee.module.core.BaseActivity
import mapan.prototype.ee.module.main.LoginActivity
import mapan.prototype.ee.util.Log
import mapan.prototype.ee.util.Util
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class FormDisplayActivity : BaseActivity() {
    lateinit var binding: ActivityFormDisplayBinding
    lateinit var fastAdapter: FastAdapter<AdapterProductDisplay>
    lateinit var adapter: ItemAdapter<AdapterProductDisplay>
    var listProduct = ArrayList<AdapterProductDisplay>()
    var id: String?= null
    var email: String?= null
    var type: Int?= null
    var isMD: Boolean = true

    var dateDialog : DatePickerDialog.OnDateSetListener ?= null
    var pos = -1
    private lateinit var file: File
    private var images: ArrayList<Uri> = ArrayList<Uri>()
    private var base64Images: ArrayList<String> = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormDisplayBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initConfig()
    }

    override fun initConfig() {
        email = intent.getStringExtra("email")
        id = intent.getStringExtra(Constants.DATA_ID)
        type = intent.getIntExtra(Constants.DATA_TYPE,-1)
        isMD = intent.getBooleanExtra(Constants.DATA_ISMD,false)
        adapter = ItemAdapter<AdapterProductDisplay>()
        fastAdapter = FastAdapter.with(adapter)
        initUI()
    }

    override fun initUI() {
        binding.toolbar.title.text = "Foto Display"

        var layoutManager = LinearLayoutManager(this)
        binding.list.layoutManager = layoutManager
        binding.list.setAdapter(fastAdapter)
        binding.list.addItemDecoration(DividerItemDecoration(this, layoutManager.getOrientation()))
        binding.list.isNestedScrollingEnabled = false
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

        binding.buttonReset.setOnClickListener {
            reset()
        }
        binding. buttonSearch.setOnClickListener {
            filter()
        }

        binding.buttonSave.setOnClickListener {

        }
    }

    fun loadProduct(){
        for(i in 0 ..9){
            var item = AdapterProductDisplay()
            item.id = listProduct.size+1
            item.name = "Produk "+item.id.toString()
            if(isMD){
                item.merchant = 0
                item.werehause = 0
                item.isCheck = false
                item.isEnable = true
            }else{
                item.merchant = 50
                item.werehause = 100
                item.isCheck = false
                item.calendar = Util.parseDate("2021-12-02T09:10:20.000Z",Constants.DATE_OUT_FORMAT_DEF5)
                item.isEnable = false
            }
            listProduct.add(item)
        }
        adapter.add(listProduct)
    }

    fun loadData(){
        loadProduct()
        if(!isMD!!){

            binding.buttonPicture.visibility = View.GONE
            binding.buttonSave.visibility = View.GONE
            var stringImages = ArrayList<String>()
            for(i in 0 ..9) {
                var image = "https://arsitagx-master-article.s3-ap-southeast-1.amazonaws.com/article-photo/241/48768e32aa84accaff381167e95285cb.jpg"
                stringImages.add(image)
            }
            var imageAdapter = ImagePagerAdapter(this,stringImages,null,
                ImageView.ScaleType.CENTER_INSIDE)
            binding.productImageViewPager.adapter = imageAdapter
            binding.productImageIndicator.setViewPager(binding.productImageViewPager)
            imageAdapter.notifyDataSetChanged()


        }else{

            binding.buttonSave.visibility = View.VISIBLE

            binding.buttonPicture.setOnClickListener(View.OnClickListener {
//                if (PermissionHelper.checkCameraPermission(this)) {
                    takePicture()
//                }
            })

            dateDialog = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                var cal = Calendar.getInstance()
                cal.set(year,month,dayOfMonth)
                adapter!!.getAdapterItem(pos)!!.calendar = cal.time
                fastAdapter!!.notifyDataSetChanged()
            }
            binding.buttonSave.setOnClickListener {

            }
        }
    }


    private fun reset(){
        binding.search.setText("")
        filter()
    }

    private fun filter(){
        adapter!!.clear()
        var sentence = binding.search.text.toString()
        var newData = ArrayList<AdapterProductDisplay>()
        adapter.clear()
        listProduct!!.forEach {
            var isAdd = false
            if(!sentence.equals("")){
                if(it.name!!.toLowerCase().contains(sentence.toLowerCase())){
                    isAdd = true
                }
            }else{
                isAdd = true
            }
            if(isAdd){
                newData.add(it)
            }
        }
        adapter.add(newData)

        if(newData.size > 0){
            binding.empty.visibility = View.GONE
            binding.list.visibility = View.VISIBLE
        }else{
            binding.empty.visibility = View.VISIBLE
            binding.list.visibility = View.GONE
        }
    }

    fun updateViewImage(){
        var adapter = ImagePagerAdapter(this,null,images,
            ImageView.ScaleType.CENTER_INSIDE)
        binding.productImageViewPager.adapter = adapter
        binding.productImageIndicator.setViewPager(binding.productImageViewPager)
        adapter.notifyDataSetChanged()
    }

    fun deleteImage(position:Int){
        images.removeAt(position)
        base64Images.removeAt(position)
        updateViewImage()
    }


    fun pickExpDate(position:Int){
        var cal = Calendar.getInstance()
        val dialogDate = DatePickerDialog(this@FormDisplayActivity, dateDialog,
            cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH))
        pos = position
        dialogDate.show()
    }

    private fun convertBitmapToString(bitmap: Bitmap): String {
        var baos = ByteArrayOutputStream()
        var width = bitmap.width
        var height = bitmap.height
        var scale =  (1000.toDouble()/width.toDouble())
        if(height > width){
            scale =  (1000.toDouble()/height.toDouble())
        }
        var wScale = (width*scale).toInt()
        var hScale = (height*scale).toInt()
        var resizeBitmap = Bitmap.createScaledBitmap(bitmap, wScale, hScale, false)
        resizeBitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos)
        var stringImage = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)

        return stringImage
    }

    fun takePicture() {
        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val newdir = File(dir.getPath() + "/enakeco/")
        newdir.mkdirs()

        file = File(newdir.absolutePath + "/" + System.currentTimeMillis() + ".jpg")
        try {
            file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val uri: Uri
        val intentCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            uri = FileProvider.getUriForFile(this, applicationContext.packageName + ".mapan.prototype.ee.provider", file!!)
            intentCamera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        } else {
            uri = Uri.fromFile(file)
        }

        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(intentCamera, Constants.imageCaptureRequestCode)
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.imageCaptureRequestCode) {
            if (resultCode == Activity.RESULT_OK) {
                var resultUriImage = Uri.fromFile(File(file.path))
                images.add(resultUriImage)
                var bitmap = BitmapFactory.decodeFile(file.path)

                var base64 = convertBitmapToString(bitmap)
                base64Images.add(base64)
                updateViewImage()
            } else {
                file.delete();

            }
        }
    }
}
