package com.easit.pdfmaker.ui.editResume

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.easit.pdfmaker.NavigationIcon
import com.easit.pdfmaker.UploadButton
import com.easit.pdfmaker.connectivityStatus
import com.easit.pdfmaker.constants.Constant
import com.easit.pdfmaker.kotlinModels.AllResultData
import com.easit.pdfmaker.kotlinModels.SettingsReplica
import com.easit.tailor.helper.ConnectionStatus

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun EditResumeScreen(
    allResultData: AllResultData,
    settings: SettingsReplica,
    onReturn: () -> Unit,
) {
    //
    val viewModel = viewModel<EditResumeViewModel>()
    val context = LocalContext.current
    val connection by connectivityStatus()
    val isConnected = connection === ConnectionStatus.Available
    var dismissDialogVisibleBack by remember { mutableStateOf(false) }
    var isUpdateSaved by remember { mutableStateOf(false) }
    var showInternetAlert by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (!settings.extremeAmoledMode) MaterialTheme.colorScheme.surfaceContainerHigh else Color.Black
                ),
                title = { Text("Profiles") },
                navigationIcon = {
                    NavigationIcon {
                        if (isUpdateSaved) {
                            onReturn()
                        }else {
                            dismissDialogVisibleBack = true
                        }
                    }
                },
                actions = {
                    //YouTubeButton(onClicked = { uriHandler.openUri(Constant.YOUTUBE_LINK_CREATE_PROFILE) })
                }
            )
        },
        floatingActionButton = {
            UploadButton(
                text = "Upload",
                onClick = {
                    if (isConnected){
                        if (!isUpdateSaved) {
                            /*if (profileViewModel.profileNameTextField.value.isNotBlank()){
                                profileViewModel.updateProfileDetail()
                            }else{
                                Toast.makeText(context, "Profile name cannot be left blank.", Toast.LENGTH_SHORT).show()
                            }*/
                        }else{
                            Toast.makeText(context, "Your details are up to date.", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        showInternetAlert = true
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = it.calculateTopPadding(),
                    start = Constant.PADDING.dp,
                    end = Constant.PADDING.dp
                )
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}