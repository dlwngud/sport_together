package com.wngud.sport_together.ui.review

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.wngud.sport_together.databinding.FragmentAddReviewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddReviewFragment : Fragment() {

    private lateinit var binding: FragmentAddReviewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddReviewBinding.inflate(layoutInflater, container, false)

        binding.run {
            toolbarAddReview.setNavigationOnClickListener {
                backPress()
            }
        }
        return binding.root
    }

    private fun backPress() {
        findNavController().popBackStack()
    }
}