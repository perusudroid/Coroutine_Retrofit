package com.perusu.coroutineapp.view.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.perusu.coroutineapp.R

class CoroutineActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handleTitle(R.id.action_flow_sample)
        navController = findNavController(R.id.navHostFragment)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_app, menu);
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        handleTitle(item.itemId)
        navController.navigate(
            when (item.itemId) {
                R.id.action_static -> R.id.staticPicFragment
                R.id.action_recylcer -> R.id.recyclerFragment
                R.id.action_flow -> R.id.recyclerFlowFragment
                R.id.action_flow_sample -> R.id.sampleFragment
                else -> R.id.staticPicFragment
            }
        )
        return false
    }

    private fun handleTitle(itemId: Int) {
        supportActionBar?.title =  when (itemId) {
            R.id.action_static -> "Flow with delay"
            R.id.action_recylcer -> "Recyclerview demo"
            R.id.action_flow -> "Recycler demo using Flow"
            R.id.action_flow_sample -> "Flow Samples"
            else -> "Coroutines App"
        }
    }
}