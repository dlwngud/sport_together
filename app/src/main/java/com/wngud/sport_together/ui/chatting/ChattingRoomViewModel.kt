package com.wngud.sport_together.ui.chatting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wngud.sport_together.App
import com.wngud.sport_together.domain.model.ChattingRoom
import com.wngud.sport_together.domain.repository.ChattingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChattingRoomViewModel @Inject constructor(
    private val chattingRepository: ChattingRepository
) : ViewModel() {

    private val _roomList = MutableStateFlow<List<ChattingRoom>>(emptyList())
    val roomList = _roomList.asStateFlow()

    init {
        getAllMyChattingRooms()
    }

    private fun getAllMyChattingRooms() = viewModelScope.launch {
        val chattingRooms = chattingRepository.getAllChattingRoom()
        _roomList.update { chattingRooms.filter { it.users.contains(App.auth.currentUser!!.uid) } }
    }
}