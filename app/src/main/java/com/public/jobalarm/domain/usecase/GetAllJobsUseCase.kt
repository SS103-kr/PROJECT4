package com.jobalarm.domain.usecase

import com.jobalarm.domain.model.JobPosting
import com.jobalarm.domain.model.JobSort
import com.jobalarm.domain.repository.JobRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllJobsUseCase @Inject constructor(
    private val repo: JobRepository
) {
    operator fun invoke(sort: JobSort): Flow<List<JobPosting>> = repo.observeAll(sort)
}
