package com.easit.pdfmaker

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.easit.pdfmaker.javaModels.data.CoverLetterItem
import com.easit.pdfmaker.javaModels.data.EducationItem
import com.easit.pdfmaker.javaModels.data.ExperienceItem
import com.easit.pdfmaker.javaModels.data.ProjectItem
import com.easit.pdfmaker.javaModels.data.ResumeItem
import com.easit.pdfmaker.kotlinModels.AllResultData
import com.easit.pdfmaker.kotlinModels.CoverLetterData
import com.easit.pdfmaker.kotlinModels.ResumeData
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

fun deserialize(text: String): AllResultData {
    val data: AllResultData = Json.decodeFromString(text.trim())
    return data
}

fun serialize(data: AllResultData): String{
    val serializedString: String = Json.encodeToString(data)
    return serializedString
}

fun resumeConverter(item: ResumeData): ResumeItem {
    val experienceArray = ArrayList<ExperienceItem>()
    val educationArray = ArrayList<EducationItem>()
    val projectArray = ArrayList<ProjectItem>()
    if (item.experienceList != null){
        for (i in item.experienceList){
            experienceArray.add(
                ExperienceItem(
                    i.experienceRole,
                    i.experienceCompanyName,
                    i.experienceCompanyLocation,
                    i.experienceWorkDate,
                    i.experienceItemsList
                )
            )
        }
    }

    if (item.educationList != null){
        for (i in item.educationList){
            educationArray.add(
                EducationItem(
                    i.schoolName,
                    i.schoolLocation,
                    i.graduatedDate,
                    i.degreeEarned
                )
            )
        }
    }

    if (item.projectList != null){
        for (i in item.projectList){
            projectArray.add(
                ProjectItem(
                    i.projectName,
                    i.projectDetail
                )
            )
        }
    }

    return ResumeItem(
        item.name,
        item.role,
        item.phone,
        item.email,
        item.location,
        item.linkCover1,
        item.link1,
        item.linkCover2,
        item.link2,
        item.objective,

        experienceArray,
        educationArray,
        projectArray,

        item.skillsList,
        item.softSkillsList,
        item.certificationList,
        item.hobbiesList
    )
}

fun coverLetterConverter(item: CoverLetterData): CoverLetterItem {
    return CoverLetterItem(
        item.name,
        item.role,
        item.location,
        item.date,
        item.companyName,
        item.companyAddress,
        item.companyLocation,
        item.mainContent,
        item.closingSalutation
    )
}

fun savePdfToExternalStorage(context: Context, pdfData: ByteArray, fileName: String) {
    // Get the Documents directory path for all versions
    val documentsDir = File(Environment.getExternalStorageDirectory().absolutePath + "/Documents/TailoR-AI")

    // Create the directory if it doesn't exist
    if (!documentsDir.exists()) {
        documentsDir.mkdirs()
    }

    // Create the PDF file in the Documents directory
    val pdfFile = File(documentsDir, "$fileName.pdf")

    try {
        val outputStream: OutputStream = FileOutputStream(pdfFile)
        outputStream.write(pdfData)
        outputStream.flush()
        outputStream.close()

        // Notify the user the file was saved
        Toast.makeText(context, "PDF saved to: ${pdfFile.absolutePath}", Toast.LENGTH_LONG).show()
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Failed to save PDF", Toast.LENGTH_SHORT).show()
    }
}

fun fileToByteArray(file: File): ByteArray? {
    return try {
        file.readBytes()
    } catch (e: Exception) {
        e.printStackTrace()
        null  // Return null if there was an error reading the file
    }
}

fun requestPermissions(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            9999//REQUEST_CODE
        )
    }
}