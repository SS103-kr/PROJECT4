package com.jobalarm.domain.model

data class JobPosting(
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
    val pbancUrl: String
)
