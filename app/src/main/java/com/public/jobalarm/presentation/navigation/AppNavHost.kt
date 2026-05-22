package com.jobalarm.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.jobalarm.presentation.alert.AlertSettingScreen
import com.jobalarm.presentation.category.CategoryDetailScreen
import com.jobalarm.presentation.detail.JobDetailScreen
import com.jobalarm.presentation.main.MainScreen
import com.jobalarm.presentation.search.SearchScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Main.route) {

        composable(Screen.Main.route) {
            MainScreen(
                onOpenSearch = { navController.navigate(Screen.Search.route) },
                onOpenAlertSetting = { navController.navigate(Screen.AlertSetting.route) },
                onOpenDetail = { sn -> navController.navigate(Screen.JobDetail.create(sn)) }
            )
        }

        composable(Screen.Search.route) {
            SearchScreen(
                onBack = { navController.popBackStack() },
                onOpenDetail = { sn -> navController.navigate(Screen.JobDetail.create(sn)) }
            )
        }

        composable(Screen.AlertSetting.route) {
            AlertSettingScreen(onBack = { navController.popBackStack() })
        }

        composable(
            route = Screen.CategoryDetail.route,
            arguments = listOf(navArgument(Screen.CategoryDetail.ARG_CODE) { type = NavType.StringType })
        ) { backStackEntry ->
            val code = backStackEntry.arguments?.getString(Screen.CategoryDetail.ARG_CODE).orEmpty()
            CategoryDetailScreen(
                code = code,
                onBack = { navController.popBackStack() },
                onOpenDetail = { sn -> navController.navigate(Screen.JobDetail.create(sn)) }
            )
        }

        composable(
            route = Screen.JobDetail.route,
            arguments = listOf(navArgument(Screen.JobDetail.ARG_SN) { type = NavType.StringType }),
            deepLinks = listOf(navDeepLink { uriPattern = "jobalarm://detail/{${Screen.JobDetail.ARG_SN}}" })
        ) { backStackEntry ->
            val sn = backStackEntry.arguments?.getString(Screen.JobDetail.ARG_SN).orEmpty()
            JobDetailScreen(sn = sn, onBack = { navController.popBackStack() })
        }
    }
}
