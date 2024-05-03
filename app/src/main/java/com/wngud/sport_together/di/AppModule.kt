package com.wngud.sport_together.di

import com.wngud.sport_together.data.db.remote.ExerciseDataSource
import com.wngud.sport_together.data.db.remote.UserDataSource
import com.wngud.sport_together.domain.repository.UserRepository
import com.wngud.sport_together.ui.login.LoginViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideLoginViewModel(userRepository: UserRepository) = LoginViewModel(userRepository)

    @Provides
    @Singleton
    fun provideUserDataSource() = UserDataSource()

    @Provides
    @Singleton
    fun provideExerciseDataSource() = ExerciseDataSource()
}