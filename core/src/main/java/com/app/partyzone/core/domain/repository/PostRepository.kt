package com.app.partyzone.core.domain.repository

import android.net.Uri
import com.app.partyzone.core.domain.SellerPost
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    suspend fun createPost(post: SellerPost, images: List<Uri>): Boolean
    fun getSellerPosts(): Flow<List<SellerPost>>
}