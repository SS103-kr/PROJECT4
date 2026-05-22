package com.jobalarm.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jobalarm.data.local.dao.AlertOrgDao
import com.jobalarm.data.local.dao.BookmarkDao
import com.jobalarm.data.local.dao.JobPostingDao
import com.jobalarm.data.local.dao.NotifiedPostDao
import com.jobalarm.data.local.dao.RecentSearchDao
import com.jobalarm.data.local.entity.AlertOrgEntity
import com.jobalarm.data.local.entity.BookmarkEntity
import com.jobalarm.data.local.entity.JobPostingEntity
import com.jobalarm.data.local.entity.NotifiedPostEntity
import com.jobalarm.data.local.entity.RecentSearchEntity

@Database(
    entities = [
        JobPostingEntity::class,
        AlertOrgEntity::class,
        NotifiedPostEntity::class,
        BookmarkEntity::class,
        RecentSearchEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun jobPostingDao(): JobPostingDao
    abstract fun alertOrgDao(): AlertOrgDao
    abstract fun notifiedPostDao(): NotifiedPostDao
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun recentSearchDao(): RecentSearchDao
}
