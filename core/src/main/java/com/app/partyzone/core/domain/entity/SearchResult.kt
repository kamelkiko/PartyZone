package com.app.partyzone.core.domain.entity

data class SearchResult(
    val id: String,
    val name: String,
    val location: String,
    val sellerId: String,
    val imageUrl: String,
    val itemId: String,
    val type: String,
    val isFav: Boolean,
    val favId: String? = null,
    val price: Double? = null,
)