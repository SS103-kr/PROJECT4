package com.jobalarm.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "alert_orgs",
    indices = [Index(value = ["orgNm"], unique = true)]
)
data class AlertOrgEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orgNm: String,
    val orgClsfNm: String,
    val addedAt: Long
)
