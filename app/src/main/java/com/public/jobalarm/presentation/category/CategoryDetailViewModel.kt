package com.jobalarm.presentation.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jobalarm.domain.model.JobPosting
import com.jobalarm.domain.model.JobSort
import com.jobalarm.domain.repository.BookmarkRepository
import com.jobalarm.domain.usecase.GetJobsByCategoryUseCase
import com.jobalarm.domain.usecase.ToggleBookmarkUseCase
import com.jobalarm.util.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CategoryDetailViewModel @Inject constructor(
    private val getJobsByCategory: GetJobsByCategoryUseCase,
    private val toggleBookmark: ToggleBookmarkUseCase,
    bookmarkRepository: BookmarkRepository
) : ViewModel() {

    private val _sort = MutableStateFlow(JobSort.LATEST)
    val sort: StateFlow<JobSort> = _sort.asStateFlow()

    private val _code = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val jobs: StateFlow<List<JobPosting>> =
        combine(_code, _sort) { code, sort -> code to sort }
            .flatMapLatest { (code, sort) ->
                getJobsByCategory(code).map { list -> applySort(list, sort) }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val bookmarkedSns: StateFlow<Set<String>> = bookmarkRepository.observeAll()
        .map { list -> list.map { it.recrutPbancSn }.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptySet())

    fun setCode(code: String) { _code.value = code }
    fun setSort(s: JobSort) { _sort.update { s } }

    fun onToggleBookmark(posting: JobPosting) {
        viewModelScope.launch { toggleBookmark(posting) }
    }

    private fun applySort(list: List<JobPosting>, sort: JobSort): List<JobPosting> = when (sort) {
        JobSort.LATEST -> list.sortedByDescending { it.pbancBgngYmd }
        JobSort.DEADLINE -> list.sortedBy {
            DateUtils.dDay(it.pbancEndYmd, LocalDate.now()) ?: Int.MAX_VALUE
        }
        JobSort.ORG_NAME -> list.sortedBy { it.instNm }
    }
}
