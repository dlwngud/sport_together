package com.wngud.sport_together.domain.repository

import com.wngud.sport_together.domain.model.Exercise
import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {

    suspend fun saveExercise(exercise: Exercise)
    suspend fun getAllExercises(): Flow<List<Exercise>>
}