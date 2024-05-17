package com.wngud.sport_together.ui.review

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.wngud.sport_together.App
import com.wngud.sport_together.databinding.FragmentAddReviewBinding
import com.wngud.sport_together.domain.model.Review
import com.wngud.sport_together.ui.mypage.MypageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddReviewFragment : Fragment() {

    private lateinit var binding: FragmentAddReviewBinding
    private val reviewViewModel: ReviewViewModel by viewModels()
    private val mypageViewModel: MypageViewModel by viewModels()

    private val pickMultipleMedia =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(5)) { uris ->
            if (uris.isNotEmpty()) {
                initRecyclerView(uris)
                selectedPhotosUri.addAll(uris)
            }
        }
    private val selectedPhotosUri = mutableListOf<Uri>()
    private var profileImage = ""
    private var nickname = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddReviewBinding.inflate(layoutInflater, container, false)

        binding.run {
            toolbarAddReview.setNavigationOnClickListener {
                backPress()
            }

            btnSaveAddReview.setOnClickListener {
                saveReview()
            }

            btnGetPictureAddReview.setOnClickListener {
                pickPhotos()
            }

            lifecycleScope.launch {
                mypageViewModel.user.collect { user ->
                    profileImage = user.profileImage
                    nickname = user.nickname
                }
            }
        }
        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    private fun saveReview() {
        val uid = App.auth.currentUser!!.uid
        val content = binding.etAddReview.text.toString()
        val now = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy.MM.dd")
        val date = sdf.format(now.time)
        val createTime = System.currentTimeMillis()
        val newReview = Review(
            uid = uid,
            content = content,
            profileImage = profileImage,
            time = date,
            createTime = createTime,
            nickname = nickname
        )
        reviewViewModel.saveReview(newReview, selectedPhotosUri)
        backPress()
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