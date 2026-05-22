package com.jobalarm.domain.repository

import com.jobalarm.data.local.entity.RecentSearchEntity
import com.jobalarm.domain.model.JobPosting
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun search(q: String): Flow<List<JobPosting>>
    fun observeRecent(): Flow<List<RecentSearchEntity>>
    suspend fun saveRecent(q: String)
    suspend fun deleteRecent(q: String)
    suspend fun clearRecent()
}
