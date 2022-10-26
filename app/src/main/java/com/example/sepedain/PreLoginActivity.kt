package com.example.sepedain

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class PreLoginActivity : AppCompatActivity() {
    private lateinit var btnGetStarted: Button
    private lateinit var tvlogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_login)

        btnGetStarted = findViewById(R.id.btnGetStarted_activity_pre_login)
        tvlogin = findViewById(R.id.tvLogin_activity_pre_login)

        btnGetStarted.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        tvlogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}