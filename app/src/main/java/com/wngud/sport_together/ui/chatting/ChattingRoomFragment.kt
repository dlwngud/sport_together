package com.wngud.sport_together.ui.chatting

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.findFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.navArgument
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.wngud.sport_together.R
import com.wngud.sport_together.databinding.FragmentChattingRoomBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChattingRoomFragment : Fragment() {

    private lateinit var binding: FragmentChattingRoomBinding
    private val chattingRoomViewModel: ChattingRoomViewModel by viewModels()
    private val chattingViewModel: ChattingViewModel by viewModels()
    private val chattingRoomAdapter by lazy {
        ChattingRoomAdapter(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChattingRoomBinding.inflate(layoutInflater, container, false)

        binding.run {
            toolbarChattingRoom.setNavigationOnClickListener {
                backPress()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                chattingRoomViewModel.roomList.collectLatest {
                    chattingRoomAdapter.submitList(it)
                }
            }
        }

        initChattingRecyclerView()

        return binding.root
    }

    private fun initChattingRecyclerView() {
        binding.rvChattingRoom.run {
            adapter = chattingRoomAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))

            chattingRoomAdapter.setItemClickListener(object :
                ChattingRoomAdapter.onItemClickListener {
                override fun onItemClick(position: Int) {
                    val roomId = chattingRoomViewModel.roomList.value[position].roomId
                    val counterUserNickname = chattingRoomViewModel.roomList.value[position].nickname
                    val counterUserProfileImage = chattingRoomViewModel.roomList.value[position].profileImage
                    Log.i("room", roomId)
//                    chattingViewModel.getChatting(roomId)
                    val bundle = Bundle()
                    bundle.putString("roomId", roomId)
                    bundle.putString("counterUserNickname", counterUserNickname)
                    bundle.putString("counterUserProfileImage", counterUserProfileImage)
                    findNavController().navigate(R.id.nav_chatting, bundle)
                }
            })
        }
    }

    private fun backPress() {
        findNavController().popBackStack()
    }
}