package com.wngud.sport_together.ui.chatting

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wngud.sport_together.domain.model.Chatting
import com.wngud.sport_together.domain.repository.ChattingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChattingViewModel @Inject constructor(
    private val chattingRepository: ChattingRepository
) : ViewModel() {

    private val _chattingList = MutableStateFlow<List<Chatting>>(emptyList())
    val chattingList = _chattingList.asStateFlow()

    fun sendChatting(chatting: Chatting, users: List<String>) = viewModelScope.launch {
        chattingRepository.sendChatting(chatting, users)
    }

    fun getChatting(roomId: String) = viewModelScope.launch {
        chattingRepository.getChatting(roomId).collectLatest { result ->
            result
                .onSuccess { chattings ->
                    _chattingList.update { chattings }.apply {
                        for (i in chattings) {
                            Log.i("chatting", i.content)
                        }
                    }
                }
                .onFailure {
                    Log.i("error", it.message.toString())
                }
        }
    }
}