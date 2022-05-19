package com.example.storeproject.customerSide

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.storeproject.customerSide.adapter.ProductAdapterCustomer
import com.example.storeproject.R
import com.example.storeproject.databinding.ActivityShowCategroyWithProductCustomerBinding
import com.example.storeproject.model.MyProduct
import com.example.storeproject.model.ResponseProduct
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class ShowCategroyWithProductCustomer : AppCompatActivity() {
    private lateinit var binding: ActivityShowCategroyWithProductCustomerBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private val arrayList = ArrayList<ResponseProduct>()
    private lateinit var productAdapterCustomer: ProductAdapterCustomer
    private var categoryName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShowCategroyWithProductCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)

                recyclerView = binding.rvStores
                db = Firebase.firestore
                categoryName = intent.getStringExtra("name")!!


            Picasso.get().load(intent.getStringExtra("image")).placeholder(R.drawable.progress_animation)
            .into(binding.imgCategory)
                getProductData(categoryName)

//        refresh
                binding.refresh.setOnRefreshListener {
            if (binding.refresh.isRefreshing) {
                binding.refresh.isRefreshing = false
            }
            arrayList.clear()
            getProductData(categoryName)
        }





    }

    private fun getProductData(categoryName: String) {
        binding.txtCategory.text = categoryName
        val id = intent.getStringExtra("id")
        db.collection("product").whereEqualTo("category_id", id).get()
            .addOnSuccessListener { result ->
//            if (result.isEmpty) {
//                Toast.makeText(context, "No Task Found", Toast.LENGTH_SHORT).show()
//            }
                for (i in result) {
                    val id = i.id
                    try {
                        val responseProduct = ResponseProduct()
                        responseProduct.id = id
                        responseProduct.myProduct = i.toObject(MyProduct::class.java)
                        arrayList.add(responseProduct)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                productAdapterCustomer = ProductAdapterCustomer(arrayList)
                binding.rvStores.apply {
                    adapter = productAdapterCustomer
                }
            }
            .addOnFailureListener { error ->
                Log.e("ayat", "$error")
            }
    }



}