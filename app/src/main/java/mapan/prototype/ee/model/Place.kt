package mapan.prototype.ee.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class Place : Serializable {
    @Expose
    var id: String? = null

    @Expose
    var name: String? = null

    @Expose
    var province_id: String? = null

    @Expose
    var latitude: Double? = null

    @Expose
    var longitude: Double? = null

    @Expose
    var city: ArrayList<Place>? = null
}