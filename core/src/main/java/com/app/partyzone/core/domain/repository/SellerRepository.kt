package com.app.partyzone.core.domain.repository

import com.app.partyzone.core.domain.entity.Seller

interface SellerRepository {
    fun getCurrentSeller(): Seller
    fun getSellerById(id: Seller): Seller
    fun updateCurrentSeller(seller: Seller)
}