package mapan.prototype.ee.util

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import mapan.prototype.ee.config.Constants

class PermissionHelper {

    companion object  {

    fun checkCameraPermission(context: Activity): Boolean {
        val cameraCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)

        val writeExternalCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (cameraCheck == PackageManager.PERMISSION_GRANTED && writeExternalCheck == PackageManager.PERMISSION_GRANTED) {
            return true
        } else {
            ActivityCompat.requestPermissions(context,
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    Constants.imageCaptureRequestCode)
            return false
        }
    }

    fun checkReadPermission(context: Activity): Boolean {
        val readExternalCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)

        if (readExternalCheck == PackageManager.PERMISSION_GRANTED) {
            return true
        } else {
            ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.selectImageRequestCode)
            return false
        }
    }

    fun checkLocationPermission(context: Activity): Boolean {
        val readExternalCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)

        if (readExternalCheck == PackageManager.PERMISSION_GRANTED) {
            return true
        } else {
            ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    Constants.chooseLocationRequestCode)
            return false
        }
    }

    fun checkCallPermission(context: Activity): Boolean {
        val phoneCall = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)

        if (phoneCall == PackageManager.PERMISSION_GRANTED) {
            return true
        } else {
            ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.CALL_PHONE),
                    Constants.phoneRequestCode)
            return false
        }
    }

    fun checkStoragePermission(context: Activity): Boolean {
        val writeExternal = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val readxternal = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)

        if (writeExternal == PackageManager.PERMISSION_GRANTED || readxternal == PackageManager.PERMISSION_GRANTED) {
            return true
        } else {
            ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.storageRequestCode)
            return false
        }
    }
}
}