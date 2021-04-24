package com.pinandgo

import android.content.Context
import android.widget.Toast
import org.json.JSONArray
import java.lang.Exception

const val KEY : String = "sharedPreferencesKey"

class PinList : ArrayList<Pin>() {

    // Singleton implementation
    companion object{
        @Volatile private var INSTANCE : PinList? = null

        fun getInstance(context: Context) : PinList =
            INSTANCE ?: synchronized(this){
                INSTANCE ?: PinList().also { INSTANCE = it; it.load(context) }
            }
    }

    // Must be called after changes on list (add pin, remove pin, etc)
    fun save(context : Context){
        val array = JSONArray()
        for (pin in this ){
            array.put(pin.toJson())
        }

        val prefs = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY, array.toString()).apply()
    }

    private fun load(context: Context){
        try{
            val prefs = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
            val array = JSONArray(prefs.getString(KEY, JSONArray().toString()))

            for(i in 0 until array.length()){
                this.add(Pin(array.optJSONObject(i)))
            }
        }
        catch (e : Exception){
            Toast.makeText(context, "Error loading pins from memory", Toast.LENGTH_SHORT).show()
        }
    }

}