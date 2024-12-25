package com.easit.pdfmaker.kotlinModels.makers

import android.util.Log
import com.easit.pdfmaker.kotlinModels.CoverLetterData
import com.lowagie.text.Document
import com.lowagie.text.DocumentException
import com.lowagie.text.Element
import com.lowagie.text.PageSize
import com.lowagie.text.pdf.PdfWriter
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Objects

class CoverLetterMaker(
    private val path: String,
    private val themeColor: String,
    private val styleType: String
){
    fun createCoverLetter(item: CoverLetterData){
        when (styleType){
            "ALPHA" -> createTypeAlpha(item)
            "BETA" -> createDefault()
            "DELTA" -> createDefault()
            "GAMMA" -> createDefault()
            "OMEGA" -> createDefault()
        }
    }

    private fun createTypeAlpha(item: CoverLetterData){
        val document = Document(PageSize.A4)
        val spacing = 10f

        try {
            PdfWriter.getInstance(document, Files.newOutputStream(Paths.get(path)))
            document.open()

            val name = createHeader(item.name)
            name.alignment = Element.ALIGN_RIGHT
            document.add(name)

            val role = createMain(item.role)
            role.alignment = Element.ALIGN_RIGHT
            document.add(role)

            val location = createMain(item.location)
            location.alignment = Element.ALIGN_RIGHT
            document.add(location)

            val date = createMain(item.date)
            date.alignment = Element.ALIGN_RIGHT
            document.add(date)

            //
            document.add(createHeader("Hiring Manager"))
            document.add(createMain(item.companyName))
            document.add(createMain(item.companyLocation))
            document.add(createMain(item.companyAddress))

            //
            val salutation = createMain("Dear Hiring Manager,")
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


            //document.add(createMain(item.getName()));
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