package com.example.storeproject.customerSide.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.storeproject.activity.ProductDetailsActivity
import com.example.storeproject.databinding.FragmentDesginProductCustomerBinding
import com.example.storeproject.model.Rate
import com.example.storeproject.model.ResponseProduct
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import java.util.stream.Collectors

class ProductAdapterCustomer(private var data: ArrayList<ResponseProduct>) :
    RecyclerView.Adapter<ProductAdapterCustomer.MyViewHolder>() {
    class MyViewHolder(var binding: FragmentDesginProductCustomerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            FragmentDesginProductCustomerBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false)
        return MyViewHolder(binding)
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val responseProduct = data[position]
        holder.binding.namePruductCustomer.text = responseProduct.myProduct.name

        Picasso.get().load(responseProduct.myProduct.product_img)
            .into(holder.binding.imageProductCustomer)

        holder.binding.arroDetails.setOnClickListener {
            val intent = Intent(holder.itemView.context, ProductDetailsActivity::class.java)
            intent.putExtra("productId", data[position].id)
            holder.itemView.context.startActivity(intent)
        }
        val db = Firebase.firestore
        val rateList = java.util.ArrayList<Rate>()
        db.collection("rating").get().addOnSuccessListener { result ->
            if (!result.isEmpty) {

                for (i in result) {
                    val rate = i.toObject(Rate::class.java)
                    rateList.add(rate)
                }
                val empl: List<Rate> = rateList.stream().filter { s -> s.pid == responseProduct.id }
                    .collect(Collectors.toList())
                if (empl.isNotEmpty()) {
                    var rateValue = 0
                    Log.e("TAG", "inputData: ${empl.count()} ${empl.size}")
                    for (rate in empl.iterator()) {
                        rateValue += rate.rating
                    }
                    val result = rateValue / empl.size
                    holder.binding.ratingBar.rating = result.toFloat()
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
