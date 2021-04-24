package com.pinandgo

import org.json.JSONObject
import java.net.URL

const val JSON_LINK = "link"
const val JSON_DOMAIN = "domain"

class Pin (val link : String){

    val domain : String = URL(link).host

    // Constructor from JSONObject of SharedPrefs
    constructor(json : JSONObject) : this(json.getString(JSON_LINK))

    // Parser Pin -> JSONObject for SharedPrefs
    fun toJson() : JSONObject{
        val json = JSONObject()
        json.put(JSON_LINK, link)
        json.put(JSON_DOMAIN, domain)
        return json
    }

}