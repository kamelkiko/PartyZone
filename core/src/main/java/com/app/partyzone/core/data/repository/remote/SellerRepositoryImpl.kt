package com.app.partyzone.core.data.repository.remote

import com.app.partyzone.core.domain.entity.Seller
import com.app.partyzone.core.domain.repository.SellerRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SellerRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
) : SellerRepository {

    override suspend fun getCurrentSeller(): Seller {
        val userAuthId = firebaseAuth.currentUser?.uid
        val userDocument = firestore.collection("sellers")
            .document(userAuthId ?: "")
            .get()
            .await()

        return Seller(
            id = userDocument.get("id").toString(),
            name = userDocument.get("name").toString(),
            email = userDocument.get("email").toString(),
            photoUrl = userDocument.get("photoUrl").toString(),
            location = userDocument.get("location").toString(),
            description = userDocument.get("description").toString(),
            contactInfo = userDocument.get("contactInfo").toString(),
        )
    }

    override suspend fun getSellerById(id: String): Seller {
        val userDocument = firestore.collection("sellers")
            .document(id)
            .get()
            .await()

        return Seller(
            id = userDocument.get("id").toString(),
            name = userDocument.get("name").toString(),
            email = userDocument.get("email").toString(),
            photoUrl = userDocument.get("photoUrl").toString(),
            location = userDocument.get("location").toString(),
            description = userDocument.get("description").toString(),
            contactInfo = userDocument.get("contactInfo").toString(),
        )
    }

    override suspend fun updateCurrentSeller(seller: Seller) {

    }
}