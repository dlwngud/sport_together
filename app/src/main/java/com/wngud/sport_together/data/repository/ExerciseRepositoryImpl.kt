package com.wngud.sport_together.data.repository

import android.util.Log
import com.wngud.sport_together.data.db.remote.ExerciseDataSource
import com.wngud.sport_together.domain.model.Exercise
import com.wngud.sport_together.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ExerciseRepositoryImpl @Inject constructor(private val exerciseDataSource: ExerciseDataSource) :
    ExerciseRepository {
    override suspend fun saveExercise(exercise: Exercise) {
        exerciseDataSource.saveExercise(exercise)
    }

    override suspend fun getAllExercises(): Flow<List<Exercise>> {
        return exerciseDataSource.getAllExercises()
    }
}