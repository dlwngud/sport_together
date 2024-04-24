package com.wngud.sport_together.ui.mypage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.wngud.sport_together.App
import com.wngud.sport_together.R
import com.wngud.sport_together.databinding.FragmentMypageBinding
import com.wngud.sport_together.domain.model.User
import com.wngud.sport_together.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MypageFragment : Fragment() {

    private lateinit var binding: FragmentMypageBinding
    private val mypageViewModel: MypageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageBinding.inflate(inflater, container, false)

        binding.run {
            tvFollowerMypage.setOnClickListener {
                it.findNavController().navigate(R.id.nav_follow_manager)
            }

            tvFollowingMypage.setOnClickListener {
                it.findNavController().navigate(R.id.nav_follow_manager)
            }

            ivProfileMypage.setOnClickListener {
                it.findNavController().navigate(R.id.nav_profile_edit)
            }
            btnMypage.setOnClickListener {
                mypageViewModel.logout()
            }
            toolbarMypage.setNavigationOnClickListener {
                backPress()
            }
            lifecycleScope.launch {
                mypageViewModel.user.collect { user ->
                    if (user.profileImage.isNotEmpty()) {
                        showProfile(user)
                    }
                    tvNicknameMypage.text = user.nickname
                    tvIntroduceMypage.text = user.introduce
                }
            }
        }
        observeUiEvent()
        return binding.root
    }

    private fun showProfile(user: User) {
        mypageViewModel.getUserProfile(user.profileImage){
            if (it.isSuccessful) {
                Glide.with(this@MypageFragment).load(it.result)
                    .placeholder(R.drawable.app_icon).error(R.drawable.app_icon)
                    .into(binding.ivProfileMypage)
            }
        }
    }

    private fun moveToLogin() {
        startActivity(Intent(requireContext(), LoginActivity::class.java))
        requireActivity().finish()
    }

    private fun observeUiEvent() = lifecycleScope.launch {
        mypageViewModel.eventFlow.collect { event -> handleEvent(event) }
    }

    private fun handleEvent(event: MypageEvent) = when (event) {
        is MypageEvent.MoveToLogin -> moveToLogin()
    }

    private fun backPress() {
        findNavController().popBackStack()
    }
}