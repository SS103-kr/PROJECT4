package com.jobalarm.domain.usecase

import com.jobalarm.domain.repository.JobRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoryCountUseCase @Inject constructor(
    private val repo: JobRepository
) {
    operator fun invoke(code: String): Flow<Int> = repo.categoryCount(code)
}
