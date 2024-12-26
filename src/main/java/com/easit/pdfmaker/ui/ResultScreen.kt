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
import com.easit.pdfmaker.utils.DefaultKeys
import com.easit.pdfmaker.utils.SelectStyle
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

    var sectionList by remember { mutableStateOf(listOf(Sections.OBJECTIVE, /*Sections.EXPERIENCE,*/ Sections.EDUCATION, Sections.SKILLS, Sections.SOFT_SKILLS, Sections.PROJECT, Sections.CERTIFICATIONS, Sections.HOBBIES)) }

    var buttonHeightDp by remember { mutableStateOf(30.dp) }
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

    //
    var type by remember { mutableStateOf(StyleType.ALPHA) }
    var sheetMode by remember { mutableStateOf(SheetMode.DEFAULT) }
    var isShowingResume by remember { mutableStateOf(true) }


    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = ((buttonHeightDp * 2) + 80.dp),
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetSwipeEnabled = /*true,*/false,
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
            when (sheetMode) {
                SheetMode.DEFAULT -> {
                    DefaultKeys(
                        keywords = resultViewModel.resultData.collectAsState().value.keywords,
                        sendSheetPeekHeight = { buttonHeightDp = it },
                        sendIsShowingResume = { isShowingResume = it },
                        onEditResume = {},
                        onEditCoverLetter = {},
                        onDownloadClicked = {
                            if (isShowingResume){
                                if (outputResumeFile != null)  savePdfToExternalStorage(context, fileToByteArray(outputResumeFile!!)!!, "RES-$tagId")
                            }else{
                                if (outputCoverLetterFile != null)  savePdfToExternalStorage(context, fileToByteArray(outputCoverLetterFile!!)!!, "COV-$tagId")
                            }
                        },
                        onFillBottomSheet = {
                            coroutineScope.launch {
                                if (it){
                                    scaffoldState.bottomSheetState.expand()
                                }else{
                                    scaffoldState.bottomSheetState.partialExpand()
                                }
                            }
                        }
                    )
                }
                SheetMode.STYLE -> {
                    SelectStyle {
                        type = it
                        sheetMode = SheetMode.DEFAULT
                        coroutineScope.launch {
                            scaffoldState.bottomSheetState.partialExpand()
                        }
                    }
                }
                SheetMode.SECTION -> { /**/ }
                SheetMode.UNDERLINE -> { /**/ }
                SheetMode.SKILLS -> { /**/ }
                SheetMode.SOFT_SKILLS -> { /**/ }
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

            Button(onClick = {
                if (sheetMode == SheetMode.DEFAULT){
                    sheetMode = SheetMode.STYLE
                    coroutineScope.launch {
                        scaffoldState.bottomSheetState.expand()
                    }
                }else {
                    sheetMode = SheetMode.DEFAULT
                    coroutineScope.launch {
                        scaffoldState.bottomSheetState.partialExpand()
                    }
                }
            }) { Text("Tester") }
            Spacer(modifier = Modifier.height(12.dp))

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

enum class SheetMode{
    DEFAULT, STYLE, UNDERLINE, SECTION, SKILLS, SOFT_SKILLS
}

enum class StyleType{
    ALPHA, BETA, DELTA, GAMMA, OMEGA
}

enum class Sections{
    OBJECTIVE, EXPERIENCE, EDUCATION, PROJECT, SKILLS, SOFT_SKILLS, CERTIFICATIONS, HOBBIES
}