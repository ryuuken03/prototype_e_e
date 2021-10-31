package mapan.prototype.ee.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class ProductReport : Serializable {

    @Expose
    var id: String? = null

    @Expose
    var id_product: String? = null

    @Expose
    var product: Product? = null

    @Expose
    var count: String? = null

    @Expose
    var note: String? = null

    @Expose
    var price: String? = null

    @Expose
    var stock_toko: String? = null

    @Expose
    var stock_gudang: String? = null

    @Expose
    var expiry_date: String? = null

    @Expose
    var created_at: String? = null

    @Expose
    var updated_at: String? = null

    @Expose
    var status: String? = null
}