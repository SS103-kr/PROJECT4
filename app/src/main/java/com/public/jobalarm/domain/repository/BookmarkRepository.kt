package com.jobalarm.domain.repository

import com.jobalarm.data.local.entity.BookmarkEntity
import com.jobalarm.domain.model.JobPosting
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {
    fun observeAll(): Flow<List<BookmarkEntity>>
    fun observeIsBookmarked(sn: String): Flow<Boolean>
    suspend fun toggle(posting: JobPosting)
}
