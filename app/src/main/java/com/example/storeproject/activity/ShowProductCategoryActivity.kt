package com.example.storeproject.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.storeproject.R
import com.example.storeproject.adapter.ProductAdapter
import com.example.storeproject.adapter.ProductListener
import com.example.storeproject.databinding.ActivityShowProductCategoryBinding
import com.example.storeproject.model.MyProduct
import com.example.storeproject.model.ResponseProduct
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.squareup.picasso.Picasso

class ShowProductCategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShowProductCategoryBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private val arrayList = ArrayList<ResponseProduct>()
    private lateinit var productAdapter: ProductAdapter
    private var categoryName = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShowProductCategoryBinding.inflate(layoutInflater)
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

        binding.btnAdd.setOnClickListener {
           val id =  intent.getStringExtra("id")
            val intent = Intent(this, AddProductActivity::class.java)
            intent.putExtra("name", categoryName)
            intent.putExtra("id" ,id )
            startActivity(intent)
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
                productAdapter = ProductAdapter(arrayList, object : ProductListener {
                    override fun deleteItemClick(position: Int, id: String) {
                        val b = AlertDialog.Builder(this@ShowProductCategoryActivity)
                        b.setTitle("Delete Product")
                        b.setMessage("Are You Sure want to Delete Product?")
                        b.setPositiveButton("Yes") { _, _ ->
                            deleteProduct(position, id)
                        }
                        b.setNegativeButton("No") { d, _ ->
                            d.dismiss()
                        }
                        b.create().show()
                    }

                    override fun updateItemClick(responseProduct: ResponseProduct) {
                        updateProduct(responseProduct)
                    }

                })
                binding.rvStores.apply {
                    layoutManager = GridLayoutManager(context, 2)
                    adapter = productAdapter
                }
            }
            .addOnFailureListener { error ->
                Log.e("ayat", "$error")
            }
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun deleteProduct(position: Int, id: String) {
//        Log.e("TAG", "deleteBook: $id")
        db.collection("product").document(id).delete().addOnSuccessListener {
            Toast.makeText(this, "Product deleted successfully", Toast.LENGTH_SHORT).show()
            arrayList.removeAt(position)
            productAdapter.notifyDataSetChanged()
        }.addOnFailureListener { error ->
            Toast.makeText(this, "$error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateProduct(responseProduct: ResponseProduct) {

        val intent = Intent(this, UpdateProductActivity::class.java)
        intent.putExtra("gson", Gson().toJson(responseProduct))
        startResult.launch(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    private val startResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            if (resultCode == 20) {
                arrayList.clear()
                productAdapter.notifyDataSetChanged()
                getProductData(categoryName)
            }
        }


}