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
import androidx.recyclerview.widget.LinearLayoutManager
import com.wngud.sport_together.App
import com.wngud.sport_together.databinding.FragmentChattingBinding
import com.wngud.sport_together.domain.model.Chatting
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.internal.notifyAll

@AndroidEntryPoint
class ChattingFragment : Fragment() {

    private lateinit var binding: FragmentChattingBinding
    private val chattingViewModel: ChattingViewModel by viewModels()
    private val chattingAdapter by lazy {
        ChattingAdapter(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChattingBinding.inflate(layoutInflater, container, false)

        val roomId = arguments?.getString("roomId")
        val counterUserNickname = arguments?.getString("counterUserNickname")
        val counterUserProfileImage = arguments?.getString("counterUserProfileImage")
        val counterUid = arguments?.getString("counterUid")
        roomId?.let {
            chattingViewModel.getChatting(it)
        }
        chattingAdapter.setCounterUserInfo(counterUserNickname, counterUserProfileImage)

        binding.run {
            toolbarChatting.title = counterUserNickname
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                chattingViewModel.chattingList.collectLatest { chattings ->
                    Log.i("chatting_frag", "chattings 사이즈 ${chattings.size}")
                    chattingAdapter.submitList(chattings)
                    if(chattings.isNotEmpty()) binding.rvChatting.smoothScrollToPosition(chattings.lastIndex)
                }
            }
        }

        binding.run {
            btnChatting.setOnClickListener {
                val chatting = Chatting(
                    senderId = App.auth.currentUser!!.uid,
                    content = etChatting.text.toString()
                )
                chattingViewModel.sendChatting(chatting, listOf(App.auth.currentUser!!.uid, counterUid!!))
                etChatting.setText("")
            }
        }

        initChattingRecyclerView()

        return binding.root
    }

    private fun initChattingRecyclerView() {
        binding.rvChatting.run {
            adapter = chattingAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}