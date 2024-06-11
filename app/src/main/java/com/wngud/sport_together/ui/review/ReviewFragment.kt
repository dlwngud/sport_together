package com.wngud.sport_together.ui.review

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.wngud.sport_together.R
import com.wngud.sport_together.databinding.FragmentReviewBinding
import com.wngud.sport_together.domain.model.Review
import com.wngud.sport_together.ui.mypage.MypageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ReviewFragment : Fragment() {

    private lateinit var binding: FragmentReviewBinding
    private val reviewViewModel: ReviewViewModel by viewModels()
    private val userViewModel: MypageViewModel by viewModels()

    @Inject
    lateinit var reviewAdapter: ReviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReviewBinding.inflate(layoutInflater, container, false)

        binding.run {
            fbtnReview.setOnClickListener {
                it.findNavController().navigate(R.id.nav_add_review)
            }

            toolbarReview.setNavigationOnClickListener {
                backPress()
            }

            swipeLayoutReview.setOnRefreshListener {
                reviewViewModel.getAllReviews()
                swipeLayoutReview.isRefreshing = false
                rvReview.scrollToPosition(0)
            }

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    reviewViewModel.review.collectLatest {
                        reviewAdapter.submitList(it.reviews)
                    }
                }
            }

            initChattingRecyclerView()
        }

        return binding.root
    }

    private fun initChattingRecyclerView() {
        binding.rvReview.run {
            adapter = reviewAdapter
            layoutManager = LinearLayoutManager(requireContext())

            reviewAdapter.setItemClickListener(object :
                ReviewAdapter.onItemClickListener {
                override fun onItemClick(position: Int) {
                    showDialog(reviewViewModel.review.value.reviews[position])
                }
            })
        }
    }

    private fun showDialog(review: Review) {
        lifecycleScope.launch(Dispatchers.Main) {
            val isFollowing = userViewModel.getFollowingStatus(review.uid)
            val builder = AlertDialog.Builder(requireContext())
            if (isFollowing) {
                builder.setTitle("${review.nickname}님을 팔로잉을 해제하겠습니까?")
                    .setPositiveButton("네") { dialog, which ->
                        userViewModel.unfollowing(review.uid)
                    }.setNegativeButton("아니오") { dialog, which ->

                    }.create().show()
            } else {
                builder.setTitle("${review.nickname}님을 팔로잉 하겠습니까?")
                    .setPositiveButton("네") { dialog, which ->
                        userViewModel.following(review.uid)
                    }.setNegativeButton("아니오") { dialog, which ->

                    }.create().show()
            }
        }
    }

    private fun backPress() {
        findNavController().popBackStack()
    }
}