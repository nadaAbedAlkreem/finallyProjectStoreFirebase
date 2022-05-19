package com.example.storeproject.adapter

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.storeproject.R
import com.example.storeproject.activity.ShowProductCategoryActivity
import com.example.storeproject.activity.UpdateCategoryActivity
import com.example.storeproject.databinding.CategoryItemRowBinding
import com.example.storeproject.model.Categories
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso


class CategoryAdapter(
    var activity: Activity,
    private var data: ArrayList<Categories>, private var listener: CategoryListener,
) : RecyclerView.Adapter<CategoryAdapter.MyView>() {
    private lateinit var db: FirebaseFirestore

    class MyView(var binding: CategoryItemRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView {
        val binding = CategoryItemRowBinding.inflate(activity.layoutInflater, parent, false)
        return MyView(binding)
    }


    override fun onBindViewHolder(holder: MyView, position: Int) {
        db = Firebase.firestore
        holder.binding.nameCategorydesgin.text = data[position].name
        Picasso.get().load(data[position].image).placeholder(R.drawable.progress_animation)
            .into(holder.binding.imageCategoryDesgin)
        Log.e("nada", data[position].image)
        holder.binding.deletCateoryItem.setOnClickListener {
            listener.deleteItemClick(position)
        }
        holder.binding.update.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateCategoryActivity::class.java)
            intent.putExtra("name", data[position].name)
            holder.itemView.context.startActivity(intent)
        }
        holder.binding.arro.setOnClickListener {
            val intent = Intent(holder.itemView.context, ShowProductCategoryActivity::class.java)
            intent.putExtra("name", data[position].name)
            intent.putExtra("image", data[position].image)
            intent.putExtra("id", data[position].id)


            holder.itemView.context.startActivity(intent)
        }
    }


    override fun getItemCount(): Int {
        return data.size

    }

    interface CategoryListener {
        fun deleteItemClick(position: Int)
    }
}
