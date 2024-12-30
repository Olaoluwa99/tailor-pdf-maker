package com.easit.pdfmaker.data

import androidx.compose.ui.graphics.Color
import com.easit.pdfmaker.R

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

data class ResumeStyle(
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
    ResumeStyle("alphaStyle", "Modern", R.drawable.resume_template1),
    ResumeStyle("betaStyle", "Real", R.drawable.resume_template2),
    ResumeStyle("deltaStyle", "Fine", R.drawable.resume_template3),
    ResumeStyle("gammaStyle", "Dual 1", R.drawable.resume_template4),
    ResumeStyle("omegaStyle", "Dual 2", R.drawable.resume_template5),
)

val colorList = listOf(
    ColorData(name = "Black", tagColor = Color.Black, mainColor = ThemeColor.BLACK),
    ColorData(name = "Red", tagColor = Color.Red, mainColor = ThemeColor.RED),
    ColorData(name = "Green", tagColor = Color.Green, mainColor = ThemeColor.GREEN),
    ColorData(name = "Blue", tagColor = Color.Blue, mainColor = ThemeColor.BLUE),
    ColorData(name = "Yellow", tagColor = Color.Yellow, mainColor = ThemeColor.YELLOW),
    ColorData(name = "Light-gray", tagColor = Color.LightGray, mainColor = ThemeColor.LIGHT_GRAY),
    ColorData(name = "Dark-gray", tagColor = Color.DarkGray, mainColor = ThemeColor.DARK_GRAY)
)