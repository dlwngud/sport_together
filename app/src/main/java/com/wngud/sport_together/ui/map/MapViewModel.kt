package com.wngud.sport_together.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wngud.sport_together.domain.model.Exercise
import com.wngud.sport_together.domain.repository.ExerciseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(private val exerciseRepository: ExerciseRepository) :
    ViewModel() {

    fun saveExercise(exercise: Exercise) = viewModelScope.launch {
        exerciseRepository.saveExercise(exercise)
    }
}