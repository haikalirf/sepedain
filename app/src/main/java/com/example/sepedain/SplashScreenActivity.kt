package com.example.sepedain

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()

        // pauses the activity for 2 seconds as a splash screen
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val intent = Intent(this, PreLoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}