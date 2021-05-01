package com.pinandgo

import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import javax.net.ssl.HttpsURLConnection

const val JSON_INTENT_URL = "link"
const val JSON_DOMAIN = "domain"
const val JSON_TITLE = "title"
const val JSON_DESC = "desc"
const val JSON_DATE = "date"
const val JSON_FAV = "fav"
const val JSON_IMAGE_URL = "image"
const val JSON_DOMAIN_IMAGE_URL = "domain_image"

const val QUERY_BASE_URL = "https://api.linkpreview.net"
const val QUERY_KEY = "key"
const val QUERY_LINK = "q"
const val QUERY_KEY_LIST_1 = "63480a8cb19ed4d7252b56793bf13170"

class Pin {

    var intentUrl : String
    var domain : String
    var title : String
    var description : String
    var date : Date
    var fav : Boolean
    var imageUrl : String
    var domainImageUrl : String

    // Constructor from JSONObject of SharedPrefs
    constructor(json : JSONObject) {
        intentUrl = json.getString(JSON_INTENT_URL)
        domain = json.getString(JSON_DOMAIN)
        title = json.getString(JSON_TITLE)
        description = json.getString(JSON_DESC)
        date = Date(json.getLong(JSON_DATE))
        fav = json.getBoolean(JSON_FAV)
        imageUrl = json.getString(JSON_IMAGE_URL)
        domainImageUrl = json.getString(JSON_DOMAIN_IMAGE_URL)
    }

    // Constructor from received intent -> Perform load of data using HTTPS
    // WARNING: Must be called inside a coroutine
    // WARNING: May throw exception
    @Throws(Exception::class)
    constructor (url: String) {
        val connResultStr : String? = connection(url)

        // May throw exception (if string is null) -> to be caught by caller
        val json = JSONObject(connResultStr!!)
        intentUrl = url
        val tmp = URL(intentUrl).host
        domain = if (tmp.startsWith("www.")) tmp.substring(4) else tmp;
        title = json.getString("title")
        description = json.getString("description")
        imageUrl = json.getString("image")
        date = Calendar.getInstance().time
        fav = false

        val domainConnResultStr : String? = connection(domain)

        // May throw exception (if string is null) -> to be caught by caller
        val domainJson = JSONObject(domainConnResultStr!!)
        domainImageUrl = domainJson.getString("image")
    }

    // Pin -> JSONObject for SharedPrefs
    fun toJson() : JSONObject{
        val json = JSONObject()
        json.put(JSON_INTENT_URL, intentUrl)
        json.put(JSON_DOMAIN, domain)
        json.put(JSON_TITLE, title)
        json.put(JSON_DESC, description)
        json.put(JSON_DATE, date.time)
        json.put(JSON_FAV, fav)
        json.put(JSON_IMAGE_URL, imageUrl)
        json.put(JSON_DOMAIN_IMAGE_URL, domainImageUrl)
        return json
    }

    // Perform HTTPS connection to LinkPreviewAPI
    private fun connection(link : String) : String?{
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
            //TODO: warn user
            //Toast.makeText(this, "Error fetching online data", Toast.LENGTH_LONG).show()
            return null
        }
        finally {
            conn?.disconnect()
            reader?.close()
        }
    }

    // Parse long string returned by LinkPreviewAPI -> String
    private fun parseInputStream(reader: BufferedReader): String{
        val stringBuilder = StringBuilder()
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            stringBuilder.append(line + "\n")
        }

        return stringBuilder.toString()
    }


}