package com.jobalarm.domain.repository

import com.jobalarm.domain.model.JobPosting
import com.jobalarm.domain.model.JobSort
import kotlinx.coroutines.flow.Flow

interface JobRepository {
    fun observeAll(sort: JobSort): Flow<List<JobPosting>>
    fun observeByCategory(code: String): Flow<List<JobPosting>>
    fun observeBySn(sn: String): Flow<JobPosting?>
    fun categoryCount(code: String): Flow<Int>
    suspend fun refresh(pageNo: Int = 1, instNm: String? = null): Result<Int>
    suspend fun fetchByOrg(instNm: String): Result<List<JobPosting>>
}
