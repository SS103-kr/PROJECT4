package com.jobalarm.util

object Constants {
    const val NOTIF_CHANNEL_ID = "job_alerts"
    const val NOTIF_GROUP_KEY = "JOB_ALERT_GROUP"
    const val WORK_NAME_SYNC = "job_sync_periodic"
    const val DEEPLINK_URI_BASE = "jobalarm://detail/"
    const val LOCAL_INSTITUTION_URL = "https://job.cleaneye.go.kr/user/ypRecruitment.do"

    const val CATEGORY_A = "A"
    const val CATEGORY_B = "B"
    const val CATEGORY_C = "C"
    const val CATEGORY_D = "D"

    val CATEGORY_CODES = listOf(CATEGORY_A, CATEGORY_B, CATEGORY_C, CATEGORY_D)

    fun categoryName(code: String): String = when (code) {
        CATEGORY_A -> "공기업"
        CATEGORY_B -> "준정부기관"
        CATEGORY_C -> "기타공공기관"
        CATEGORY_D -> "지방공기업"
        else -> "기타"
    }
}
