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
import javax.net.ssl.HttpsURLConnection

class RegisterNewPin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_new_pin)

        val textIntentURL : TextView = findViewById(R.id.textIntentURL)
        val textDomain : TextView = findViewById(R.id.textDomain)
        val textTitle : TextView = findViewById(R.id.textTitle)
        val textDesc : TextView = findViewById(R.id.textDesc)
        val textImageURL : TextView = findViewById(R.id.textImageURL)
        val imageView : ImageView = findViewById(R.id.image)

        if(intent?.action == Intent.ACTION_SEND && intent?.type == "text/plain"){
            GlobalScope.launch(Dispatchers.Main) {
                try{
                    val link = intent.getStringExtra(Intent.EXTRA_TEXT)!!
                    val pin : Pin = withContext(Dispatchers.IO){
                        Pin(link)           // Throw exception if null -> catch and warn user
                    }

                    textIntentURL.text = pin.intentUrl
                    textDomain.text = pin.domain
                    textTitle.text = pin.title
                    textDesc.text = pin.description
                    textImageURL.text = pin.imageUrl

                    textIntentURL.setOnClickListener{
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(pin.intentUrl)
                        startActivity(intent)
                    }

                    PinList.add(pin, applicationContext)

                    Glide.with(applicationContext)
                        .load(pin.imageUrl)
                        .fitCenter()
                        .into(imageView)
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