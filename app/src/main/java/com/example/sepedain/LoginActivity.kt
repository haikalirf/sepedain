package com.example.sepedain

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.sepedain.databinding.ActivityLoginBinding
import com.example.sepedain.main.MainActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.apply {
            btnLoginActivityLogin.setOnClickListener {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                Animatoo.animateSlideRight(this@LoginActivity)
                finish()
            }

            tvSignUpActivityLogin.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
                Animatoo.animateSlideRight(this@LoginActivity)
                finish()
            }
        }
    }
}