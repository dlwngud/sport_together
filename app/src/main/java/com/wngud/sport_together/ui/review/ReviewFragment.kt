package com.wngud.sport_together.ui.review

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.wngud.sport_together.R
import com.wngud.sport_together.databinding.FragmentReviewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewFragment : Fragment() {

    private lateinit var binding: FragmentReviewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReviewBinding.inflate(layoutInflater, container, false)

        binding.run {
            fbtnReview.setOnClickListener {
                it.findNavController().navigate(R.id.nav_add_review)
            }
        }

        return binding.root
    }
}