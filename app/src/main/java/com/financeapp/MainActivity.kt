package com.financeapp

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.financeapp.core.security.RootDetection
import com.financeapp.core.security.RootedDeviceScreen
import com.financeapp.navigation.FinanceNavGraph
import com.financeapp.presentation.theme.FinanceAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var rootDetection: RootDetection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Pencegahan Screen Record & Screenshot
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        enableEdgeToEdge()
        setContent {
            FinanceAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    if (rootDetection.isDeviceRooted()) {
                        RootedDeviceScreen()
                    } else {
                        FinanceNavGraph()
                    }
                }
            }
        }
    }
}