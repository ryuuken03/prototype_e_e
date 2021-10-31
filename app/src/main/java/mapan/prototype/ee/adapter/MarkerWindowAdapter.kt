package mapan.prototype.ee.adapter

import android.content.Context
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import android.app.Activity
import android.widget.TextView
import mapan.prototype.ee.R

class MarkerWindowAdapter :GoogleMap.InfoWindowAdapter{
    private var context: Context? = null

    constructor(context: Context){
        this.context = context
    }

    override fun getInfoContents(marker: Marker): View? {
        return null
    }

    override fun getInfoWindow(marker: Marker): View {
        val view = (context as Activity).layoutInflater
                .inflate(R.layout.window_marker, null)
        var address : TextView = view.findViewById(R.id.address)
        address.text = marker.title
        address.visibility = View.VISIBLE
        return view
    }
}