package com.example.sepedain

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.sepedain.databinding.ActivityPreLoginBinding

class PreLoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPreLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_login)
        binding.apply {
            btnGetStartedActivityPreLogin.setOnClickListener {
                val intent = Intent(this@PreLoginActivity, RegisterActivity::class.java)
                startActivity(intent)
                finish()
            }

            tvLoginActivityPreLogin.setOnClickListener {
                val intent = Intent(this@PreLoginActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}