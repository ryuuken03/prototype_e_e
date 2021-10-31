package mapan.prototype.ee.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class MerchantDisplay : Serializable {
    @Expose
    var id: String? = null

    @Expose
    var id_merchant: String? = null

    @Expose
    var merchant: Merchant? = null

    @Expose
    var detail: ArrayList<Image>? = null

    @Expose
    var report: ArrayList<ProductReport>? = null

    @Expose
    var created_at: String? = null

    @Expose
    var updated_at: String? = null

}