package com.perusu.coroutineapp.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.perusu.coroutineapp.R

class CoroutineActivity : AppCompatActivity() {

    private var mode = 1
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = findNavController(R.id.navHostFragment)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        mode = if(mode == 1) 2 else 1
        menu?.add(0, mode, Menu.NONE, if (mode == 1) R.string.recylcerView else R.string.staticPics)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        Log.d("CoroutineActivity", "onOptionsItemSelected: item ${item.itemId}")
        when (mode) {
            1 -> navController.navigate(R.id.recyclerFragment)
            2 -> navController.navigate(R.id.staticPicFragment)
        }
        return false
    }
}