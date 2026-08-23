package com.financeapp.presentation.screen.scan

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.financeapp.domain.model.TransactionCategory
import com.financeapp.presentation.theme.*
import java.io.File
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import android.Manifest
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ScanReceiptScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAddTransaction: (amount: String, merchant: String, category: TransactionCategory) -> Unit,
    viewModel: ScanReceiptViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val cameraPermission = rememberPermissionState(Manifest.permission.CAMERA)
    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) cameraImageUri?.let { viewModel.onImageSelected(it) }
    }
    val context = LocalContext.current
    val openCamera = {
        if (cameraPermission.status.isGranted) {
            val file = File.createTempFile("receipt_", ".jpg", context.cacheDir)
            val uri = FileProvider.getUriForFile(
                context, "${context.packageName}.provider", file
            )
            cameraImageUri = uri
            cameraLauncher.launch(uri)
        } else {
            cameraPermission.launchPermissionRequest()
        }
    }

    // ── Error handling ────────────────────────────────────────────
    if (uiState.errorMessage != null) {
        AlertDialog(
            onDismissRequest = { viewModel.clearError() },
            icon = {
                Icon(
                    imageVector = Icons.Default.ErrorOutline,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(36.dp)
                )
            },
            title = {
                Text(
                    text = "Gagal Memproses Struk",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    text = uiState.errorMessage ?: "Terjadi kesalahan saat proses gambar struk. pastikan foto struk terlihat jelas dan pencahayaan cukup.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.clearError()
                        viewModel.reset()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Indigo500),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Coba Lagi", color = TextOnDark)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.clearError()
                        onNavigateToAddTransaction("", "", TransactionCategory.OTHER)
                    }
                ) {
                    Text("Input Manual", color = Indigo500)
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = MaterialTheme.colorScheme.surface
        )
    }

    // ── Launcher: Galeri (Photo Picker) ──────────────────────────
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let { viewModel.onImageSelected(it) }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // ── Header Indigo ─────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.28f)
                .background(Brush.verticalGradient(listOf(Indigo700, Indigo500)))
        )

        Column(modifier = Modifier.fillMaxSize()) {

            // Header bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 16.dp)
                    .padding(top = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali",
                        tint = TextOnDark
                    )
                }
                Text(
                    text = "Scan Struk",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextOnDark
                )
            }

            // Panel putih melengkung
            Card(
                modifier = Modifier.fillMaxWidth().weight(1f),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                val isLoading = uiState.isOcrLoading || uiState.isParsingLoading
                val hasResult = uiState.receiptData != null

                when {
                    // ── Fase 1: Pilih Gambar ──────────────────────
                    !isLoading && !hasResult -> {
                        PhasePickImage(
                            onGalleryClick = {
                                galleryLauncher.launch(
                                    PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            },
                            onCameraClick = openCamera
                        )
                    }

                    // ── Fase 2: Loading ───────────────────────────
                    isLoading -> {
                        PhaseLoading(isOcrPhase = uiState.isOcrLoading)
                    }

                    // ── Fase 3: Review Hasil ──────────────────────
                    else -> {
                        PhaseReview(
                            uiState = uiState,
                            imageUri = uiState.imageUri,
                            onAmountChange = viewModel::onAmountChange,
                            onMerchantChange = viewModel::onMerchantChange,
                            onScanAgain = { viewModel.reset() },
                            onConfirm = {
                                onNavigateToAddTransaction(
                                    uiState.editedAmount,
                                    uiState.editedMerchant,
                                    uiState.editedCategory
                                )
                            }
                        )
                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

// ── Composable: Fase Pilih Gambar ─────────────────────────────────
@Composable
private fun PhasePickImage(
    onGalleryClick: () -> Unit,
    onCameraClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "📄",
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Scan Struk Belanja",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Foto struk kamu dan AI akan mengekstrak\ninformasi transaksi secara otomatis",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(40.dp))

        // Tombol Kamera
        Button(
            onClick = onCameraClick,
            modifier = Modifier.fillMaxWidth().height(54.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Indigo500)
        ) {
            Icon(Icons.Default.CameraAlt, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Ambil Foto", fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Tombol Galeri
        OutlinedButton(
            onClick = onGalleryClick,
            modifier = Modifier.fillMaxWidth().height(54.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(Icons.Default.Image, contentDescription = null, tint = Indigo500)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Pilih dari Galeri", color = Indigo500, fontWeight = FontWeight.SemiBold)
        }
    }
}

// ── Composable: Fase Loading ──────────────────────────────────────
@Composable
private fun PhaseLoading(isOcrPhase: Boolean) {
    val statusMessage = remember(isOcrPhase) {
        if (isOcrPhase) {
            listOf(
                "Membaca teks dari struk...",
                "Mendeteksi tulisan & angka..."
            )
        } else {
            listOf(
                "Menganalisis struk dengan Gemini AI...",
                "Mendeteksi nama toko & total belanja...",
                "Mengkategorikan transaksi secara cerdas...",
                "Menyiapkan formulir transaksi..."
            )
        }
    }
    var currentMessageIndex by remember { mutableStateOf(0) }
    LaunchedEffect(statusMessage) {
        currentMessageIndex = 0
        while (true) {
            delay(2200)
            if (currentMessageIndex < statusMessage.size - 1) {
                currentMessageIndex++
            }
        }
    }
    val infiniteTransition = rememberInfiniteTransition(label = "pulseTransition")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 0.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(140.dp)
        ) {
            Box(
                Modifier
                    .size(110.dp)
                    .scale(pulseScale)
                    .clip(CircleShape)
                    .background(Indigo500.copy(alpha = pulseAlpha))
            )
            Box(
                Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Indigo500),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isOcrPhase) Icons.Default.ReceiptLong else Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = TextOnDark,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
        Spacer(Modifier.height(32.dp))
        CircularProgressIndicator(
            Modifier.size(28.dp),
            color = Indigo500,
            strokeWidth = 3.dp
        )
        Spacer(Modifier.height(20.dp))
        AnimatedContent(
            targetState = statusMessage[currentMessageIndex],
            transitionSpec = {
                slideInVertically { height -> height / 2 } + fadeIn() togetherWith slideOutVertically { height -> -height / 2 } + fadeOut()
            },
            label = "statusMessageAnimation"
        ) { text ->
            Text(
                text,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(
            "Proses ini membutuhkan waktu beberapa detik",
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
    }
}

// ── Composable: Fase Review ───────────────────────────────────────
@Composable
private fun PhaseReview(
    uiState: ScanReceiptUiState,
    imageUri: Uri?,
    onAmountChange: (String) -> Unit,
    onMerchantChange: (String) -> Unit,
    onScanAgain: () -> Unit,
    onConfirm: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Hasil Scan",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        // Preview gambar
        imageUri?.let { uri ->
            AsyncImage(
                model = uri,
                contentDescription = "Struk",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
        }

        // Field Nominal
        OutlinedTextField(
            value = uiState.editedAmount,
            onValueChange = onAmountChange,
            label = { Text("Total (Rp)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            prefix = { Text("Rp ") },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )

        // Field Nama Toko
        OutlinedTextField(
            value = uiState.editedMerchant,
            onValueChange = onMerchantChange,
            label = { Text("Nama Toko / Merchant") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )

        // Info kategori yang terdeteksi
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Lavender100),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Kategori terdeteksi:", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = uiState.editedCategory.name,
                    fontWeight = FontWeight.Bold,
                    color = Indigo500
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Tombol Konfirmasi
        Button(
            onClick = onConfirm,
            modifier = Modifier.fillMaxWidth().height(54.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Indigo500)
        ) {
            Text("Buat Transaksi", fontWeight = FontWeight.SemiBold, color = TextOnDark)
        }

        // Tombol Scan Ulang
        TextButton(
            onClick = onScanAgain,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Scan Ulang", color = TextSecondary)
        }
    }
}