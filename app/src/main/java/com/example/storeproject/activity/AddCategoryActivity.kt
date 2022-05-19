package com.example.storeproject.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.storeproject.databinding.ActivityAddCategoryBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class AddCategoryActivity : AppCompatActivity() {
    private var tag = "nada"
    private var imageUri: Uri? = null
    private var fileURI: Uri? = null
    private val PICK_IMAGE_REQUEST = 111
    private lateinit var binding: ActivityAddCategoryBinding
    lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = Firebase.firestore
        val storage = Firebase.storage
        val storageRef = storage.reference
        val imageRef = storageRef.child("images")

        binding.addIcon.setOnClickListener {
            startFileChooser()
        }
        binding.btnupdate.setOnClickListener {
            // Get the data from an ImageView as bytes
            binding.progressBar.visibility = View.VISIBLE
            val bitmap = (binding.imgaCatgory.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val data = baos.toByteArray()
            val childRef =
                imageRef.child(System.currentTimeMillis().toString() + "NadaProjectImages.png")
            val uploadTask = childRef.putBytes(data)
            uploadTask.addOnFailureListener {
                Log.e(tag, it.message.toString())
            }.addOnSuccessListener { _ ->
                Log.e(tag, "Image Upload Successfully")
                childRef.downloadUrl.addOnSuccessListener {
                    Log.e(tag, it.toString())
                    fileURI = it
                    val nameCategores = binding.nameCategory.text.toString()
                    addCategory(nameCategores, fileURI!!.toString())
                    Toast.makeText(this, "Image Upload Successfully", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Log.e("nada", "not add")
                }
            }
        }

    }

    private fun startFileChooser() {
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_PICK
        startActivityForResult(i, PICK_IMAGE_REQUEST)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )
        if (requestCode == 111 && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data!!
            Log.e(tag, imageUri.toString())
            binding.imgaCatgory.setImageURI(imageUri)

        }
    }

    private fun addCategory(name: String, image: String) {
        val data = hashMapOf(
            "name" to name,
            "image" to image
        )
        db.collection("DataCategory").add(data).addOnSuccessListener {
            binding.progressBar.visibility = View.GONE
            Toast.makeText(this, "Add Successfully", Toast.LENGTH_SHORT).show()

            Log.e(tag, "created and add category")
        }.addOnFailureListener {
            Log.e(tag, it.message!!)
        }

    }
}