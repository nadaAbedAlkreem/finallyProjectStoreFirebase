package com.example.storeproject.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.example.storeproject.customerSide.activity.ButtomActivity
import com.example.storeproject.databinding.FragmentLoginCustomerBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginCustomerActivity : AppCompatActivity() {

    lateinit var db: FirebaseFirestore
    lateinit var binding: FragmentLoginCustomerBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentLoginCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        db = Firebase.firestore
        auth = FirebaseAuth.getInstance()
        ///////////////////////////sure//login
        val sharedPref =
            getSharedPreferences("loginAndLogoutOP", Context.MODE_PRIVATE)
               val email = sharedPref.getString("data", "")
               val password = sharedPref.getString("password", "")

                if (email != "" && password !="" ) {
                    startActivity(Intent(this, ButtomActivity::class.java))
                 }
////////////////////////////////
        binding.btn.setOnClickListener {
            if(binding.email.text.toString().isEmpty() ){
                binding.email.setError("enter name")
            }else if (binding.password.text.toString().isEmpty()){
                binding.password.setError("enter password")
            }
            //////////
            login()
            //////////
        }
        binding.sign.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
  ////////////////////////////////////vaildation
        binding.password.doOnTextChanged { text, start, before, count ->

            if(text!!.length<8){
                binding.textinputlayout.error = "Minimum 8"
            }else if(text!!.length>8){
                binding.textinputlayout.error = null
            }
        }


    }

    private fun login() {
        val emailU = binding.email.text.toString()
        val passwordU = binding.password.text.toString()
        auth.signInWithEmailAndPassword(emailU, passwordU)
            .addOnSuccessListener {
                // Sign in success, update UI 0with the signed-in user's information
                Log.d("nada", "signInWithEmail:success")
                startActivity(Intent(this, MainActivity::class.java))

            }.addOnFailureListener {
                Log.e("nada", "signInWithEmail:faiuer")
                Log.e("hzm", emailU)

                db.collection("customer").whereEqualTo("email", emailU)
                    .whereEqualTo("password", passwordU).get().addOnSuccessListener { result ->
                        if (!result.isEmpty) {
                            for (i in result) {
                                Log.e("TAG", "login: ${i.id}")
                                val sharedPref =
                             getSharedPreferences("dataUser", Context.MODE_PRIVATE)
                                sharedPref.edit().putString("customerId", i.id).putString("data" , i.getString("email")).putString("password" ,i.getString("password") ).apply()


                                getSharedPreferences("loginAndLogoutOP", Context.MODE_PRIVATE)
                                sharedPref.edit().putString("data" , i.getString("email")).putString("password" ,i.getString("password") ).apply()

                            }
                            startActivity(Intent(this, ButtomActivity::class.java))
                        } else {
                            Toast.makeText(this, "Email or password wrong", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }
                    .addOnFailureListener { error ->
                        Log.e("hzm", error.message.toString())
                        Toast.makeText(this, "sign up Failure", Toast.LENGTH_SHORT).show()
                    }
            }
    }
}