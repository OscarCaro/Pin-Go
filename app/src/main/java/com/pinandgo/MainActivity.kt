package com.pinandgo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

const val PREFS_SELEC_FRAG = "sel_frag"

const val LIST_FRAG_TAG = "list"
const val ADD_FRAG_TAG = "add"
const val FOLDER_FRAG_TAG = "folder"


class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigation : BottomNavigationView
    private lateinit var listFragment : Fragment
    private lateinit var addFragment : Fragment
    private lateinit var folderFragment : Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listFragment = supportFragmentManager.findFragmentByTag(LIST_FRAG_TAG) ?: ListFragment()
        addFragment = supportFragmentManager.findFragmentByTag(ADD_FRAG_TAG) ?: AddPinFragment()
        folderFragment = supportFragmentManager.findFragmentByTag(FOLDER_FRAG_TAG) ?: FoldersFragment()

        bottomNavigation = findViewById(R.id.bottom_nav_view)
        bottomNavigation.setOnNavigationItemSelectedListener {

            if (intent?.action == Intent.ACTION_SEND && intent?.type == "text/plain"){
                val link = intent.getStringExtra(Intent.EXTRA_TEXT)!!
                addFragment.arguments = Bundle().apply { this.putString(BUNDLE_INTENT_LINK, link) }
            }
            else{
                addFragment.arguments = null
            }

            intent?.action = null
            intent = null   // To process it only once

            when(it.itemId){
                R.id.action_list -> changeScreen(listFragment, LIST_FRAG_TAG)
                R.id.action_add -> changeScreen(addFragment, ADD_FRAG_TAG)
                R.id.action_folder -> changeScreen(folderFragment, FOLDER_FRAG_TAG)
            }

            val prefs = getSharedPreferences(KEY, Context.MODE_PRIVATE)
            prefs.edit().putInt(PREFS_SELEC_FRAG, it.itemId).apply()

            true
        }
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
        else{
            val prefs = getSharedPreferences(KEY, Context.MODE_PRIVATE)
            bottomNavigation.selectedItemId = prefs.getInt(PREFS_SELEC_FRAG, R.id.action_list)
        }
    }

    private fun changeScreen(fragment : Fragment, tag: String) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment, tag).commit()
    }

}