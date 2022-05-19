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
import com.example.storeproject.databinding.FragmentAddProductBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream


@Suppress("DEPRECATION")
class AddProductActivity : AppCompatActivity() {
    private lateinit var binding: FragmentAddProductBinding
    private var fileURI: Uri? = null
    private val PICK_IMAGE_REQUEST = 111
    private var imgURI: Uri? = null
    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // upload img
        val storage = Firebase.storage
        val storageRef = storage.reference
        val imgRef = storageRef.child("productImages")
        binding.addImg.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_PICK
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
        db = Firebase.firestore
        binding.add.setOnClickListener {
            val name = binding.edtName.text.toString()
            val category = intent.getStringExtra("id")
//           Toast.makeText(this , "${category} nada"  , Toast.LENGTH_SHORT).show()
            val description = binding.edtDescription.text.toString()
            val price = binding.edtprice.text.toString().toDouble()
            val bitmap = (binding.addImg.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val data = baos.toByteArray()
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
                    addProduct(name, category!!, description, price, fileURI)

                }
            }
        }
    }

    private fun addProduct(
        name: String, category: String, description: String, price: Double,
        product_img: Uri?,
    ) {
        val data = hashMapOf(
            "name" to name,
            "category_id" to category,
            "description" to description,
            "price" to price,
            "product_img" to product_img
        )
        db.collection("product").add(data).addOnSuccessListener {
            Log.e("tag", "Added successfully with id:${it.id}")
            Toast.makeText(this, "Added successfully", Toast.LENGTH_SHORT).show()
            binding.edtName.setText("")
            binding.edtDescription.setText("")
            binding.edtprice.setText("")
        }
            .addOnFailureListener {
                Log.d("tag", it.message!!)
                Toast.makeText(this, "Added Failed", Toast.LENGTH_SHORT).show()

            }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            imgURI = data!!.data
            binding.addImg.setImageURI(imgURI)
        }
    }


}