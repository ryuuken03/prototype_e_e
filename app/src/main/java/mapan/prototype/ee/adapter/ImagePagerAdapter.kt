package mapan.prototype.ee.adapter

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import mapan.prototype.ee.R
import mapan.prototype.ee.module.adminspv.FormDisplayActivity
import mapan.prototype.ee.module.core.BaseActivity
import mapan.prototype.ee.util.Util

class ImagePagerAdapter(private var activity: BaseActivity, private var image: ArrayList<String>?
                        , private var imagesUri: ArrayList<Uri>?
                        , private var type:ImageView.ScaleType) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val view = LayoutInflater.from(activity).inflate(R.layout.item_image, null, false)
        var imageView: ImageView = view.findViewById(R.id.image)
        var delete: ImageView = view.findViewById(R.id.delete)

        imageView.scaleType = type
        if(image != null){
            var imageUrl = image!![position]
            if(imageUrl.equals("")){
                Util.loadImage(activity, R.color.colorGrey, imageView)
            }else{
                Util.loadImage(activity, imageUrl, imageView)
            }
            delete.visibility = View.GONE
        }else{
            var imageUrl = imagesUri!![position]
            Util.loadImage(activity, imageUrl, imageView)
            delete.setOnClickListener {
//                (activity as FormDisplayActivity).deleteImage(position)
            }
        }

        container.addView(view)
        return view
    }

    override fun getCount(): Int {
        if(image != null)
            return image!!.size
        else
            return imagesUri!!.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        (container as ViewPager).removeView(`object` as View)
    }
}