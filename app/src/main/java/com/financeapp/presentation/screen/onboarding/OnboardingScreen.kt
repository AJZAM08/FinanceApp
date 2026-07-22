package com.financeapp.presentation.screen.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlinx.coroutines.launch

data class OnboardingPage(
    val icon: ImageVector,
    val title: String,
    val description: String
)

val onBoardingPages = listOf(
    OnboardingPage(
        icon = Icons.Default.Receipt,
        title = "Catat Transaksi",
        description = "Catat setiap pemasukan dan pengeluaran dengan mudah dan cepat"
    ),
    OnboardingPage(
        icon = Icons.Default.AccountBalance,
        title = "Kelola Keuangan",
        description = "Pantau saldo dan atur keuangan kamu dengan lebih bijak setiap hari"
    ),
    OnboardingPage(
        icon = Icons.Default.BarChart,
        title = "Analisis Pengeluaran",
        description = "Lihat statistik dan laporan keuangan untuk keputusan yang lebih baik"
    )
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    onNavigateToDashboard: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val pagerState = rememberPagerState { onBoardingPages.size }
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState.isCompleted) {
        if (uiState.isCompleted) {
            onNavigateToDashboard()
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            OnboardingPageContent(page = onBoardingPages[page])
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(vertical = 24.dp)
        ) {
            repeat(onBoardingPages.size) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .size(if (isSelected) 24.dp else 8.dp, 8.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.outline
                        )
                )
            }
        }

        val isLastPage = pagerState.currentPage == onBoardingPages.size - 1

        AnimatedVisibility(visible = !isLastPage) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = { viewModel.completeOnboarding() }
                ) {
                    Text("Lewati")
                }

                Button(
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(
                                pagerState.currentPage + 1
                            )
                        }
                    }
                ) {
                    Text("Selanjutnya")
                }
            }
        }

        AnimatedVisibility(visible = isLastPage) {
            Button(
                onClick = { viewModel.completeOnboarding() },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) {
                Text(
                    text = "Mulai Sekarang",
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
private fun OnboardingPageContent(page: OnboardingPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .background(
                    MaterialTheme.colorScheme.primaryContainer
                )
        ) {
            Icon(
                imageVector = page.icon,
                contentDescription = page.title,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = page.title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = page.description,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}