package com.jobalarm.domain.usecase

import com.jobalarm.domain.model.AlertOrg
import com.jobalarm.domain.repository.AlertRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAlertOrgsUseCase @Inject constructor(
    private val repo: AlertRepository
) {
    operator fun invoke(): Flow<List<AlertOrg>> = repo.observeAll()
}
