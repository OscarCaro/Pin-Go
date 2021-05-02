package com.pinandgo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigation : BottomNavigationView
    private val listFragment = ListFragment()
    private val addFragment = AddPinFragment()
    private val folderFragment = FoldersFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigation = findViewById(R.id.bottom_nav_view)
        bottomNavigation.setOnNavigationItemSelectedListener {

            if (intent?.action == Intent.ACTION_SEND && intent?.type == "text/plain"){
                val link = intent.getStringExtra(Intent.EXTRA_TEXT)!!
                addFragment.arguments = Bundle().apply { this.putString(BUNDLE_INTENT_LINK, link) }
            }
            else{
                addFragment.arguments = null
            }

            intent = null   // To process it only once

            when(it.itemId){
                R.id.action_list -> changeScreen(listFragment)
                R.id.action_add -> changeScreen(addFragment)
                R.id.action_folder -> changeScreen(folderFragment)
            }
            true
        }

        bottomNavigation.selectedItemId = R.id.action_list
    }

    override fun onResume() {
        super.onResume()
        if (intent?.action == Intent.ACTION_SEND && intent?.type == "text/plain"){
            bottomNavigation.selectedItemId = R.id.action_add
        }
        else if (intent?.action == Intent.ACTION_SEND){
            Toast.makeText(this, "We cannot handle this media type", Toast.LENGTH_LONG).show()
            bottomNavigation.selectedItemId = R.id.action_list
        }
    }

    private fun changeScreen(fragment : Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
    }
}