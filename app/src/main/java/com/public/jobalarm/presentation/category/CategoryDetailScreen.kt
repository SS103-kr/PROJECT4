package com.jobalarm.presentation.category

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jobalarm.presentation.component.EmptyStateView
import com.jobalarm.presentation.component.JobCard
import com.jobalarm.presentation.component.SortDropdown
import com.jobalarm.util.Constants

@Composable
fun CategoryDetailScreen(
    code: String,
    onBack: () -> Unit,
    onOpenDetail: (String) -> Unit,
    viewModel: CategoryDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(code) { viewModel.setCode(code) }
    val jobs by viewModel.jobs.collectAsStateWithLifecycle()
    val sort by viewModel.sort.collectAsStateWithLifecycle()
    val bookmarkedSns by viewModel.bookmarkedSns.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        Constants.categoryName(code),
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { inner ->
        Column(Modifier.fillMaxSize().padding(inner)) {
            androidx.compose.foundation.layout.Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.End
            ) {
                SortDropdown(selected = sort, onSelect = viewModel::setSort)
            }
            if (jobs.isEmpty()) {
                EmptyStateView(message = "이 카테고리에 등록된 공고가 없습니다")
            } else {
                LazyColumn(Modifier.fillMaxSize()) {
                    items(items = jobs, key = { it.recrutPbancSn }) { job ->
                        JobCard(
                            posting = job,
                            isBookmarked = bookmarkedSns.contains(job.recrutPbancSn),
                            onClick = { onOpenDetail(job.recrutPbancSn) },
                            onBookmarkClick = { viewModel.onToggleBookmark(job) }
                        )
                    }
                }
            }
        }
    }
}
