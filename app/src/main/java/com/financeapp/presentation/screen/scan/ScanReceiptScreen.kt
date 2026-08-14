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
import androidx.compose.ui.Alignment
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
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ScanReceiptScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAddTransaction: (amount: String, merchant: String, category: TransactionCategory) -> Unit,
    viewModel: ScanReceiptViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // ── Permission Kamera ─────────────────────────────────────────
    val cameraPermission = rememberPermissionState(Manifest.permission.CAMERA)
    // ── Launcher Kamera ───────────────────────────────────────────
    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) cameraImageUri?.let { viewModel.onImageSelected(it) }
    }
    // Fungsi buka kamera dengan cek permission
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
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
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
                        PhaseLoading(
                            statusText = if (uiState.isOcrLoading)
                                "Membaca teks dari struk..." else "Menganalisis dengan AI..."
                        )
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
private fun PhaseLoading(statusText: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(color = Indigo500, strokeWidth = 3.dp)
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = statusText,
            style = MaterialTheme.typography.bodyLarge,
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
            shape = RoundedCornerShape(12.dp)
        )

        // Field Nama Toko
        OutlinedTextField(
            value = uiState.editedMerchant,
            onValueChange = onMerchantChange,
            label = { Text("Nama Toko / Merchant") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
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