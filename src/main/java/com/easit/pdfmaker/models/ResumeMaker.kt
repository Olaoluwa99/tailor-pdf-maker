package com.easit.pdfmaker.models

import android.util.Log
import com.easit.pdfmaker.data.ListFormat
import com.easit.pdfmaker.data.ResumeData
import com.easit.pdfmaker.data.Sections
import com.easit.pdfmaker.data.StyleType
import com.easit.pdfmaker.data.ThemeColor
import com.lowagie.text.Document
import com.lowagie.text.DocumentException
import com.lowagie.text.Element
import com.lowagie.text.FontFactory
import com.lowagie.text.PageSize
import com.lowagie.text.Paragraph
import com.lowagie.text.pdf.PdfPCell
import com.lowagie.text.pdf.PdfPTable
import com.lowagie.text.pdf.PdfWriter
import java.awt.Color
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

class ResumeMaker(private val path: String){
    private var toShowUnderline = false
    private var isUppercaseName = true
    private var mainThemeColor = Color.BLACK
    private var mainLinkColor = Color.BLUE
    private var skillsStyle = ListFormat.FLOW_ROW
    private var softSkillsStyle = ListFormat.FLOW_ROW
    private var hobbiesStyle = ListFormat.DOUBLE_COLUMN
    private var mainSectionList: List<Sections> = emptyList()
    //
    fun createResume(
        item: ResumeData,
        skillFormatType: ListFormat,
        softSkillFormatType: ListFormat,
        hobbiesFormatType: ListFormat,
        showUnderline: Boolean,
        uppercaseName: Boolean,
        themeColor: ThemeColor,
        linkColor: ThemeColor,
        styleType: StyleType,
        sectionList: List<Sections>,
        onPdfCreated: () -> Unit
    ){
        toShowUnderline = showUnderline
        isUppercaseName = uppercaseName
        mainSectionList = sectionList

        mainThemeColor = when (themeColor){
            ThemeColor.RED -> Color.RED
            ThemeColor.GREEN -> Color.GREEN
            ThemeColor.BLACK -> Color.BLACK
            ThemeColor.BLUE -> Color.BLUE
            ThemeColor.YELLOW -> Color.YELLOW
            ThemeColor.DARK_GRAY -> Color.DARK_GRAY
            ThemeColor.LIGHT_GRAY -> Color.LIGHT_GRAY
        }

        mainLinkColor = when (linkColor){
            ThemeColor.RED -> Color.RED
            ThemeColor.GREEN -> Color.GREEN
            ThemeColor.BLACK -> Color.BLACK
            ThemeColor.BLUE -> Color.BLUE
            ThemeColor.YELLOW -> Color.YELLOW
            ThemeColor.DARK_GRAY -> Color.DARK_GRAY
            ThemeColor.LIGHT_GRAY -> Color.LIGHT_GRAY
        }

        skillsStyle = skillFormatType
        softSkillsStyle = softSkillFormatType
        hobbiesStyle = hobbiesFormatType

        when (styleType){
            StyleType.ALPHA -> createTypeAlpha(item, onPdfCreated)
            StyleType.BETA -> createTypeBeta()
            StyleType.DELTA -> createTypeDelta()
            StyleType.GAMMA -> createTypeGamma(item, onPdfCreated)
            StyleType.OMEGA -> createTypeOmega()
        }
    }

    private fun createTypeAlpha(item: ResumeData, onPdfCreated: () -> Unit){
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
                item.link1, item.link2, false, isUppercaseName, mainLinkColor
            )
            contactItem.setAlignment("Center")
            document.add(contactItem)

            //OBJECTIVE
            if (mainSectionList.contains(Sections.OBJECTIVE)){
                if (item.objective != null) {
                    if (toShowUnderline){
                        val objectiveHeader =  createHeaderWithHorizontalLine("OBJECTIVE", mainThemeColor)
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
                        val experienceHeader =  createHeaderWithHorizontalLine("EXPERIENCE", mainThemeColor)
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
                        val skillsHeader =  createHeaderWithHorizontalLine("TECHNICAL SKILLS", mainThemeColor)
                        skillsHeader.spacingBefore = headerSpacingBefore
                        document.add(skillsHeader)
                    }else {
                        val skillsHeader = createHeader("TECHNICAL SKILLS")
                        skillsHeader.spacingBefore = headerSpacingBefore
                        document.add(skillsHeader)
                    }

                    /*var isLong = false
                    for (i in item.skillsList) {
                        isLong = i.length > 24
                    }*/

                    when (skillsStyle){
                        ListFormat.FLOW_ROW -> document.add(createCombinedParagraphSection(item.skillsList))
                        ListFormat.SINGLE_COLUMN -> document.add(createSingleColumnSection(item.skillsList))
                        ListFormat.DOUBLE_COLUMN -> document.add(createDualColumnSection(item.skillsList))
                        ListFormat.TRIPLE_COLUMN -> document.add(createTripleColumnSection(item.skillsList))
                    }
                }
            }

