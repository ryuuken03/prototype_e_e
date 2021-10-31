package mapan.prototype.ee.config

import io.realm.android.BuildConfig

/***
 * Created By Mohammad Toriq on 18/09/2021
 */
class Constants {
    companion object {

        val IS_PRODUCTION: Boolean = getIsProduction(false)
        val IS_LOG: Boolean = getIsLog()

        val REALM_NAME = "p-e-e.realm"
        val REALM_VERSION:Long = 0

        val CONNECT_TIMEOUT : Long = 60
        val READ_TIMEOUT: Long = 60
        val WRITE_TIMEOUT: Long = 60
        val IMG_CONNECT_TIMEOUT : Long = 30
        val IMG_READ_TIMEOUT: Long = 30
        val IMG_WRITE_TIMEOUT: Long = 30

        val NOTIF_TIME = 30
        val FINISH_TIME = 60
        val NOTIF_BREAK_TIME = 30
        val FINISH_BREAK_TIME = 60

        val permissionRequestCode = 0
        val imageCaptureRequestCode = 1
        val selectImageRequestCode = 2
        val chooseLocationRequestCode = 3
        val signInRequestCode = 4
        val phoneRequestCode = 5
        val storageRequestCode = 6
        val cropResultRequestCode = 7

        val DATA_ID = "ID"
        val DATA_NAME = "NAME"
        val DATA_OBJ = "OBJ"
        val DATA_LAT = "LAT"
        val DATA_LNG = "LNG"
        val DATA_ISMD = "ISMD"
        val DATA_TYPE = "TYPE"
        val DATA_TIME = "TIME"
        val DATA_NOTIF = "NOTIF"

        var DATE_OUT_FORMAT_DEF = "EEEE, d MMMM yyyy"
        var DATE_OUT_FORMAT_DEF2 = "d MMM yyyy"
        var DATE_OUT_FORMAT_DEF2_FULL = "d MMMM yyyy"
        var DATE_OUT_FORMAT_DEF3 = "yyyy-MM-dd"
        var DATE_OUT_FORMAT_DEF4 = "yyyy-MM-dd HH:mm:ss"
        var DATE_OUT_FORMAT_DEF5 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        var DATE_OUT_FORMAT_DEF_FULL = "HH:mm d MMM yyyy"
        var TIME_OUT_FORMAT = "HH:mm:ss"

        fun getIsProduction(status: Boolean): Boolean {
            return if (!BuildConfig.DEBUG) {
                true
            } else {
                status
            }
        }

        fun getIsLog(): Boolean {
            return true
//            return if (!BuildConfig.DEBUG) {
//                false
//            } else {
//                true
//            }
        }

        fun getListExtentionImage() : ArrayList<String>{
            var list = ArrayList<String>()
            list.add("jpg")
            list.add("jpeg")
            list.add("png")

            return list
        }
    }
}