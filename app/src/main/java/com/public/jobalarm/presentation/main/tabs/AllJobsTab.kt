package com.jobalarm.presentation.main.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jobalarm.domain.model.JobPosting
import com.jobalarm.domain.model.JobSort
import com.jobalarm.presentation.component.EmptyStateView
import com.jobalarm.presentation.component.JobCard
import com.jobalarm.presentation.component.SkeletonJobCard
import com.jobalarm.presentation.component.SortDropdown
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AllJobsTab(
    jobs: List<JobPosting>,
    isRefreshing: Boolean,
    loadingInitial: Boolean,
    errorMessage: String?,
    sort: JobSort,
    onSortChange: (JobSort) -> Unit,
    onRefresh: () -> Unit,
    onLoadMore: (Int) -> Unit,
    isBookmarked: (String) -> Boolean,
    onToggleBookmark: (JobPosting) -> Unit,
    onOpenDetail: (String) -> Unit
) {
    val listState = rememberLazyListState()
    var page by remember { mutableStateOf(1) }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0 }
            .distinctUntilChanged()
            .collect { lastIndex ->
                val total = listState.layoutInfo.totalItemsCount
                if (total > 0 && lastIndex >= total - 3) {
                    page += 1
                    onLoadMore(page)
                }
            }
    }

    val pullState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = onRefresh)

    Box(Modifier.fillMaxSize().pullRefresh(pullState)) {
        Column(Modifier.fillMaxSize()) {
            Box(Modifier.fillMaxSize()) {
                if (loadingInitial && jobs.isEmpty() && errorMessage == null) {
                    LazyColumn {
                        items(6) { SkeletonJobCard() }
                    }
                } else if (errorMessage != null && jobs.isEmpty()) {
                    ErrorStateView(message = errorMessage, onRetry = onRefresh)
                } else if (jobs.isEmpty()) {
                    EmptyStateView(message = "현재 등록된 공고가 없습니다")
                } else {
                    LazyColumn(state = listState) {
                        item {
                            androidx.compose.foundation.layout.Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                SortDropdown(selected = sort, onSelect = onSortChange)
                            }
                        }
                        items(items = jobs, key = { it.recrutPbancSn }) { job ->
                            JobCard(
                                posting = job,
                                isBookmarked = isBookmarked(job.recrutPbancSn),
                                onClick = { onOpenDetail(job.recrutPbancSn) },
                                onBookmarkClick = { onToggleBookmark(job) }
                            )
                        }
                        item { Spacer(Modifier.height(80.dp)) }
                    }
                }
            }
        }
        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
private fun ErrorStateView(message: String, onRetry: () -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CloudOff,
                contentDescription = null,
                modifier = Modifier.size(56.dp),
                tint = MaterialTheme.colorScheme.error
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = "데이터를 불러오지 못했습니다",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
                textAlign = TextAlign.Center,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(20.dp))
            Button(onClick = onRetry) { Text("다시 시도") }
        }
    }
}
