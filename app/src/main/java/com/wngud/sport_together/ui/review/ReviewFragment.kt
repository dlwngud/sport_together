package com.wngud.sport_together.ui.review

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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReviewFragment : Fragment() {

    private lateinit var binding: FragmentReviewBinding
    private val reviewViewModel: ReviewViewModel by viewModels()
    private val reviewAdapter by lazy {
        ReviewAdapter(requireContext())
    }

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