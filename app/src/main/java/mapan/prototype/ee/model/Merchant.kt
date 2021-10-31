package mapan.prototype.ee.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class Merchant : Serializable {
    @Expose
    var id: String? = null

    @Expose
    var name: String? = null

    @Expose
    var owner: String? = null

    @Expose
    var phone: String? = null

    @Expose
    var area: String? = null

    @Expose
    var city: String? = null

    @Expose
    var province: String? = null

    @Expose
    var latitude: Double? = null

    @Expose
    var longitude: Double? = null

    @Expose
    var address: String? = null

    @Expose
    var image: String? = null

    @Expose
    var created_at: String? = null

    @Expose
    var updated_at: String? = null

    @Expose
    var status: String? = null
}