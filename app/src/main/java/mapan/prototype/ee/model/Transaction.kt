package mapan.prototype.ee.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class Transaction : Serializable {

    @Expose
    var id: String? = null

    @Expose
    var id_merchant: String? = null

    @Expose
    var price: String? = null

    @Expose
    var note: String? = null

    @Expose
    var merchant: Merchant? = null

    @Expose
    var detail: ArrayList<ProductReport>? = null

    @Expose
    var product: ArrayList<ProductReport>? = null

    @Expose
    var created_at: String? = null

    @Expose
    var updated_at: String? = null

    @Expose
    var status: String? = null
}