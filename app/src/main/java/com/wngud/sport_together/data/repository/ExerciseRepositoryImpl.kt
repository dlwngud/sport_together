package com.wngud.sport_together.data.repository

import com.wngud.sport_together.data.db.remote.ExerciseDataSource
import com.wngud.sport_together.domain.model.Exercise
import com.wngud.sport_together.domain.repository.ExerciseRepository
import javax.inject.Inject

class ExerciseRepositoryImpl @Inject constructor(private val exerciseDataSource: ExerciseDataSource): ExerciseRepository {
    override suspend fun saveExercise(exercise: Exercise) {
        exerciseDataSource.saveExercise(exercise)
    }
}