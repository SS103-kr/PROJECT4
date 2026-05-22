package com.jobalarm.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "job_postings",
    indices = [Index(value = ["recrutPbancSn"], unique = true)]
)
data class JobPostingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val recrutPbancSn: String,
    val instNm: String,
    val instClsfNm: String,
    val instClsfCd: String,
    val recrutPbancTtl: String,
    val recrutSeNm: String,
    val hireTypeNm: String,
    val workRgnNm: String,
    val acbgCondNm: String,
    val recrutNope: String,
    val pbancBgngYmd: String,
    val pbancEndYmd: String,
    val pbancUrl: String,
    val savedAt: Long
)
