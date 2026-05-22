package com.jobalarm.presentation.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jobalarm.presentation.component.BadgeChip
import com.jobalarm.presentation.theme.categoryColor
import com.jobalarm.presentation.theme.hireColor
import com.jobalarm.util.DateUtils

@Composable
fun JobDetailScreen(
    sn: String,
    onBack: () -> Unit,
    viewModel: JobDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(sn) { viewModel.setSn(sn) }
    val job by viewModel.job.collectAsStateWithLifecycle()
    val bookmarked by viewModel.isBookmarked.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("공고 상세", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = viewModel::onToggleBookmark) {
                        Icon(
                            imageVector = if (bookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = "즐겨찾기", tint = Color.White
                        )
                    }
                    IconButton(onClick = {
                        val j = job ?: return@IconButton
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, "[${j.instNm}] ${j.recrutPbancTtl}\n${j.pbancUrl}")
                        }
                        context.startActivity(Intent.createChooser(intent, "공유"))
                    }) {
                        Icon(Icons.Default.Share, contentDescription = "공유", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { inner ->
        val current = job
        if (current == null) {
            Column(
                Modifier.fillMaxSize().padding(inner),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("불러오는 중…", color = MaterialTheme.colorScheme.outline)
            }
            return@Scaffold
        }
        Column(
            Modifier
                .fillMaxSize()
                .padding(inner)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    current.instNm.ifBlank { "-" },
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                if (current.instClsfNm.isNotBlank()) {
                    BadgeChip(text = current.instClsfNm, color = categoryColor(current.instClsfCd))
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(current.recrutPbancTtl, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(12.dp))
            Row {
                if (current.hireTypeNm.isNotBlank()) {
                    BadgeChip(text = current.hireTypeNm, color = hireColor(current.hireTypeNm))
                    Spacer(Modifier.width(6.dp))
                }
                if (current.recrutSeNm.isNotBlank()) {
                    BadgeChip(text = current.recrutSeNm, color = MaterialTheme.colorScheme.secondary)
                }
            }
            Spacer(Modifier.height(16.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {
                    InfoRow("근무 지역", current.workRgnNm)
                    InfoRow("학력 조건", current.acbgCondNm)
                    InfoRow("채용 인원", current.recrutNope)
                    InfoRow("공고 시작", DateUtils.formatDisplay(current.pbancBgngYmd))
                    InfoRow("공고 마감", "${DateUtils.formatDisplay(current.pbancEndYmd)} · ${DateUtils.dDayLabel(current.pbancEndYmd)}")
                }
            }
            Spacer(Modifier.height(20.dp))
            if (current.pbancUrl.isNotBlank()) {
                Button(
                    onClick = {
                        val uri = Uri.parse(current.pbancUrl)
                        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.OpenInNew, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("공고 원문 열기")
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(
            label,
            modifier = Modifier.width(88.dp),
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            value.ifBlank { "-" },
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
    }
}
