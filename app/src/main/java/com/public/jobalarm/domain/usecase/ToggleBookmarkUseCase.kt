package com.jobalarm.domain.usecase

import com.jobalarm.domain.model.JobPosting
import com.jobalarm.domain.repository.BookmarkRepository
import javax.inject.Inject

class ToggleBookmarkUseCase @Inject constructor(
    private val repo: BookmarkRepository
) {
    suspend operator fun invoke(posting: JobPosting) = repo.toggle(posting)
}
