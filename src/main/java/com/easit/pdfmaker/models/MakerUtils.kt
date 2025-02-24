package com.easit.pdfmaker.models

import com.easit.pdfmaker.data.EducationItem
import com.easit.pdfmaker.data.ExperienceItem
import com.easit.pdfmaker.data.ProjectItem
import com.lowagie.text.Anchor
import com.lowagie.text.Chunk
import com.lowagie.text.Document
import com.lowagie.text.Element
import com.lowagie.text.Font
import com.lowagie.text.FontFactory
import com.lowagie.text.List
import com.lowagie.text.ListItem
import com.lowagie.text.PageSize
import com.lowagie.text.Paragraph
import com.lowagie.text.Phrase
import com.lowagie.text.pdf.PdfContentByte
import com.lowagie.text.pdf.PdfPCell
import com.lowagie.text.pdf.PdfPTable
import com.lowagie.text.pdf.PdfWriter
import com.lowagie.text.pdf.draw.LineSeparator
import java.awt.Color

fun createContactDetailsSection(
    iName: String,
    iJobRole: String,
    iPhone: String, iEmail: String,
    iLocation: String,
    iLinkCover1: String?, iLinkCover2: String?,
    iLink1: String?, iLink2: String?,
    isSplitLink: Boolean,
    isUpperCase: Boolean,
    linkColor: Color
): Paragraph {
    val timesNewRomanName = FontFactory.getFont(FontFactory.TIMES_BOLD, 24f)
    val timesNewRomanRole = FontFactory.getFont(FontFactory.TIMES, 16f)
    val timesNewRomanPlain = FontFactory.getFont(FontFactory.TIMES, 12f)
    val linkFont = FontFactory.getFont(
        FontFactory.TIMES,
        12f,
        Font.UNDERLINE,
        linkColor
    ) // Blue color for hyperlink

    //
    val emailLink = Anchor(iEmail, linkFont)
    emailLink.reference = "mailto:$iEmail"
    //START
    //Name
    /*val processedName: String = if (isUpperCase) iName.uppercase() else iName
    val name = Paragraph(processedName, timesNewRomanName)
    name.setAlignment("Center")
    name.spacingAfter = 65f*/
    //Role
    val role = Paragraph(iJobRole, timesNewRomanRole)
    role.setAlignment("Center")
    role.spacingAfter = 2.5f
    //Contact
    val contact = Paragraph()
    contact.add(Chunk("$iPhone | ", timesNewRomanPlain))
    contact.add(emailLink)
    contact.setAlignment("Center")
    //Location
    val location = Paragraph(iLocation, timesNewRomanPlain)
    location.setAlignment("Center")


    //Links
    val links = Paragraph()
    if (iLink1 != null && iLinkCover1 != null) {
        if (iLink1.isNotBlank() && iLinkCover1.isNotBlank()){
            val link1 = Anchor(iLink1, linkFont)
            link1.reference = iLink1
            links.add(Chunk("$iLinkCover1: ", timesNewRomanPlain))
            links.add(link1)
        }
    }
    if (iLink2 != null && iLinkCover2 != null) {
        if (iLink2.isNotBlank() && iLinkCover2.isNotBlank()){
            val link2 = Anchor(iLink2, linkFont)
            link2.reference = iLink2
            //
            if (!isSplitLink) {
                if (iLink1 != null && iLinkCover1 != null) {
                    links.add(Chunk(" | ", timesNewRomanPlain))
                }
                links.add(Chunk("$iLinkCover2: ", timesNewRomanPlain))
                links.add(link2)
            } else {
                links.add(Chunk.NEWLINE)
                links.add(Chunk("$iLinkCover2: ", timesNewRomanPlain))
                links.add(link2)
            }
        }
    }
    links.setAlignment("Center")
    links.spacingAfter = 10f

    //CLOSE
    val section = Paragraph()
    //section.add(name)
    section.add(role)
    section.add(contact)
    section.add(location)

    val link1State = iLink1 != null && iLinkCover1 != null
    val link2State = iLink2 != null && iLinkCover2 != null
    if (link1State || link2State) {
        section.add(links)
    }
    return section
}

