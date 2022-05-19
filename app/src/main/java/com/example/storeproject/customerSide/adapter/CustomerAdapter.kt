package com.example.storeproject.customerSide.adapter

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.storeproject.customerSide.ShowCategroyWithProductCustomer
import com.example.storeproject.R
import com.example.storeproject.databinding.FragmentDesginCategoryCustomerBinding
import com.example.storeproject.model.Categories
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso


class CustomerAdapter(
    var activity: Activity,
    var data: ArrayList<Categories>,
) : RecyclerView.Adapter<CustomerAdapter.MyView>() {
    private lateinit var db: FirebaseFirestore

    class MyView(var binding: FragmentDesginCategoryCustomerBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView {
        val binding =
            FragmentDesginCategoryCustomerBinding.inflate(activity.layoutInflater, parent, false)



        return MyView(binding)
    }


    override fun onBindViewHolder(holder: MyView, position: Int) {
        db = Firebase.firestore
        holder.binding.nameCategoryCustomer.text = data[position].name
        Picasso.get().load(data[position].image).placeholder(R.drawable.progress_animation)
            .into(holder.binding.imageCategoryCustomer)
        Log.e("nada", data[position].image)
        holder.binding.arroShowProductCustomer.setOnClickListener {
            val intent =
                Intent(holder.itemView.context, ShowCategroyWithProductCustomer::class.java)

            intent.putExtra("name", data[position].name)
            intent.putExtra("image", data[position].image)
            intent.putExtra("id", data[position].id)
            holder.itemView.context.startActivity(intent)
        }

    }


    override fun getItemCount(): Int {
        return data.size

    }


}
