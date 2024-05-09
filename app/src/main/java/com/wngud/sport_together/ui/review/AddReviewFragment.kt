package com.wngud.sport_together.ui.review

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.wngud.sport_together.databinding.FragmentAddReviewBinding
import com.wngud.sport_together.domain.model.Review
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddReviewFragment : Fragment() {

    private lateinit var binding: FragmentAddReviewBinding
    private val reviewViewModel: ReviewViewModel by viewModels()

    private val pickMultipleMedia =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(5)) { uris ->
            if (uris.isNotEmpty()) {
                initRecyclerView(uris)
            } else {

            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddReviewBinding.inflate(layoutInflater, container, false)

        binding.run {
            toolbarAddReview.setNavigationOnClickListener {
                backPress()
            }

            btnGetPictureAddReview.setOnClickListener {
                pickPhotos()
            }
        }
        return binding.root
    }

    private fun initRecyclerView(uris: List<@JvmSuppressWildcards Uri>) {
        binding.cardViewAddReview1.visibility = View.GONE
        binding.rvAddReview.visibility = View.VISIBLE
        binding.rvAddReview.run {
            val rvAdapter = ImageAdapter2(uris, requireContext())
            adapter = rvAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun pickPhotos() {
        pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
    }

    private fun backPress() {
        findNavController().popBackStack()
    }
}