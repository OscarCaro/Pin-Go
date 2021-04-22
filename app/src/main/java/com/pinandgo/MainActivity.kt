package com.pinandgo

import android.app.ActivityManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView : TextView = findViewById(R.id.text)

        when {
            intent?.action == Intent.ACTION_SEND && intent.type == "text/plain" -> {

                try{
                    val pin = Pin(intent.getStringExtra(Intent.EXTRA_TEXT))
                    textView.text = "$pin.link $pin.domain"

                    textView.setOnClickListener{
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(pin.link)
                        startActivity(intent)
                    }
                } catch (e: Exception){
                    Toast.makeText(this, "Error loading data from intent", Toast.LENGTH_LONG).show()
                }
            }
            else -> {
                // Handle other intents, such as being started from the home screen
            }
        }
    }
}