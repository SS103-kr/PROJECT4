package com.jobalarm.presentation.main.tabs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jobalarm.data.local.entity.BookmarkEntity
import com.jobalarm.presentation.component.EmptyStateView
import com.jobalarm.util.DateUtils

@Composable
fun BookmarkTab(
    bookmarks: List<BookmarkEntity>,
    onOpenDetail: (String) -> Unit
) {
    if (bookmarks.isEmpty()) {
        EmptyStateView(message = "관심 공고를 즐겨찾기해보세요")
        return
    }
    LazyColumn(Modifier.fillMaxSize()) {
        items(items = bookmarks, key = { it.recrutPbancSn }) { b ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .clickable { onOpenDetail(b.recrutPbancSn) },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(Modifier.padding(12.dp)) {
                    Text(b.instNm, fontWeight = FontWeight.Bold)
                    Text(b.recrutPbancTtl, style = MaterialTheme.typography.bodyMedium)
                    Text(
                        "마감: ${DateUtils.formatDisplay(b.pbancEndYmd)} · ${DateUtils.dDayLabel(b.pbancEndYmd)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}
