package com.wngud.sport_together.ui.map

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wngud.sport_together.domain.model.Exercise
import com.wngud.sport_together.domain.repository.ExerciseRepository
import com.wngud.sport_together.ui.login.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MapUiState(
    val exercises: List<Exercise> = emptyList()
)

@HiltViewModel
class MapViewModel @Inject constructor(private val exerciseRepository: ExerciseRepository) :
    ViewModel() {

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    fun saveExercise(exercise: Exercise) = viewModelScope.launch {
        exerciseRepository.saveExercise(exercise)
    }

    fun getAllExercises() = viewModelScope.launch {
        exerciseRepository.getAllExercises().collect { exerciseList ->
            _uiState.update { uiState.value.copy(exercises = exerciseList) }
        }
    }
}