fun createContactDetailsSectionNarrow(
    iJobRole: String,
    iPhone: String, iEmail: String,
    iLocation: String,
    iLink1: String?, iLink2: String?,
    linkColor: Color
): Paragraph {
    val timesNewRomanRole = FontFactory.getFont(FontFactory.TIMES, 16f)
    val linkFont = FontFactory.getFont(
        FontFactory.TIMES,
        12f,
        Font.UNDERLINE,
        linkColor
    ) // Blue color for hyperlink

    //
    val emailLink = Anchor(iEmail, linkFont)
    emailLink.reference = "mailto:$iEmail"
    //START
    //Role
    val role = Paragraph(iJobRole, timesNewRomanRole)
    role.setAlignment("Center")
    role.spacingAfter = 2.5f


    //CONTACT
    val contactItem = createHalfContactSection(
        iPhone, iEmail, iLocation,
        iLink1, iLink2, linkColor
    )

    //CLOSE
    val section = Paragraph()
    section.add(role)
    section.add(contactItem)
    return section
}

fun createHalfContactSection(
    iPhone: String, iEmail: String,
    iLocation: String,
    iLink1: String?, iLink2: String?, linkColor: Color
): Paragraph {
    val timesNewRomanPlain = FontFactory.getFont(FontFactory.TIMES, 12f)
    val linkFont = FontFactory.getFont(FontFactory.TIMES, 12f, Font.UNDERLINE, linkColor)

    //
    val emailLink = Anchor(iEmail, linkFont)
    emailLink.reference = "mailto:$iEmail"
    //START
    //
    val phone = Paragraph(iPhone, timesNewRomanPlain)
    val contact = Paragraph()
    contact.add(emailLink)
    //Location
    val location = Paragraph(iLocation, timesNewRomanPlain)


    //Links
    val links = Paragraph()
    if (!iLink1.isNullOrBlank()){
        val link1 = Anchor(iLink1, linkFont)
        link1.reference = iLink1
        links.add(link1)
    }

    //
    if (!iLink2.isNullOrBlank()){
        val link2 = Anchor(iLink2, linkFont)
        link2.reference = iLink2
        links.add(Chunk.NEWLINE)
        links.add(link2)
    }
    //
    if (!iLink1.isNullOrBlank() || !iLink2.isNullOrBlank()) links.spacingAfter = 10f


    //CLOSE
    val section = Paragraph()
    section.add(phone)
    section.add(contact)
    section.add(location)
    if (!iLink1.isNullOrBlank() || !iLink2.isNullOrBlank()) section.add(links)

    //
    return section
}

fun createContactDetailsSectionSplit(
    iName: String,
    iJobRole: String,
    iPhone: String, iEmail: String,
    iLocation: String,
    iLink1: String?, iLink2: String?,
    linkColor: Color, isUpperCase: Boolean
): Paragraph {
    //
    val timesNewRomanBoldBig = FontFactory.getFont(FontFactory.TIMES_BOLD, 24f)
    val timesNewRomanRole = FontFactory.getFont(FontFactory.TIMES, 16f)

    val resultParagraph = Paragraph("", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12f))
    val contactItem = createHalfContactSection(
        iPhone, iEmail, iLocation,
        iLink1, iLink2, linkColor
    )
    /*contactItem.setAlignment("Center")
    resultParagraph.add(contactItem)*/

    val section2 = Paragraph()
    section2.add(contactItem)
    section2.alignment = Element.ALIGN_RIGHT


    //
    val dualListTable = PdfPTable(2)
    dualListTable.headerRows = 0
    dualListTable.widthPercentage = 100f
    dualListTable.setWidths(intArrayOf(60, 40))


    //
    val dualCell1 = PdfPCell()
    val processedName: String = if (isUpperCase) iName.uppercase() else iName
    dualCell1.addElement(Paragraph(processedName, timesNewRomanBoldBig))
    dualCell1.addElement(Paragraph(iJobRole, timesNewRomanRole))
    dualCell1.border = PdfPCell.NO_BORDER
    dualCell1.setPadding(0f)
    dualListTable.addCell(dualCell1)

    //
    val dualCell2 = PdfPCell()
    dualCell2.addElement(section2)
    dualCell2.border = PdfPCell.NO_BORDER
    dualCell2.setPadding(0f)
    dualListTable.addCell(dualCell2)

    resultParagraph.add(dualListTable)
    return resultParagraph
}

