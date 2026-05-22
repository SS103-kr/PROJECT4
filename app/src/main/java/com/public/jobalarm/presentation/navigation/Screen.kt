package com.jobalarm.presentation.navigation

sealed class Screen(val route: String) {
    data object Main : Screen("main")
    data object Search : Screen("search")
    data object AlertSetting : Screen("alert_setting")
    data object CategoryDetail : Screen("category/{code}") {
        fun create(code: String) = "category/$code"
        const val ARG_CODE = "code"
    }
    data object JobDetail : Screen("detail/{recrutPbancSn}") {
        fun create(sn: String) = "detail/$sn"
        const val ARG_SN = "recrutPbancSn"
    }
}
