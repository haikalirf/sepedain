package com.example.sepedain

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.sepedain.databinding.ActivityLoginBinding
import com.example.sepedain.main.MainActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        auth = FirebaseAuth.getInstance()

        binding.apply {
            btnLoginActivityLogin.setOnClickListener {
                val email = binding.etEmailActivityLogin.text.toString().trim()
                val pass = binding.etPasswordActivityLogin.text.toString().trim()

                if (email.isNotEmpty() && pass.isNotEmpty()) {
                    auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            Animatoo.animateSlideRight(this@LoginActivity)
                            finish()
                        } else {
                            Toast.makeText(this@LoginActivity, "User does not exist", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Every field must be filled", Toast.LENGTH_SHORT).show()
                }
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