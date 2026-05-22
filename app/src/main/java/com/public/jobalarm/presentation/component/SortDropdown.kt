package com.jobalarm.presentation.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jobalarm.domain.model.JobSort

@Composable
fun SortDropdown(
    selected: JobSort,
    onSelect: (JobSort) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val label = when (selected) {
        JobSort.LATEST -> "최신순"
        JobSort.DEADLINE -> "마감임박순"
        JobSort.ORG_NAME -> "기관명순"
    }
    TextButton(onClick = { expanded = true }, modifier = modifier.padding(horizontal = 4.dp)) {
        Row {
            Icon(Icons.Default.Sort, contentDescription = null)
            Text("  $label")
            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            JobSort.values().forEach { sort ->
                val txt = when (sort) {
                    JobSort.LATEST -> "최신순"
                    JobSort.DEADLINE -> "마감임박순"
                    JobSort.ORG_NAME -> "기관명순"
                }
                DropdownMenuItem(
                    text = { Text(txt) },
                    onClick = { onSelect(sort); expanded = false }
                )
            }
        }
    }
}
