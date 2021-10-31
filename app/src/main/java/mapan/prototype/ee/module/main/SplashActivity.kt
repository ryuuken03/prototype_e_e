package mapan.prototype.ee.module.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import io.realm.Realm
import mapan.prototype.ee.PrototypeEEApp
import mapan.prototype.ee.R
import mapan.prototype.ee.databinding.ActivityLoginBinding
import mapan.prototype.ee.databinding.ActivitySplashBinding
import mapan.prototype.ee.module.core.BaseActivity

class SplashActivity : BaseActivity() {

    lateinit var binding: ActivitySplashBinding
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        realm = PrototypeEEApp.getInstance(this).realmInstance()
        initConfig()
    }

    override fun initConfig() {
        initUI()
    }

    override fun initUI() {

        Handler().postDelayed(Runnable {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        },2000)
        setListener()
    }

    override fun setListener() {

    }
}
