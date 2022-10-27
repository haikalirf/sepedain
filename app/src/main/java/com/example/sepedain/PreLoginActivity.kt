package com.example.sepedain

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.sepedain.databinding.ActivityPreLoginBinding

class PreLoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPreLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.apply {
            btnGetStartedActivityPreLogin.setOnClickListener {
                val intent = Intent(this@PreLoginActivity, RegisterActivity::class.java)
                startActivity(intent)
                Animatoo.animateSlideRight(this@PreLoginActivity)
                finish()
            }

            tvLoginActivityPreLogin.setOnClickListener {
                val intent = Intent(this@PreLoginActivity, LoginActivity::class.java)
                startActivity(intent)
                Animatoo.animateSlideRight(this@PreLoginActivity)
                finish()
            }
        }
    }
}