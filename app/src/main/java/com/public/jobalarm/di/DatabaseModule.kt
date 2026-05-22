package com.jobalarm.di

import android.content.Context
import androidx.room.Room
import com.jobalarm.data.local.AppDatabase
import com.jobalarm.data.local.dao.AlertOrgDao
import com.jobalarm.data.local.dao.BookmarkDao
import com.jobalarm.data.local.dao.JobPostingDao
import com.jobalarm.data.local.dao.NotifiedPostDao
import com.jobalarm.data.local.dao.RecentSearchDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "jobalarm.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides fun provideJobDao(db: AppDatabase): JobPostingDao = db.jobPostingDao()
    @Provides fun provideAlertDao(db: AppDatabase): AlertOrgDao = db.alertOrgDao()
    @Provides fun provideNotifiedDao(db: AppDatabase): NotifiedPostDao = db.notifiedPostDao()
    @Provides fun provideBookmarkDao(db: AppDatabase): BookmarkDao = db.bookmarkDao()
    @Provides fun provideRecentSearchDao(db: AppDatabase): RecentSearchDao = db.recentSearchDao()
}
