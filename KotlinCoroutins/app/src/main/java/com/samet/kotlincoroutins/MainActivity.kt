package com.samet.kotlincoroutins

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val nameInput = findViewById<EditText>(R.id.nameInput)
        val submitButton = findViewById<Button>(R.id.submitButton)

        submitButton.setOnClickListener {
            val name: String? = nameInput.text.toString().takeIf { it.isNotBlank() }

            if (name != null) {
                // Intent oluşturuluyor
                val intent = Intent(this, SecondActivity::class.java)
                intent.putExtra("USER_NAME", name)

                startActivity(intent)
            } else {
                Toast.makeText(this, "Lütfen bir ad giriniz!", Toast.LENGTH_SHORT).show()
            }
        }
    }
        
}
