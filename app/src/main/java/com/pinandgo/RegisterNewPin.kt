package com.pinandgo

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import javax.net.ssl.HttpsURLConnection

class RegisterNewPin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_new_pin)

        val textTitle : TextView = findViewById(R.id.textTitle)
        val textDesc : TextView = findViewById(R.id.textDesc)
        val textDomain : TextView = findViewById(R.id.textDomain)
        val textDate : TextView = findViewById(R.id.textDate)
        val imageView : ImageView = findViewById(R.id.image)

        if(intent?.action == Intent.ACTION_SEND && intent?.type == "text/plain"){
            GlobalScope.launch(Dispatchers.Main) {
                try{
                    val link = intent.getStringExtra(Intent.EXTRA_TEXT)!!
                    val pin : Pin = withContext(Dispatchers.IO){
                        Pin(link)           // Throw exception if null -> catch and warn user
                    }

                    textTitle.text = pin.title.capitalize(Locale.ROOT)
                    textDesc.text = pin.description.capitalize(Locale.ROOT)
                    textDomain.text = pin.domain
                    textDate.text = SimpleDateFormat("HH:mm dd MMMM", Locale.getDefault()).format(pin.date).toString()

                    Glide.with(applicationContext)
                        .load(pin.imageUrl)
                        .circleCrop()
                        .into(imageView)

                    PinList.add(pin, applicationContext)

                } catch (e: Exception){
                    Toast.makeText(applicationContext, "Error loading data from intent", Toast.LENGTH_LONG).show()
                }
            }
        }
        else {
            Toast.makeText(this, "We cannot handle this media type", Toast.LENGTH_LONG).show()
        }
    }
}