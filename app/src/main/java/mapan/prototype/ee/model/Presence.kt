package mapan.prototype.ee.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class Presence : Serializable {
    @Expose
    var id_merchant: String? = null

    @Expose
    var merchant: String? = null

    @Expose
    var latitude: Double? = null

    @Expose
    var longitude: Double? = null

    @Expose
    var activity: String? = null

    @Expose
    var created_at: String? = null
}