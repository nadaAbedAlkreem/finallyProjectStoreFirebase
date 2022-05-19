package com.example.storeproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.storeproject.databinding.ProductItemBinding
import com.example.storeproject.model.ResponseProduct
import com.squareup.picasso.Picasso

class ProductAdapter(
    private var data: ArrayList<ResponseProduct>,
    private var listener: ProductListener,
) :
    RecyclerView.Adapter<ProductAdapter.MyViewHolder>() {
    class MyViewHolder(var binding: ProductItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val responseProduct = data[position]
        holder.binding.txtName.text = responseProduct.myProduct.name
        holder.binding.btnUpdate.setOnClickListener {
            listener.updateItemClick(responseProduct)
        }
        holder.binding.btnDelete.setOnClickListener {
            listener.deleteItemClick(position, responseProduct.id)
        }
        Picasso.get().load(responseProduct.myProduct.product_img).into(holder.binding.imgProduct)
//        holder.binding.arroProdcat.setOnClickListener {
//            val intent = Intent(holder.itemView.context, ProductDetailsActivity::class.java)
//            intent.putExtra("id", data[position].id)
//            holder.itemView.context.startActivity(intent)
//        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}

interface ProductListener {
    fun deleteItemClick(position: Int, id: String)
    fun updateItemClick(responseProduct: ResponseProduct)

}