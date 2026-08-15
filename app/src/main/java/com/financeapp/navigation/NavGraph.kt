package com.financeapp.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.financeapp.presentation.screen.dashboard.DashboardScreen
import com.financeapp.presentation.screen.onboarding.OnboardingScreen
import com.financeapp.presentation.screen.splash.SplashScreen
import com.financeapp.presentation.screen.transaction.AddTransactionScreen
import com.financeapp.presentation.screen.transaction.TransactionListScreen
import com.financeapp.presentation.screen.statistics.StatisticsScreen
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.financeapp.presentation.component.BottomNavBar
import com.financeapp.presentation.component.bottomNavItems
import com.financeapp.domain.model.TransactionCategory
import com.financeapp.presentation.screen.scan.ScanReceiptScreen

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Dashboard : Screen("dashboard")
    object AddTransaction : Screen("add_transaction")
    object TransactionList : Screen("transaction_list")
    object ScanReceipt     : Screen("scan_receipt")
    object Statistics : Screen("statistics")
    object EditTransaction : Screen("edit_transaction/{transactionId}") {
        fun createRoute(id: Long) = "edit_transaction/$id"
    }
}

@Composable
fun FinanceNavGraph(
    navController: NavHostController = rememberNavController(),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavRoutes = bottomNavItems.map { it.route }
    val showBottomBar = currentRoute in bottomNavRoutes
    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(Screen.Dashboard.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(paddingValues),
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(350)
                ) + fadeIn(animationSpec = tween(350))
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(350),
                    targetOffset = { it / 4 }
                ) + fadeOut(animationSpec = tween(350))
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(350),
                    initialOffset = { -it / 4 }
                ) + fadeIn(animationSpec = tween(350))
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(350)
                ) + fadeOut(animationSpec = tween(350))
            }
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

            composable(route = Screen.Dashboard.route) {
                DashboardScreen(
                    onNavigateToAddTransaction    = { navController.navigate(Screen.AddTransaction.route) },
                    onNavigateToEditTransaction   = { id -> navController.navigate(Screen.EditTransaction.createRoute(id)) },
                    onNavigateToTransactionList   = { navController.navigate(Screen.TransactionList.route) },
                    onNavigateToStatistics        = { navController.navigate(Screen.Statistics.route) },
                    onNavigateToScanReceipt       = { navController.navigate(Screen.ScanReceipt.route) }  // ← TAMBAH
                )
            }

            composable(route = Screen.Statistics.route) { _ ->
                StatisticsScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(
                route = Screen.AddTransaction.route +
                        "?amount={amount}&merchant={merchant}&category={category}",
                arguments = listOf(
                    navArgument("amount")   { defaultValue = ""; nullable = true },
                    navArgument("merchant") { defaultValue = ""; nullable = true },
                    navArgument("category") { defaultValue = ""; nullable = true }
                )
            ) { backStackEntry ->
                val amount   = backStackEntry.arguments?.getString("amount") ?: ""
                val merchant = backStackEntry.arguments?.getString("merchant") ?: ""
                val category = backStackEntry.arguments?.getString("category")
                    ?.let { runCatching { TransactionCategory.valueOf(it) }.getOrNull() }

                AddTransactionScreen(
                    onNavigateBack    = { navController.popBackStack() },
                    prefillAmount     = amount,
                    prefillMerchant   = merchant,
                    prefillCategory   = category
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

            composable(route = Screen.ScanReceipt.route) {
                ScanReceiptScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToAddTransaction = { amount, merchant, category ->
                        // Navigasi ke AddTransaction dengan data pre-filled
                        // lewat route dengan argument
                        navController.navigate(
                            Screen.AddTransaction.route +
                                    "?amount=$amount&merchant=$merchant&category=${category.name}"
                        )
                    }
                )
            }
        }
    }
}