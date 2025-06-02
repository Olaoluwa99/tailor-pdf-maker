package com.easit.pdfmaker

import android.os.Environment
import android.widget.Toast
import com.easit.pdfmaker.data.AllResultData
import kotlinx.serialization.json.Json
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import com.easit.pdfmaker.constants.Constant
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import java.io.File

fun deserializeAllResultData(text: String): AllResultData {
    val data: AllResultData = Json.decodeFromString(text.trim())
    return data
}

fun fileToByteArray(file: File): ByteArray? {
    return try {
        file.readBytes()
    } catch (e: Exception) {
        e.printStackTrace()
        null  // Return null if there was an error reading the file
    }
}

fun showPdfNotification(context: Context, pdfFilePath: String, tag: String) {
    val channelId = "pdf_notification_channel"
    val notificationId = 1001

    // Create the Intent to open the PDF file
    val pdfFile = File(pdfFilePath)
    val pdfUri: Uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", pdfFile)

    val openPdfIntent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(pdfUri, "application/pdf")
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    }

    val pendingIntent = PendingIntent.getActivity(
        context,
        0,
        openPdfIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    // Create Notification Channel for Android O and above
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            "PDF Notifications",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    // Build the Notification
    val notification = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.notification_image)
        .setContentTitle("Download Complete - ($tag)")
        .setContentText("Your PDF has been successfully downloaded. Tap to open.")
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .build()

    // Show the Notification
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.notify(notificationId, notification)
}

fun launchPdf(context: Context, absolutePath: String) {
    val file = File(absolutePath)
    if (!file.exists()) {
        return
    }

    val uri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
//        FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    } else {
        Uri.fromFile(file)
    }

    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, "application/pdf")
        flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_GRANT_READ_URI_PERMISSION
    }

    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@Composable
fun rememberReviewTask(reviewManager: ReviewManager): ReviewInfo? {
    var reviewInfo: ReviewInfo? by remember {
        mutableStateOf(null)
    }
    reviewManager.requestReviewFlow().addOnCompleteListener {
        if (it.isSuccessful) {
            reviewInfo = it.result
        }
    }
    return reviewInfo
}

fun savePdfToExternalStorage(context: Context, pdfData: ByteArray, fileName: String, onSaveCompleted: (String) -> Unit) {
    val fileNameWithExtension = "$fileName.pdf"

    val savedFilePath: String? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        // Android 10+ (Scoped Storage) - Use MediaStore API
        savePdfUsingMediaStore(context, pdfData, fileNameWithExtension)
    } else {
        // Android 9 and below - Use traditional File API
        savePdfUsingFileAPI(pdfData, fileNameWithExtension)
    }

    if (savedFilePath != null) {
        showPdfNotification(context, savedFilePath, fileName)
        Toast.makeText(context, "PDF saved to: $savedFilePath", Toast.LENGTH_LONG).show()
        onSaveCompleted(savedFilePath)
    } else {
        Toast.makeText(context, "Failed to save PDF", Toast.LENGTH_SHORT).show()
        onSaveCompleted(Constant.FAILED_DOWNLOAD)
    }
}

/**
 * Save PDF in Android 10+ using MediaStore API
 */
@RequiresApi(Build.VERSION_CODES.Q)
private fun savePdfUsingMediaStore(context: Context, pdfData: ByteArray, fileName: String): String? {
    val resolver = context.contentResolver

    // Delete existing file if it already exists
    deleteExistingFile(context, fileName)

    val contentValues = ContentValues().apply {
        put(MediaStore.Files.FileColumns.DISPLAY_NAME, fileName)
        put(MediaStore.Files.FileColumns.MIME_TYPE, "application/pdf")
        put(MediaStore.Files.FileColumns.RELATIVE_PATH, "Documents/TailoR-AI/")
    }

    val uri: Uri? = resolver.insert(MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL), contentValues)

    uri?.let {
        resolver.openOutputStream(it)?.use { outputStream ->
            outputStream.write(pdfData)
        }
        return getFilePathFromUri(context, uri)
    }
    return null
}

/**
 * Function to check and delete an existing file with the same name
 */
@RequiresApi(Build.VERSION_CODES.Q)
private fun deleteExistingFile(context: Context, fileName: String) {
    val resolver = context.contentResolver
    val collection = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)

    val selection = "${MediaStore.Files.FileColumns.RELATIVE_PATH} = ? AND ${MediaStore.Files.FileColumns.DISPLAY_NAME} = ?"
    val selectionArgs = arrayOf("Documents/TailoR-AI/", fileName)

    resolver.delete(collection, selection, selectionArgs)
}

/**
 * Save PDF in Android 9 and below using traditional File API
 */
private fun savePdfUsingFileAPI(pdfData: ByteArray, fileName: String): String? {
    val documentsDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "TailoR-AI")

    if (!documentsDir.exists()) {
        documentsDir.mkdirs()
    }

    val pdfFile = File(documentsDir, fileName)
    return try {
        pdfFile.outputStream().use { it.write(pdfData) }
        pdfFile.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

/**
 * Get real file path from URI
 */
private fun getFilePathFromUri(context: Context, uri: Uri): String? {
    val projection = arrayOf(MediaStore.MediaColumns.DATA)
    context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA))
        }
    }
    return null
}
