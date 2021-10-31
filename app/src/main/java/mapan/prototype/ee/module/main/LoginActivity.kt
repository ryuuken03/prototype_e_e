package mapan.prototype.ee.module.main

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import mapan.prototype.ee.R
import mapan.prototype.ee.databinding.ActivityLoginBinding
import mapan.prototype.ee.module.adminspv.DashboardActivity
import mapan.prototype.ee.module.core.BaseActivity
import mapan.prototype.ee.module.md.MainActivity
import mapan.prototype.ee.util.Util
import java.util.*

class LoginActivity : BaseActivity() {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initConfig()
    }

    override fun initConfig() {
        initUI()
    }

    override fun initUI() {
        binding.loginEmail.setText("md@mail.com")
        binding.loginPassword.setText("password")
        setListener()
    }

    fun selectEmail(id:Int){
        when(id){
            1->{
                binding.loginEmail.setText("admin@mail.com")
            }
            2->{
                binding.loginEmail.setText("spv@mail.com")
            }
            3->{
                binding.loginEmail.setText("md@mail.com")
            }
        }
        var email = binding.loginEmail.text.toString()
        if(email.contains("admin")||email.contains("spv")){
            var intent = Intent(this@LoginActivity, DashboardActivity::class.java)
            intent.putExtra("email",email)
            startActivity(intent)
        }else{
            var intent = Intent(this@LoginActivity, MainActivity::class.java)
            intent.putExtra("email",email)
            startActivity(intent)
        }
        finish()
    }

    override fun setListener() {

        binding.selectAdmin.setOnClickListener {
            selectEmail(1)
        }
        binding.loginAdmin.setOnClickListener {
            selectEmail(1)
        }
        binding.selectSPV.setOnClickListener {
            selectEmail(2)
        }
        binding.loginSPV.setOnClickListener {
            selectEmail(2)
        }
        binding.selectMD.setOnClickListener {
            selectEmail(3)
        }
        binding.loginMD.setOnClickListener {
            selectEmail(3)
        }

        binding.loginPassword.setOnTouchListener(View.OnTouchListener { v, event ->

            var DRAWABLE_RIGHT = 2
            if(event.action == MotionEvent.ACTION_UP){
                if(event.rawX >= (binding.loginPassword.right - binding.loginPassword.compoundDrawables[DRAWABLE_RIGHT].bounds.width())){

                    if(binding.loginPassword.transformationMethod == null){
                        binding.loginPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                    }else{
                        binding.loginPassword.transformationMethod = null
                    }

                    true
                }
            }

            false
        })

        binding.buttonLogin.setOnClickListener({
            if(validate()){
                var email = binding.loginEmail.text.toString()
                if(email.contains("admin")||email.contains("spv")){
                    var intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                    intent.putExtra("email",email)
                    startActivity(intent)
                }else{
                    var intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.putExtra("email",email)
                    startActivity(intent)
                }
            }
        })
    }

    private fun validate():Boolean {
        binding.loginEmail.error = null
        binding.loginPassword.error = null

        val email = binding.loginEmail.text.toString()
        val pass = binding.loginPassword.text.toString()

        var cancel = false
        var focus: View? = null

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            binding.loginEmail.error = getString(R.string.error_email_empty)
            focus = binding.loginEmail
            cancel = true
        } else if (!Util.isEmailValid(email)) {
            binding.loginEmail.error = getString(R.string.error_email_invalid)
            focus = binding.loginEmail
            cancel = true
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(pass)&& !cancel) {
            binding.loginPassword.error = getString(R.string.error_password_empty)
            focus = binding.loginPassword
            cancel = true
        } else if (!Util.isPasswordValid(pass)&& !cancel) {
            binding.loginPassword.error = getString(R.string.error_password_invalid)
            focus = binding.loginPassword
            cancel = true
        }

        if (cancel) {
            focus?.requestFocus()
        }


        return !cancel
    }
}
