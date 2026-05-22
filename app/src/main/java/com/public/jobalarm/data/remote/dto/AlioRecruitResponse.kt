package com.jobalarm.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlioRecruitResponse(
    @SerialName("result") val result: List<ItemDto> = emptyList(),
    @SerialName("resultCode") val resultCode: Int = 200,
    @SerialName("totalCount") val totalCount: Int = 0,
    @SerialName("resultMsg") val resultMsg: String = ""
)

@Serializable
data class ItemDto(
    @SerialName("recrutPblntSn") val recrutPblntSn: Int? = null,
    @SerialName("instNm") val instNm: String? = null,
    @SerialName("recrutPbancTtl") val recrutPbancTtl: String? = null,
    @SerialName("pbancBgngYmd") val pbancBgngYmd: String? = null,
    @SerialName("pbancEndYmd") val pbancEndYmd: String? = null,
    @SerialName("srcUrl") val srcUrl: String? = null,
    @SerialName("recrutSeNm") val recrutSeNm: String? = null,
    @SerialName("hireTypeNmLst") val hireTypeNmLst: String? = null,
    @SerialName("workRgnNmLst") val workRgnNmLst: String? = null,
    @SerialName("acbgCondNmLst") val acbgCondNmLst: String? = null,
    @SerialName("recrutNope") val recrutNope: Int? = null,
    @SerialName("ongoingYn") val ongoingYn: String? = null,
    @SerialName("pblntInstCd") val pblntInstCd: String? = null
)
