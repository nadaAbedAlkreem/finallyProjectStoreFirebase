package com.example.storeproject.activity


import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.storeproject.R
import com.example.storeproject.customerSide.CartFragment
import com.example.storeproject.databinding.ActivityDetailsBinding
import com.example.storeproject.model.Rate
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import java.util.*
import java.util.stream.Collectors

class ProductDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var ratingBar: RatingBar
    val tag = "nada"
    private var customerId = ""
    private var productId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = Firebase.firestore
        productId = intent.getStringExtra("productId")!!
        Log.e("TAG", "onCreate: productId $productId")
        val sharedPref = getSharedPreferences("dataUser", Context.MODE_PRIVATE)
        customerId = sharedPref.getString("customerId", "")!!
        Log.e("TAG", "onCreate: customerId $customerId")

        binding.btnCart.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, CartFragment()).addToBackStack("").commit()
        }

        binding.btnCart.setOnClickListener {
            //purchases
            db.collection("product").get().addOnSuccessListener { query ->
                for (i in query) {
                    if (productId == i.id) {
                        addPurchases(i.getString("name")!!,
                            i.getString("product_img"),
                            i.get("price").toString().toDouble())
                    }
                }
            }.addOnFailureListener {
                Log.e("nada", "failure")
            }
        }

        db.collection("product").get().addOnSuccessListener { query ->
            for (i in query) {
                if (productId == i.id) {
                    binding.txtName.text = i.getString("name")
                    binding.txetPrice.text = i.get("price").toString()
                    binding.txetDescription.text = i.getString("description")
                    Picasso.get().load(i.getString("product_img")).into(binding.Img)
                }
            }
        }.addOnFailureListener { Log.e("nada", "failuer") }
        //rating
        ratingBar = binding.ratingBar
        binding.btnRating.setOnClickListener {
            inputData()
        }

    }

    @SuppressLint("NewApi")
    private fun inputData() {
        val rateList = ArrayList<Rate>()
        db.collection("rating").get().addOnSuccessListener { result ->
            if (result.isEmpty) {
                Log.e("TAG", "inputData: here")
                val rating = ratingBar.rating
                val data = hashMapOf(
                    "cid" to customerId,
                    "pid" to productId,
                    "rating" to rating
                )
                db.collection("rating").add(data)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Rating Successfully", Toast.LENGTH_SHORT)
                            .show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "not add", Toast.LENGTH_SHORT).show()
                    }
            } else {
                for (i in result) {
                    val rate = i.toObject(Rate::class.java)
                    rateList.add(rate)
                }
                val empl: List<Rate> = rateList.stream().filter { s -> s.pid == productId }
                    .collect(Collectors.toList()).filter { s -> s.cid == customerId }
                Log.e("TAG", "inputData: ${empl.size}")

                if (empl.isEmpty()) {
                    val rating = ratingBar.rating
                    val data = hashMapOf(
                        "cid" to customerId,
                        "pid" to productId,
                        "rating" to rating
                    )
                    db.collection("rating").add(data)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Rating Successfully", Toast.LENGTH_SHORT)
                                .show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "not add", Toast.LENGTH_SHORT).show()
                        }
                }else{
                    Toast.makeText(this, "You already rating this product", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun addPurchases(name: String, image: String?, price: Double) {
        val data = hashMapOf(
            "name" to name,
            "price" to price,
            "image" to image
        )
        db.collection("purchases").add(data).addOnSuccessListener {
            Toast.makeText(this, "Add Successfully", Toast.LENGTH_SHORT).show()
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, CartFragment())
                .addToBackStack("").commit()
            Log.e(tag, "created  purchases")
        }.addOnFailureListener {
            Log.e(tag, it.message!!)
        }

    }
}
