package com.financeapp.presentation.screen.dashboard

import com.financeapp.core.datastore.UserPreferences
import com.financeapp.domain.model.TransactionType
import com.financeapp.domain.usecase.BalanceInfo
import com.financeapp.domain.usecase.DeleteTransactionUseCase
import com.financeapp.domain.usecase.GetAllTransactionsUseCase
import com.financeapp.domain.usecase.GetBalanceUseCase
import com.financeapp.domain.usecase.GetCategoryStatisticsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val getAllTransactions: GetAllTransactionsUseCase = mockk()
    private val getBalance: GetBalanceUseCase = mockk()
    private val getCategoryStatistics: GetCategoryStatisticsUseCase = mockk()
    private val deleteTransaction: DeleteTransactionUseCase = mockk()
    private val userPreferences: UserPreferences = mockk(relaxed = true)

    private val isBalanceHiddenFlow = MutableStateFlow(false)

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { getAllTransactions() } returns flowOf(emptyList())
        every { getBalance() } returns flowOf(BalanceInfo(50000L, 20000L, 30000L))
        every { getCategoryStatistics(TransactionType.EXPENSE) } returns flowOf(emptyList())
        every { userPreferences.isBalanceHidden } returns isBalanceHiddenFlow
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadDashboardData updates uiState with balance and isBalanceHidden correctly`() = runTest(testDispatcher) {
        val viewModel = DashboardViewModel(
            getAllTransactions,
            getBalance,
            getCategoryStatistics,
            deleteTransaction,
            userPreferences
        )

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(30000L, state.balanceInfo.balance)
        assertEquals(50000L, state.balanceInfo.totalIncome)
        assertEquals(20000L, state.balanceInfo.totalExpense)
        assertFalse(state.isBalanceHidden)
    }

    @Test
    fun `toggleBalanceVisibility updates userPreferences with negated value`() = runTest(testDispatcher) {
        coEvery { userPreferences.setBalanceHidden(any()) } coAnswers {
            isBalanceHiddenFlow.value = firstArg()
        }

        val viewModel = DashboardViewModel(
            getAllTransactions,
            getBalance,
            getCategoryStatistics,
            deleteTransaction,
            userPreferences
        )
        advanceUntilIdle()

        viewModel.toggleBalanceVisibility()
        advanceUntilIdle()

        coVerify(exactly = 1) { userPreferences.setBalanceHidden(true) }
        assertTrue(viewModel.uiState.value.isBalanceHidden)
    }
}
