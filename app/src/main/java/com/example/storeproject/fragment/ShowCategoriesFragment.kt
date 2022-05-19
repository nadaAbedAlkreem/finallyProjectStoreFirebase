package com.example.storeproject.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager

import com.example.storeproject.activity.AddCategoryActivity
import com.example.storeproject.adapter.CategoryAdapter
import com.example.storeproject.databinding.FragmentShowCategoresBinding
import com.example.storeproject.model.Categories
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ShowCategoriesFragment : Fragment() {
    lateinit var db: FirebaseFirestore

    private lateinit var binding: FragmentShowCategoresBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentShowCategoresBinding.inflate(inflater, container, false)
        db = Firebase.firestore

        getCategories()

        binding.addCatgorybtn.setOnClickListener {
            startActivity(Intent(requireContext(), AddCategoryActivity::class.java))
        }

        binding.refresh.setOnRefreshListener {
            if (binding.refresh.isRefreshing) {
                binding.refresh.isRefreshing = false
            }
            getCategories()
        }


        return binding.root
    }

    private fun getCategories() {
        val arrayList = ArrayList<Categories>()
        db.collection("DataCategory").get().addOnSuccessListener { query ->
            for (getData in query) {
                val result = Categories(
                    getData.id,
                    getData.getString("name")!!,
                    getData.getString("image")!!
                )
                arrayList.add(result)
                Log.e("tag", "yes  and ${result.name}")
            }

            binding.recyclerItem.apply {
                layoutManager =
                    GridLayoutManager(requireActivity(), 2)
                adapter = CategoryAdapter(requireActivity(),
                    arrayList,
                    object : CategoryAdapter.CategoryListener {
                        @SuppressLint("NotifyDataSetChanged")
                        override fun deleteItemClick(position: Int) {
                            val alertDialog = AlertDialog.Builder(activity)
                            alertDialog.setTitle("Delete Student")
                            alertDialog.setMessage("Are you sure?")
                            alertDialog.setPositiveButton("Yes") { _, _ ->
                                db.collection("DataCategory").document(arrayList[position].id)
                                    .delete()
                                    .addOnSuccessListener {
                                        arrayList.removeAt(position)
                                        adapter!!.notifyDataSetChanged()
                                        Log.e("nada", "success delete")
                                    }
                                    .addOnFailureListener {
                                        Log.e("nada", "failure delete")
                                    }
                            }.setNegativeButton("No") { d, _ ->
                                d.dismiss()
                            }
                            alertDialog.create().show()
                        }
                    })
            }

        }.addOnFailureListener {
            Log.e("nada", it.message.toString())
        }

    }
}