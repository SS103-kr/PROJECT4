package com.jobalarm.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "notified_posts",
    indices = [Index(value = ["recrutPbancSn"], unique = true)]
)
data class NotifiedPostEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val recrutPbancSn: String,
    val notifiedAt: Long
)
