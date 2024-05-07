package com.wngud.sport_together.data.db.remote

import android.util.Log
import com.google.firebase.firestore.toObject
import com.wngud.sport_together.App
import com.wngud.sport_together.domain.model.Exercise
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext

class ExerciseDataSource {

    suspend fun saveExercise(exercise: Exercise) {
        withContext(Dispatchers.IO) {
            val exerciseId = App.db.collection("exercises").document().id
            exercise.exerciseId = exerciseId

            App.db.collection("exercises").document(exerciseId).set(exercise)
                .addOnSuccessListener {
                    Log.d("TAG", "운동 정보 저장 성공: $exerciseId")
                }
                .addOnFailureListener { e ->
                    Log.w("TAG", "운동 정보 저장 실패: ${e.message}")
                }
        }
    }

    suspend fun getAllExercises(): Flow<List<Exercise>> = callbackFlow {
        val exerciseList = mutableListOf<Exercise>()

        App.db.collection("exercises")
            .get()
            .addOnSuccessListener { result ->
                Log.d("exercise_ds", "성공 ${result.size()}")
                for (document in result) {
                    val exercise = document.toObject<Exercise>()
                    exerciseList.add(exercise)
                }
                trySend(exerciseList)
                Log.d("exercise_ds", exerciseList.size.toString())
            }.addOnFailureListener {
                Log.d("exercise_ds", "실패")
            }
        awaitClose {  }
    }
}