fun createHeader(text: String?): Paragraph {
    val timesNewRomanBold = FontFactory.getFont(FontFactory.TIMES_BOLD, 12f)
    val header = Paragraph(text, timesNewRomanBold)
    header.spacingAfter = 5f // Add some spacing after the header
    return header
}

fun createHeader(text: String?, size: Float, font: Font?): Paragraph {
    val timesNewRomanBold = FontFactory.getFont(FontFactory.TIMES_BOLD, size)
    val selectedFont = font ?: timesNewRomanBold
    val header = Paragraph(text, selectedFont)
    header.spacingAfter = 5f // Add some spacing after the header
    return header
}

fun createSubHeader(text: String?, size: Float): Paragraph {
    val timesNewRomanBold = FontFactory.getFont(FontFactory.TIMES, size)
    val header = Paragraph(text, timesNewRomanBold)
    header.spacingAfter = 5f // Add some spacing after the header
    return header
}

fun createHeaderWithHorizontalLine(headerText: String, lineColor: Color): Paragraph {
    val timesNewRomanBold = FontFactory.getFont(FontFactory.TIMES_BOLD, 12f)
    val header = Paragraph(headerText, timesNewRomanBold)

    val lineSeparator = LineSeparator()
    lineSeparator.lineWidth = 1f
    lineSeparator.lineColor = lineColor
    lineSeparator.percentage = 100f

    val lineChunk = Chunk(lineSeparator)
    val test = Paragraph(Chunk(lineSeparator))
    test.spacingBefore = -5f

    val section = Paragraph()
    section.add(header)
    section.add(lineChunk)
    section.spacingAfter = 10f

    return section
}

fun createHorizontalLine(lineColor: Color): Paragraph {

    val lineSeparator = LineSeparator()
    lineSeparator.lineWidth = 1f
    lineSeparator.lineColor = lineColor
    lineSeparator.percentage = 100f

    val lineChunk = Chunk(lineSeparator)
    val line = Paragraph(Chunk(lineSeparator))
    line.spacingBefore = 5f
    line.spacingAfter = 5f

    val section = Paragraph()
    section.add(lineChunk)
    section.spacingAfter = 10f

    return section
}

fun createHeaderWithHorizontalLine(headerText: String, lineColor: Color, font: Font): Paragraph {
    val header = Paragraph(headerText, font)

    val lineSeparator = LineSeparator()
    lineSeparator.lineWidth = 1f
    lineSeparator.lineColor = lineColor
    lineSeparator.percentage = 100f

    val lineChunk = Chunk(lineSeparator)
    val test = Paragraph(Chunk(lineSeparator))
    test.spacingBefore = -5f

    val section = Paragraph()
    section.add(header)
    section.add(lineChunk)
    section.spacingAfter = 10f

    return section
}

fun createMain(text: String?): Paragraph {
    val timesNewRomanPlain = FontFactory.getFont(FontFactory.TIMES, 12f)
    return Paragraph(text, timesNewRomanPlain)
}

fun createMainBold(text: String?): Paragraph {
    val timesNewRomanPlain = FontFactory.getFont(FontFactory.TIMES_BOLD, 12f)
    return Paragraph(text, timesNewRomanPlain)
}

fun createObjectiveSection(text: String?): Paragraph {
    val timesNewRomanPlain = FontFactory.getFont(FontFactory.TIMES, 12f)

    val objectiveText = Paragraph(text, timesNewRomanPlain)
    //
    val section = Paragraph()
    section.add(objectiveText)

    return section
}

