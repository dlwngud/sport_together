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
import com.wngud.sport_together.R
import com.wngud.sport_together.databinding.FragmentMypageBinding
import com.wngud.sport_together.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MypageFragment : Fragment() {

    private lateinit var binding: FragmentMypageBinding
    private val mypageViewModel: MypageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageBinding.inflate(layoutInflater, container, false)

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
        }

        observeUiEvent()
        return binding.root
    }

    private fun moveToLogin() {
        startActivity(Intent(requireContext(), LoginActivity::class.java))
        requireActivity().finish()
    }

    private fun observeUiEvent() = lifecycleScope.launch {
        mypageViewModel.eventFlow.collect { event -> handleEvent(event) }
    }

    private fun handleEvent(event: MypageEvent) = when(event) {
        is MypageEvent.MoveToLogin -> moveToLogin()
    }
}