package com.jobalarm.domain.usecase

import com.jobalarm.domain.model.JobPosting
import com.jobalarm.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchJobsUseCase @Inject constructor(
    private val repo: SearchRepository
) {
    operator fun invoke(q: String): Flow<List<JobPosting>> = repo.search(q)
}
