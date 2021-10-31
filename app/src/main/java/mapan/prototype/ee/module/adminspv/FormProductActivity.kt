package mapan.prototype.ee.module.adminspv

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import mapan.prototype.ee.R
import mapan.prototype.ee.adapter.AdapterMenu
import mapan.prototype.ee.config.Constants
import mapan.prototype.ee.databinding.ActivityFormMerchantBinding
import mapan.prototype.ee.databinding.ActivityFormProductBinding
import mapan.prototype.ee.model.Place
import mapan.prototype.ee.module.core.BaseActivity
import mapan.prototype.ee.util.PermissionHelper
import mapan.prototype.ee.util.Util
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

class FormProductActivity : BaseActivity() {
    lateinit var binding: ActivityFormProductBinding
    var id: String?= null
    var email: String?= null
    var type: Int?= null
    var first = false
    var file: File?= null
    var base64 = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormProductBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initConfig()
    }

    override fun initConfig() {
        email = intent.getStringExtra("email")
        id = intent.getStringExtra(Constants.DATA_ID)
        initUI()
    }

    override fun initUI() {
        binding.toolbar.title.text = "Data Produk"
        loadData()
        setListener()
    }

    override fun setListener() {
        binding.toolbar.buttonBack.setOnClickListener {
            onBackPressed()
        }

        binding.area.setOnClickListener {
            createSourceDialog()
        }

        binding.buttonSave.setOnClickListener {

        }
    }

    fun loadData(){
        if(!id.equals("-1")){
            var name = "Produk "+id
            var price = "10000"
            var description = "Deskripsi Produk "+id
            var emailData = email!!
            binding.textProduct.setText(name)
            binding.textSellPrice.setText(price)
            binding.textDescription.setText(description)

            var image = "https://samudranesia.id/wp-content/uploads/2020/08/Kios-warga.jpg"

            Util.loadImage(this,image,binding.photo)

            binding.takePhoto.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        finish()
    }

    private fun createRecyclerDialog(sources: ArrayList<AdapterMenu>) {
//        val adapter = FlexibleAdapter(sources)
        var adapter = ItemAdapter<AdapterMenu>()
        var fastAdapter = FastAdapter.with(adapter)
        val recycler = RecyclerView(this)
        recycler.overScrollMode = View.OVER_SCROLL_NEVER

        recycler.adapter = fastAdapter
        var layoutManager  = LinearLayoutManager(this)
        recycler.addItemDecoration(DividerItemDecoration(this, layoutManager.getOrientation()))
        recycler.layoutManager = layoutManager

        fastAdapter.onClickListener = { view, adapter, item, position ->
            dialog.dismiss()
            when (position){
                0->{
                    if (PermissionHelper.checkCameraPermission(this@FormProductActivity)) {
                        takePicture()
                    }
                }
                1 ->{
                    if (PermissionHelper.checkReadPermission(this@FormProductActivity)) {
                        selectPicture()
                    }
                }
            }
            false
        }

        showRecyclerDialog(getString(R.string.text_change_profile), recycler, true, false)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == Constants.selectImageRequestCode){
            if(grantResults[0] == PackageManager.PERMISSION_DENIED) {
                if (PermissionHelper.checkReadPermission(this@FormProductActivity)) {
                    selectPicture()
                }
            }
        }else if (requestCode == Constants.imageCaptureRequestCode){
            if(grantResults[0] == PackageManager.PERMISSION_DENIED) {
                if (PermissionHelper.checkReadPermission(this@FormProductActivity)) {
                    takePicture()
                }
            }
        }
    }

    fun createSourceDialog() {
        var menus =  arrayListOf<AdapterMenu>()

        val menu = AdapterMenu()
        menu.icon = R.drawable.ic_photo_camera_black_24dp
        menu.name = getString(R.string.text_camera)
        menus.add(menu)

        val menu1 = AdapterMenu()
        menu1.icon = R.drawable.ic_photo_library_black_24dp
        menu1.name = getString(R.string.text_photo)
        menus.add(menu1)

        createRecyclerDialog(menus)
    }

    fun takePicture() {
        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val newdir = File(dir.getPath() + "/enakeco/")
        newdir.mkdirs()

        file = File(newdir.absolutePath + "/" + System.currentTimeMillis() + ".jpg")
        try {
            file!!.createNewFile()
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

    fun selectPicture() {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"

        startActivityForResult(photoPickerIntent, Constants.selectImageRequestCode)
    }

    fun getRealPathFromURI(contentURI: Uri, context: Activity): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.managedQuery(contentURI, projection, null, null, null) ?: return null
        val column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        if (cursor.moveToFirst()) {
            return cursor.getString(column_index)
        } else {
            return null
        }
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == PickLocationActivity.FORM_LOCATION) {
//            if (resultCode == Activity.RESULT_OK) {
//                val address = data?.getStringExtra(PickLocationActivity.LOCATION_NAME)
//                val latLng = data?.getParcelableExtra<LatLng>(PickLocationActivity.LOCATION_LATLNG)
//                location = Place()
//                location!!.latitude = latLng!!.latitude
//                location!!.longitude = latLng!!.longitude
//                var message = latLng!!.latitude.toString()+":"+latLng!!.longitude
//                coordinate.setText(getString(R.string.text_coordinate_choosed))
//            }
//        }else
        if(requestCode == Constants.imageCaptureRequestCode){
            if (resultCode == Activity.RESULT_OK) {
                var fullPath = file!!.path

                var bitmap = BitmapFactory.decodeFile(fullPath)

                base64 = convertBitmapToString(bitmap)
                Util.loadImageBitmap(binding.photo,bitmap)
                binding.takePhoto.visibility = View.GONE

            }
//            else {
//                file.delete();
//            }
        }else if(requestCode == Constants.selectImageRequestCode){
            if (resultCode == Activity.RESULT_OK) {
                var selectedImage = data!!.data!!
                var fullPath = getRealPathFromURI(selectedImage,this)!!

                var bitmap = BitmapFactory.decodeFile(fullPath)
                base64 = convertBitmapToString(bitmap)
                Util.loadImageBitmap(binding.photo,bitmap)
                binding.takePhoto.visibility = View.GONE
            }
        }
    }
}
