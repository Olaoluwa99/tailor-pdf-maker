package com.easit.pdfmaker.kotlinModels.makers

import android.util.Log
import com.easit.pdfmaker.kotlinModels.ResumeData
import com.easit.pdfmaker.ui.Sections
import com.easit.pdfmaker.ui.StyleType
import com.easit.pdfmaker.ui.ThemeColor
import com.lowagie.text.Document
import com.lowagie.text.DocumentException
import com.lowagie.text.Element
import com.lowagie.text.PageSize
import com.lowagie.text.pdf.PdfWriter
import java.awt.Color
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Objects

class ResumeMaker(private val path: String){
    private var toShowUnderline = false
    private var color = Color.BLACK
    private var mainSectionList: List<Sections> = emptyList()
    //
    fun createResume(item: ResumeData, skillFormatType: String, softSkillFormatType: String, showUnderline: Boolean, themeColor: ThemeColor, styleType: StyleType, sectionList: List<Sections>){
        toShowUnderline = showUnderline
        mainSectionList = sectionList

        color = when (themeColor){
            ThemeColor.RED -> Color.RED
            ThemeColor.GREEN -> Color.GREEN
            ThemeColor.BLACK -> Color.BLACK
            ThemeColor.BLUE -> Color.BLUE
            ThemeColor.YELLOW -> Color.YELLOW
            ThemeColor.DARK_GRAY -> Color.DARK_GRAY
            ThemeColor.LIGHT_GRAY -> Color.LIGHT_GRAY
        }

        when (styleType){
            StyleType.ALPHA -> createTypeAlpha(item)
            StyleType.BETA -> createTypeBeta()
            StyleType.DELTA -> createTypeDelta()
            StyleType.GAMMA -> createTypeGamma()
            StyleType.OMEGA -> createTypeOmega()
        }
    }

