package com.example.storeproject.customerSide

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storeproject.customerSide.adapter.ProductAdapterCustomerCard
import com.example.storeproject.databinding.ActivityCardBinding
import com.example.storeproject.model.Purchases
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CartFragment : Fragment() {
    private lateinit var binding: ActivityCardBinding
    private lateinit var db: FirebaseFirestore
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = ActivityCardBinding.inflate(inflater, container, false)
        db = Firebase.firestore
        getpurchases()
        return binding.root
    }

    private fun getpurchases() {
        val arrayList = ArrayList<Purchases>()
        db.collection("purchases").get().addOnSuccessListener { query ->
            for (getData in query) {
                val result = Purchases(
                    getData.id,
                    getData.getString("name")!!,
                    getData.getString("image")!!,
                    getData.get("price").toString().toDouble()
                )
                arrayList.add(result)
                Log.e("nada", "yes  and ${result.name}")
            }
            binding.recyclerItem.apply {
                layoutManager =
                    LinearLayoutManager(requireContext())
                adapter = ProductAdapterCustomerCard(requireActivity(),arrayList)
            }
        }.addOnFailureListener {
            Log.e("nada", it.message.toString())
        }
    }
}