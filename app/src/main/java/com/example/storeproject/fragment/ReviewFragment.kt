package com.example.storeproject.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.storeproject.adapter.RateAdapter
import com.example.storeproject.databinding.FragmentReviewBinding
import com.example.storeproject.model.Rate
import com.example.storeproject.model.ResponseRating
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ReviewFragment : Fragment() {
    lateinit var binding: FragmentReviewBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private val ratingList = ArrayList<ResponseRating>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding =
            FragmentReviewBinding.inflate(inflater, container, false)
        recyclerView = binding.rvRate
        db = Firebase.firestore
        getProductData()

        return binding.root
    }

    private fun getProductData() {
        db.collection("rating").get().addOnSuccessListener { result ->
            Log.e("TAG", "getRatingData: ${result.documents}")
            for (i in result) {
                val id = i.id
                try {
                    val responseRating = ResponseRating()
                    responseRating.id = id
                    Log.e("TAG", "getProductData////: $id")
                    responseRating.rate = i.toObject(Rate::class.java)

                    ratingList.add(responseRating)


                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            binding.rvRate.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = RateAdapter(ratingList)
            }
        }.addOnFailureListener {}
    }

}