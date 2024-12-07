package com.easit.pdfmaker

import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Upload
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.easit.pdfmaker.helper.currentConnectivityStatus
import com.easit.pdfmaker.helper.observeConnectivityAsFlow
import com.easit.tailor.helper.ConnectionStatus

@Composable
fun UploadButton(
    text: String,
    onClick: () -> Unit
) {
    ExtendedFloatingActionButton(
        modifier = Modifier.imePadding(),
        shape = RoundedCornerShape(24.dp),
        onClick = { onClick() },
        icon = { Icon(Icons.Rounded.Upload, text) },
        text = { Text(text = text) },
    )
}

@Composable
fun connectivityStatus(): State<ConnectionStatus> {
    val context = LocalContext.current

    return produceState(initialValue = context.currentConnectivityStatus) {
        context.observeConnectivityAsFlow().collect{ value = it }
    }
}

@Composable
fun NavigationIcon(onBackNavClicked: () -> Unit) {
    IconButton(onClick = onBackNavClicked) {
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
            contentDescription = "Back",
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}