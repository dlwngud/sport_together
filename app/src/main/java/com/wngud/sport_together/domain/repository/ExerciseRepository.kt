package com.wngud.sport_together.domain.repository

import com.wngud.sport_together.domain.model.Exercise

interface ExerciseRepository {

    suspend fun saveExercise(exercise: Exercise)
}