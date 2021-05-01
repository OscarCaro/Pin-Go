package com.pinandgo

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
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
import kotlin.system.exitProcess

class RegisterNewPin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_new_pin)

        val textTitle : TextView = findViewById(R.id.textTitle)
        val textDesc : TextView = findViewById(R.id.textDesc)
        val textDomain : TextView = findViewById(R.id.textDomain)
        val textDate : TextView = findViewById(R.id.textDate)
        val imageView : ImageView = findViewById(R.id.image)
        val openButton : ImageButton = findViewById(R.id.button_open)
        val deleteButton : ImageButton = findViewById(R.id.button_delete)
        val favoriteButton : ImageButton = findViewById(R.id.button_favorite)
        val shareButton : ImageButton = findViewById(R.id.button_share)

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

                    if (pin.fav){
                        favoriteButton.setImageResource(R.drawable.ic_baseline_star_24)
                    }
                    else{
                        favoriteButton.setImageResource(R.drawable.ic_baseline_star_border_24)
                    }

                    PinList.add(pin, applicationContext)

                    openButton.setOnClickListener{
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(pin.intentUrl)
                        startActivity(intent)
                    }

                    deleteButton.setOnClickListener{
                        PinList.delete(pin, applicationContext)
                        finish()
                    }

                    favoriteButton.setOnClickListener{
                        if (pin.fav){
                            PinList.changeFav(false, pin, applicationContext)
                            favoriteButton.setImageResource(R.drawable.ic_baseline_star_border_24)
                        }
                        else{
                            PinList.changeFav(true, pin, applicationContext)
                            favoriteButton.setImageResource(R.drawable.ic_baseline_star_24)
                        }
                    }

                    shareButton.setOnClickListener{
                        val sendIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, pin.intentUrl)
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, null)
                        startActivity(shareIntent)
                    }

                    findViewById<ProgressBar>(R.id.progress_bar).visibility = View.GONE
                    findViewById<ConstraintLayout>(R.id.button_group).visibility = View.VISIBLE

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