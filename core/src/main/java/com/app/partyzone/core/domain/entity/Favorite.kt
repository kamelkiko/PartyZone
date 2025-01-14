package com.app.partyzone.core.domain.entity

data class Favorite(
    val id: String,
    val userId: String,
    val type: String,  //seller , post , offer
    val itemId: String,
    val sellerId:String,
    val name: String,
    val location: String,
    val imageUrl: String? = null,
    val price: Double? = null
)