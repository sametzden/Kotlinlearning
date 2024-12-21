package com.samet.kotlincoroutins

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_second)

        val userName: String? = intent.getStringExtra("USER_NAME")
        val welcomeText = findViewById<TextView>(R.id.welcomeText)

        // Nullable kontrolü
        welcomeText.text = if (userName != null) {
            "Hoş geldin, $userName!"
        } else {
            "Hoş geldiniz!"
        }
    }
}