package com.jobalarm.presentation.alert

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jobalarm.domain.model.AlertOrg
import com.jobalarm.domain.usecase.AddAlertOrgUseCase
import com.jobalarm.domain.usecase.GetAlertOrgsUseCase
import com.jobalarm.domain.usecase.RemoveAlertOrgUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlertSettingViewModel @Inject constructor(
    getAlertOrgs: GetAlertOrgsUseCase,
    private val addAlertOrg: AddAlertOrgUseCase,
    private val removeAlertOrg: RemoveAlertOrgUseCase
) : ViewModel() {

    val orgs: StateFlow<List<AlertOrg>> = getAlertOrgs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun add(orgNm: String, orgClsfNm: String = "") {
        val n = orgNm.trim()
        if (n.isEmpty()) return
        viewModelScope.launch { addAlertOrg(n, orgClsfNm) }
    }

    fun remove(orgNm: String) {
        viewModelScope.launch { removeAlertOrg(orgNm) }
    }
}
