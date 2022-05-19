package com.example.storeproject.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.storeproject.databinding.FragmentSignupBinding
import com.example.storeproject.model.Customer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {
    lateinit var binding: FragmentSignupBinding
    lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        auth = Firebase.auth
        binding.date.setOnClickListener {
            date()
        }
        binding.login.setOnClickListener {
            startActivity(Intent(this, LoginCustomerActivity::class.java))
        }

        binding.btn.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            addCustomer(binding.name.text.toString(), email, password, binding.date.text.toString())
        }
    }

    private fun addCustomer(name: String, email: String, password: String, date: String) {
        db = Firebase.firestore

        val customer = Customer(name, email, date, password)
        db.collection("customer").add(customer).addOnSuccessListener {
            Log.d("hzm", "added successful with id: ${it.id}")
            startActivity(Intent(this, LoginCustomerActivity::class.java))
        }.addOnFailureListener {
            Log.d("hzm", it.message!!)
        }

    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.N)
    fun date() {
        val current = Calendar.getInstance()
        val day1 = current.get(Calendar.DAY_OF_MONTH)
        val month1 = current.get(Calendar.MONTH)
        val year1 = current.get(Calendar.YEAR)
        val picker = DatePickerDialog(this,
            { view, year, month, dayOfMonth ->
                binding.date.setText("$year : $month :$dayOfMonth")
            }, year1, month1, day1)
        picker.show()
    }

}