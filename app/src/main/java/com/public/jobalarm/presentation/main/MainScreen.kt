package com.jobalarm.presentation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.jobalarm.domain.model.UiState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jobalarm.presentation.main.tabs.AllJobsTab
import com.jobalarm.presentation.main.tabs.BookmarkTab
import com.jobalarm.presentation.main.tabs.LocalPublicTab
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    onOpenSearch: () -> Unit,
    onOpenAlertSetting: () -> Unit,
    onOpenDetail: (String) -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val tabs = listOf("국가공공기관", "지방공공기관", "즐겨찾기")
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val scope = rememberCoroutineScope()

    val jobs by viewModel.jobs.collectAsStateWithLifecycle()
    val bookmarks by viewModel.bookmarks.collectAsStateWithLifecycle()
    val bookmarkedSns = remember(bookmarks) { bookmarks.map { it.recrutPbancSn }.toSet() }
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val loadingInitial by viewModel.loadingInitial.collectAsStateWithLifecycle()
    val sort by viewModel.sort.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        viewModel.errorEvent.collect { message ->
            snackbarHostState.showSnackbar(message = message, actionLabel = "닫기")
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "공공기관채용알리미",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                actions = {
                    IconButton(onClick = onOpenAlertSetting) {
                        Icon(Icons.Default.NotificationsActive, contentDescription = "알림 설정", tint = Color.White)
                    }
                    IconButton(onClick = onOpenSearch) {
                        Icon(Icons.Default.Search, contentDescription = "검색", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { inner ->
        Column(Modifier.fillMaxSize().padding(inner)) {
            TabRow(selectedTabIndex = pagerState.currentPage) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                        text = { Text(title) }
                    )
                }
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                Box(Modifier.fillMaxSize()) {
                    when (page) {
                        0 -> AllJobsTab(
                            jobs = jobs,
                            isRefreshing = isRefreshing,
                            loadingInitial = loadingInitial,
                            errorMessage = (uiState as? UiState.Error)?.message,
                            sort = sort,
                            onSortChange = viewModel::setSort,
                            onRefresh = viewModel::refresh,
                            onLoadMore = viewModel::loadMore,
                            isBookmarked = { bookmarkedSns.contains(it) },
                            onToggleBookmark = viewModel::onBookmarkToggle,
                            onOpenDetail = onOpenDetail
                        )
                        1 -> LocalPublicTab()
                        2 -> BookmarkTab(
                            bookmarks = bookmarks,
                            onOpenDetail = onOpenDetail
                        )
                    }
                }
            }
        }
    }
}
