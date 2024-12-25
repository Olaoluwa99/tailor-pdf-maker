package com.easit.pdfmaker.ui

import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.TextSnippet
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ChipColors
import androidx.compose.material3.ElevatedSuggestionChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.easit.pdfmaker.NavigationIcon
import com.easit.pdfmaker.deserializeAllResultData
import com.easit.pdfmaker.fileToByteArray
import com.easit.pdfmaker.kotlinModels.PdfMakerSettingsReplica
import com.easit.pdfmaker.kotlinModels.makers.CoverLetterMaker
import com.easit.pdfmaker.kotlinModels.makers.ResumeMaker
import com.easit.pdfmaker.savePdfToExternalStorage
import com.lowagie.text.FontFactory
import com.lowagie.text.Paragraph
import com.rajat.pdfviewer.compose.PdfRendererViewCompose
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ResultScreen(
    isOnlyResume: Boolean,
    isOnlyCoverLetter: Boolean,
    tagId: String,
    resultString: String,
    settings: PdfMakerSettingsReplica,
    onReturn: () -> Unit,
    onEditResume: () -> Unit,
    onEditCoverLetter: () -> Unit
) {

    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val context = LocalContext.current
    val resultViewModel = viewModel<ResultViewModel>()

    BackHandler {
        onReturn()
    }

    //
    /*var resumeItemFile by remember { mutableStateOf<File?>(null) }
    var coverLetterItemFile by remember { mutableStateOf<File?>(null) }*/

    var resumeItemPath by remember { mutableStateOf("") }
    var coverLetterItemPath by remember { mutableStateOf("") }
    var hasFilesBeenRetrieved by remember { mutableStateOf(false) }

    var outputResumeFile: File? by remember { mutableStateOf(null) }
    var outputCoverLetterFile: File? by remember { mutableStateOf(null) }
    var isShowingResume by remember { mutableStateOf(true) }

    var sectionList by remember { mutableStateOf(listOf(Sections.OBJECTIVE, /*Sections.EXPERIENCE,*/ Sections.EDUCATION, Sections.SKILLS, Sections.SOFT_SKILLS, Sections.PROJECT, Sections.CERTIFICATIONS, Sections.HOBBIES)) }

    LaunchedEffect (key1 = 0) {
        resultViewModel.setUserItem(
            data = deserializeAllResultData(resultString),
        )

        if (resultString.isNotBlank()) {
            try {
                //Setup path and create file
                resumeItemPath = "${File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "/files/mains").absolutePath}/RES-$tagId.pdf"
                coverLetterItemPath = "${File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "/files/mains").absolutePath}/COV-$tagId.pdf"
                outputResumeFile = File(resumeItemPath)
                outputCoverLetterFile = File(coverLetterItemPath)

                //Ensure the parent directory exists
                outputResumeFile!!.parentFile?.mkdirs()
                outputCoverLetterFile!!.parentFile?.mkdirs()

                //
                if (outputResumeFile != null ){
                    if (!outputResumeFile!!.exists()) outputResumeFile!!.createNewFile()
                }
                if (outputCoverLetterFile != null ){
                    if (!outputCoverLetterFile!!.exists()) outputCoverLetterFile!!.createNewFile()
                }

                //
                ResumeMaker(resumeItemPath)
                    .createResume(
                        item = resultViewModel.resultData.value.resume!!,
                        skillFormatType = "SINGLE-LIST",
                        softSkillFormatType = "WRAP",
                        showUnderline = true,
                        themeColor = ThemeColor.BLACK,
                        styleType = StyleType.ALPHA,
                        sectionList = sectionList
                    )
                //
                CoverLetterMaker(
                    coverLetterItemPath,
                    "",
                    "ALPHA"
                ).createCoverLetter(resultViewModel.resultData.value.coverLetter!!)
                Toast.makeText(context, "--Success--", Toast.LENGTH_SHORT).show()
                hasFilesBeenRetrieved = true
            }catch (io: IOException){
                println(io.localizedMessage)
                io.printStackTrace()
                Toast.makeText(context, "File creating issue I/O", Toast.LENGTH_SHORT).show()
            }catch (e: Exception){
                println(e.localizedMessage)
                e.printStackTrace()
                Toast.makeText(context, "File creating issue Default", Toast.LENGTH_SHORT).show()
            }
        }else {
            hasFilesBeenRetrieved = false
        }
    }


    val localDensity = LocalDensity.current
    var buttonHeightDp by remember { mutableStateOf(0.dp) }

    val iconSize by remember { mutableIntStateOf(16) }
    var actionButtonText by remember { mutableStateOf("Resume") }
    var nonActionButtonText by remember { mutableStateOf("Cover letter") }
    var keywordAction by remember { mutableStateOf("View") }
    var isBottomSheetFull by remember { mutableStateOf(false) }
    var showKeywordsSection by remember { mutableStateOf(false) }
    //var buttonSize by remember { mutableStateOf(IntSize.Zero) }


    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = ((buttonHeightDp * 2) + 80.dp),
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetSwipeEnabled = false,
        sheetDragHandle = null,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (!settings.extremeAmoledMode) MaterialTheme.colorScheme.surfaceContainerHigh else Color.Black
                ),
                title = { Text(""/*"Resume & Cover letter"*/) },
                navigationIcon = {
                    NavigationIcon { onReturn() }
                },
                actions = {/**/}
            )
        },
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    Button(
                        modifier = Modifier
                            .weight(1f),
                        shape = RoundedCornerShape(50),
                        contentPadding = PaddingValues(vertical = 16.dp),
                        onClick = {
                            if (isShowingResume){
                                isShowingResume = false
                                actionButtonText = "Cover letter"
                                nonActionButtonText = "Resume"
                            }else{
                                isShowingResume = true
                                actionButtonText = "Resume"
                                nonActionButtonText = "Cover letter"
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.TextSnippet,
                            contentDescription = "Edit",
                            modifier = Modifier.size(iconSize.dp),
                            tint = MaterialTheme.colorScheme.background
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "View $nonActionButtonText", overflow = TextOverflow.Ellipsis, color = MaterialTheme.colorScheme.background)
                    }

                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        modifier = Modifier
                            .weight(1f),
                        shape = RoundedCornerShape(50),
                        contentPadding = PaddingValues(vertical = 16.dp),
                        onClick = {
                            if (isShowingResume){
                                if (outputResumeFile != null)  savePdfToExternalStorage(context, fileToByteArray(outputResumeFile!!)!!, "RES-$tagId")
                            }else{
                                if (outputCoverLetterFile != null)  savePdfToExternalStorage(context, fileToByteArray(outputCoverLetterFile!!)!!, "COV-$tagId")
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Download,
                            contentDescription = "Download",
                            modifier = Modifier.size(iconSize.dp),
                            tint = MaterialTheme.colorScheme.background
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Download", overflow = TextOverflow.Ellipsis, color = MaterialTheme.colorScheme.background)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    Button(
                        modifier = Modifier
                            .weight(1f),
                        shape = RoundedCornerShape(50),
                        contentPadding = PaddingValues(vertical = 16.dp),
                        onClick = {
                            if (isShowingResume){
                                onEditResume()
                            }else{
                                onEditCoverLetter()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Edit",
                            modifier = Modifier.size(iconSize.dp),
                            tint = MaterialTheme.colorScheme.background
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Edit $actionButtonText", overflow = TextOverflow.Ellipsis, color = MaterialTheme.colorScheme.background)
                    }

                    Spacer(modifier = Modifier.width(16.dp))
                    OutlinedButton(
                        modifier = Modifier
                            .weight(1f)
                            .onGloballyPositioned { coordinates ->
                                buttonHeightDp = with(localDensity) { coordinates.size.height.toDp() }
                            },
                            /*.onGloballyPositioned { layoutCoordinates ->
                                buttonSize = layoutCoordinates.size
                            },*/
                        shape = RoundedCornerShape(50),
                        contentPadding = PaddingValues(vertical = 16.dp),
                        onClick = {
                            coroutineScope.launch {
                                if (!isBottomSheetFull){
                                    showKeywordsSection = true
                                    scaffoldState.bottomSheetState.expand()
                                    keywordAction = "Hide"
                                    isBottomSheetFull = true
                                }else {
                                    scaffoldState.bottomSheetState.partialExpand()
                                    showKeywordsSection = false
                                    keywordAction = "View"
                                    isBottomSheetFull = false
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Key,
                            contentDescription = "View Keywords",
                            modifier = Modifier.size(iconSize.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "$keywordAction Keywords", overflow = TextOverflow.Ellipsis, color = MaterialTheme.colorScheme.primary)
                    }
                }

                //
                Spacer(modifier = Modifier.height(48.dp))
                if/*AnimatedVisibility*/ (showKeywordsSection) {
                    Column(
                        modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = "KEYWORDS (${resultViewModel.resultData.collectAsState().value.keywords.size})",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontSize = 17.sp,
                        )

                        FlowRow(
                            modifier = Modifier
                                .safeDrawingPadding()
                                .fillMaxWidth(1f)
                                //.padding(16.dp)
                                .wrapContentHeight(align = Alignment.Top),
                            verticalArrangement = Arrangement.spacedBy(0.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            for(item in resultViewModel.resultData.collectAsState().value.keywords){
                                ElevatedSuggestionChip(
                                    label = {
                                        Text(
                                            text = item,
                                            fontSize = MaterialTheme.typography.bodySmall.fontSize
                                        )
                                    },
                                    onClick = { /**/ },
                                    colors = ChipColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        labelColor = MaterialTheme.colorScheme.background,
                                        disabledContainerColor = Color.Unspecified,
                                        disabledLabelColor = Color.Unspecified,
                                        disabledLeadingIconContentColor = Color.Unspecified,
                                        disabledTrailingIconContentColor = Color.Unspecified,
                                        leadingIconContentColor = Color.Unspecified,
                                        trailingIconContentColor = Color.Unspecified
                                    )
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(48.dp))
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = 16.dp,
                    start = 16.dp,
                    end = 16.dp
                )
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            //
            if (hasFilesBeenRetrieved){
                if (resumeItemPath.isNotBlank()){
                    if (isShowingResume){
                        if (outputResumeFile!!.exists()){
                            PdfRendererViewCompose(
                                file = outputResumeFile,
                                lifecycleOwner = LocalLifecycleOwner.current
                            )
                        }
                    }else{
                        if (outputCoverLetterFile!!.exists()){
                            PdfRendererViewCompose(
                                file = outputCoverLetterFile,
                                lifecycleOwner = LocalLifecycleOwner.current
                            )
                        }
                    }
                }
            }
        }
    }
}

enum class ThemeColor{
    RED, GREEN, BLACK, BLUE, YELLOW, LIGHT_GRAY, DARK_GRAY
}

enum class StyleType{
    ALPHA, BETA, DELTA, GAMMA, OMEGA
}

enum class Sections{
    OBJECTIVE, EXPERIENCE, EDUCATION, PROJECT, SKILLS, SOFT_SKILLS, CERTIFICATIONS, HOBBIES
}