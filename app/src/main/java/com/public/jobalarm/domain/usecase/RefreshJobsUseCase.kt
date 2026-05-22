package com.jobalarm.domain.usecase

import com.jobalarm.domain.repository.JobRepository
import javax.inject.Inject

class RefreshJobsUseCase @Inject constructor(
    private val repo: JobRepository
) {
    suspend operator fun invoke(pageNo: Int = 1): Result<Int> = repo.refresh(pageNo)
}
