package com.easit.pdfmaker.data

import androidx.compose.ui.graphics.Color
import com.easit.pdfmaker.R
import com.easit.pdfmaker.constants.Constant
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class PdfUiData(
    //Resume
    val isUnderlinedR: Boolean,
    val isUppercaseNameR: Boolean,
    val themeColorR: ThemeColor,
    val linkColorR: ThemeColor,
    val styleTypeR: StyleType,
    val sectionR: List<Sections>,
    val skillsListFormatR: ListFormat,
    val softSkillsListFormatR: ListFormat,
    val hobbiesListFormatR: ListFormat,

    //Cover letter
    val isUppercaseNameCL: Boolean,
    val themeColorCL: ThemeColor,
    val styleTypeCL: StyleType,
)

fun deserializePdfUiData(text: String): PdfUiData {
    val data: PdfUiData = Json.decodeFromString(text.trim())
    return data
}

fun serializePdfUiData(data: PdfUiData): String{
    val serializedString: String = Json.encodeToString(data)
    return serializedString
}

data class ItemStyle(
    val tag: String,
    val title: String,
    val image: Int
)

data class ListTypeData(
    val title: String,
    val tag: ListFormat,
    val image: Int
)

data class ColorData(
    val name: String,
    val tagColor: Color,
    val mainColor: ThemeColor
)

data class SectionData(
    val tag: String,
    val section: Sections
)

val allSectionList = listOf(
    SectionData("Objective", Sections.OBJECTIVE),
    SectionData("Experience", Sections.EXPERIENCE),
    SectionData("Education", Sections.EDUCATION),
    SectionData("Projects", Sections.PROJECT),
    SectionData("Skills", Sections.SKILLS),
    SectionData("Soft skills", Sections.SOFT_SKILLS),
    SectionData("Certification", Sections.CERTIFICATIONS),
    SectionData("Hobbies", Sections.HOBBIES),
)

val listTypeList = listOf(
    ListTypeData("Flow Row", ListFormat.FLOW_ROW, R.drawable.list_type1),
    ListTypeData("Single Column", ListFormat.SINGLE_COLUMN, R.drawable.list_type2),
    ListTypeData("Double Column", ListFormat.DOUBLE_COLUMN, R.drawable.list_type3),
    ListTypeData("Triple Column", ListFormat.TRIPLE_COLUMN, R.drawable.list_type4),
)

val resumeStyleList = listOf(
    ItemStyle("alphaStyle", "Real", R.drawable.resume_template1),
    ItemStyle("betaStyle", "Modern", R.drawable.resume_template2),
    ItemStyle("deltaStyle", "Fine", R.drawable.resume_template3),
    ItemStyle("gammaStyle", "Dual 1", R.drawable.resume_template4),
    ItemStyle("omegaStyle", "Dual 2", R.drawable.resume_template5)
)

val coverLetterStyleList = listOf(
    ItemStyle("alphaStyle", "Not-Real", R.drawable.resume_template1),
    ItemStyle("betaStyle", "Not-Modern", R.drawable.resume_template2),
    ItemStyle("deltaStyle", "Not-Fine", R.drawable.resume_template3),
    ItemStyle("gammaStyle", "Not-Dual 1", R.drawable.resume_template4),
    ItemStyle("omegaStyle", "Not-Dual 2", R.drawable.resume_template5)
)

val d = Color.Red
val colorList = listOf(
    ColorData(name = "Black", tagColor = Color.Black, mainColor = ThemeColor.BLACK),
    ColorData(name = "Red", tagColor = awtColorToComposeColor(Constant.RED), mainColor = ThemeColor.RED),
    ColorData(name = "Green", tagColor = awtColorToComposeColor(Constant.GREEN), mainColor = ThemeColor.GREEN),
    ColorData(name = "Blue", tagColor = Color.Blue, mainColor = ThemeColor.BLUE),
    ColorData(name = "Yellow", tagColor = awtColorToComposeColor(Constant.YELLOW), mainColor = ThemeColor.YELLOW),
    ColorData(name = "Light-gray", tagColor = Color.LightGray, mainColor = ThemeColor.LIGHT_GRAY),
    ColorData(name = "Dark-gray", tagColor = Color.DarkGray, mainColor = ThemeColor.DARK_GRAY)
)

fun awtColorToComposeColor(awtColor: java.awt.Color): Color {
    return Color(
        red = awtColor.red / 255f,
        green = awtColor.green / 255f,
        blue = awtColor.blue / 255f,
        alpha = awtColor.alpha / 255f
    )
}