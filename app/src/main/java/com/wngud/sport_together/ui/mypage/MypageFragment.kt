package com.wngud.sport_together.ui.mypage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.wngud.sport_together.R
import com.wngud.sport_together.databinding.FragmentMypageBinding

class MypageFragment : Fragment() {

    private lateinit var binding: FragmentMypageBinding

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
        }
        return binding.root
    }
}