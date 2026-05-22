package com.jobalarm.domain.usecase

import com.jobalarm.data.local.entity.RecentSearchEntity
import com.jobalarm.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveRecentSearchesUseCase @Inject constructor(
    private val repo: SearchRepository
) {
    operator fun invoke(): Flow<List<RecentSearchEntity>> = repo.observeRecent()
}

class SaveRecentSearchUseCase @Inject constructor(
    private val repo: SearchRepository
) {
    suspend operator fun invoke(q: String) = repo.saveRecent(q)
}

class DeleteRecentSearchUseCase @Inject constructor(
    private val repo: SearchRepository
) {
    suspend operator fun invoke(q: String) = repo.deleteRecent(q)
}
