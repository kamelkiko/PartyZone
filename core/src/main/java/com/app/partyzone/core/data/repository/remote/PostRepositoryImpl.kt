package com.app.partyzone.core.data.repository.remote

import android.net.Uri
import com.app.partyzone.core.domain.SellerPost
import com.app.partyzone.core.domain.repository.PostRepository
import com.app.partyzone.core.domain.util.AuthorizationException
import com.app.partyzone.core.domain.util.UnknownErrorException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val firebaseAuth: FirebaseAuth,
) : PostRepository {

    override suspend fun createPost(post: SellerPost, images: List<Uri>): Boolean {
        firebaseAuth.currentUser ?: throw AuthorizationException.UnAuthorizedException

        val postId = firestore.collection("Post").document().id
        // Upload images to Firebase Storage
        val imageUrls = mutableListOf<String>()
        val storageRef = storage.reference
        images.forEachIndexed { index, uri ->
            val photoRef = storageRef.child("posts/$postId/image_$index.jpg")
            try {
                photoRef.putFile(uri).await()
                val url = photoRef.downloadUrl.await().toString()
                imageUrls.add(url)
            } catch (e: Exception) {
                throw UnknownErrorException("Failed to upload image: ${e.message}")
            }
        }

        // Update post with image URLs
        val updatedPost = post.copy(images = imageUrls)

        // Save post to Firestore
        return try {
            firestore.collection("Post").document(postId).set(post.copy(id = postId)).await()
            true
        } catch (e: Exception) {
            throw UnknownErrorException("Failed to create post: ${e.message}")
        }
    }

    override fun getSellerPosts(): Flow<List<SellerPost>> = callbackFlow {
        val collectionRef = firestore.collection("Post")
            .whereEqualTo("sellerId", firebaseAuth.currentUser?.uid)
            .orderBy("timestamp", Query.Direction.DESCENDING)

        val listener = collectionRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val posts = snapshot.toObjects(SellerPost::class.java)
                trySend(posts).isSuccess
            }
        }

        awaitClose {
            listener.remove()
        }
    }
}