            //SOFT SKILLS
            if (mainSectionList.contains(Sections.SOFT_SKILLS)){
                if (item.softSkillsList != null) {
                    if (toShowUnderline){
                        val softSkillsHeader =  createHeaderWithHorizontalLine("SOFT SKILLS", mainThemeColor)
                        softSkillsHeader.spacingBefore = headerSpacingBefore
                        document.add(softSkillsHeader)
                    }else {
                        val softSkillsHeader = createHeader("SOFT SKILLS")
                        softSkillsHeader.spacingBefore = headerSpacingBefore
                        document.add(softSkillsHeader)
                    }
                    when (softSkillsStyle){
                        ListFormat.FLOW_ROW -> document.add(createCombinedParagraphSection(item.softSkillsList))
                        ListFormat.SINGLE_COLUMN -> document.add(createSingleColumnSection(item.softSkillsList))
                        ListFormat.DOUBLE_COLUMN -> document.add(createDualColumnSection(item.softSkillsList))
                        ListFormat.TRIPLE_COLUMN -> document.add(createTripleColumnSection(item.softSkillsList))
                    }
                    //document.add(createDualColumnSection(item.softSkillsList))
                }
            }

            //EDUCATION
            if (mainSectionList.contains(Sections.EDUCATION)){
                if (item.educationList != null) {
                    if (toShowUnderline){
                        val educationHeader =  createHeaderWithHorizontalLine("EDUCATION", mainThemeColor)
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
                        val projectsHeader =  createHeaderWithHorizontalLine("PROJECTS", mainThemeColor)
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
                        val certificationsHeader =  createHeaderWithHorizontalLine("CERTIFICATIONS", mainThemeColor)
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
                        val hobbiesHeader =  createHeaderWithHorizontalLine("HOBBIES", mainThemeColor)
                        hobbiesHeader.spacingBefore = headerSpacingBefore
                        document.add(hobbiesHeader)
                    }else {
                        val hobbiesHeader = createHeader("HOBBIES")
                        hobbiesHeader.spacingBefore = headerSpacingBefore
                        document.add(hobbiesHeader)
                    }
                    //
                    //document.add(createDualColumnSection(item.hobbiesList))
                    when (hobbiesStyle){
                        ListFormat.FLOW_ROW -> document.add(createCombinedParagraphSection(item.hobbiesList))
                        ListFormat.SINGLE_COLUMN -> document.add(createSingleColumnSection(item.hobbiesList))
                        ListFormat.DOUBLE_COLUMN -> document.add(createDualColumnSection(item.hobbiesList))
                        ListFormat.TRIPLE_COLUMN -> document.add(createTripleColumnSection(item.hobbiesList))
                    }
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

        //Callback
        onPdfCreated()
    }

    private fun createTypeBeta(){
        /**/
    }

    private fun createTypeDelta(){
        /**/
    }

    private fun createTypeGamma(item: ResumeData, onPdfCreated: () -> Unit){
        val document = Document(PageSize.A4)
        val multiItemSpacing = 10f //5f;
        val headerSpacingAfter = 7.5f
        val headerSpacingAfterExperience = 7.5f
        val headerSpacingBefore = 10f

        try {
            PdfWriter.getInstance(document, Files.newOutputStream(Paths.get(path)))
            document.open()

            //HEADER
            document.add(createContactDetailsSectionSplit(
                iName = item.name, iJobRole = item.role, iPhone = item.phone, iEmail = item.email,
                iLocation = item.location, iLink1 = item.link1 ?: "", iLink2 = item.link2 ?: "",
                linkColor = mainLinkColor, isUpperCase = isUppercaseName
            ))

            //OBJECTIVE
            if (mainSectionList.contains(Sections.OBJECTIVE)){
                if (item.objective != null) {
                    if (toShowUnderline){
                        val objectiveHeader =  createHeaderWithHorizontalLine("OBJECTIVE", mainThemeColor)
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
            //


            //TODO - TABLE START
            val mainDualListTable = PdfPTable(3)
            mainDualListTable.setHeaderRows(0)
            mainDualListTable.widthPercentage = 100F
            mainDualListTable.setWidths(intArrayOf(35, 5, 60))

            //
            mainDualListTable.setSpacingBefore(headerSpacingBefore)

            // Allow the table to split across multiple pages
            mainDualListTable.isSplitLate = false
            mainDualListTable.isSplitRows = true


            //
            val mainDualCell1 = PdfPCell()
            
            //

            //TODO - ADD ZONE - 1
            //mainDualCell1.addElement(section1);

            //EDUCATION
            if (mainSectionList.contains(Sections.EDUCATION)){
                if (item.educationList != null) {
                    if (toShowUnderline){
                        val educationHeader =  createHeaderWithHorizontalLine("EDUCATION", mainThemeColor)
                        educationHeader.spacingBefore = headerSpacingBefore
                        //document.add(educationHeader)
                        mainDualCell1.addElement(educationHeader)
                    }else {
                        val educationHeader = createHeader("EDUCATION")
                        //educationHeader.spacingBefore = headerSpacingBefore
                        mainDualCell1.addElement(educationHeader)
                    }

                    //
                    item.educationList.forEachIndexed { index, educationItem ->
                        val educationText = createEducationSectionSplit(educationItem)
                        if (index != item.educationList.size - 1) educationText.spacingAfter = multiItemSpacing
                        //document.add(educationText)
                        mainDualCell1.addElement(educationText)
                    }
                }
            }



            //TECHNICAL SKILLS
            if (mainSectionList.contains(Sections.SKILLS)){
                if (item.skillsList.isNotEmpty()) {
                    //val skillsHeader = createHeader("TECHNICAL SKILLS")
                    if (toShowUnderline){
                        val skillsHeader =  createHeaderWithHorizontalLine("TECHNICAL SKILLS", mainThemeColor)
                        skillsHeader.spacingBefore = headerSpacingBefore
                        //document.add(skillsHeader)
                        mainDualCell1.addElement(skillsHeader)
                    }else {
                        val skillsHeader = createHeader("TECHNICAL SKILLS")
                        skillsHeader.spacingBefore = headerSpacingBefore
                        //document.add(skillsHeader)
                        mainDualCell1.addElement(skillsHeader)
                    }

                    when (skillsStyle){
                        ListFormat.FLOW_ROW -> mainDualCell1.addElement(createCombinedParagraphSection(item.skillsList))
                        ListFormat.SINGLE_COLUMN -> mainDualCell1.addElement(createSingleColumnSectionSplit(item.skillsList))
                        ListFormat.DOUBLE_COLUMN -> mainDualCell1.addElement(createSingleColumnSectionSplit(item.skillsList))
                        ListFormat.TRIPLE_COLUMN -> mainDualCell1.addElement(createSingleColumnSectionSplit(item.skillsList))
                    }
                }
            }


            //SOFT SKILLS
            if (mainSectionList.contains(Sections.SOFT_SKILLS)){
                if (item.softSkillsList != null) {
                    if (toShowUnderline){
                        val softSkillsHeader =  createHeaderWithHorizontalLine("SOFT SKILLS", mainThemeColor)
                        softSkillsHeader.spacingBefore = headerSpacingBefore
                        //document.add(softSkillsHeader)
                        mainDualCell1.addElement(softSkillsHeader)
                    }else {
                        val softSkillsHeader = createHeader("SOFT SKILLS")
                        softSkillsHeader.spacingBefore = headerSpacingBefore
                        //document.add(softSkillsHeader)
                        mainDualCell1.addElement(softSkillsHeader)
                    }
                    when (softSkillsStyle){
                        ListFormat.FLOW_ROW -> mainDualCell1.addElement(createCombinedParagraphSection(item.softSkillsList))
                        ListFormat.SINGLE_COLUMN -> mainDualCell1.addElement(createSingleColumnSectionSplit(item.softSkillsList))
                        ListFormat.DOUBLE_COLUMN -> mainDualCell1.addElement(createSingleColumnSectionSplit(item.softSkillsList))
                        ListFormat.TRIPLE_COLUMN -> mainDualCell1.addElement(createSingleColumnSectionSplit(item.softSkillsList))
                    }
                    //document.add(createDualColumnSection(item.softSkillsList))
                }
            }


            //HOBBIES
            if (mainSectionList.contains(Sections.HOBBIES)){
                if (item.hobbiesList != null) {
                    if (toShowUnderline){
                        val hobbiesHeader =  createHeaderWithHorizontalLine("HOBBIES", mainThemeColor)
                        hobbiesHeader.spacingBefore = headerSpacingBefore
                        //document.add(hobbiesHeader)
                        mainDualCell1.addElement(hobbiesHeader)
                    }else {
                        val hobbiesHeader = createHeader("HOBBIES")
                        hobbiesHeader.spacingBefore = headerSpacingBefore
                        //document.add(hobbiesHeader)
                        mainDualCell1.addElement(hobbiesHeader)
                    }
                    //
                    //document.add(createDualColumnSection(item.hobbiesList))
                    when (hobbiesStyle){
                        ListFormat.FLOW_ROW -> mainDualCell1.addElement(createCombinedParagraphSection(item.hobbiesList))
                        ListFormat.SINGLE_COLUMN -> mainDualCell1.addElement(createSingleColumnSectionSplit(item.hobbiesList))
                        ListFormat.DOUBLE_COLUMN -> mainDualCell1.addElement(createSingleColumnSectionSplit(item.hobbiesList))
                        ListFormat.TRIPLE_COLUMN -> mainDualCell1.addElement(createSingleColumnSectionSplit(item.hobbiesList))
                    }
                }
            }


            //SET 2
            //
            val mainDualCell2 = PdfPCell()

            //EXPERIENCE
            if (mainSectionList.contains(Sections.EXPERIENCE)){
                if (item.experienceList != null) {
                    if (toShowUnderline){
                        val experienceHeader =  createHeaderWithHorizontalLine("EXPERIENCE", mainThemeColor)
                        experienceHeader.spacingBefore = headerSpacingBefore
                        //document.add(experienceHeader)
                        mainDualCell2.addElement(experienceHeader)
                    }else {
                        val experienceHeader = createHeader("EXPERIENCE")
                        experienceHeader.spacingBefore = headerSpacingBefore
                        //document.add(experienceHeader)
                        mainDualCell2.addElement(experienceHeader)
                    }
                    for(experienceItem in item.experienceList){
                        val experienceSection = createExperienceSectionSplit(experienceItem)
                        experienceSection.alignment = Element.ALIGN_JUSTIFIED
                        /*val timesNewRomanBold = FontFactory.getFont(FontFactory.TIMES_BOLD, 12f)
                        val timesNewRomanPlain = FontFactory.getFont(FontFactory.TIMES, 12f)
                        val timesNewRomanItalics = FontFactory.getFont(FontFactory.TIMES_ITALIC, 12f)

                        //
                        val role = Paragraph(experienceItem.experienceRole, timesNewRomanBold)
                        val workDate = Paragraph(experienceItem.experienceWorkDate, timesNewRomanPlain)


                        //SET 3
                        val boldLocation = Chunk(experienceItem.experienceCompanyName + " |", timesNewRomanBold)
                        val regularLocation = Chunk(" " + experienceItem.experienceCompanyLocation, timesNewRomanItalics)
                        val locationText = Paragraph()
                        locationText.add(boldLocation)
                        locationText.add(regularLocation)


                        //SET 4
                        val bulletList = Paragraph("", timesNewRomanPlain)
                        for (nItem in experienceItem.experienceItemsList) {
                            val listItem = Chunk("•  $nItem\n", timesNewRomanPlain)
                            bulletList.add(listItem)
                        }
                        bulletList.alignment = Element.ALIGN_JUSTIFIED

                        //CLOSE
                        val section = Paragraph()
                        section.add(role)
                        section.add(workDate)
                        section.add(locationText)
                        section.add(bulletList)*/

                        mainDualCell2.addElement(experienceSection)
                    }
                }
            }




            //TODO - CLOSING
            mainDualCell1.border = PdfPCell.NO_BORDER
            mainDualCell1.setPadding(0f)
            mainDualListTable.addCell(mainDualCell1)


            //
            val mainDualCellBlank = PdfPCell()
            mainDualCellBlank.border = PdfPCell.NO_BORDER
            mainDualCellBlank.setPadding(0f)
            mainDualListTable.addCell(mainDualCellBlank)


            //
            mainDualCell2.border = PdfPCell.NO_BORDER
            mainDualCell2.setPadding(0f)
            mainDualCell2.horizontalAlignment = Element.ALIGN_JUSTIFIED
            mainDualListTable.addCell(mainDualCell2)


            //mainDualListTable.addCell(mainDualCell2);
            //
            document.add(mainDualListTable)







            //EXPERIENCE
            /*if (mainSectionList.contains(Sections.EXPERIENCE)){
                if (item.experienceList != null) {
                    if (toShowUnderline){
                        val experienceHeader =  createHeaderWithHorizontalLine("EXPERIENCE", mainThemeColor)
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
                        val skillsHeader =  createHeaderWithHorizontalLine("TECHNICAL SKILLS", mainThemeColor)
                        skillsHeader.spacingBefore = headerSpacingBefore
                        document.add(skillsHeader)
                    }else {
                        val skillsHeader = createHeader("TECHNICAL SKILLS")
                        skillsHeader.spacingBefore = headerSpacingBefore
                        document.add(skillsHeader)
                    }

                    when (skillsStyle){
                        ListFormat.FLOW_ROW -> document.add(createCombinedParagraphSection(item.skillsList))
                        ListFormat.SINGLE_COLUMN -> document.add(createSingleColumnSection(item.skillsList))
                        ListFormat.DOUBLE_COLUMN -> document.add(createDualColumnSection(item.skillsList))
                        ListFormat.TRIPLE_COLUMN -> document.add(createTripleColumnSection(item.skillsList))
                    }
                }
            }

            //SOFT SKILLS
            if (mainSectionList.contains(Sections.SOFT_SKILLS)){
                if (item.softSkillsList != null) {
                    if (toShowUnderline){
                        val softSkillsHeader =  createHeaderWithHorizontalLine("SOFT SKILLS", mainThemeColor)
                        softSkillsHeader.spacingBefore = headerSpacingBefore
                        document.add(softSkillsHeader)
                    }else {
                        val softSkillsHeader = createHeader("SOFT SKILLS")
                        softSkillsHeader.spacingBefore = headerSpacingBefore
                        document.add(softSkillsHeader)
                    }
                    when (softSkillsStyle){
                        ListFormat.FLOW_ROW -> document.add(createCombinedParagraphSection(item.softSkillsList))
                        ListFormat.SINGLE_COLUMN -> document.add(createSingleColumnSection(item.softSkillsList))
                        ListFormat.DOUBLE_COLUMN -> document.add(createDualColumnSection(item.softSkillsList))
                        ListFormat.TRIPLE_COLUMN -> document.add(createTripleColumnSection(item.softSkillsList))
                    }
                    //document.add(createDualColumnSection(item.softSkillsList))
                }
            }

            //EDUCATION
            if (mainSectionList.contains(Sections.EDUCATION)){
                if (item.educationList != null) {
                    if (toShowUnderline){
                        val educationHeader =  createHeaderWithHorizontalLine("EDUCATION", mainThemeColor)
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
                        val projectsHeader =  createHeaderWithHorizontalLine("PROJECTS", mainThemeColor)
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
                        val certificationsHeader =  createHeaderWithHorizontalLine("CERTIFICATIONS", mainThemeColor)
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
                        val hobbiesHeader =  createHeaderWithHorizontalLine("HOBBIES", mainThemeColor)
                        hobbiesHeader.spacingBefore = headerSpacingBefore
                        document.add(hobbiesHeader)
                    }else {
                        val hobbiesHeader = createHeader("HOBBIES")
                        hobbiesHeader.spacingBefore = headerSpacingBefore
                        document.add(hobbiesHeader)
                    }
                    //
                    //document.add(createDualColumnSection(item.hobbiesList))
                    when (hobbiesStyle){
                        ListFormat.FLOW_ROW -> document.add(createCombinedParagraphSection(item.hobbiesList))
                        ListFormat.SINGLE_COLUMN -> document.add(createSingleColumnSection(item.hobbiesList))
                        ListFormat.DOUBLE_COLUMN -> document.add(createDualColumnSection(item.hobbiesList))
                        ListFormat.TRIPLE_COLUMN -> document.add(createTripleColumnSection(item.hobbiesList))
                    }
                }
            }*/
        } catch (de: DocumentException) {
            println(de.message)
            Log.e("Error - Doc", de.message ?: "")
        } catch (de: IOException) {
            println(de.message)
            Log.e("Error - IO", de.message ?: "")
        }
        document.close()

        //Callback
        onPdfCreated()
    }

    private fun createTypeOmega(){
        /**/
    }
}