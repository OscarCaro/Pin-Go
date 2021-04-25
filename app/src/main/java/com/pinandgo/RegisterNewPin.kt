package com.pinandgo

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast

class RegisterNewPin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_new_pin)

        val text1 : TextView = findViewById(R.id.text1)
        val text2 : TextView = findViewById(R.id.text2)

        if(intent?.action == Intent.ACTION_SEND && intent?.type == "text/plain"){
            try{
                val pin = Pin(intent.getStringExtra(Intent.EXTRA_TEXT)!!)       // Throw exception if null -> catch and warn user
                text1.text = pin.link
                text2.text = pin.domain

                text1.setOnClickListener{
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(pin.link)
                    startActivity(intent)
                }

                PinList.add(pin, this)

            } catch (e: Exception){
                Toast.makeText(this, "Error loading data from intent", Toast.LENGTH_LONG).show()
            }
        }
    }
}