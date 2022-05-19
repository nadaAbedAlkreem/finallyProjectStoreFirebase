package com.example.storeproject.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.storeproject.databinding.RateItemBinding
import com.example.storeproject.model.MyProduct
import com.example.storeproject.model.ResponseRating
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class RateAdapter(private var list: ArrayList<ResponseRating>) :
    RecyclerView.Adapter<RateAdapter.MyViewHolder>() {
    var db: FirebaseFirestore = Firebase.firestore

    class MyViewHolder(var binding: RateItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val rate = list[position]
        holder.binding.ratingBar.rating = rate.rate.rating.toFloat()
        Log.e("TAG", "onBindViewHolder: ${rate.rate.rating}")
        db.collection("product").document(rate.rate.pid).get().addOnSuccessListener { result ->
            val product = result.toObject(MyProduct::class.java)
            holder.binding.txtName.text = product!!.name
            Picasso.get().load(product.product_img).into(holder.binding.imgProduct)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}