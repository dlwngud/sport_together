package com.wngud.sport_together.ui.chatting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.wngud.sport_together.R
import com.wngud.sport_together.databinding.FragmentChattingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChattingFragment : Fragment() {

    private lateinit var binding: FragmentChattingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChattingBinding.inflate(layoutInflater, container, false)

        binding.run {
            toolbarChatting.setNavigationOnClickListener {
                backPress()
            }
        }

        initChattingRecyclerView()

        return binding.root
    }

    private fun initChattingRecyclerView() {
        binding.rvChatting.run {
            val chattingAdapter = ChattingAdapter()
            adapter = chattingAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
    }

    private fun backPress() {
        findNavController().popBackStack()
    }
}