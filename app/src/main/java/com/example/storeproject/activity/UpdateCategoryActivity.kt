package com.example.storeproject.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.storeproject.databinding.ActivityUpdateCategoryBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream

class UpdateCategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateCategoryBinding
    private lateinit var db: FirebaseFirestore
    private var imageUri: Uri? = null
    private var tag = "nada"

    private var fileURI: Uri? = null
    private val PICK_IMAGE_REQUEST = 111
    private var categoryName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout for this fragment
        binding = ActivityUpdateCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        categoryName = intent.getStringExtra("name")!!
        db = Firebase.firestore
        val storage = Firebase.storage
        val storageRef = storage.reference
        val imageRef = storageRef.child("images")

        db.collection("DataCategory").get().addOnSuccessListener { query ->
            for (docu in query) {
                if (categoryName == docu.getString("name")) {
                    Picasso.get().load(docu.getString("image")).into(binding.imgaCatgory)
                    binding.nameCategory.setText(docu.getString("name"))
                }
            }
        }.addOnFailureListener {
            Log.e("nada", "failure")
        }
        binding.addIcon.setOnClickListener {
            startFileChooser()
        }
        binding.UpdateCateoryItem.setOnClickListener {
            val bitmap = (binding.imgaCatgory.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val data = baos.toByteArray()
            val childRef =
                imageRef.child(System.currentTimeMillis().toString() + "NadaProjectImages.png")
            val uploadTask = childRef.putBytes(data)
            uploadTask.addOnFailureListener {
                Log.e(tag, it.message.toString())
            }.addOnSuccessListener {
                Log.e(tag, "Image Upload Successfully")
                childRef.downloadUrl.addOnSuccessListener {
                    Log.e(tag, it.toString())
                    fileURI = it
                    updateData(binding.nameCategory.text.toString(), fileURI.toString())
                }.addOnFailureListener {
                    Log.e(tag, "failure update")
                }
            }
        }
    }


    private fun updateData(name: String, image: String) {
        db = Firebase.firestore
        db.collection("DataCategory").get().addOnSuccessListener { query ->
            for (docu in query) {
                if (categoryName == docu.getString("name")) {
                    Log.e("nada", categoryName)
                    db.collection("DataCategory").document(docu.id)
                        .update("name", name, "image", image)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Update Successfully", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .addOnFailureListener {
                            Log.e("nada", "failure")
                        }

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
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 111 && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data!!
            Log.e(tag, imageUri.toString())
            binding.imgaCatgory.setImageURI(imageUri)

        }
    }
}