package com.wngud.sport_together.data.repository

import android.net.Uri
import com.wngud.sport_together.App
import com.wngud.sport_together.domain.model.Review
import com.wngud.sport_together.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor() : ReviewRepository {
    override suspend fun saveReview(review: Review, uris: List<Uri>) {
        val ref = App.db.collection("reviews")
        val reviewId = ref.document().id
        val reviewImages = mutableListOf<String>()
        for (i in uris.indices) {
            reviewImages.add("images/reviews/${reviewId}/${i}.jpg")
        }
        val updateReview = review.copy(reviewId = reviewId, images = reviewImages)

        ref.document(reviewId)
            .set(updateReview)
            .addOnSuccessListener {
                for (uri in uris.withIndex()) {
                    App.storage.reference.child("images/reviews/${reviewId}/${uri.index}.jpg")
                        .putFile(uri.value)
                }
            }
    }

    override suspend fun getAllReviews(): Flow<List<Review>> {
        return flow {
            val reviews = App.db.collection("reviews").get().await().toObjects(Review::class.java).sortedByDescending { it.createTime }
            emit(reviews)
        }
    }
}