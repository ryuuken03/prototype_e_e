package mapan.prototype.ee.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.util.Patterns
import android.widget.ImageView
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import mapan.prototype.ee.R
import mapan.prototype.ee.config.Constants
import java.lang.Exception
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Util {
    companion object {

        fun isNameValid(name: String): Boolean {
            return name.length >= 3
        }

        fun isEmptySpace(text: String):Boolean{
            var textChar = text.removeSuffix(" ")
            return textChar.length <= 0
        }

        fun isEmailValid(email: String): Boolean {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun isPhoneValid(phone: String): Boolean {
            return Patterns.PHONE.matcher(phone).matches()
        }

        fun isPasswordValid(password: String): Boolean {
            return password.length >= 6
        }

        fun formatDate(date: Date, format: String): String {
            val result = SimpleDateFormat(format, Locale.getDefault())
            return result.format(date)
        }

        fun parseDate(date: String, format: String): Date? {
            val formatter = SimpleDateFormat(format, Locale.getDefault())

            try {
                return formatter.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return null
        }

        fun convertDate(date: String, inFormat: String, outFormat: String): String {
            val origin = SimpleDateFormat(inFormat, Locale.getDefault())
            val result = SimpleDateFormat(outFormat, Locale.getDefault())

            var ret = ""
            try {
                ret = result.format(origin.parse(date))
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return ret
        }

        //load image from uri
        fun loadImage(context: Context, uri: Any, view: ImageView) {
            GlideApp.with(context).load(uri).thumbnail(GlideApp.with(context).load(R.drawable.loading)).into(view)
        }

        fun loadImageBitmap(view: ImageView, image: Bitmap) {
            view.setImageBitmap(image)
        }
    }

}