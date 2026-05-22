package com.jobalarm.domain.usecase

import com.jobalarm.domain.repository.AlertRepository
import javax.inject.Inject

class AddAlertOrgUseCase @Inject constructor(
    private val repo: AlertRepository
) {
    suspend operator fun invoke(orgNm: String, orgClsfNm: String) = repo.add(orgNm, orgClsfNm)
}
