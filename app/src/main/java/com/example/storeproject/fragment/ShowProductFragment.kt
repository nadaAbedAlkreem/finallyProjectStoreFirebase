package com.example.storeproject.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
 import com.example.storeproject.activity.UpdateProductActivity
import com.example.storeproject.adapter.ProductAdapter
import com.example.storeproject.adapter.ProductListener
import com.example.storeproject.databinding.FragmentShowProductBinding
import com.example.storeproject.model.MyProduct
import com.example.storeproject.model.ResponseProduct
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson

class ShowProductFragment : Fragment() {
    private lateinit var binding: FragmentShowProductBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private val arrayList = ArrayList<ResponseProduct>()
    private lateinit var productAdapter: ProductAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentShowProductBinding.inflate(inflater, container, false)
        recyclerView = binding.rvStores
        db = Firebase.firestore
        getProductData()

//        refresh
        binding.refresh.setOnRefreshListener {
            if (binding.refresh.isRefreshing) {
                binding.refresh.isRefreshing = false
            }
            arrayList.clear()
            getProductData()
        }

        return binding.root
    }

    private fun getProductData() {
        db.collection("product").get().addOnSuccessListener { result ->
            Log.e("TAG", "getProductData: ${result.documents}")
             for (i in result) {
                val id = i.id
                try {
                    val responseProduct = ResponseProduct()
                    responseProduct.id = id
                    Log.e("TAG", "getProductData: $id")
                    responseProduct.myProduct = i.toObject(MyProduct::class.java)
                    arrayList.add(responseProduct)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            productAdapter = ProductAdapter(arrayList, object : ProductListener {
                override fun deleteItemClick(position: Int, id: String) {
                    val b = AlertDialog.Builder(context)
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
            Toast.makeText(context, "Product deleted successfully", Toast.LENGTH_SHORT).show()
            arrayList.removeAt(position)
            productAdapter.notifyDataSetChanged()
        }.addOnFailureListener { error ->
            Toast.makeText(context, "$error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateProduct(responseProduct: ResponseProduct) {

        val intent = Intent(requireContext(), UpdateProductActivity::class.java)
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
                getProductData()
            }
        }


}