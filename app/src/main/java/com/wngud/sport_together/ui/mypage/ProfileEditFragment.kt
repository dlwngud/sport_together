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
import androidx.navigation.fragment.findNavController
import com.wngud.sport_together.App
import com.wngud.sport_together.R
import com.wngud.sport_together.databinding.FragmentProfileEditBinding
import dagger.hilt.android.AndroidEntryPoint

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_edit ,container, false)

        binding.run {
            ivProfileProfileEdit.setOnClickListener {
                setProfileImage()
            }
            btnProfileEdit.setOnClickListener {
                saveProfile()
            }
        }

        return binding.root
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

    private fun saveProfile() {
        App.currentUser.profileImage = imageUri.toString()
        App.currentUser.nickname = binding.etNicknameProfileEdit.text.toString()
        App.currentUser.introduce = binding.etIntroduceProfileEdit.text.toString()
        findNavController().popBackStack()
    }
}