fun createExperienceSection(experienceItem: ExperienceItem): Paragraph {
    val timesNewRomanBold = FontFactory.getFont(FontFactory.TIMES_BOLD, 12f)
    val timesNewRomanPlain = FontFactory.getFont(FontFactory.TIMES, 12f)
    val timesNewRomanItalics = FontFactory.getFont(FontFactory.TIMES_ITALIC, 12f)

    // SET 2
    val table = PdfPTable(2)
    table.widthPercentage = 100f // Make table span the full width of the page
    table.setWidths(intArrayOf(50, 50)) // Set column widths (50% for left, 50% for right)
    table.setSpacingBefore(3f) // No space before the table
    table.setSpacingAfter(3f) // No space after the table
    //
    val leftCell = PdfPCell(Phrase(0f, experienceItem.experienceRole, timesNewRomanBold))
    leftCell.border = PdfPCell.NO_BORDER // Remove the border
    leftCell.horizontalAlignment = Element.ALIGN_LEFT // Align text to the left
    leftCell.setPadding(0f) // Remove padding inside the cell
    table.addCell(leftCell)
    //
    val rightCell = PdfPCell(Phrase(0f, experienceItem.experienceWorkDate, timesNewRomanPlain))
    rightCell.border = PdfPCell.NO_BORDER // Remove the border
    rightCell.horizontalAlignment = Element.ALIGN_RIGHT // Align text to the right
    rightCell.setPadding(0f) // Remove padding inside the cell
    table.addCell(rightCell)


    //SET 3
    val boldLocation = Chunk(experienceItem.experienceCompanyName + " |", timesNewRomanBold)
    val regularLocation = Chunk(" " + experienceItem.experienceCompanyLocation, timesNewRomanItalics)
    val locationText = Paragraph()
    locationText.add(boldLocation)
    locationText.add(regularLocation)


    //SET 4
    val bulletList = List(List.UNORDERED)
    bulletList.symbolIndent = 10f
    bulletList.setListSymbol("•")
    for (nItem in experienceItem.experienceItemsList) {
        val item = ListItem("  $nItem", timesNewRomanPlain)
        item.setAlignment("Justify")
        bulletList.add(item)
    }

    //CLOSE
    val section = Paragraph()
    //section.add(experienceHeader);
    section.add(table)
    section.add(locationText)
    section.add(bulletList)

    return section
}

fun createExperienceSectionSplit(experienceItem: ExperienceItem): Paragraph {
    val timesNewRomanBold = FontFactory.getFont(FontFactory.TIMES_BOLD, 12f)
    val timesNewRomanPlain = FontFactory.getFont(FontFactory.TIMES, 12f)
    val timesNewRomanItalics = FontFactory.getFont(FontFactory.TIMES_ITALIC, 12f)

    //
    val role = Paragraph(experienceItem.experienceRole, timesNewRomanBold)
    val workDate = Paragraph(experienceItem.experienceWorkDate, timesNewRomanPlain)


    //SET 3
    val boldLocation = Chunk(experienceItem.experienceCompanyName + " |", timesNewRomanBold)
    val regularLocation = Chunk(" " + experienceItem.experienceCompanyLocation, timesNewRomanItalics)
    val locationText = Paragraph("", timesNewRomanPlain)
    locationText.add(boldLocation)
    locationText.add(regularLocation)
    val blank = Paragraph("", FontFactory.getFont(FontFactory.TIMES_BOLD, 1f))


    //SET 4
    val bulletList = Paragraph("", timesNewRomanPlain)
    for (nItem in experienceItem.experienceItemsList) {
        val listItem = Chunk("•  $nItem\n", timesNewRomanPlain)
        bulletList.add(listItem)
    }
    bulletList.alignment = Element.ALIGN_JUSTIFIED
    bulletList.spacingBefore = 100f

    //CLOSE
    val section = Paragraph()
    section.add(role)
    section.add(workDate)
    section.add(locationText)
    section.add(blank)
    section.add(bulletList)

    return section
}

