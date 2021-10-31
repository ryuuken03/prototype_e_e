package mapan.prototype.ee

import android.app.Application
import android.content.Context
//import com.downloader.PRDownloader
//import com.downloader.PRDownloaderConfig
import io.realm.Realm
import io.realm.RealmConfiguration
import mapan.prototype.ee.config.Constants


class PrototypeEEApp : Application() {

    private var realm:Realm ? = null

    companion object{
        fun getInstance(context:Context):PrototypeEEApp{
            return (context.applicationContext as PrototypeEEApp)
        }
    }

    override fun onCreate() {
        super.onCreate()
//        var configDownloader = PRDownloaderConfig.newBuilder()
//                .setReadTimeout(Constant.TIMEOUT)
//                .setConnectTimeout(Constant.TIMEOUT)
//                .build()
//
//        PRDownloader.initialize(getApplicationContext(),configDownloader)

        Realm.init(applicationContext);

        val config = RealmConfiguration.Builder()
                .name(Constants.REALM_NAME)
                .schemaVersion(Constants.REALM_VERSION)
                .deleteRealmIfMigrationNeeded()
                .build()
        realm = Realm.getInstance(config)
    }

    fun realmInstance():Realm{
        return realm!!
    }
}