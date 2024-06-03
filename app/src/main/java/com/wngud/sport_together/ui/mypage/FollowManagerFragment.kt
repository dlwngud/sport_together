package com.wngud.sport_together.ui.mypage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewPager()
    }

    private fun initViewPager() {
        val viewPagerAdapter = FollowViewPagerAdapter(requireActivity())
        viewPagerAdapter.addFragment(FollowerFragment())
        viewPagerAdapter.addFragment(FollowingFragment())

        binding.vpFollowManager.run {
            adapter = viewPagerAdapter
        }

        TabLayoutMediator(
            binding.tabLayoutFollowManager,
            binding.vpFollowManager
        ) { tab, position ->
            when (position) {
                0 -> tab.text = "팔로워"
                else -> tab.text = "팔로잉"
            }
        }.attach()
    }

    private inner class FollowViewPagerAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {
        var fragments: ArrayList<Fragment> = ArrayList()

        fun addFragment(fragment: Fragment) {
            fragments.add(fragment)
            notifyItemInserted(fragments.size - 1)
        }

        override fun getItemCount(): Int = fragments.size
        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }
    }
}