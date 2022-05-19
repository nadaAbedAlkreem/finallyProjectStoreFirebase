package com.example.storeproject.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment


import com.example.storeproject.R
import com.example.storeproject.databinding.ActivityMainBinding
import com.example.storeproject.fragment.ReviewFragment
import com.example.storeproject.fragment.ShowCategoriesFragment
import com.example.storeproject.fragment.ShowProductFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toggle = ActionBarDrawerToggle(this,  binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        swipe(ShowCategoriesFragment())
        swipe(ShowCategoriesFragment())
        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navAddCategory -> swipe(ShowCategoriesFragment())
                R.id.navAddProduct -> swipe(ShowProductFragment())
                R.id.navReview -> swipe(ReviewFragment())
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        when (item.itemId) {
            R.id.navLogOut -> {
//                Toast.makeText(this, "Log out", Toast.LENGTH_SHORT).show()
                val i = Intent(this, LoginCustomerActivity::class.java)
                startActivity(i)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun swipe(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment)
            .addToBackStack("").commit()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.log_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

}