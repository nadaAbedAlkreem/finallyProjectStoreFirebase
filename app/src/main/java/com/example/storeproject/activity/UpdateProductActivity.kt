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
import com.example.storeproject.databinding.FragmentUpdateProductBinding
import com.example.storeproject.model.ResponseProduct
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream


class UpdateProductActivity : AppCompatActivity() {
    private lateinit var binding: FragmentUpdateProductBinding
    lateinit var db: FirebaseFirestore
    lateinit var responseProduct: ResponseProduct
    private var fileURI: Uri? = null
    private var imgURI: Uri? = null
    private val PICK_IMAGE_REQUEST = 111
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentUpdateProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setComponent()
    }

    private fun setComponent() {
        db = Firebase.firestore
        val gsonString = intent.getStringExtra("gson")
        responseProduct = Gson().fromJson(gsonString, ResponseProduct::class.java)
        binding.edtName.setText(responseProduct.myProduct.name)
        binding.edtDescription.setText(responseProduct.myProduct.description)
        binding.edtprice.setText(responseProduct.myProduct.price.toString())
        val storage = Firebase.storage
        val storageRef = storage.reference
        val imgRef = storageRef.child("productImages")
        binding.addImg.setOnClickListener {

            val intent = Intent()
            intent.action = Intent.ACTION_PICK
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        db.collection("product").get().addOnSuccessListener {
            for (docu in it) {
                if (docu.id == responseProduct.id) {
                    val image = docu.getString("product_img")
                    Picasso.get().load(image).into(binding.addImg)
                }
            }
        }
        binding.btnUpdate.setOnClickListener {
            val bitmap = (binding.addImg.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()
            Log.e("TAG", "setComponent: $data")
            val childRef = imgRef.child(System.currentTimeMillis().toString() + "_ayatimages.png")
            val uploadTask = childRef.putBytes(data)
            uploadTask.addOnFailureListener { exception ->
                Log.e("TAG", "${exception.message}")
            }.addOnSuccessListener {
                Log.e("TAG", "Success")
                Toast.makeText(this, "Image Upload Successfully", Toast.LENGTH_SHORT).show()
                childRef.downloadUrl.addOnSuccessListener { uri ->
                    Log.e("TAG", uri.toString())
                    fileURI = uri
                    updateData(fileURI.toString())
                }

            }
        }

    }

    private fun updateData(product_img: String) {
        val name = binding.edtName.text.toString().trim()
        // val category = binding.edtCategory.text.toString().trim()
        val description = binding.edtDescription.text.toString().trim()
        val price = binding.edtprice.text.toString().toDouble()

        val data = hashMapOf(
            "name" to name,
            // "category_id" to category,
            "description" to description,
            "price" to price,
            "product_img" to product_img
        )
        db.collection("product").document(responseProduct.id)
            .update(data as Map<String, Any>)
            .addOnSuccessListener {
                Log.e("TAG", "updateData: $data")
                Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show()
                setResult(20)
                finish()

            }.addOnFailureListener { error ->
                Log.e("TAG", "updateData: $error")
            }


    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            imgURI = data!!.data
            Log.e("TAG", "onActivityResult: $imgURI")
            binding.addImg.setImageURI(imgURI)
        }
    }
}