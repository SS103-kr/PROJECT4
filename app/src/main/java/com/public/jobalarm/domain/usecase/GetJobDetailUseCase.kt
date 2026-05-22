package com.jobalarm.domain.usecase

import com.jobalarm.domain.model.JobPosting
import com.jobalarm.domain.repository.JobRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetJobDetailUseCase @Inject constructor(
    private val repo: JobRepository
) {
    operator fun invoke(sn: String): Flow<JobPosting?> = repo.observeBySn(sn)
}
