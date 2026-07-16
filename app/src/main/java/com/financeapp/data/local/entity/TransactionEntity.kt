package com.financeapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.financeapp.domain.model.TransactionType
import com.financeapp.domain.model.TransactionCategory

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "amount")
    val amount: Long,
    @ColumnInfo(name = "type")
    val type: TransactionType,
    @ColumnInfo(name = "category")
    val category: TransactionCategory,
    @ColumnInfo(name = "payment_method")
    val paymentMethod: String,
    @ColumnInfo(name = "bank_name")
    val bankName: String? = null,
    @ColumnInfo(name = "due_date")
    val dueDate: Long? = null,
    @ColumnInfo(name = "wallet_name")
    val walletName: String? = null,
    @ColumnInfo(name = "note")
    val note: String = "",
    @ColumnInfo(name = "date")
    val date: Long,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
)
