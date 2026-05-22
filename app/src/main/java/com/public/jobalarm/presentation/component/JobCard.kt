package com.jobalarm.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jobalarm.domain.model.JobPosting
import com.jobalarm.presentation.theme.DeadlineRed
import com.jobalarm.presentation.theme.categoryColor
import com.jobalarm.presentation.theme.hireColor
import com.jobalarm.util.DateUtils

@Composable
fun JobCard(
    posting: JobPosting,
    isBookmarked: Boolean,
    onClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dDay = DateUtils.dDay(posting.pbancEndYmd)
    val isUrgent = dDay != null && dDay in 0..3
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = posting.instNm.ifBlank { "-" },
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (posting.instClsfNm.isNotBlank()) {
                    BadgeChip(text = posting.instClsfNm, color = categoryColor(posting.instClsfCd))
                }
                Spacer(Modifier.width(4.dp))
                IconButton(onClick = onBookmarkClick, modifier = Modifier.size(36.dp)) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = "즐겨찾기",
                        tint = if (isBookmarked) MaterialTheme.colorScheme.primary
                               else MaterialTheme.colorScheme.outline
                    )
                }
            }
            Spacer(Modifier.height(4.dp))
            Text(
                text = posting.recrutPbancTtl.ifBlank { "-" },
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (posting.hireTypeNm.isNotBlank()) {
                    BadgeChip(text = posting.hireTypeNm, color = hireColor(posting.hireTypeNm))
                    Spacer(Modifier.width(6.dp))
                }
                if (posting.workRgnNm.isNotBlank()) {
                    Icon(
                        Icons.Default.Place, contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )
                    Spacer(Modifier.width(2.dp))
                    Text(
                        text = posting.workRgnNm,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                }
                Spacer(Modifier.weight(1f))
                val deadlineColor: Color = if (isUrgent) DeadlineRed else MaterialTheme.colorScheme.outline
                Text(
                    text = "${DateUtils.formatDisplay(posting.pbancEndYmd)} · ${DateUtils.dDayLabel(posting.pbancEndYmd)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = deadlineColor,
                    fontWeight = if (isUrgent) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}
