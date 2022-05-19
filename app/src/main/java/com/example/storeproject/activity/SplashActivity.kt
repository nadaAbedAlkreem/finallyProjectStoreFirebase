package com.example.storeproject.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.storeproject.R
import com.example.storeproject.databinding.ActivitySplashBinding


@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val splashScreen = 3000
    private lateinit var topAnimation: Animation
    private lateinit var bottomAnimation: Animation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //hide status bar
        supportActionBar?.hide()
        //create Animation
        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)
        binding.imgAnimation.animation = topAnimation
        binding.imgTitle.animation = bottomAnimation
//        val sharedPref = getSharedPreferences("MyPref", MODE_PRIVATE)
        Handler(Looper.getMainLooper()).postDelayed({
//            swipe(ShowCategoriesFragment())
//            val isLogin = sharedPref.getBoolean("login", false)
//            if (isLogin) {
//                swipe(ShowProductFragment())
//            }
////            } else {
                startActivity(Intent(this, LoginCustomerActivity::class.java))
////            }

        }, splashScreen.toLong())
    }

//    private fun swipe(fragment: Fragment) {
//        supportFragmentManager.beginTransaction().replace(R.id.main, fragment)
//            .addToBackStack("").commit()
//    }
}