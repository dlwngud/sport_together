package com.wngud.sport_together.ui.review

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wngud.sport_together.domain.model.Review
import com.wngud.sport_together.domain.repository.ReviewRepository
import com.wngud.sport_together.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class reviewUiState(
    val reviews: List<Review> = emptyList()
)

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _reviews = MutableStateFlow(reviewUiState())
    val review = _reviews.asStateFlow()

    init {
        getAllReviews()
    }

    fun saveReview(review: Review, uris: List<Uri>) = viewModelScope.launch {
        reviewRepository.saveReview(review, uris)
    }

    fun getAllReviews() = viewModelScope.launch {
        reviewRepository.getAllReviews().collectLatest { reviewList ->
            Log.i("review_vm", reviewList.size.toString())
            _reviews.update { it.copy(reviewList) }
        }
    }

    suspend fun getFollowingStatus(uid: String) = userRepository.getFollowingStatus(uid)
}