package com.easit.pdfmaker.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.Button
import androidx.compose.material3.ChipColors
import androidx.compose.material3.ElevatedSuggestionChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import com.easit.pdfmaker.R
import com.easit.pdfmaker.constants.Constant
import com.easit.pdfmaker.fileToByteArray
import com.easit.pdfmaker.savePdfToExternalStorage
import com.easit.pdfmaker.ui.StyleType
import kotlinx.coroutines.launch

//TODO - VARIOUS SHEETS FOR RESULT EDITING

/**
 * SHOW UNDERLINE AUTO FROM RESULT SCREEN
 * */

/**
 * DEFAULT BUTTON KEYS
 * */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DefaultKeys(
    keywords: List<String>,
    sendSheetPeekHeight: (Dp) -> Unit,
    sendIsShowingResume: (Boolean) -> Unit,
    onFillBottomSheet: (Boolean) -> Unit,
    onEditResume: () -> Unit,
    onEditCoverLetter: () -> Unit,
    onDownloadClicked: () -> Unit
) {

    var isShowingResume by remember { mutableStateOf(true) }
    val localDensity = LocalDensity.current
    var buttonHeightDp by remember { mutableStateOf(0.dp) }
    val iconSize by remember { mutableIntStateOf(16) }
    var actionButtonText by remember { mutableStateOf("Resume") }
    var nonActionButtonText by remember { mutableStateOf("Cover letter") }
    var keywordAction by remember { mutableStateOf("View") }
    var isBottomSheetFull by remember { mutableStateOf(false) }
    var showKeywordsSection by remember { mutableStateOf(false) }

    //
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
                        sendIsShowingResume(false)
                        actionButtonText = "Cover letter"
                        nonActionButtonText = "Resume"
                    }else{
                        isShowingResume = true
                        sendIsShowingResume(true)
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
                onClick = { onDownloadClicked() }
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
                        sendSheetPeekHeight(buttonHeightDp)
                    },
            shape = RoundedCornerShape(50),
            contentPadding = PaddingValues(vertical = 16.dp),
            onClick = {
                //showKeywordsSection = !showKeywordsSection
                //TODO
                if (!isBottomSheetFull){
                    showKeywordsSection = true
                    onFillBottomSheet(true)
                    //scaffoldState.bottomSheetState.expand()
                    keywordAction = "Hide"
                    isBottomSheetFull = true
                }else {
                    onFillBottomSheet(false)
                    //scaffoldState.bottomSheetState.partialExpand()
                    showKeywordsSection = false
                    keywordAction = "View"
                    isBottomSheetFull = false
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
        if (showKeywordsSection) {
            Column(
                modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "KEYWORDS (${keywords.size})",
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
                    for(item in keywords){
                        ElevatedSuggestionChip(
                            label = {
                                Text(
                                    text = item,
                                    fontSize = MaterialTheme.typography.bodySmall.fontSize
                                )
                            },
                            onClick = {  },
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
}

/**
 * STYLE
 * */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SelectStyle(
    onItemSelected: (StyleType) -> Unit
) {

    var reload by remember { mutableIntStateOf(0) }
    var selectedIndex by remember { mutableIntStateOf(0) }
    var showStyleFullScreen by remember { mutableStateOf(false) }
    var selectedStyleType by remember { mutableStateOf(StyleType.ALPHA) }
    var selectedExpandStyleImageId by remember { mutableIntStateOf(R.drawable.resume_template1) }
    
    //
    Column {
        //
        Column {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp),
                onClick = {
                    onItemSelected(selectedStyleType)
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "Done",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.background
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Completed", color = MaterialTheme.colorScheme.background)
            }
            Spacer(modifier = Modifier.height(18.dp))
        }


        //
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            key (reload){
                FlowRow(
                    modifier = Modifier
                        .safeDrawingPadding()
                        .fillMaxWidth(1f)
                        //.padding(16.dp)
                        .wrapContentHeight(align = Alignment.Top),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround,//spacedBy(8.dp),
                    maxItemsInEachRow = 2
                ) {
                    resumeStyleList.forEachIndexed { index, resumeStyle ->
                        ImageItem(
                            imageId = resumeStyle.image,
                            typeName = resumeStyle.title,
                            isSelected = selectedIndex == index,
                            onClick = {
                                selectedIndex = index
                                when (selectedIndex){
                                    1 -> {
                                        selectedStyleType = StyleType.ALPHA
                                        reload += 1
                                    }
                                    2 -> {
                                        selectedStyleType = StyleType.BETA
                                        reload += 1
                                    }
                                    3 -> {
                                        selectedStyleType = StyleType.DELTA
                                        reload += 1
                                    }
                                    4 -> {
                                        selectedStyleType = StyleType.GAMMA
                                        reload += 1
                                    }
                                    5 -> {
                                        selectedStyleType = StyleType.OMEGA
                                        reload += 1
                                    }
                                }
                            },
                            onFillScreen = {
                                selectedExpandStyleImageId = resumeStyle.image
                                showStyleFullScreen = true
                            }
                        )
                    }
                }
            }
        }
        
        when {
            showStyleFullScreen -> {
                StyleImageFullScreen(
                    imageId = selectedExpandStyleImageId,
                    onDismiss = { showStyleFullScreen = false }
                )
            }
        }
    }
}

/**
 * COLOR
 * */

/**
 * SKILLS FORMATTING
 * */

/**
 * SOFT SKILLS FORMATTING
 * */

@Composable
fun ImageItem(
    imageId: Int,
    typeName: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    onFillScreen: () -> Unit
) {
    val selectedColor = MaterialTheme.colorScheme.primary
    val unSelectedColor = MaterialTheme.colorScheme.background
    var color by remember { mutableStateOf(unSelectedColor) }
    when {
        isSelected -> color = selectedColor
    }
    Box(
        modifier = Modifier
            .fillMaxWidth(0.45f)
            .background(color)
            .padding(2.dp)
            .clickable {
                //color = selectedColor
                onClick()
            },
        contentAlignment = Alignment.BottomEnd
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.BottomEnd
            ) {
                Image(
                    painter = painterResource(id = imageId),
                    contentDescription = "Image tag",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onClick() }
                )

                //Floating Box
                Box(
                    modifier = Modifier.padding(8.dp)
                ){
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(MaterialTheme.colorScheme.primary/*Constant.BROWN*/)
                            .clickable { onFillScreen() }
                            .padding(4.dp)
                    ){
                        Icon(
                            imageVector = Icons.Filled.Fullscreen,
                            contentDescription = "Fill screen",
                            tint = MaterialTheme.colorScheme.background/*Color.White*/
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surfaceContainerHigh),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = typeName, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun StyleImageFullScreen(
    imageId: Int,
    onDismiss: ()-> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false),
        content = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp)
                    .zIndex(10F),
                contentAlignment = Alignment.BottomEnd
            ) {
                Image(
                    painter = painterResource(id = imageId),
                    contentDescription = "Resume Image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxWidth()
                )

                //Floating Box
                Box(
                    modifier = Modifier.padding(12.dp)
                ){
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(MaterialTheme.colorScheme.primary/*Constant.BROWN*/)
                            .clickable { onDismiss() }
                            .padding(4.dp)
                    ){
                        Icon(
                            imageVector = Icons.Filled.FullscreenExit,
                            contentDescription = "Exit full screen",
                            tint = MaterialTheme.colorScheme.background/*Color.White*/
                        )
                    }
                }
            }
        },
    )
}

data class ResumeStyle(
    val id: Int,
    val tag: String,
    val title: String,
    val image: Int
)

val resumeStyleList = listOf(
    ResumeStyle(Constant.STYLE_1, "alphaStyle", "Modern", R.drawable.resume_template1),
    ResumeStyle(Constant.STYLE_2, "betaStyle", "Real", R.drawable.resume_template2),
    ResumeStyle(Constant.STYLE_3, "deltaStyle", "Fine", R.drawable.resume_template3),
    ResumeStyle(Constant.STYLE_4, "gammaStyle", "Dual 1", R.drawable.resume_template4),
    ResumeStyle(Constant.STYLE_5, "omegaStyle", "Dual 2", R.drawable.resume_template5),
)

//@Previ
@Composable
private fun Matters() {
    var type by remember { mutableStateOf(StyleType.ALPHA) }
    MaterialTheme {
        Column {
            SelectStyle { type = it }
        }
    }
}