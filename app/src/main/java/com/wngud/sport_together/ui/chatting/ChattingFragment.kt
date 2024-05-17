package com.wngud.sport_together.ui.chatting

import android.os.Bundle
import android.util.Log
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
import com.wngud.sport_together.App
import com.wngud.sport_together.databinding.FragmentChattingBinding
import com.wngud.sport_together.domain.model.Chatting
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChattingFragment : Fragment() {

    private lateinit var binding: FragmentChattingBinding
    private val chattingViewModel: ChattingViewModel by viewModels()
    private val chattingAdapter by lazy {
        ChattingAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChattingBinding.inflate(layoutInflater, container, false)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                chattingViewModel.chattingList.collectLatest {
                    chattingAdapter.submitList(it)
                    Log.i("chatting", it.size.toString())
                }
            }
        }

        binding.run {
            btnChatting.setOnClickListener {
                val chatting = Chatting(
                    senderId = App.auth.currentUser!!.uid,
                    content = etChatting.text.toString()
                )
                chattingViewModel.sendChatting(chatting, listOf("user1", "user3"))
            }
        }

        initChattingRecyclerView()

        return binding.root
    }

    private fun initChattingRecyclerView() {
        binding.rvChatting.run {
            adapter = chattingAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
    }
}