package com.wngud.sport_together.ui.mypage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wngud.sport_together.databinding.FragmentFollowManagerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowManagerFragment : Fragment() {

    private lateinit var binding: FragmentFollowManagerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowManagerBinding.inflate(layoutInflater, container, false)

        // 뷰페이저로 팔로워, 팔로잉 구분 or chipGroup, chip 사용
        return binding.root
    }
}