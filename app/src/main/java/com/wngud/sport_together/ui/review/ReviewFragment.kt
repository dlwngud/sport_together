package com.wngud.sport_together.ui.review

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.google.protobuf.Internal
import com.wngud.sport_together.App
import com.wngud.sport_together.R
import com.wngud.sport_together.databinding.FragmentReviewBinding
import com.wngud.sport_together.ui.chatting.ChattingAdapter
import com.wngud.sport_together.ui.mypage.MypageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.internal.notify

@AndroidEntryPoint
class ReviewFragment : Fragment() {

    private lateinit var binding: FragmentReviewBinding
    private val reviewViewModel: ReviewViewModel by viewModels()
    private val reviewAdapter by lazy {
        ReviewAdapter(requireContext())
    }

    @SuppressLint("NotifyDataSetChanged")
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
        }
    }

    private fun backPress() {
        findNavController().popBackStack()
    }
}