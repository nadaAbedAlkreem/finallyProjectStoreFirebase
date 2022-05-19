package com.example.storeproject.customerSide

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.storeproject.customerSide.adapter.CustomerAdapter
import com.example.storeproject.R
import com.example.storeproject.databinding.FragmentShowCategoryCustomerBinding
import com.example.storeproject.model.Categories

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ShowCategoryCustomer : Fragment() {
   lateinit var binding : FragmentShowCategoryCustomerBinding
    lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShowCategoryCustomerBinding.inflate(inflater, container, false)
        db = Firebase.firestore

        getAllUser()

        binding.refresh.setOnRefreshListener {
            if (binding.refresh.isRefreshing) {
                binding.refresh.isRefreshing = false
            }
            getAllUser()
        }
       binding.searchIconNO.setOnClickListener {
         swipe(Search())
       }
        binding.search.setOnClickListener {
            swipe(Search())
        }


        return binding.root
    }
    private fun getAllUser() {
        val arrayList = ArrayList<Categories>()
        db.collection("DataCategory").get().addOnSuccessListener { query ->

            for (getData in query) {


                val result = Categories(
                    getData.id,
                    getData.getString("name")!!,
                    getData.getString("image")!!
                )
                arrayList.add(result)
                Log.e("nada", "yes  and ${result.name}")
            }

            binding.recyclerCustomerCagetory.apply {
                layoutManager =
                    GridLayoutManager(requireActivity(), 2)
                adapter = CustomerAdapter(requireActivity(), arrayList)
            }

        }.addOnFailureListener {
            Log.e("nada", it.message.toString())
        }

    }

    private fun swipe(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.container, fragment)
            .addToBackStack("").commit()
    }

}