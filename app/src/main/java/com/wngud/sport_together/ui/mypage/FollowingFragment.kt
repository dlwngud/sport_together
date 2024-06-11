package com.wngud.sport_together.ui.mypage

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.wngud.sport_together.databinding.FragmentFollowingBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FollowingFragment : Fragment() {

    private lateinit var binding: FragmentFollowingBinding
    private val mypageViewModel: MypageViewModel by viewModels()
    @Inject
    lateinit var followingAdapter: FollowingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowingBinding.inflate(layoutInflater, container,  false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mypageViewModel.user.collectLatest {
                    followingAdapter.submitList(it.following)
                }
            }
        }

        setRecyclerView()
    }

    private fun setRecyclerView() {
        binding.rvFollowing.run {
            adapter = followingAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))

            followingAdapter.setItemClickListener(object :
                FollowingAdapter.onItemClickListener {
                override fun onItemClick(position: Int) {
                    showDialog(mypageViewModel.user.value.follower[position])
                }
            })
        }
    }

    private fun showDialog(uid: String) {
        lifecycleScope.launch(Dispatchers.Main) {
            val user = mypageViewModel.getUserInfo(uid)
            val isFollowing = mypageViewModel.getFollowingStatus(uid)
            val builder = AlertDialog.Builder(requireContext())
            if (isFollowing) {
                builder.setTitle("${user.nickname}님을 팔로잉을 해제하겠습니까?")
                    .setPositiveButton("네") { dialog, which ->
                        mypageViewModel.unfollowing(uid)
                    }.setNegativeButton("아니오") { dialog, which ->

                    }.create().show()
            } else {
                builder.setTitle("${user.nickname}님을 팔로잉 하겠습니까?")
                    .setPositiveButton("네") { dialog, which ->
                        mypageViewModel.following(uid)
                    }.setNegativeButton("아니오") { dialog, which ->

                    }.create().show()
            }
        }
    }
}