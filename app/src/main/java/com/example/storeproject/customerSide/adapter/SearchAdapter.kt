package com.example.storeproject.customerSide.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.storeproject.activity.ProductDetailsActivity
import com.example.storeproject.databinding.FragmentDesginSearchBinding
import com.example.storeproject.model.ResponseProduct
import com.squareup.picasso.Picasso


class SearchAdapter(
    private var data: ArrayList<ResponseProduct>
) : RecyclerView.Adapter<SearchAdapter.MyViewHolder>() {

    class MyViewHolder(var binding: FragmentDesginSearchBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = FragmentDesginSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val responseProduct = data[position]
        holder.binding.nameSearch.text = responseProduct.myProduct.name

        Picasso.get().load(responseProduct.myProduct.product_img).into(holder.binding.imageSearch)
        holder.binding.showDetails.setOnClickListener {
            val intent = Intent(holder.itemView.context, ProductDetailsActivity::class.java)
            intent.putExtra("productId", data[position].id)
            holder.itemView.context.startActivity(intent)
        }
    }


    override fun getItemCount(): Int {
        return data.size

    }


}
