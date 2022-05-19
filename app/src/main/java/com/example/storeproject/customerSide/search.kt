package com.example.storeproject.customerSide

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storeproject.customerSide.adapter.SearchAdapter
import com.example.storeproject.databinding.FragmentSearchBinding
import com.example.storeproject.model.MyProduct
import com.example.storeproject.model.ResponseProduct
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class Search : Fragment() {
    lateinit var binding : FragmentSearchBinding
    lateinit var db: FirebaseFirestore

    private val arrayList = ArrayList<ResponseProduct>()
    private lateinit var searchAdapter: SearchAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        db = Firebase.firestore


        binding.searchIcon.setOnClickListener {
          val nameSearch  =   binding.search.text.toString()
            getSearchUser(nameSearch)

         }




        return binding.root
    }

    private  fun getSearchUser(name : String) {
         db.collection("product").whereEqualTo("name" , name).get().addOnSuccessListener { query ->
            for (getData in query) {
                val id = getData.id
                try {
                    val responseProduct = ResponseProduct()
                    responseProduct.id = id
                    responseProduct.myProduct = getData.toObject(MyProduct::class.java)
                    arrayList.add(responseProduct)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
             searchAdapter = SearchAdapter(arrayList)

             binding.searchRecycler.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = searchAdapter
            }
        }.addOnFailureListener {
            Log.e("nada", "not found result")
        }

    }



}
