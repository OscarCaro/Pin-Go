package com.pinandgo

import android.app.ActivityManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigation : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigation = findViewById(R.id.bottom_nav_view)
        bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.action_list -> changeScreen()
                R.id.action_add -> changeScreen()
                R.id.action_folder -> changeScreen()
            }
            true
        }

        bottomNavigation.selectedItemId = R.id.action_list
    }

    private fun changeScreen() {
        val frag = ListFragment.newInstance()
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, frag).commit()
    }
}