package com.example.storeproject.customerSide.adapter

import android.app.Activity
import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.storeproject.databinding.FragmentDesginCardBinding
import com.example.storeproject.model.Purchases
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class ProductAdapterCustomerCard(var activity: Activity, var data: ArrayList<Purchases>) :
    RecyclerView.Adapter<ProductAdapterCustomerCard.MyViewHolder>() {
    class MyViewHolder(var binding: FragmentDesginCardBinding) :
        RecyclerView.ViewHolder(binding.root)
    private lateinit var db: FirebaseFirestore

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            FragmentDesginCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.namecard.text = data[position].name
        holder.binding.price.text = data[position].price.toString()
        Picasso.get().load(data[position].image).into(holder.binding.imageCard)
        var count = 0
        holder.binding.plus.setOnClickListener {
//            holder.binding.counter(count).toString().toInt()
            count++
            holder.binding.counter.text = count.toString()
        }
        holder.binding.reduse.setOnClickListener {
            if (count >0 ) {
                count--
                holder.binding.counter.text = count.toString()
            }
        }
        holder.binding.btnDelete.setOnClickListener {
            val b = AlertDialog.Builder(activity)
            b.setTitle("Delete Product")
            b.setMessage("Are You Sure want to Delete Product?")
            b.setPositiveButton("Yes") { _, _ ->
                delete(position, data[position].id)
            }
            b.setNegativeButton("No") { d, _ ->
                d.dismiss()
            }
            b.create().show()
        }
        }

    override fun getItemCount(): Int {
        return data.size
    }
    private fun  delete(position: Int , id: String){
        db = Firebase.firestore

        db.collection("purchases").document(id).delete().addOnSuccessListener {
//            Toast.makeText(this, "Product deleted successfully", Toast.LENGTH_SHORT).show()
            Log.e("nada" , "delete successfully")
            data.removeAt(position)
          }.addOnFailureListener { error ->
            Log.e("nada" ,"delete error" )
         }
    }
}
