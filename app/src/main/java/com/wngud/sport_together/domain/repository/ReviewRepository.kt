package com.wngud.sport_together.domain.repository

import android.net.Uri
import com.wngud.sport_together.domain.model.Review
import kotlinx.coroutines.flow.Flow

interface ReviewRepository {

    suspend fun saveReview(review: Review, uris: List<Uri>)

    suspend fun getAllReviews(): Flow<List<Review>>

    suspend fun saveReviewImages(uris: List<Uri>)
}