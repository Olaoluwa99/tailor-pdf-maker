package com.easit.pdfmaker.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import com.easit.pdfmaker.data.ColorData
import com.easit.pdfmaker.data.ListTypeData

@Composable
fun ColorItem(
    data: ColorData,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box (
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(if (isSelected) data.tagColor else MaterialTheme.colorScheme.surfaceContainerHigh)
            .clickable { onClick() }
            .padding(2.dp)
    ){
        Row (
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(50))
                    .background(data.tagColor)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Box{
                Text(text = data.name)
                Text(text = "                     ")
            }
        }
    }
}

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
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh),
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
fun ListTypeItem(
    type: ListTypeData,
    isSelected: Boolean,
    onClick: ()-> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(2.dp)
            .clickable { onClick() }
    ){
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.background)
                .clickable { onClick() }
                .padding(8.dp)
        ) {
            Text(text = type.title, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                    .padding(2.dp)
            ){
                //
                Image(
                    painter = painterResource(id = type.image),
                    contentDescription = "Image tag",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onClick() }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
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

@Composable
fun ResultFixItem(
    imageVector: ImageVector,
    title: String,
    onAction: ()-> Unit
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .clickable { onAction() }
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ){
            Icon(
                imageVector = imageVector,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            style = LocalTextStyle.current.merge(TextStyle(lineHeight = 1.2.em)),
            //modifier = Modifier.fillMaxWidth(),
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            overflow = TextOverflow.Ellipsis
        )
        //Text(text = title, fontWeight = FontWeight.Light, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
fun SectionItem(
    title: String,
    defaultIsChecked: Boolean,
    onCheckedChange: ()-> Unit
) {
    //var isChecked by remember { mutableStateOf(false) }
    //LaunchedEffect(key1 = 0) { isChecked = defaultIsChecked }
    var reload by remember { mutableIntStateOf(0) }
    //LaunchedEffect(key1 = Unit) { reload += reload }

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                //isChecked = !isChecked
                onCheckedChange()
                reload += reload
            }
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        key(reload) {
            Checkbox(
                checked = defaultIsChecked,
                onCheckedChange = {
                    //isChecked = !isChecked
                    onCheckedChange()
                    reload += reload
                },
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = title, fontWeight = FontWeight.Bold)
    }
}

