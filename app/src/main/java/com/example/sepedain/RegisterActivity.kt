package com.example.sepedain

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.sepedain.databinding.ActivityRegisterBinding
import com.example.sepedain.dataclasses.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()

        binding.btnSignMeUpActivityRegister.setOnClickListener {
            val uname = binding.etUsernameActivityRegister.text.toString().trim()
            val email = binding.etEmailActivityRegister.text.toString().trim()
            val pass = binding.etPasswordActivityRegister.text.toString().trim()

            if (uname.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        dbRef = FirebaseDatabase.getInstance().reference
                        val currUid = auth.currentUser?.uid!!
                        dbRef.child("user").child(currUid).setValue(User(uname, email, currUid))
                        Toast.makeText(this, "Successfully created an account", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        Animatoo.animateSlideLeft(this)
                        finish()
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Every field must be filled", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvLoginActivityRegister
            .setOnClickListener {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                Animatoo.animateSlideLeft(this)
                finish()
            }
    }
}