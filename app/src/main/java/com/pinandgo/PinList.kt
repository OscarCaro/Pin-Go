package com.pinandgo

import android.content.Context
import android.widget.Toast
import org.json.JSONArray
import java.lang.Exception

const val KEY : String = "sharedPreferencesKey"

// OBJECT = default Kotlin implementation of Singleton pattern
object PinList {

    private lateinit var list : ArrayList<Pin>

    fun add(pin: Pin, context: Context){
        checkLoad(context)
        list.add(pin)
    }

    fun get(position : Int, context: Context): Pin {
        checkLoad(context)
        return list[position]
    }

    fun size (context: Context) : Int{
        return list.size
    }

    private fun save(context : Context){
        val array = JSONArray()
        for (pin in list){
            array.put(pin.toJson())
        }

        val prefs = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY, array.toString()).apply()
    }

    @Synchronized private fun checkLoad(context: Context){
        if (!::list.isInitialized){
            try{
                val prefs = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
                val array = JSONArray(prefs.getString(KEY, JSONArray().toString()))

                for(i in 0 until array.length()){
                    list.add(Pin(array.optJSONObject(i)))
                }
            }
            catch (e : Exception){
                Toast.makeText(context, "Error loading pins from memory", Toast.LENGTH_SHORT).show()
            }
        }
    }

}