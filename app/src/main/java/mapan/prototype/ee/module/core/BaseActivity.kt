package mapan.prototype.ee.module.core

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import mapan.prototype.ee.R
import mapan.prototype.ee.config.Constants
import mapan.prototype.ee.util.InitializerUi

abstract class BaseActivity : AppCompatActivity(), InitializerUi  {

    lateinit var dialog: AlertDialog

    fun isTablet(): Boolean {
        return resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

    fun checkPermission(permissions: Array<String>): Boolean {
        permissions
                .map { ContextCompat.checkSelfPermission(this, it) }
                .forEach {
                    if (it == PackageManager.PERMISSION_DENIED) {
                        ActivityCompat.requestPermissions(this, permissions, Constants.permissionRequestCode)
                        return false
                    }
                }
        return true
    }

    fun showLoadingDialog() {
        val vv = layoutInflater.inflate(R.layout.dialog_custom_view, null)

        val params = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.setMargins(0, resources.getDimension(R.dimen.spacing_2x).toInt(), 0, resources.getDimension(R.dimen.spacing_2x).toInt())

        val progress = ProgressBar(this)
        progress.layoutParams = params

        var container:LinearLayout = vv.findViewById(R.id.container)
        var buttonDone:Button = vv.findViewById(R.id.buttonDone)

        container.addView(progress)
        buttonDone.visibility = View.GONE

        showDialog(getString(R.string.text_loading), vv, false)
    }

    fun showAlertDialog(title: String, message: String?, listener: View.OnClickListener, cancelOnTouchOutside: Boolean) {
        val vv = layoutInflater.inflate(R.layout.dialog_alert, null, false)

        var text:TextView = vv.findViewById(R.id.text)
        var buttonOk:Button = vv.findViewById(R.id.buttonOk)
        text.text = message
        buttonOk.setOnClickListener(listener)

        showDialog(title, vv, cancelOnTouchOutside)
    }

    fun showPromptDialog(title: String, message: String, button: String, listener: View.OnClickListener, cancelOnTouchOutside: Boolean) {
        val vv = layoutInflater.inflate(R.layout.dialog_prompt, null, false)
        var text:TextView = vv.findViewById(R.id.text)
        var buttonCancel:Button = vv.findViewById(R.id.buttonCancel)
        var buttonOk:Button = vv.findViewById(R.id.buttonOk)

        text.text = message
        buttonCancel.setOnClickListener({ dialog.dismiss() })
        buttonOk.text = button
        buttonOk.setOnClickListener(listener)

        showDialog(title, vv, cancelOnTouchOutside)
    }

    fun showCustomDialog(title: String, view: View, button: String, listener: View.OnClickListener, cancelOnTouchOutside: Boolean, limitHeight: Boolean) {
        val vv = layoutInflater.inflate(R.layout.dialog_custom_view, null, false)
        var container:LinearLayout = vv.findViewById(R.id.container)
        var buttonDone:Button = vv.findViewById(R.id.buttonDone)

        if (limitHeight) {
            container.layoutParams.height = resources.getDimension(R.dimen.dialog_height).toInt()
        }
        container.addView(view)

        buttonDone.text = button
        buttonDone.setOnClickListener(listener)

        showDialog(title, vv, cancelOnTouchOutside)
    }

    //show custom template dialog
    private fun showDialog(title: String, content: View, cancelOnTouchOutside: Boolean) {
        val builder = AlertDialog.Builder(this)

        val v = layoutInflater.inflate(R.layout.dialog_header, null, false)
        var headerTitle:TextView = v.findViewById(R.id.headerTitle)
        headerTitle.text = title

        builder.setCustomTitle(v)
        builder.setView(content)

        dialog = builder.create()
        dialog.setCanceledOnTouchOutside(cancelOnTouchOutside)
        dialog.show()
    }

    fun showRecyclerDialog(title: String, view: View, cancelOnTouchOutside: Boolean, limitHeight: Boolean) {
        val v = layoutInflater.inflate(R.layout.dialog_custom_view, null, false)

        val container = v.findViewById<LinearLayout>(R.id.container)
        var buttonDone :Button = v.findViewById(R.id.buttonDone)
        if (limitHeight) {
            container.layoutParams.height = resources.getDimension(R.dimen.dialog_height).toInt()
        }
        container.addView(view)

        buttonDone.setOnClickListener(View.OnClickListener { dialog.dismiss() })

        showDialog(title, v, cancelOnTouchOutside)
    }
}