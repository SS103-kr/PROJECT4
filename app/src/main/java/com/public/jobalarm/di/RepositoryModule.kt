package com.jobalarm.di

import com.jobalarm.data.repository.AlertRepositoryImpl
import com.jobalarm.data.repository.BookmarkRepositoryImpl
import com.jobalarm.data.repository.JobRepositoryImpl
import com.jobalarm.data.repository.SearchRepositoryImpl
import com.jobalarm.domain.repository.AlertRepository
import com.jobalarm.domain.repository.BookmarkRepository
import com.jobalarm.domain.repository.JobRepository
import com.jobalarm.domain.repository.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds @Singleton
    abstract fun bindJobRepository(impl: JobRepositoryImpl): JobRepository

    @Binds @Singleton
    abstract fun bindAlertRepository(impl: AlertRepositoryImpl): AlertRepository

    @Binds @Singleton
    abstract fun bindBookmarkRepository(impl: BookmarkRepositoryImpl): BookmarkRepository

    @Binds @Singleton
    abstract fun bindSearchRepository(impl: SearchRepositoryImpl): SearchRepository
}
