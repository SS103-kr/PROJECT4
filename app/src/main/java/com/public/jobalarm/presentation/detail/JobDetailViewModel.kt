package com.jobalarm.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jobalarm.domain.model.JobPosting
import com.jobalarm.domain.repository.BookmarkRepository
import com.jobalarm.domain.usecase.GetJobDetailUseCase
import com.jobalarm.domain.usecase.ToggleBookmarkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobDetailViewModel @Inject constructor(
    private val getJobDetail: GetJobDetailUseCase,
    private val bookmarkRepository: BookmarkRepository,
    private val toggleBookmark: ToggleBookmarkUseCase
) : ViewModel() {

    private val _sn = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val job: StateFlow<JobPosting?> = _sn
        .flatMapLatest { getJobDetail(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val isBookmarked: StateFlow<Boolean> = _sn
        .flatMapLatest { bookmarkRepository.observeIsBookmarked(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    fun setSn(sn: String) { _sn.value = sn }

    fun onToggleBookmark() {
        val current = job.value ?: return
        viewModelScope.launch { toggleBookmark(current) }
    }
}
