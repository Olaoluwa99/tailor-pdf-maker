package com.easit.pdfmaker.models

import android.util.Log
import com.easit.pdfmaker.data.CoverLetterData
import com.easit.pdfmaker.data.StyleType
import com.easit.pdfmaker.data.ThemeColor
import com.lowagie.text.Document
import com.lowagie.text.DocumentException
import com.lowagie.text.Element
import com.lowagie.text.PageSize
import com.lowagie.text.Paragraph
import com.lowagie.text.pdf.PdfWriter
import java.awt.Color
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

class CoverLetterMaker(private val path: String){

    private var mainThemeColor = Color.BLACK
    private var isUppercaseName = true

    fun createCoverLetter(
        item: CoverLetterData,
        themeColor: ThemeColor,
        styleType: StyleType,
        uppercaseName: Boolean,
        onPdfCreated: () -> Unit
    ){
        isUppercaseName = uppercaseName

        //
        mainThemeColor = when (themeColor){
            ThemeColor.RED -> Color.RED
            ThemeColor.GREEN -> Color.GREEN
            ThemeColor.BLACK -> Color.BLACK
            ThemeColor.BLUE -> Color.BLUE
            ThemeColor.YELLOW -> Color.YELLOW
            ThemeColor.DARK_GRAY -> Color.DARK_GRAY
            ThemeColor.LIGHT_GRAY -> Color.LIGHT_GRAY
        }

        when (styleType){
            StyleType.ALPHA -> createTypeAlpha(item, onPdfCreated)
            StyleType.BETA -> createDefault()
            StyleType.DELTA -> createDefault()
            StyleType.GAMMA -> createDefault()
            StyleType.OMEGA -> createDefault()
        }
    }

    private fun createTypeAlpha(item: CoverLetterData, onPdfCreated: () -> Unit){
        val document = Document(PageSize.A4)
        val spacing = 10f

        try {
            PdfWriter.getInstance(document, Files.newOutputStream(Paths.get(path)))
            document.open()

            val processedName: String = if (isUppercaseName) item.name.uppercase() else item.name
            val name = createHeader(processedName, 24f)
            //name.alignment = Element.ALIGN_RIGHT
            document.add(name)

            val role = createSubHeader(item.role, 16f)
            //role.alignment = Element.ALIGN_RIGHT
            document.add(role)

            document.add(Paragraph("  "))

            val location = createMain(item.location)
            //location.alignment = Element.ALIGN_RIGHT
            document.add(location)

            val date = createMain(item.date)
            //date.alignment = Element.ALIGN_RIGHT
            document.add(date)

            //
            document.add(Paragraph("  "))

            //
            document.add(createHeader(item.hiringManagerName, 12f))
            document.add(createMain(item.companyName))
            document.add(createMain(item.companyLocation))
            document.add(createMain(item.companyAddress))
            //
            document.add(Paragraph("  "))

            //
            val salutation = createMain("${item.hiringManagerSalute},")
            salutation.spacingBefore = spacing
            salutation.spacingAfter = spacing
            document.add(salutation)

            //
            val mainContent = createMain(item.mainContent)
            mainContent.alignment = Element.ALIGN_JUSTIFIED
            mainContent.spacingAfter = spacing
            document.add(mainContent)

            //
            document.add(createMain(item.closingSalutation))

            //
            document.add(Paragraph("   "))
            //
            document.add(createMain(item.closingName))

            //
            onPdfCreated()
        } catch (de: DocumentException) {
            System.err.println(de.message)
            Log.e("Error - Doc", de.message ?: "")
        } catch (de: IOException) {
            System.err.println(de.message)
            Log.e("Error - IO", de.message ?: "")
        }
        document.close()
    }

    private fun createDefault(){
        /**/
    }
}