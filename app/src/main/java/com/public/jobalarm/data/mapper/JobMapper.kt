package com.jobalarm.data.mapper

import com.jobalarm.data.local.entity.JobPostingEntity
import com.jobalarm.data.remote.dto.ItemDto
import com.jobalarm.domain.model.JobPosting

private fun String?.clean(default: String = ""): String {
    if (this == null) return default
    val t = this.trim()
    if (t.isEmpty() || t == "-" || t == "해당없음") return default
    return t
}

fun ItemDto.toEntity(
    instClsfCd: String,
    instClsfNm: String,
    now: Long = System.currentTimeMillis()
): JobPostingEntity? {
    val sn = recrutPblntSn?.toString()?.clean() ?: return null
    if (sn.isEmpty()) return null
    return JobPostingEntity(
        recrutPbancSn = sn,
        instNm = instNm.clean(),
        instClsfNm = instClsfNm,
        instClsfCd = instClsfCd,
        recrutPbancTtl = recrutPbancTtl.clean(),
        recrutSeNm = recrutSeNm.clean(),
        hireTypeNm = hireTypeNmLst.clean(),
        workRgnNm = workRgnNmLst.clean(),
        acbgCondNm = acbgCondNmLst.clean(),
        recrutNope = recrutNope?.toString() ?: "",
        pbancBgngYmd = pbancBgngYmd.clean(),
        pbancEndYmd = pbancEndYmd.clean(),
        pbancUrl = srcUrl.clean(),
        savedAt = now
    )
}

fun JobPostingEntity.toDomain(): JobPosting = JobPosting(
    recrutPbancSn = recrutPbancSn,
    instNm = instNm,
    instClsfNm = instClsfNm,
    instClsfCd = instClsfCd,
    recrutPbancTtl = recrutPbancTtl,
    recrutSeNm = recrutSeNm,
    hireTypeNm = hireTypeNm,
    workRgnNm = workRgnNm,
    acbgCondNm = acbgCondNm,
    recrutNope = recrutNope,
    pbancBgngYmd = pbancBgngYmd,
    pbancEndYmd = pbancEndYmd,
    pbancUrl = pbancUrl
)