fun createExperienceSectionHeader(experienceItem: ExperienceItem): Paragraph {
    val timesNewRomanBold = FontFactory.getFont(FontFactory.TIMES_BOLD, 12f)
    val timesNewRomanPlain = FontFactory.getFont(FontFactory.TIMES, 12f)
    val timesNewRomanItalics = FontFactory.getFont(FontFactory.TIMES_ITALIC, 12f)

    //
    val role = Paragraph(experienceItem.experienceRole, timesNewRomanBold)
    val workDate = Paragraph(experienceItem.experienceWorkDate, timesNewRomanPlain)


    //SET 3
    val boldLocation = Chunk(experienceItem.experienceCompanyName + " |", timesNewRomanBold)
    val regularLocation = Chunk(" " + experienceItem.experienceCompanyLocation, timesNewRomanItalics)
    val locationText = Paragraph("", timesNewRomanPlain)
    locationText.add(boldLocation)
    locationText.add(regularLocation)

    //CLOSE
    val section = Paragraph()
    section.add(role)
    section.add(workDate)
    section.add(locationText)

    return section
}

fun createExperienceSectionContent(experienceItem: ExperienceItem): Paragraph {
    val timesNewRomanPlain = FontFactory.getFont(FontFactory.TIMES, 12f)

    //SET 4
    val bulletList = Paragraph("", timesNewRomanPlain)
    for (nItem in experienceItem.experienceItemsList) {
        val listItem = Chunk("•  $nItem\n", timesNewRomanPlain)
        bulletList.add(listItem)
    }
    return bulletList
}

fun createEducationSection(item: EducationItem): Paragraph {
    val timesNewRomanBold = FontFactory.getFont(FontFactory.TIMES_BOLD, 12f)
    val timesNewRomanPlain = FontFactory.getFont(FontFactory.TIMES, 12f)
    val timesNewRomanItalics = FontFactory.getFont(FontFactory.TIMES_ITALIC, 12f)
    val section = Paragraph()

    //
    val schoolName = Chunk(item.schoolName + " |", timesNewRomanBold)
    val schoolLocation = Chunk(" " + item.schoolLocation, timesNewRomanItalics)
    val schoolDetail = Paragraph()
    schoolDetail.add(schoolName)
    schoolDetail.add(schoolLocation)
    //
    val educationTable = PdfPTable(2)
    educationTable.widthPercentage = 100f
    educationTable.setWidths(intArrayOf(65, 35))
    educationTable.setSpacingBefore(3f)
    //
    val schoolDetailItem = PdfPCell(schoolDetail)
    schoolDetailItem.border = PdfPCell.NO_BORDER
    schoolDetailItem.horizontalAlignment = Element.ALIGN_LEFT
    schoolDetailItem.setPadding(0f)
    educationTable.addCell(schoolDetailItem)
    //
    val graduatedDate = PdfPCell(Phrase(0f, item.graduatedDate, timesNewRomanPlain))
    graduatedDate.border = PdfPCell.NO_BORDER
    graduatedDate.horizontalAlignment = Element.ALIGN_RIGHT
    graduatedDate.setPadding(0f)
    educationTable.addCell(graduatedDate)
    //
    section.add(educationTable)


    //DEGREE DETAILS
    val degreeDetail = Paragraph(item.degreeEarned, timesNewRomanPlain)
    degreeDetail.setAlignment("Justify")
    degreeDetail.spacingAfter = 10f
    section.add(degreeDetail)

    return section
}

fun createEducationSectionSplit(item: EducationItem): Paragraph {
    val timesNewRomanBold = FontFactory.getFont(FontFactory.TIMES_BOLD, 12f)
    val timesNewRomanPlain = FontFactory.getFont(FontFactory.TIMES, 12f)
    val timesNewRomanItalics = FontFactory.getFont(FontFactory.TIMES_ITALIC, 12f)
    val section = Paragraph()

    //
    val schoolName = Chunk(item.schoolName + " |", timesNewRomanBold)
    val schoolLocation = Chunk(" " + item.schoolLocation, timesNewRomanItalics)
    val schoolDetail = Paragraph()
    schoolDetail.add(schoolName)
    schoolDetail.add(schoolLocation)
    val graduationDate = Paragraph("Graduated - ${item.graduatedDate}", timesNewRomanPlain)
    val degreeEarned = Paragraph(item.degreeEarned, timesNewRomanPlain)
    degreeEarned.spacingAfter = 10f

    //
    section.add(schoolDetail)
    section.add(graduationDate)
    section.add(degreeEarned)
    //
    return section
}

