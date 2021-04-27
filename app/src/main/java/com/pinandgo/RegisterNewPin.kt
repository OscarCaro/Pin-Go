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

const val QUERY_BASE_URL = "https://api.linkpreview.net"
const val QUERY_KEY = "key"
const val QUERY_LINK = "q"
const val QUERY_FIELDS = "fields"

const val QUERY_KEY_LIST_1 = "63480a8cb19ed4d7252b56793bf13170"


class RegisterNewPin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_new_pin)

        val text1 : TextView = findViewById(R.id.text1)
        val text2 : TextView = findViewById(R.id.text2)
        val imageView : ImageView = findViewById(R.id.image)

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


                ///////////////////////////////////


                GlobalScope.launch(Dispatchers.Main) {
                    val result = withContext(Dispatchers.IO){
                        val result = connection(pin.link)
                        fromJSONResponse(result)
                    }
                    text1.text = result

                    Glide.with(applicationContext)
                            .load(result)
                            .placeholder(R.drawable.ic_launcher_foreground)
                            .fitCenter()
                            .into(imageView)

                }



                ///////////////////////////////////

            } catch (e: Exception){
                Toast.makeText(this, "Error loading data from intent", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun fromJSONResponse(string : String) : String {
        // This heavy processing is performed on a kotlin coroutine (code in MainActivity) to avoid blocking the UI thread
        try{

            val data = JSONObject(string)

            val title : String = data.getString("title")
            val description : String = data.getString("description")
            val imageURL : String = data.getString("image")
            val url : String = data.getString("url")

            return imageURL
        }
        catch(e:Exception){
            Toast.makeText(this, "Error using API", Toast.LENGTH_SHORT).show()
            return ""
        }
    }

    private fun connection(link : String) : String{
        var conn : HttpsURLConnection? = null
        var reader : BufferedReader? = null

        try{
            val uri = Uri.parse(QUERY_BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_KEY, QUERY_KEY_LIST_1)
                    .appendQueryParameter(QUERY_LINK, link)
                    .build()

            val url = URL(uri.toString())
            conn = url.openConnection() as HttpsURLConnection
            conn.connect()

            reader = BufferedReader(InputStreamReader(conn.inputStream))
            return parseInputStream(reader)
        }
        catch (e: IOException){
            Toast.makeText(this, "Error fetching online data", Toast.LENGTH_LONG).show()
            return ""
        }
        finally {
            conn?.disconnect()
            reader?.close()
        }

    }

    private fun parseInputStream(reader: BufferedReader): String{
        val stringBuilder = StringBuilder()
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            stringBuilder.append(line + "\n")
        }

        Log.d("API result:", stringBuilder.toString())

        return stringBuilder.toString()
    }
}