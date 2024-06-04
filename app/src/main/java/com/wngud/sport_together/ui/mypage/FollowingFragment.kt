package com.wngud.sport_together.ui.mypage

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
import com.wngud.sport_together.R
import com.wngud.sport_together.databinding.FragmentFollowingBinding
import com.wngud.sport_together.ui.review.ReviewAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FollowingFragment : Fragment() {

    private lateinit var binding: FragmentFollowingBinding
    private val mypageViewModel: MypageViewModel by viewModels()
    private val followingAdapter by lazy {
        FollowingAdapter(requireContext())
    }

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
//                    followingAdapter.submitList(it.following)
                }
            }
        }

        setRecyclerView()
    }

    private fun setRecyclerView() {
        binding.rvFollowing.run {
//            adapter = followingAdapter
//            layoutManager = LinearLayoutManager(requireContext())
//            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
    }
}