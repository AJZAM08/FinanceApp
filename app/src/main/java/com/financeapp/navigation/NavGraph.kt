package com.financeapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.financeapp.presentation.screen.dashboard.DashboardScreen
import com.financeapp.presentation.screen.onboarding.OnboardingScreen
import com.financeapp.presentation.screen.splash.SplashScreen
import com.financeapp.presentation.screen.transaction.AddTransactionScreen
import com.financeapp.presentation.screen.transaction.TransactionListScreen
import com.financeapp.presentation.screen.statistics.StatisticsScreen

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Dashboard : Screen("dashboard")
    object AddTransaction : Screen("add_transaction")
    object TransactionList : Screen("transaction_list")
    object Statistics : Screen("statistics")
    object EditTransaction : Screen("edit_transaction/{transactionId}") {
        fun createRoute(id: Long) = "edit_transaction/$id"
    }
}

@Composable
fun FinanceNavGraph(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(route = Screen.Splash.route) { _ ->
            SplashScreen(
                onNavigateToDashboard = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Splash.route) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToOnboarding = {
                    navController.navigate(Screen.Onboarding.route) {
                        popUpTo(Screen.Splash.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = Screen.Onboarding.route) { _ ->
            OnboardingScreen(
                onNavigateToDashboard = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Onboarding.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = Screen.Dashboard.route) { _ ->
            DashboardScreen(
                onNavigateToAddTransaction = {
                    navController.navigate(Screen.AddTransaction.route)
                },
                onNavigateToEditTransaction = { id ->
                    navController.navigate(
                        Screen.EditTransaction.createRoute(id)
                    )
                },
                onNavigateToTransactionList = {
                    navController.navigate(Screen.TransactionList.route)
                },
                onNavigateToStatistics = {
                    navController.navigate(Screen.Statistics.route)
                }
            )
        }

        composable(route = Screen.Statistics.route) { _ ->
            StatisticsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(route = Screen.AddTransaction.route) { _ ->
            AddTransactionScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(route = Screen.TransactionList.route) { _ ->
            TransactionListScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEditTransaction = { id ->
                    navController.navigate(
                        Screen.EditTransaction.createRoute(id)
                    )
                }
            )
        }

        composable(
            route = Screen.EditTransaction.route,
            arguments = listOf(
                navArgument("transactionId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry: NavBackStackEntry ->
            val transactionId = backStackEntry
                .arguments
                ?.getLong("transactionId") ?: 0L
            AddTransactionScreen(
                transactionId = transactionId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}