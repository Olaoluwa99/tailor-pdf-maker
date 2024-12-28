package com.easit.pdfmaker.utils

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.TextSnippet
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FormatUnderlined
import androidx.compose.material.icons.filled.FreeBreakfast
import androidx.compose.material.icons.filled.InsertPageBreak
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.SevereCold
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ChipColors
import androidx.compose.material3.ElevatedSuggestionChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.easit.pdfmaker.R
import com.easit.pdfmaker.constants.Constant
import com.easit.pdfmaker.data.ListFormat
import com.easit.pdfmaker.data.Sections
import com.easit.pdfmaker.data.StyleType
import com.easit.pdfmaker.data.ThemeColor
import com.easit.pdfmaker.data.allSectionList
import com.easit.pdfmaker.data.colorList
import com.easit.pdfmaker.data.listTypeList
import com.easit.pdfmaker.data.resumeStyleList

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
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
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
 * SECTIONS
 * */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SelectSections(
    defaultSectionList: List<Sections>,
    onConfirm: (List<Sections>) -> Unit,
    onDismiss: () -> Unit
) {

    var reload by remember { mutableIntStateOf(0) }
    var currentSectionList by remember { mutableStateOf(mutableListOf<Sections>()) }

    LaunchedEffect(key1 = 0) { currentSectionList = defaultSectionList.toMutableList() }

    //
    Column {
        //
        Column {
            Spacer(modifier = Modifier.height(Constant.SHEET_HEADER_SPACING.dp))
            ConfirmDismissAction(
                onConfirm = { onConfirm(currentSectionList) },
                onDismiss = { onDismiss() }
            )
            Spacer(modifier = Modifier.height(18.dp))
        }

        //
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            key(reload) {
                LazyColumn (
                    modifier = Modifier.padding(16.dp),
                    //verticalArrangement = Arrangement.spacedBy(12.dp)
                ){
                    items(allSectionList.size){
                        key(reload) {
                            SectionItem(
                                title = allSectionList[it].tag,
                                defaultIsChecked = currentSectionList.contains(allSectionList[it].section),
                                onCheckedChange = {
                                    val item = allSectionList[it].section
                                    if (currentSectionList.contains(item)){
                                        currentSectionList.remove(item)
                                    }else {
                                        currentSectionList.add(item)
                                    }
                                    reload += reload
                                }
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(48.dp))
                    }
                }

                /*FlowRow(
                    modifier = Modifier
                        .safeDrawingPadding()
                        .fillMaxWidth(1f)
                        //.padding(16.dp)
                        .wrapContentHeight(align = Alignment.Top),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround,//spacedBy(8.dp),
                    maxItemsInEachRow = 2
                ) {
                    //
                    for (mainItem in allSectionList){
                        SectionItem(
                            title = mainItem.tag,
                            isChecked = currentSectionList.contains(mainItem.section),
                            onCheckedChange = {
                                val item = mainItem.section
                                if (currentSectionList.contains(item)){
                                    currentSectionList.remove(item)
                                    reload += reload
                                }else {
                                    currentSectionList.add(item)
                                    reload += reload
                                }
                            }
                        )
                    }
                }*/
            }
        }
    }
}

/**
 * STYLE
 * */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SelectStyle(
    onItemSelected: (StyleType) -> Unit,
    onDismiss: () -> Unit
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
            Spacer(modifier = Modifier.height(Constant.SHEET_HEADER_SPACING.dp))
            ConfirmDismissAction(
                onConfirm = { onItemSelected(selectedStyleType) },
                onDismiss = { onDismiss() }
            )
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
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ColorSelector(
    defaultSelectedColor: ThemeColor = ThemeColor.BLACK,
    onColorClick: (ThemeColor) -> Unit,
    onDismiss: () -> Unit
) {
    var reload by remember { mutableIntStateOf(0) }
    var currentSelectedColor by remember { mutableStateOf(ThemeColor.BLACK) }

    LaunchedEffect(key1 = 0) { currentSelectedColor = defaultSelectedColor }

    //
    Column {
        //
        Column {
            Spacer(modifier = Modifier.height(Constant.SHEET_HEADER_SPACING.dp))
            ConfirmDismissAction(
                onConfirm = { onColorClick(currentSelectedColor) },
                onDismiss = { onDismiss() }
            )
            Spacer(modifier = Modifier.height(18.dp))
        }

        //
        key (reload) {
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
                for (colorData in colorList){
                    ColorItem(
                        data = colorData,
                        isSelected = colorData.mainColor == currentSelectedColor,
                        onClick = {
                            currentSelectedColor = colorData.mainColor
                            reload += reload
                        }
                    )
                }
            }
        }
    }
}

