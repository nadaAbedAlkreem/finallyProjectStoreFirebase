package com.example.storeproject.model

data class MyProduct(
    val name: String = "",
    val product_img: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val category_id: String = "",
)

data class ResponseProduct(
    var id: String = "",
    var myProduct: MyProduct = MyProduct(),
)