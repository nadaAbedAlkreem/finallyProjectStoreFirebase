package com.example.storeproject.model

data class Rate(
    var cid: String = "",
    var pid: String = "",
    var rating: Int = 0,
)

data class ResponseRating(
    var id: String = "",
    var rate: Rate = Rate(),
)