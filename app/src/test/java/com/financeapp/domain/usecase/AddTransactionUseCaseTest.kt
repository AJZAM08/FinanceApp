package com.financeapp.domain.usecase

import com.financeapp.domain.model.PaymentMethod
import com.financeapp.domain.model.Transaction
import com.financeapp.domain.model.TransactionCategory
import com.financeapp.domain.model.TransactionType
import com.financeapp.domain.repository.TransactionRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class AddTransactionUseCaseTest {

    private val repository: TransactionRepository = mockk()
    private lateinit var useCase: AddTransactionUseCase

    @BeforeEach
    fun setUp() {
        useCase = AddTransactionUseCase(repository)
    }

    private fun createDummyTransaction(
        title: String = "Makan siang",
        amount: Long = 35000L,
        type: TransactionType = TransactionType.EXPENSE
    ) = Transaction(
        id = 0L,
        title = title,
        amount = amount,
        type = type,
        category = TransactionCategory.FOOD,
        paymentMethod = PaymentMethod.Cash,
        date = LocalDateTime.now()
    )

    @Test
    fun `given valid transaction, when invoked, should return success`() = runTest {
        // ARRANGE
        val transaction = createDummyTransaction()
        coEvery {
            repository.insertTransaction(transaction)
        } returns 1L

        // ACT
        val result = useCase(transaction)

        // ASSERT
        assertTrue(result.isSuccess)
        assertEquals(1L, result.getOrNull())
        coVerify(exactly = 1) {
            repository.insertTransaction(transaction)
        }
    }

    @Test
    fun `given empty title, when invoked, should return failure`() = runTest {
        // ARRANGE
        val transaction = createDummyTransaction(title = "")

        // ACT
        val result = useCase(transaction)

        // ASSERT
        assertTrue(result.isFailure)
        assertEquals(
            "Judul transaksi tidak boleh kosong",
            result.exceptionOrNull()?.message
        )
        coVerify(exactly = 0) {
            repository.insertTransaction(any())
        }
    }

    @Test
    fun `given zero amount, when invoked, should return failure`() = runTest {
        val transaction = createDummyTransaction(amount = 0L)
        val result = useCase(transaction)
        assertTrue(result.isFailure)
        assertEquals(
            "Nominal transaksi harus lebih besar dari 0",
            result.exceptionOrNull()?.message
        )
    }

    @Test
    fun `given negative amount, when invoked, should return failure`() = runTest {
        val transaction = createDummyTransaction(amount = -1000L)
        val result = useCase(transaction)
        assertTrue(result.isFailure)
    }

    @Test
    fun `given repository throws exception, when invoked, should return failure`() = runTest {
        val transaction = createDummyTransaction()
        coEvery {
            repository.insertTransaction(transaction)
        } throws Exception("Database error")

        val result = useCase(transaction)

        assertTrue(result.isFailure)
        assertEquals("Database error", result.exceptionOrNull()?.message)
    }
}