    private fun createTypeAlpha(item: ResumeData){
        val document = Document(PageSize.A4)
        val multiItemSpacing = 10f //5f;
        val headerSpacingAfter = 7.5f
        val headerSpacingAfterExperience = 7.5f
        val headerSpacingBefore = 10f

        try {
            PdfWriter.getInstance(document, Files.newOutputStream(Paths.get(path)))
            document.open()

            /*Paragraph mains = new Paragraph("This is the Header");
            document.add(mains);*/

            //HEADER
            val contactItem = createContactDetailsSection(
                item.name, item.role, item.phone,
                item.email, item.location,
                item.linkCover1, item.linkCover2,
                item.link1, item.link2, false
            )
            contactItem.setAlignment("Center")
            document.add(contactItem)

            //OBJECTIVE
            if (mainSectionList.contains(Sections.OBJECTIVE)){
                if (item.objective != null) {
                    if (toShowUnderline){
                        val objectiveHeader =  createHeaderWithHorizontalLine("OBJECTIVE", color)
                        objectiveHeader.spacingBefore = headerSpacingBefore
                        document.add(objectiveHeader)
                    }else {
                        val objectiveHeader = createHeader("OBJECTIVE")
                        objectiveHeader.spacingBefore = headerSpacingBefore
                        document.add(objectiveHeader)
                    }

                    //
                    val objectiveItem = createObjectiveSection(item.objective)
                    objectiveItem.alignment = Element.ALIGN_JUSTIFIED
                    document.add(objectiveItem)
                }
            }

            //EXPERIENCE
            if (mainSectionList.contains(Sections.EXPERIENCE)){
                if (item.experienceList != null) {
                    if (toShowUnderline){
                        val experienceHeader =  createHeaderWithHorizontalLine("EXPERIENCE", color)
                        experienceHeader.spacingBefore = headerSpacingBefore
                        document.add(experienceHeader)
                    }else {
                        val experienceHeader = createHeader("EXPERIENCE")
                        experienceHeader.spacingBefore = headerSpacingBefore
                        document.add(experienceHeader)
                    }
                    item.experienceList.forEachIndexed { index, experienceItem ->
                        val experienceText = createExperienceSection(experienceItem)
                        if (index != item.experienceList.size - 1) experienceText.spacingAfter = multiItemSpacing
                        document.add(experienceText)
                    }
                }
            }

            //TECHNICAL SKILLS
            if (mainSectionList.contains(Sections.SKILLS)){
                if (item.skillsList.isNotEmpty()) {
                    //val skillsHeader = createHeader("TECHNICAL SKILLS")
                    if (toShowUnderline){
                        val skillsHeader =  createHeaderWithHorizontalLine("TECHNICAL SKILLS", color)
                        skillsHeader.spacingBefore = headerSpacingBefore
                        document.add(skillsHeader)
                    }else {
                        val skillsHeader = createHeader("TECHNICAL SKILLS")
                        skillsHeader.spacingBefore = headerSpacingBefore
                        document.add(skillsHeader)
                    }

                    var isLong = false
                    for (i in item.skillsList) {
                        isLong = i.length > 24
                    }
                    if (isLong) {
                        document.add(createSingleColumnSection(item.skillsList))
                    } else {
                        document.add(createDualColumnSection(item.skillsList))
                    }
                }
            }

            //SOFT SKILLS
            if (mainSectionList.contains(Sections.SOFT_SKILLS)){
                if (item.softSkillsList != null) {
                    if (toShowUnderline){
                        val softSkillsHeader =  createHeaderWithHorizontalLine("SOFT SKILLS", color)
                        softSkillsHeader.spacingBefore = headerSpacingBefore
                        document.add(softSkillsHeader)
                    }else {
                        val softSkillsHeader = createHeader("SOFT SKILLS")
                        softSkillsHeader.spacingBefore = headerSpacingBefore
                        document.add(softSkillsHeader)
                    }
                    document.add(createDualColumnSection(item.softSkillsList))
                }
            }

            //EDUCATION
            if (mainSectionList.contains(Sections.EDUCATION)){
                if (item.educationList != null) {
                    if (toShowUnderline){
                        val educationHeader =  createHeaderWithHorizontalLine("EDUCATION", color)
                        educationHeader.spacingBefore = headerSpacingBefore
                        document.add(educationHeader)
                    }else {
                        val educationHeader = createHeader("EDUCATION")
                        educationHeader.spacingBefore = headerSpacingBefore
                        document.add(educationHeader)
                    }

                    //
                    item.educationList.forEachIndexed { index, educationItem ->
                        val educationText = createEducationSection(educationItem)
                        if (index != item.educationList.size - 1) educationText.spacingAfter = multiItemSpacing
                        document.add(educationText)
                    }
                }
            }

            //PROJECT
            if (mainSectionList.contains(Sections.PROJECT)){
                if (item.projectList != null) {
                    if (toShowUnderline){
                        val projectsHeader =  createHeaderWithHorizontalLine("PROJECTS", color)
                        projectsHeader.spacingBefore = headerSpacingBefore
                        document.add(projectsHeader)
                    }else {
                        val projectsHeader = createHeader("PROJECTS")
                        projectsHeader.spacingBefore = headerSpacingBefore
                        document.add(projectsHeader)
                    }
                    //
                    item.projectList.forEachIndexed { index, projectItem ->
                        val projectSectionItem = createProjectsSection(projectItem)
                        projectSectionItem.alignment = Element.ALIGN_JUSTIFIED
                        if (index != item.projectList.size - 1) projectSectionItem.spacingAfter = multiItemSpacing
                        document.add(projectSectionItem)
                    }
                }
            }

            //CERTIFICATIONS
            if (mainSectionList.contains(Sections.CERTIFICATIONS)){
                if (item.certificationList != null) {
                    if (toShowUnderline){
                        val certificationsHeader =  createHeaderWithHorizontalLine("CERTIFICATIONS", color)
                        certificationsHeader.spacingBefore = headerSpacingBefore
                        document.add(certificationsHeader)
                    }else {
                        val certificationsHeader = createHeader("CERTIFICATIONS")
                        certificationsHeader.spacingBefore = headerSpacingBefore
                        document.add(certificationsHeader)
                    }
                    //
                    document.add(createSingleColumnSection(item.certificationList))
                }
            }

            //HOBBIES
            if (mainSectionList.contains(Sections.HOBBIES)){
                if (item.hobbiesList != null) {
                    if (toShowUnderline){
                        val hobbiesHeader =  createHeaderWithHorizontalLine("HOBBIES", color)
                        hobbiesHeader.spacingBefore = headerSpacingBefore
                        document.add(hobbiesHeader)
                    }else {
                        val hobbiesHeader = createHeader("HOBBIES")
                        hobbiesHeader.spacingBefore = headerSpacingBefore
                        document.add(hobbiesHeader)
                    }
                    //
                    document.add(createDualColumnSection(item.hobbiesList))
                }
            }
        } catch (de: DocumentException) {
            println(de.message)
            Log.e("Error - Doc", de.message ?: "")
        } catch (de: IOException) {
            println(de.message)
            Log.e("Error - IO", de.message ?: "")
        }
        document.close()
    }

    private fun createTypeBeta(){
        /**/
    }

    private fun createTypeDelta(){
        /**/
    }

    private fun createTypeGamma(){
        /**/
    }

    private fun createTypeOmega(){
        /**/
    }
}