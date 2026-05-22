package com.jobalarm.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jobalarm.data.local.entity.RecentSearchEntity
import com.jobalarm.domain.model.JobPosting
import com.jobalarm.domain.usecase.DeleteRecentSearchUseCase
import com.jobalarm.domain.usecase.ObserveRecentSearchesUseCase
import com.jobalarm.domain.usecase.SaveRecentSearchUseCase
import com.jobalarm.domain.usecase.SearchJobsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchJobs: SearchJobsUseCase,
    observeRecent: ObserveRecentSearchesUseCase,
    private val saveRecent: SaveRecentSearchUseCase,
    private val deleteRecent: DeleteRecentSearchUseCase
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    val recent: StateFlow<List<RecentSearchEntity>> = observeRecent()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val results: StateFlow<List<JobPosting>> = _query
        .debounce(300)
        .flatMapLatest { q ->
            if (q.isBlank()) flowOf<List<JobPosting>>(emptyList()) else searchJobs(q)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun onQueryChange(q: String) { _query.value = q }

    fun commitSearch() {
        val q = _query.value.trim()
        if (q.isNotEmpty()) viewModelScope.launch { saveRecent(q) }
    }

    fun deleteRecentQuery(q: String) {
        viewModelScope.launch { deleteRecent(q) }
    }
}
