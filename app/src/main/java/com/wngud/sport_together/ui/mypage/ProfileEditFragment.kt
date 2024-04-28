package com.wngud.sport_together.ui.mypage

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
            if (uri != null) {
                imageUri = uri
                binding.ivProfileProfileEdit.setImageURI(uri)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileEditBinding.inflate(inflater, container, false)

        binding.run {
            ivProfileProfileEdit.setOnClickListener {
                setProfileImage()
            }
            btnProfileEdit.setOnClickListener {
                editProfile()
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
        if(isAdded){
            mypageViewModel.getUserProfile(user.profileImage) {
                if (it.isSuccessful) {
                    Glide.with(this).load(it.result)
                        .placeholder(R.drawable.app_icon).error(R.drawable.app_icon)
                        .into(binding.ivProfileProfileEdit)
                }
            }
        }
    }

    private fun editProfile() {
        lifecycleScope.launch {
            val fileName = App.auth.currentUser!!.uid
            if (imageUri != null) {
                mypageViewModel.editUserProfile(fileName, imageUri!!)
            }
            val editNickname = binding.etNicknameProfileEdit.text.toString()
            val editIntroduce = binding.etIntroduceProfileEdit.text.toString()
            val editUser = User(
                email = App.auth.currentUser!!.email!!,
                uid = App.auth.currentUser!!.uid,
                nickname = editNickname,
                introduce = editIntroduce,
                profileImage = "images/users/${fileName}.jpg"
            )
            Log.i("tag","filename "+fileName)
            mypageViewModel.editUserInfo(editUser)
            backPress()
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