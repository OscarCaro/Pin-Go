package com.pinandgo

import android.content.Context
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import org.json.JSONArray
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

const val KEY : String = "sharedPreferencesKey"

// OBJECT = default Kotlin implementation of Singleton pattern
object PinList{

    private lateinit var list : ArrayList<Pin>

    fun add(pin: Pin, context: Context){
        checkLoad(context)
        list.add(pin)
        save(context)
    }

    fun get(position : Int, context: Context): Pin {
        checkLoad(context)
        return list[position]
    }

    fun getList(context: Context) : ArrayList<Pin>{
        checkLoad(context)
        return list
    }

    fun getSortedList(context: Context, query: CharSequence?) : ArrayList<Pin>{
        checkLoad(context)
        return if (query?.toString()?.isNotEmpty() == true){
            val filteredList = ArrayList<Pin>()
            val searchTerm = query.toString().toLowerCase(Locale.ROOT)
            for (pin in list){
                if (pin.title.toLowerCase(Locale.ROOT).contains(searchTerm)
                    || pin.description.toLowerCase(Locale.ROOT).contains(searchTerm)
                    || pin.domain.toLowerCase(Locale.ROOT).contains(searchTerm)){
                    filteredList.add(pin)
                }
            }
            filteredList
        } else{
            list
        }
    }

    fun getMappedList(context: Context) : HashMap<String, ArrayList<Pin>>{
        checkLoad(context)
        val map = HashMap<String, ArrayList<Pin>>()
        for (pin in list){
            val currList = map.get(pin.domain) ?: ArrayList<Pin>().apply { map.put(pin.domain, this) }
            currList.add(pin)
        }
        return map
    }

    fun sortByDate(ascending: Boolean){
        if (ascending) list.sortBy { it.date }
        else list.sortByDescending { it.date }
    }

    fun moveFavsToTop(){
        var newIdx = 0;
        for (i in list.indices){
            if (list[i].fav){
                list.add(newIdx++, list.removeAt(i))
            }
        }
    }

    @Synchronized private fun save(context : Context){
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
                list = ArrayList()
                
                val prefs = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
                val array = JSONArray(prefs.getString(KEY, JSONArray().toString()))

                for(i in 0 until array.length()){
                    list.add(Pin(array.optJSONObject(i)))
                }
                sortByDate(true)
                moveFavsToTop()
            }
            catch (e : Exception){
                Toast.makeText(context, "Error loading pins from memory", Toast.LENGTH_SHORT).show()
            }
        }
    }
}