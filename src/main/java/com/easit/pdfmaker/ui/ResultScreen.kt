package com.easit.pdfmaker.ui

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.easit.pdfmaker.LoadingDialog
import com.easit.pdfmaker.NavigationIcon
import com.easit.pdfmaker.R
import com.easit.pdfmaker.constants.Constant
import com.easit.pdfmaker.data.ListFormat
import com.easit.pdfmaker.deserializeAllResultData
import com.easit.pdfmaker.fileToByteArray
import com.easit.pdfmaker.data.PdfMakerSettingsReplica
import com.easit.pdfmaker.data.PdfUiData
import com.easit.pdfmaker.data.ReloadState
import com.easit.pdfmaker.data.Sections
import com.easit.pdfmaker.data.SheetMode
import com.easit.pdfmaker.data.StyleType
import com.easit.pdfmaker.data.ThemeColor
import com.easit.pdfmaker.launchPdf
import com.easit.pdfmaker.models.CoverLetterMaker
import com.easit.pdfmaker.models.ResumeMaker
import com.easit.pdfmaker.rememberReviewTask
import com.easit.pdfmaker.savePdfToExternalStorage
import com.easit.pdfmaker.utils.ColorSelector
import com.easit.pdfmaker.utils.DefaultKeys
import com.easit.pdfmaker.utils.ListFormatSheet
import com.easit.pdfmaker.utils.ResultFixItem
import com.easit.pdfmaker.utils.ResultSelectionColumn
import com.easit.pdfmaker.utils.SelectSections
import com.easit.pdfmaker.utils.SelectStyle
import com.google.android.play.core.review.ReviewManagerFactory
import com.rajat.pdfviewer.compose.PdfRendererViewCompose
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    launchUiData: PdfUiData?,
    isOnlyResume: Boolean,
    isOnlyCoverLetter: Boolean,
    tagId: String,
    resultString: String,
    settings: PdfMakerSettingsReplica,
    onReturn: () -> Unit,
    onEditResume: (PdfUiData) -> Unit,
    onEditCoverLetter: (PdfUiData) -> Unit,
    onAnalyticsItemClicked: (Int) -> Unit
) {

    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val context = LocalContext.current
    val resultViewModel = viewModel<ResultViewModel>()

    BackHandler {
        onReturn()
    }

    val reviewManager = remember { ReviewManagerFactory.create(context) }
    val reviewInfo = rememberReviewTask(reviewManager)

    val screenHeight = LocalConfiguration.current.screenHeightDp
    var savedPdfPath by remember { mutableStateOf("") }
    var resumeItemPath by remember { mutableStateOf("") }
    var coverLetterItemPath by remember { mutableStateOf("") }
    var hasFilesBeenRetrieved by remember { mutableStateOf(false) }

    var outputResumeFile: File? by remember { mutableStateOf(null) }
    var outputCoverLetterFile: File? by remember { mutableStateOf(null) }

    var sectionList by remember { mutableStateOf(listOf(Sections.OBJECTIVE, Sections.EXPERIENCE, Sections.EDUCATION, Sections.SKILLS, Sections.SOFT_SKILLS, Sections.PROJECT, Sections.CERTIFICATIONS, Sections.HOBBIES)) }

    //
    var skillFormatType by remember { mutableStateOf(ListFormat.FLOW_ROW) }
    var softSkillFormatType by remember { mutableStateOf(ListFormat.FLOW_ROW) }
    var hobbiesFormatType by remember { mutableStateOf(ListFormat.TRIPLE_COLUMN) }
    var showUnderline by remember { mutableStateOf(true) }
    var isUpperCaseNameResume by remember { mutableStateOf(true) }
    var progressDialogVisible by remember { mutableStateOf(false) }
    var showDownloadDialog by remember { mutableStateOf(false) }

    var buttonHeightDp by remember { mutableStateOf(30.dp) }
    var hasReloaded by remember { mutableIntStateOf(0) }
    //var hasReloadedCoverLetter by remember { mutableIntStateOf(0) }

    when (resultViewModel.delayCompleted.collectAsState().value){
        ReloadState.COMPLETED -> {
            progressDialogVisible = false
            hasReloaded += 1
            resultViewModel.resetDelay()
        }
        ReloadState.ACTIVE -> { /**/ }
        ReloadState.INITIAL -> { /**/ }
    }

    //  Cover letter
    var selectedStyleCoverLetter by remember { mutableStateOf(StyleType.ALPHA) }
    var selectedThemeColorCoverLetter by remember { mutableStateOf(ThemeColor.BLACK) }
    var isUpperCaseNameCoverLetter by remember { mutableStateOf(true) }

    //  Resume
    var selectedStyleResume by remember { mutableStateOf(StyleType.ALPHA) }
    var selectedThemeColorResume by remember { mutableStateOf(ThemeColor.BLACK) }
    var selectedLinkColorResume by remember { mutableStateOf(ThemeColor.BLUE) }

    LaunchedEffect(key1 = 0) {
        if (launchUiData != null){
            showUnderline = launchUiData.isUnderlinedR
            isUpperCaseNameResume = launchUiData.isUppercaseNameR
            selectedThemeColorResume = launchUiData.themeColorR
            selectedLinkColorResume = launchUiData.linkColorR
            selectedStyleResume = launchUiData.styleTypeR
            sectionList = launchUiData.sectionR
            skillFormatType = launchUiData.skillsListFormatR
            softSkillFormatType = launchUiData.softSkillsListFormatR
            hobbiesFormatType = launchUiData.hobbiesListFormatR


            selectedThemeColorCoverLetter = launchUiData.themeColorCL
            isUpperCaseNameCoverLetter = launchUiData.isUppercaseNameCL
            selectedStyleCoverLetter = launchUiData.styleTypeCL
        }
    }

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
                        skillFormatType = skillFormatType,
                        softSkillFormatType = softSkillFormatType,
                        hobbiesFormatType = hobbiesFormatType,
                        showUnderline = showUnderline,
                        uppercaseName = isUpperCaseNameResume,
                        themeColor = selectedThemeColorResume,
                        linkColor = selectedLinkColorResume,
                        styleType = selectedStyleResume,
                        sectionList = sectionList,
                        onPdfCreated = { hasFilesBeenRetrieved = true }
                    )
                //
                CoverLetterMaker(coverLetterItemPath)
                    .createCoverLetter(
                        item = resultViewModel.resultData.value.coverLetter!!,
                        themeColor = selectedThemeColorCoverLetter,
                        styleType = selectedStyleCoverLetter,
                        uppercaseName = isUpperCaseNameCoverLetter,
                        onPdfCreated = { hasFilesBeenRetrieved = true }
                    )
                //Toast.makeText(context, "--Success--", Toast.LENGTH_SHORT).show()
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
    var sheetMode by remember { mutableStateOf(SheetMode.DEFAULT) }
    var isShowingResume by remember { mutableStateOf(true) }
    var showEditSection by remember { mutableStateOf(false) }
    var hasShownEditSection by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = 0) { if (isOnlyCoverLetter) isShowingResume =false }


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
                    key (isShowingResume){
                        DefaultKeys(
                            isShowingResume = isShowingResume,
                            isOnlyCoverLetter = isOnlyCoverLetter,
                            isOnlyResume = isOnlyResume,
                            keywords = resultViewModel.resultData.collectAsState().value.keywords,
                            sendSheetPeekHeight = { buttonHeightDp = it },
                            sendIsShowingResume = {
                                isShowingResume = it
                                onAnalyticsItemClicked(0)
                            },
                            onEditResume = {
                                onEditResume(
                                    PdfUiData(
                                        isUnderlinedR = showUnderline,
                                        isUppercaseNameR = isUpperCaseNameResume,
                                        themeColorR = selectedThemeColorResume,
                                        linkColorR = selectedLinkColorResume,
                                        styleTypeR = selectedStyleResume,
                                        sectionR = sectionList,
                                        skillsListFormatR = skillFormatType,
                                        softSkillsListFormatR = softSkillFormatType,
                                        hobbiesListFormatR = hobbiesFormatType,

                                        //
                                        isUppercaseNameCL = isUpperCaseNameCoverLetter,
                                        themeColorCL = selectedThemeColorCoverLetter,
                                        styleTypeCL = selectedStyleCoverLetter,
                                    )
                                )
                                onAnalyticsItemClicked(1)
                            },
                            onEditCoverLetter = {
                                onEditCoverLetter(
                                    PdfUiData(
                                        isUnderlinedR = showUnderline,
                                        isUppercaseNameR = isUpperCaseNameResume,
                                        themeColorR = selectedThemeColorResume,
                                        linkColorR = selectedLinkColorResume,
                                        styleTypeR = selectedStyleResume,
                                        sectionR = sectionList,
                                        skillsListFormatR = skillFormatType,
                                        softSkillsListFormatR = softSkillFormatType,
                                        hobbiesListFormatR = hobbiesFormatType,

                                        //
                                        isUppercaseNameCL = isUpperCaseNameCoverLetter,
                                        themeColorCL = selectedThemeColorCoverLetter,
                                        styleTypeCL = selectedStyleCoverLetter,
                                    )
                                )
                                onAnalyticsItemClicked(1)
                            },
                            onDownloadClicked = {
                                reviewInfo?.let {
                                    reviewManager.launchReviewFlow(context as Activity, reviewInfo)
                                }
                                val activity = (context as? Activity)
                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        != PackageManager.PERMISSION_GRANTED) {
                                        savedPdfPath = Constant.FAILED_PERMISSION
                                        if (activity != null) {
                                            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 9191)
                                        }
                                        showDownloadDialog = true
                                        //Toast.makeText(context, "Please tap the Download button again after granting the required permissions.", Toast.LENGTH_LONG).show()
                                    }else{
                                        if (isOnlyCoverLetter){
                                            if (outputCoverLetterFile != null)  savePdfToExternalStorage(context, fileToByteArray(outputCoverLetterFile!!)!!, "COV-$tagId") { value ->
                                                savedPdfPath = value
                                                showDownloadDialog = true
                                            }
                                        }else if (isOnlyResume){
                                            if (outputResumeFile != null)  savePdfToExternalStorage(context, fileToByteArray(outputResumeFile!!)!!, "RES-$tagId") { value ->
                                                savedPdfPath = value
                                                showDownloadDialog = true
                                            }
                                        }else{
                                            if (isShowingResume){
                                                if (outputResumeFile != null)  savePdfToExternalStorage(context, fileToByteArray(outputResumeFile!!)!!, "RES-$tagId") { value ->
                                                    savedPdfPath = value
                                                    showDownloadDialog = true
                                                }
                                            }else{
                                                if (outputCoverLetterFile != null)  savePdfToExternalStorage(context, fileToByteArray(outputCoverLetterFile!!)!!, "COV-$tagId") { value ->
                                                    savedPdfPath = value
                                                    showDownloadDialog = true
                                                }
                                            }
                                        }
                                    }
                                }else{
                                    if (isOnlyCoverLetter){
                                        if (outputCoverLetterFile != null)  savePdfToExternalStorage(context, fileToByteArray(outputCoverLetterFile!!)!!, "COV-$tagId") { value ->
                                            savedPdfPath = value
                                            showDownloadDialog = true
                                        }
                                    }else if (isOnlyResume){
                                        if (outputResumeFile != null)  savePdfToExternalStorage(context, fileToByteArray(outputResumeFile!!)!!, "RES-$tagId") { value ->
                                            savedPdfPath = value
                                            showDownloadDialog = true
                                        }
                                    }else{
                                        if (isShowingResume){
                                            if (outputResumeFile != null)  savePdfToExternalStorage(context, fileToByteArray(outputResumeFile!!)!!, "RES-$tagId") { value ->
                                                savedPdfPath = value
                                                showDownloadDialog = true
                                            }
                                        }else{
                                            if (outputCoverLetterFile != null)  savePdfToExternalStorage(context, fileToByteArray(outputCoverLetterFile!!)!!, "COV-$tagId") { value ->
                                                savedPdfPath = value
                                                showDownloadDialog = true
                                            }
                                        }
                                    }
                                }
                                onAnalyticsItemClicked(2)
                            },
                            onFillBottomSheet = {
                                coroutineScope.launch {
                                    if (it){
                                        scaffoldState.bottomSheetState.expand()
                                    }else{
                                        scaffoldState.bottomSheetState.partialExpand()
                                    }
                                }
                                onAnalyticsItemClicked(3)
                            }
                        )
                    }
                }
                SheetMode.STYLE -> {
                    SelectStyle(
                        isCoverLetter = !isShowingResume,
                        defaultSelectedStyle = if (isShowingResume) selectedStyleResume else selectedStyleCoverLetter,
                        onDismiss = {
                            /**/
                            sheetMode = SheetMode.DEFAULT
                            coroutineScope.launch {
                                scaffoldState.bottomSheetState.partialExpand()
                            }
                            showEditSection = true
                        },
                        onItemSelected = {
                            if (isShowingResume){
                                selectedStyleResume = it
                            }else {
                                selectedStyleCoverLetter = it
                                //Toast.makeText(context, it.name, Toast.LENGTH_LONG).show()
                            }

                            sheetMode = SheetMode.DEFAULT
                            coroutineScope.launch {
                                scaffoldState.bottomSheetState.partialExpand()
                            }
                            showEditSection = false
                            progressDialogVisible = true

                            if (isShowingResume){
                                ResumeMaker(resumeItemPath)
                                    .createResume(
                                        item = resultViewModel.resultData.value.resume!!,
                                        skillFormatType = skillFormatType,
                                        softSkillFormatType = softSkillFormatType,
                                        hobbiesFormatType = hobbiesFormatType,
                                        showUnderline = showUnderline,
                                        uppercaseName = isUpperCaseNameResume,
                                        themeColor = selectedThemeColorResume,
                                        linkColor = selectedLinkColorResume,
                                        styleType = selectedStyleResume,
                                        sectionList = sectionList,
                                        onPdfCreated = {
                                            resultViewModel.delayTimer(5000)
                                        }
                                    )
                            }else{
                                CoverLetterMaker(coverLetterItemPath)
                                    .createCoverLetter(
                                        item = resultViewModel.resultData.value.coverLetter!!,
                                        themeColor = selectedThemeColorCoverLetter,
                                        styleType = selectedStyleCoverLetter,
                                        uppercaseName = isUpperCaseNameCoverLetter,
                                        onPdfCreated = { resultViewModel.delayTimer(5000) }
                                    )
                            }
                        }
                    )
                }
                SheetMode.SECTION -> {
                    SelectSections(
                        defaultSectionList = sectionList,
                        onConfirm = {
                            sectionList = it
                            sheetMode = SheetMode.DEFAULT
                            coroutineScope.launch {
                                scaffoldState.bottomSheetState.partialExpand()
                            }
                            showEditSection = false
                            progressDialogVisible = true
                            ResumeMaker(resumeItemPath)
                                .createResume(
                                    item = resultViewModel.resultData.value.resume!!,
                                    skillFormatType = skillFormatType,
                                    softSkillFormatType = softSkillFormatType,
                                    hobbiesFormatType = hobbiesFormatType,
                                    showUnderline = showUnderline,
                                    uppercaseName = isUpperCaseNameResume,
                                    themeColor = selectedThemeColorResume,
                                    linkColor = selectedLinkColorResume,
                                    styleType = selectedStyleResume,
                                    sectionList = sectionList,
                                    onPdfCreated = {
                                        //hasReloaded += hasReloaded
                                        resultViewModel.delayTimer(5000)
                                    }
                                )
                        },
                        onDismiss = {
                            sheetMode = SheetMode.DEFAULT
                            coroutineScope.launch {
                                scaffoldState.bottomSheetState.partialExpand()
                            }
                            showEditSection = true
                        }
                    )
                }
                SheetMode.COLOR -> {
                    ColorSelector(
                        defaultSelectedColor = if (isShowingResume) selectedThemeColorResume else selectedThemeColorCoverLetter,
                        onColorClick = {
                            if (isShowingResume){
                                selectedThemeColorResume = it
                            }else selectedThemeColorCoverLetter = it

                            sheetMode = SheetMode.DEFAULT
                            coroutineScope.launch {
                                scaffoldState.bottomSheetState.partialExpand()
                            }
                            showEditSection = false
                            progressDialogVisible = true

                            if (isShowingResume){
                                ResumeMaker(resumeItemPath)
                                    .createResume(
                                        item = resultViewModel.resultData.value.resume!!,
                                        skillFormatType = skillFormatType,
                                        softSkillFormatType = softSkillFormatType,
                                        hobbiesFormatType = hobbiesFormatType,
                                        showUnderline = showUnderline,
                                        uppercaseName = isUpperCaseNameResume,
                                        themeColor = selectedThemeColorResume,
                                        linkColor = selectedLinkColorResume,
                                        styleType = selectedStyleResume,
                                        sectionList = sectionList,
                                        onPdfCreated = {
                                            resultViewModel.delayTimer(5000)
                                        }
                                    )
                            }else{
                                CoverLetterMaker(coverLetterItemPath)
                                    .createCoverLetter(
                                        item = resultViewModel.resultData.value.coverLetter!!,
                                        themeColor = selectedThemeColorCoverLetter,
                                        styleType = selectedStyleCoverLetter,
                                        uppercaseName = isUpperCaseNameCoverLetter,
                                        onPdfCreated = { resultViewModel.delayTimer(5000) }
                                    )
                            }
                        },
                        onDismiss = {
                            sheetMode = SheetMode.DEFAULT
                            coroutineScope.launch {
                                scaffoldState.bottomSheetState.partialExpand()
                            }
                            showEditSection = true
                        }
                    )
                }
                SheetMode.LINK_COLOR -> {
                    ColorSelector(
                        defaultSelectedColor = selectedLinkColorResume,
                        onColorClick = {
                            selectedLinkColorResume = it
                            sheetMode = SheetMode.DEFAULT
                            coroutineScope.launch {
                                scaffoldState.bottomSheetState.partialExpand()
                            }
                            showEditSection = false
                            progressDialogVisible = true
                            ResumeMaker(resumeItemPath)
                                .createResume(
                                    item = resultViewModel.resultData.value.resume!!,
                                    skillFormatType = skillFormatType,
                                    softSkillFormatType = softSkillFormatType,
                                    hobbiesFormatType = hobbiesFormatType,
                                    showUnderline = showUnderline,
                                    uppercaseName = isUpperCaseNameResume,
                                    themeColor = selectedThemeColorResume,
                                    linkColor = selectedLinkColorResume,
                                    styleType = selectedStyleResume,
                                    sectionList = sectionList,
                                    onPdfCreated = {
                                        //hasReloaded += hasReloaded
                                        resultViewModel.delayTimer(5000)
                                    }
                                )
                        },
                        onDismiss = {
                            sheetMode = SheetMode.DEFAULT
                            coroutineScope.launch {
                                scaffoldState.bottomSheetState.partialExpand()
                            }
                            showEditSection = true
                        }
                    )
                }
                SheetMode.SKILLS -> {
                    ListFormatSheet(
                        isInvalidListType = selectedStyleResume == StyleType.GAMMA || selectedStyleResume == StyleType.OMEGA,
                        defaultIsSelected = skillFormatType,
                        onCompleted = {
                            skillFormatType = it
                            sheetMode = SheetMode.DEFAULT
                            coroutineScope.launch {
                                scaffoldState.bottomSheetState.partialExpand()
                            }
                            showEditSection = false
                            progressDialogVisible = true
                            ResumeMaker(resumeItemPath)
                                .createResume(
                                    item = resultViewModel.resultData.value.resume!!,
                                    skillFormatType = skillFormatType,
                                    softSkillFormatType = softSkillFormatType,
                                    hobbiesFormatType = hobbiesFormatType,
                                    showUnderline = showUnderline,
                                    uppercaseName = isUpperCaseNameResume,
                                    themeColor = selectedThemeColorResume,
                                    linkColor = selectedLinkColorResume,
                                    styleType = selectedStyleResume,
                                    sectionList = sectionList,
                                    onPdfCreated = {
                                        resultViewModel.delayTimer(5000)
                                    }
                                )
                        },
                        onDismiss = {
                            sheetMode = SheetMode.DEFAULT
                            coroutineScope.launch {
                                scaffoldState.bottomSheetState.partialExpand()
                            }
                            showEditSection = true
                        }
                    )
                }
                SheetMode.SOFT_SKILLS -> {
                    ListFormatSheet(
                        isInvalidListType = selectedStyleResume == StyleType.GAMMA || selectedStyleResume == StyleType.OMEGA,
                        defaultIsSelected = skillFormatType,
                        onCompleted = {
                            softSkillFormatType = it
                            sheetMode = SheetMode.DEFAULT
                            coroutineScope.launch {
                                scaffoldState.bottomSheetState.partialExpand()
                            }
                            showEditSection = false
                            progressDialogVisible = true
                            ResumeMaker(resumeItemPath)
                                .createResume(
                                    item = resultViewModel.resultData.value.resume!!,
                                    skillFormatType = skillFormatType,
                                    softSkillFormatType = softSkillFormatType,
                                    hobbiesFormatType = hobbiesFormatType,
                                    showUnderline = showUnderline,
                                    uppercaseName = isUpperCaseNameResume,
                                    themeColor = selectedThemeColorResume,
                                    linkColor = selectedLinkColorResume,
                                    styleType = selectedStyleResume,
                                    sectionList = sectionList,
                                    onPdfCreated = {
                                        resultViewModel.delayTimer(5000)
                                    }
                                )
                        },
                        onDismiss = {
                            sheetMode = SheetMode.DEFAULT
                            coroutineScope.launch {
                                scaffoldState.bottomSheetState.partialExpand()
                            }
                            showEditSection = true
                        }
                    )
                }
                SheetMode.HOBBIES -> {
                    ListFormatSheet(
                        isInvalidListType = selectedStyleResume == StyleType.GAMMA || selectedStyleResume == StyleType.OMEGA,
                        defaultIsSelected = skillFormatType,
                        onCompleted = {
                            hobbiesFormatType = it
                            sheetMode = SheetMode.DEFAULT
                            coroutineScope.launch {
                                scaffoldState.bottomSheetState.partialExpand()
                            }
                            showEditSection = false
                            progressDialogVisible = true
                            ResumeMaker(resumeItemPath)
                                .createResume(
                                    item = resultViewModel.resultData.value.resume!!,
                                    skillFormatType = skillFormatType,
                                    softSkillFormatType = softSkillFormatType,
                                    hobbiesFormatType = hobbiesFormatType,
                                    showUnderline = showUnderline,
                                    uppercaseName = isUpperCaseNameResume,
                                    themeColor = selectedThemeColorResume,
                                    linkColor = selectedLinkColorResume,
                                    styleType = selectedStyleResume,
                                    sectionList = sectionList,
                                    onPdfCreated = {
                                        resultViewModel.delayTimer(5000)
                                    }
                                )
                        },
                        onDismiss = {
                            sheetMode = SheetMode.DEFAULT
                            coroutineScope.launch {
                                scaffoldState.bottomSheetState.partialExpand()
                            }
                            showEditSection = true
                        }
                    )
                }
            }
        },
    ) { innerPadding ->
        Row (modifier = Modifier.padding(bottom = (buttonHeightDp * 2) + 80.dp)/*(screenHeight/4).dp)*/){
            Box(
                modifier = Modifier
                    .weight(1f),
                contentAlignment = Alignment.TopEnd
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(
                            top = innerPadding.calculateTopPadding(),
                            bottom = 16.dp,
                            start = 8.dp,
                            end = 8.dp
                        )
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(36.dp))

                    //
                    if (hasFilesBeenRetrieved){
                        if (resumeItemPath.isNotBlank()){
                            if (isOnlyCoverLetter){
                                if (outputCoverLetterFile!!.exists()){
                                    Box(
                                        modifier = Modifier
                                            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                                            .padding(2.dp)
                                    ){
                                        key(hasReloaded){
                                            PdfRendererViewCompose(
                                                file = outputCoverLetterFile,
                                                lifecycleOwner = LocalLifecycleOwner.current
                                            )
                                        }
                                    }
                                }
                            }else if(isOnlyResume){
                                if (outputResumeFile!!.exists()){
                                    Box(
                                        modifier = Modifier
                                            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                                            .padding(2.dp)
                                    ){
                                        key(hasReloaded){
                                            PdfRendererViewCompose(
                                                file = outputResumeFile,
                                                lifecycleOwner = LocalLifecycleOwner.current
                                            )
                                        }
                                    }
                                }
                            }else{
                                if (isShowingResume){
                                    if (outputResumeFile!!.exists()){
                                        Box(
                                            modifier = Modifier
                                                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                                                .padding(2.dp)
                                        ){
                                            key(hasReloaded){
                                                PdfRendererViewCompose(
                                                    file = outputResumeFile,
                                                    lifecycleOwner = LocalLifecycleOwner.current
                                                )
                                            }
                                        }
                                    }
                                }else{
                                    if (outputCoverLetterFile!!.exists()){
                                        Box(
                                            modifier = Modifier
                                                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                                                .padding(2.dp)
                                        ){
                                            key(hasReloaded){
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
                }

                if (!showEditSection){
                    Box(
                        modifier = Modifier.padding(top = 36.dp, end = 8.dp)
                    ){
                        ResultFixItem(
                            buttonAlpha = Constant.VISIBILITY_ALPHA,
                            imageVector = Icons.Default.Visibility,
                            title = "Show Edit",
                            onAction = {
                                showEditSection = true
                                if (!hasShownEditSection){
                                    onAnalyticsItemClicked(4)
                                    hasShownEditSection = true
                                }
                            }
                        )
                    }
                }
            }


            AnimatedVisibility (showEditSection){
                Column (
                    modifier = Modifier
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(end = 8.dp)
                ){
                    Spacer(modifier = Modifier.height(36.dp))
                    ResultSelectionColumn(
                        isShowingCoverLetter = !isShowingResume,
                        onHideClicked = { showEditSection = false },
                        onStyleClicked = {
                            showEditSection = false
                            sheetMode = SheetMode.STYLE
                            coroutineScope.launch {
                                scaffoldState.bottomSheetState.expand()
                            }
                        },
                        onSectionsClicked = {
                            showEditSection = false
                            sheetMode = SheetMode.SECTION
                            coroutineScope.launch {
                                scaffoldState.bottomSheetState.expand()
                            }
                        },
                        onColorClicked = {
                            showEditSection = false
                            sheetMode = SheetMode.COLOR
                            coroutineScope.launch {
                                scaffoldState.bottomSheetState.expand()
                            }
                        },
                        onLinkColorClicked = {
                            showEditSection = false
                            sheetMode = SheetMode.LINK_COLOR
                            coroutineScope.launch {
                                scaffoldState.bottomSheetState.expand()
                            }
                        },
                        onUnderlineClicked = {
                            progressDialogVisible = true
                            showUnderline = !showUnderline
                            showEditSection = false
                            ResumeMaker(resumeItemPath)
                                .createResume(
                                    item = resultViewModel.resultData.value.resume!!,
                                    skillFormatType = skillFormatType,
                                    softSkillFormatType = softSkillFormatType,
                                    hobbiesFormatType = hobbiesFormatType,
                                    showUnderline = showUnderline,
                                    uppercaseName = isUpperCaseNameResume,
                                    themeColor = selectedThemeColorResume,
                                    linkColor = selectedLinkColorResume,
                                    styleType = selectedStyleResume,
                                    sectionList = sectionList,
                                    onPdfCreated = {
                                        //hasReloaded += hasReloaded
                                        resultViewModel.delayTimer(5000)
                                    }
                                )
                        },
                        onNameCaseClicked = {
                            progressDialogVisible = true
                            if (isShowingResume) isUpperCaseNameResume = !isUpperCaseNameResume else isUpperCaseNameCoverLetter = !isUpperCaseNameCoverLetter
                            showEditSection = false
                            if (isShowingResume){
                                ResumeMaker(resumeItemPath)
                                    .createResume(
                                        item = resultViewModel.resultData.value.resume!!,
                                        skillFormatType = skillFormatType,
                                        softSkillFormatType = softSkillFormatType,
                                        hobbiesFormatType = hobbiesFormatType,
                                        showUnderline = showUnderline,
                                        uppercaseName = isUpperCaseNameResume,
                                        themeColor = selectedThemeColorResume,
                                        linkColor = selectedLinkColorResume,
                                        styleType = selectedStyleResume,
                                        sectionList = sectionList,
                                        onPdfCreated = {
                                            resultViewModel.delayTimer(5000)
                                        }
                                    )
                            }else{
                                CoverLetterMaker(coverLetterItemPath)
                                    .createCoverLetter(
                                        item = resultViewModel.resultData.value.coverLetter!!,
                                        themeColor = selectedThemeColorCoverLetter,
                                        styleType = selectedStyleCoverLetter,
                                        uppercaseName = isUpperCaseNameCoverLetter,
                                        onPdfCreated = { resultViewModel.delayTimer(5000) }
                                    )
                            }
                        },
                        onSkillsClicked = {
                            showEditSection = false
                            sheetMode = SheetMode.SKILLS
                            coroutineScope.launch {
                                scaffoldState.bottomSheetState.expand()
                            }
                        },
                        onSoftSkillsClicked = {
                            showEditSection = false
                            sheetMode = SheetMode.SOFT_SKILLS
                            coroutineScope.launch {
                                scaffoldState.bottomSheetState.expand()
                            }
                        },
                        onHobbiesClicked = {
                            showEditSection = false
                            sheetMode = SheetMode.HOBBIES
                            coroutineScope.launch {
                                scaffoldState.bottomSheetState.expand()
                            }
                        },
                        extraSpacing = screenHeight/2
                    )
                }
            }
        }

        when {
            progressDialogVisible -> {
                LoadingDialog(
                    mainText = stringResource(id = R.string.loading),
                    onActionCancel = {  }
                )
            }

            showDownloadDialog -> {
                DownloadDialog(
                    path = savedPdfPath,
                    isSuccess = savedPdfPath != Constant.FAILED_DOWNLOAD && savedPdfPath != Constant.FAILED_PERMISSION,
                    onDismiss = {
                        showDownloadDialog = false
                        savedPdfPath = "---"
                    },
                    onConfirm = {
                        launchPdf(context, savedPdfPath)
                    }
                )
            }
        }
    }
}

@Composable
fun DownloadDialog(
    path: String,
    isSuccess: Boolean = true,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    var mainText by remember { mutableStateOf("PDF saved to: $path. Select 'Open' to view your PDF file") }

    if (path == Constant.FAILED_DOWNLOAD){
        mainText = "Failed to save PDF please try again later."
    }
    if (path == Constant.FAILED_PERMISSION){
        mainText = "Please tap the Download button again after granting the required permissions."
    }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Download PDF!") },
        text = { Text(mainText) },
        modifier = Modifier.padding(16.dp),
        confirmButton = {
            OutlinedButton(onClick = { onDismiss() }) {
                Text("Dismiss")
            }
        },
        dismissButton = {
            if (isSuccess){
                Button(onClick = { onConfirm() }) {
                    Text("Open")
                }
            }
        }
    )
}