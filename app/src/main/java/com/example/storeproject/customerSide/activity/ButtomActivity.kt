package com.example.storeproject.customerSide.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.storeproject.customerSide.CartFragment
import com.example.storeproject.customerSide.ShowCategoryCustomer
import com.example.storeproject.fragment.ProfileFragment
import com.example.storeproject.R
import com.example.storeproject.activity.LoginCustomerActivity
import com.example.storeproject.databinding.ActivityButtomBinding

class ButtomActivity : AppCompatActivity() {
    lateinit var binding: ActivityButtomBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityButtomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        swipe(ShowCategoryCustomer())
        binding.btmNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navHome -> swipe(ShowCategoryCustomer())
                R.id.navProfile->swipe(ProfileFragment())
                R.id.navCart ->swipe(CartFragment())
            }
            true
        }

    }

    private fun swipe(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment)
            .addToBackStack("").commit()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.log_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navLogOut -> {
                val sharedpreferences = getSharedPreferences("loginAndLogoutOP",
                    Context.MODE_PRIVATE
                )
                val editor = sharedpreferences.edit()
                editor.putString("data", "")
                editor.putString("password", "")
                editor.commit()
                val i = Intent(this, LoginCustomerActivity::class.java)
                startActivity(i)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}