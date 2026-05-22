package com.jobalarm.domain.usecase

import com.jobalarm.data.local.entity.BookmarkEntity
import com.jobalarm.domain.repository.BookmarkRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBookmarksUseCase @Inject constructor(
    private val repo: BookmarkRepository
) {
    operator fun invoke(): Flow<List<BookmarkEntity>> = repo.observeAll()
}
