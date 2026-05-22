package com.jobalarm.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jobalarm.data.local.entity.BookmarkEntity
import com.jobalarm.domain.model.Category
import com.jobalarm.domain.model.JobPosting
import com.jobalarm.domain.model.JobSort
import com.jobalarm.domain.model.UiState
import com.jobalarm.domain.usecase.GetAllJobsUseCase
import com.jobalarm.domain.usecase.GetBookmarksUseCase
import com.jobalarm.domain.usecase.GetCategoryCountUseCase
import com.jobalarm.domain.usecase.RefreshJobsUseCase
import com.jobalarm.domain.usecase.ToggleBookmarkUseCase
import com.jobalarm.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAllJobs: GetAllJobsUseCase,
    private val getBookmarks: GetBookmarksUseCase,
    private val toggleBookmark: ToggleBookmarkUseCase,
    private val refreshJobs: RefreshJobsUseCase,
    private val getCategoryCount: GetCategoryCountUseCase
) : ViewModel() {

    private val _sort = MutableStateFlow(JobSort.LATEST)
    val sort: StateFlow<JobSort> = _sort.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _loadingInitial = MutableStateFlow(true)
    val loadingInitial: StateFlow<Boolean> = _loadingInitial.asStateFlow()

    private val _errorEvent = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val errorEvent: SharedFlow<String> = _errorEvent.asSharedFlow()

    private val _uiState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val uiState: StateFlow<UiState<Unit>> = _uiState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val jobs: StateFlow<List<JobPosting>> = _sort
        .flatMapLatest { getAllJobs(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val bookmarks: StateFlow<List<BookmarkEntity>> = getBookmarks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val categories: StateFlow<List<Category>> =
        combine(
            getCategoryCount(Constants.CATEGORY_A),
            getCategoryCount(Constants.CATEGORY_B),
            getCategoryCount(Constants.CATEGORY_C),
            getCategoryCount(Constants.CATEGORY_D)
        ) { a, b, c, d ->
            listOf(
                Category(Constants.CATEGORY_A, Constants.categoryName(Constants.CATEGORY_A), a),
                Category(Constants.CATEGORY_B, Constants.categoryName(Constants.CATEGORY_B), b),
                Category(Constants.CATEGORY_C, Constants.categoryName(Constants.CATEGORY_C), c),
                Category(Constants.CATEGORY_D, Constants.categoryName(Constants.CATEGORY_D), d)
            ).filter { it.count > 0 }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        refresh()
    }

    fun setSort(s: JobSort) { _sort.update { s } }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            val result = refreshJobs(1)
            result.onFailure { throwable ->
                val msg = throwable.message ?: "알 수 없는 오류가 발생했습니다"
                if (_loadingInitial.value) {
                    _uiState.value = UiState.Error(msg, throwable)
                } else {
                    _errorEvent.tryEmit(msg)
                }
            }
            result.onSuccess { _uiState.value = UiState.Success(Unit) }
            _isRefreshing.value = false
            _loadingInitial.value = false
        }
    }

    fun loadMore(nextPage: Int) {
        viewModelScope.launch {
            refreshJobs(nextPage).onFailure { _errorEvent.tryEmit(it.message ?: "페이지 로드 실패") }
        }
    }

    fun onBookmarkToggle(posting: JobPosting) {
        viewModelScope.launch { toggleBookmark(posting) }
    }
}
