package com.example.storeproject.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.storeproject.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream

class ProfileFragment : Fragment() {
    private var filluri: Uri? = null
    private val imge_pick_request = 111
    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth

    lateinit var binding: FragmentProfileBinding
    var imge_uri: Uri? = null
    val TAG = "hzm"

    @SuppressLint("WrongThread")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val storage = Firebase.storage
        val storageref = storage.reference
        val imgeref = storageref.child("imges")
        auth = Firebase.auth
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        getAllStudents()

        val sharedpref = requireActivity().getSharedPreferences("dataUser", Context.MODE_PRIVATE)
        val email = sharedpref.getString("data", "email")
        db.collection("ImageCustomer").whereEqualTo("email", email).get()
            .addOnSuccessListener { query ->
                for (getData in query) {
                    Picasso.get().load(getData.getString("image")).into(binding.imge)
                    Log.e("nada", getData.getString("image").toString())
                }
            }.addOnFailureListener {
                Log.e("nada", it.message.toString())
            }
        binding.imge.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_PICK
            intent.type = "image/*"
            startActivityForResult(intent, imge_pick_request)
        }

        val bitmap = (binding.imge.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val data = baos.toByteArray()
        val childRef = imgeref.child(System.currentTimeMillis().toString() + "_ayatimages.png")
        val uploadTask = childRef.putBytes(data)
        uploadTask.addOnFailureListener { exception ->
            Log.e("TAG", "${exception.message}")
        }.addOnSuccessListener {
            Log.e("TAG", "Success uploa")
            childRef.downloadUrl.addOnSuccessListener { uri ->
                Log.e("TAG", uri.toString())
                filluri = uri
                val sharedpref =
                    requireActivity().getSharedPreferences("dataUser", Context.MODE_PRIVATE)
                val email = sharedpref.getString("data", "email")

                addImage(email!!, filluri!!.toString())
            }
        }.addOnFailureListener {

            Log.e("nada", it.message.toString())

        }

        return binding.root

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == imge_pick_request && resultCode == Activity.RESULT_OK) {
            imge_uri = data!!.data
            Log.e("TAG", imge_uri.toString())
            binding.imge.setImageURI(imge_uri)
        }
    }

    private fun getAllStudents() {
        db = Firebase.firestore
        val sharedpref = requireActivity().getSharedPreferences("dataUser", Context.MODE_PRIVATE)
        val email1 = sharedpref.getString("data", "email")
        val password = sharedpref.getString("password", "password")

        Log.e("nadak", email1.toString())
        db.collection("customer").whereEqualTo("email", email1).whereEqualTo("password", password)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {

                    Log.e("hzm", document.id)
                    Log.e("nada", document.getString("name").toString())
                    Log.e("nada", document.getString("date").toString())
                    binding.name.text = document.getString("name")!!
                    binding.email.text = document.getString("email")!!
                    binding.date.text = document.getString("date")!!
                    binding.password.text = document.getString("password")!!
                }


            }
            .addOnFailureListener { error ->
                Log.e("nada", error.message.toString())
            }
    }


    private fun addImage(emailUser: String, image: String) {
        val data = hashMapOf(
            "emailUser" to emailUser,
            "image" to image
        )
        db.collection("ImageCustomer").add(data).addOnSuccessListener {
            Toast.makeText(requireActivity(), "Add Successfully image", Toast.LENGTH_SHORT).show()

        }.addOnFailureListener {
            Toast.makeText(requireActivity(), "Add failure image", Toast.LENGTH_SHORT).show()

            Log.e(tag, it.message!!)
        }

    }

    private fun getImageCustomer() {


    }
}
