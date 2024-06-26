package com.wngud.sport_together.di

import com.wngud.sport_together.data.repository.ChattingRepositoryImpl
import com.wngud.sport_together.data.repository.ExerciseRepositoryImpl
import com.wngud.sport_together.data.repository.ReviewRepositoryImpl
import com.wngud.sport_together.data.repository.UserRepositoryImpl
import com.wngud.sport_together.domain.repository.ChattingRepository
import com.wngud.sport_together.domain.repository.ExerciseRepository
import com.wngud.sport_together.domain.repository.ReviewRepository
import com.wngud.sport_together.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindExerciseRepository(exerciseRepositoryImpl: ExerciseRepositoryImpl): ExerciseRepository

    @Binds
    @Singleton
    abstract fun bindReviewRepository(reviewRepositoryImpl: ReviewRepositoryImpl): ReviewRepository

    @Binds
    @Singleton
    abstract fun bindChattingRepository(chattingRepositoryImpl: ChattingRepositoryImpl): ChattingRepository
}