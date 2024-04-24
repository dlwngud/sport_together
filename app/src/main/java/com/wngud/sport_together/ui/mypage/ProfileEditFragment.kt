package com.wngud.sport_together.ui.mypage

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.wngud.sport_together.App
import com.wngud.sport_together.R
import com.wngud.sport_together.databinding.FragmentProfileEditBinding
import com.wngud.sport_together.domain.model.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileEditFragment : Fragment() {

    private lateinit var binding: FragmentProfileEditBinding
    private val mypageViewModel: MypageViewModel by viewModels()
    private var imageUri: Uri? = null

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                imageUri = uri
                binding.ivProfileProfileEdit.setImageURI(uri)
            } else {
                binding.ivProfileProfileEdit.setImageResource(R.drawable.app_icon)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileEditBinding.inflate(inflater ,container, false)

        binding.run {
            ivProfileProfileEdit.setOnClickListener {
                setProfileImage()
            }
            btnProfileEdit.setOnClickListener {
                backPress()
            }
            toolbarProfileEdit.setNavigationOnClickListener {
                backPress()
            }

            lifecycleScope.launch {
                mypageViewModel.user.collect { user ->
                    if (user.profileImage.isNotEmpty()) {
                        showProfile(user)
                    }
                    etNicknameProfileEdit.setText(user.nickname)
                    etIntroduceProfileEdit.setText(user.introduce)
                }
            }
        }

        return binding.root
    }

    private fun showProfile(user: User) {
        App.storage.reference.child(user.profileImage).downloadUrl.addOnCompleteListener {
            if (it.isSuccessful) {
                Glide.with(this@ProfileEditFragment).load(it.result)
                    .placeholder(R.drawable.app_icon).error(R.drawable.app_icon)
                    .into(binding.ivProfileProfileEdit)
            }
        }
    }

    private fun setProfileImage() {
        val mimeType = "image/*"
        pickMedia.launch(
            PickVisualMediaRequest(
                ActivityResultContracts.PickVisualMedia.SingleMimeType(
                    mimeType
                )
            )
        )
    }

    private fun backPress() {
        findNavController().popBackStack()
    }
}