/**
 * LIST FORMATTING
 * */
@Composable
fun ListFormatSheet(
    defaultIsSelected: ListFormat,
    onCompleted: (ListFormat)-> Unit,
    onDismiss: ()-> Unit
) {
    //
    var isSelected by remember { mutableStateOf(ListFormat.FLOW_ROW) }
    var reload by remember { mutableIntStateOf(0) }
    LaunchedEffect(key1 = 0) { isSelected = defaultIsSelected }


    //
    Column{
        Column {
            Spacer(modifier = Modifier.height(Constant.SHEET_HEADER_SPACING.dp))
            ConfirmDismissAction(
                onConfirm = { onCompleted(isSelected) },
                onDismiss = { onDismiss() }
            )
            Spacer(modifier = Modifier.height(18.dp))
        }

        //
        key(reload) {
            LazyColumn (
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ){
                items(listTypeList.size){
                    ListTypeItem(
                        type = listTypeList[it],
                        isSelected = isSelected == listTypeList[it].tag,
                        onClick = {
                            isSelected = listTypeList[it].tag
                            reload += reload
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(48.dp))
                }
            }
        }
    }
}

/**
 * RESULT COLUMN
 * */
@Composable
fun ResultSelectionColumn(
    onHideClicked: ()-> Unit,
    onStyleClicked: ()-> Unit,
    onColorClicked: ()-> Unit,
    onLinkColorClicked: ()-> Unit,
    onUnderlineClicked: ()-> Unit,
    onSkillsClicked: ()-> Unit,
    onSoftSkillsClicked: ()-> Unit,
    onHobbiesClicked: ()-> Unit,
    onSectionsClicked: ()-> Unit,
){
    val spacing by remember { mutableStateOf(12.dp) }
    //
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //
        ResultFixItem(
            imageVector = Icons.Default.VisibilityOff,
            "Hide panel",
            onAction = {onHideClicked()}
        )
        Spacer(modifier = Modifier.height(spacing))

        ResultFixItem(
            imageVector = Icons.Default.InsertPageBreak,
            "Sections",
            onAction = {onSectionsClicked()}
        )
        Spacer(modifier = Modifier.height(spacing))

        ResultFixItem(
            imageVector = Icons.Default.AutoFixHigh,
            "Style",
            onAction = {onStyleClicked()}
        )
        Spacer(modifier = Modifier.height(spacing))

        ResultFixItem(
            imageVector = Icons.Default.Palette,
            "Color",
            onAction = {onColorClicked()}
        )
        Spacer(modifier = Modifier.height(spacing))

        ResultFixItem(
            imageVector = Icons.Default.Palette,
            "Link Color",
            onAction = {onLinkColorClicked()}
        )
        Spacer(modifier = Modifier.height(spacing))

        ResultFixItem(
            imageVector = Icons.Default.FormatUnderlined,
            "Underline",
            onAction = {onUnderlineClicked()}
        )
        Spacer(modifier = Modifier.height(spacing))

        ResultFixItem(
            imageVector = Icons.Default.SevereCold,
            "Skills",
            onAction = {onSkillsClicked()}
        )
        Spacer(modifier = Modifier.height(spacing))

        ResultFixItem(
            imageVector = Icons.Default.SevereCold,
            "Soft skills",
            onAction = {onSoftSkillsClicked()}
        )
        Spacer(modifier = Modifier.height(spacing))

        ResultFixItem(
            imageVector = Icons.Default.FreeBreakfast,
            "Hobbies",
            onAction = {onHobbiesClicked()}
        )
    }
}

/**
 * CONFIRM/ DISMISS ACTION
 * */
@Composable
fun ConfirmDismissAction(
    onConfirm: ()-> Unit,
    onDismiss: ()-> Unit
) {
    Row (
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            onClick = {
                onConfirm()
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
        Spacer(modifier = Modifier.width(16.dp))

        OutlinedButton(
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp),
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            onClick = {
                onDismiss()
            }
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Dismiss",
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "Dismiss", color = MaterialTheme.colorScheme.primary)
        }
    }
}