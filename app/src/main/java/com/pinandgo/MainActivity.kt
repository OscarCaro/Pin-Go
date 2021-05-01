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
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigation : BottomNavigationView
    private val listFragment = ListFragment()
    private val addFragment = ListFragment()
    private val folderFragment = FoldersFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigation = findViewById(R.id.bottom_nav_view)
        bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.action_list -> changeScreen(listFragment)
                R.id.action_add -> changeScreen(addFragment)
                R.id.action_folder -> changeScreen(folderFragment)
            }
            true
        }

        bottomNavigation.selectedItemId = R.id.action_list
    }

    private fun changeScreen(fragment : Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
    }
}