fun createProjectsSection(item: ProjectItem): Paragraph {
    val timesNewRomanBold = FontFactory.getFont(FontFactory.TIMES_BOLD, 12f)
    val timesNewRomanPlain = FontFactory.getFont(FontFactory.TIMES, 12f)
    val section = Paragraph()

    //SET
    val projectTitle = Paragraph(item.projectName, timesNewRomanBold)
    val projectDetail = Paragraph(item.projectDetail, timesNewRomanPlain)
    projectDetail.setAlignment("Justify")
    //
    section.add(projectTitle)
    section.add(projectDetail)
    return section
}

fun createSingleColumnSection(list: kotlin.collections.List<String>): Paragraph {
    val timesNewRomanPlain = FontFactory.getFont(FontFactory.TIMES, 12f)

    //SET
    val bulletList = List(List.UNORDERED)
    bulletList.symbolIndent = 10f
    bulletList.setListSymbol("•")
    for (i in list) {
        val item = ListItem("  $i", timesNewRomanPlain)
        item.setAlignment("Justify")
        bulletList.add(item)
    }

    //CLOSE
    val section = Paragraph()
    section.add(bulletList)

    return section
}

fun createSingleColumnSectionSplit(list: kotlin.collections.List<String>): Paragraph {
    val timesNewRomanPlain = FontFactory.getFont(FontFactory.TIMES, 12f)

    //SET
    val bulletList = Paragraph("", timesNewRomanPlain)
    for (item in list) {
        val listItem = Chunk("•  $item\n", timesNewRomanPlain)
        bulletList.add(listItem)
    }

    //CLOSE
    val section = Paragraph()
    section.add(bulletList)

    return section
}

fun createDualColumnSection(dualItem: kotlin.collections.List<String>): Paragraph {
    val timesNewRomanBold = FontFactory.getFont(FontFactory.TIMES_BOLD, 12f)
    val timesNewRomanPlain = FontFactory.getFont(FontFactory.TIMES, 12f)
    val listA: MutableList<String> = ArrayList()
    val listB: MutableList<String> = ArrayList()
    splitList(dualItem, listA, listB)

    //
    val dualList1 = List(List.UNORDERED)
    dualList1.symbolIndent = 12f
    dualList1.setListSymbol("•")
    for (i in listA - 1) {
        dualList1.add(ListItem("  $i", timesNewRomanPlain))
    }
    //
    val dualList2 = List(List.UNORDERED)
    dualList2.symbolIndent = 12f
    dualList2.setListSymbol("•")
    for (i in listB) {
        dualList2.add(ListItem("  $i", timesNewRomanPlain))
    }
    //-----------------------------------------------------------------------------
    val dualListTable = PdfPTable(2)
    dualListTable.headerRows = 0
    dualListTable.widthPercentage = 100f
    dualListTable.isSplitLate = false
    dualListTable.isSplitRows = true
    //
    val dualCell1 = PdfPCell()
    dualCell1.addElement(dualList1)
    dualCell1.border = PdfPCell.NO_BORDER
    dualCell1.setPadding(0f)
    dualListTable.addCell(dualCell1)
    //
    val dualCell2 = PdfPCell()
    dualCell2.addElement(dualList2)
    dualCell2.border = PdfPCell.NO_BORDER
    dualCell2.setPadding(0f)
    dualListTable.addCell(dualCell2)

    //CLOSE
    val section = Paragraph()
    //section.add(dualHeader);
    section.add(dualListTable)

    return section
}

