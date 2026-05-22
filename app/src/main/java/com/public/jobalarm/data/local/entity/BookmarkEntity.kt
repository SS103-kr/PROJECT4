package com.jobalarm.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "bookmarks",
    indices = [Index(value = ["recrutPbancSn"], unique = true)]
)
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val recrutPbancSn: String,
    val instNm: String,
    val recrutPbancTtl: String,
    val pbancEndYmd: String,
    val savedAt: Long
)
