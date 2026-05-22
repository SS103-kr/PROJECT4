package com.jobalarm.data.repository

import com.jobalarm.data.local.dao.JobPostingDao
import com.jobalarm.data.local.dao.RecentSearchDao
import com.jobalarm.data.local.entity.RecentSearchEntity
import com.jobalarm.data.mapper.toDomain
import com.jobalarm.domain.model.JobPosting
import com.jobalarm.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepositoryImpl @Inject constructor(
    private val jobDao: JobPostingDao,
    private val recentDao: RecentSearchDao
) : SearchRepository {

    override fun search(q: String): Flow<List<JobPosting>> =
        jobDao.search(q).map { list -> list.map { it.toDomain() } }

    override fun observeRecent(): Flow<List<RecentSearchEntity>> = recentDao.observeRecent()

    override suspend fun saveRecent(q: String) {
        if (q.isBlank()) return
        recentDao.insert(RecentSearchEntity(query = q, searchedAt = System.currentTimeMillis()))
        recentDao.trim()
    }

    override suspend fun deleteRecent(q: String) = recentDao.deleteByQuery(q)

    override suspend fun clearRecent() = recentDao.clear()
}