fun createTripleColumnSection(tripleItem: kotlin.collections.List<String>): Paragraph {
    val timesNewRomanPlain = FontFactory.getFont(FontFactory.TIMES, 12f)
    val listA: MutableList<String> = ArrayList()
    val listB: MutableList<String> = ArrayList()
    val listC: MutableList<String> = ArrayList()
    splitListThree(tripleItem, listA, listB, listC)

    //
    val dualList1 = List(List.UNORDERED)
    dualList1.symbolIndent = 12f
    dualList1.setListSymbol("•")
    for (i in listA - 1) {
        dualList1.add(ListItem("  $i", timesNewRomanPlain))
    }
    //
    val dualList2 = List(List.UNORDERED)
    dualList2.symbolIndent = 12f
    dualList2.setListSymbol("•")
    for (i in listB - 1) {
        dualList2.add(ListItem("  $i", timesNewRomanPlain))
    }
    //
    val dualList3 = List(List.UNORDERED)
    dualList3.symbolIndent = 12f
    dualList3.setListSymbol("•")
    for (i in listC) {
        dualList3.add(ListItem("  $i", timesNewRomanPlain))
    }


    //-----------------------------------------------------------------------------
    val dualListTable = PdfPTable(3)
    dualListTable.headerRows = 0
    dualListTable.widthPercentage = 100f
    dualListTable.isSplitLate = false
    dualListTable.isSplitRows = true
    //
    val dualCell1 = PdfPCell()
    dualCell1.addElement(dualList1)
    dualCell1.border = PdfPCell.NO_BORDER
    dualCell1.setPadding(0f)
    dualListTable.addCell(dualCell1)
    //
    val dualCell2 = PdfPCell()
    dualCell2.addElement(dualList2)
    dualCell2.border = PdfPCell.NO_BORDER
    dualCell2.setPadding(0f)
    dualListTable.addCell(dualCell2)
    //
    val dualCell3 = PdfPCell()
    dualCell3.addElement(dualList3)
    dualCell3.border = PdfPCell.NO_BORDER
    dualCell3.setPadding(0f)
    dualListTable.addCell(dualCell3)

    //CLOSE
    val section = Paragraph()
    section.add(dualListTable)

    return section
}

fun createCombinedParagraphSection(items: kotlin.collections.List<String?>): Paragraph {
    val timesNewRomanPlain = FontFactory.getFont(FontFactory.TIMES, 12f)
    val stringBuilder = StringBuilder()
    for (i in items.indices) {
        stringBuilder.append("•  ${items[i]}")
        if (i < items.size - 1) {
            stringBuilder.append("  ")
        }
    }
    val paragraphText = stringBuilder.toString()
    return Paragraph(paragraphText, timesNewRomanPlain)
}

fun splitList(
    originalList: kotlin.collections.List<String>,
    listA: MutableList<String>,
    listB: MutableList<String>
) {
    val middle = (originalList.size + 1) / 2
    listA.addAll(originalList.subList(0, middle))
    listB.addAll(originalList.subList(middle, originalList.size))
}

fun splitListThreeOld(
    originalList: kotlin.collections.List<String>,
    listA: MutableList<String>,
    listB: MutableList<String>,
    listC: MutableList<String>
) {
    val size = originalList.size
    val firstSplit = size / 3
    val secondSplit = 2 * size / 3

    listA.addAll(originalList.subList(0, firstSplit))
    listB.addAll(originalList.subList(firstSplit, secondSplit))
    listC.addAll(originalList.subList(secondSplit, size))
}

fun splitListThree(originalList: kotlin.collections.List<String>, listA: MutableList<String>, listB: MutableList<String>, listC: MutableList<String>) {
    val totalItems = originalList.size
    val chunkSize = totalItems / 3
    val remainder = totalItems % 3

    // Distribute items into three lists
    listA.addAll(originalList.subList(0, chunkSize + if (remainder > 0) 1 else 0))
    listB.addAll(originalList.subList(listA.size, listA.size + chunkSize + if (remainder > 1) 1 else 0))
    listC.addAll(originalList.subList(listA.size + listB.size, totalItems))
}


