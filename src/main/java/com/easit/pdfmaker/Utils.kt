package com.easit.pdfmaker

import android.Manifest
import android.app.Activity
import android.os.Environment
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.easit.pdfmaker.data.AllResultData
import com.easit.pdfmaker.data.PdfMakerUser
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.FileOutputStream
import java.io.OutputStream
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import java.io.File

fun deserializeAllResultData(text: String): AllResultData {
    val data: AllResultData = Json.decodeFromString(text.trim())
    return data
}

fun serializeAllResultData(data: AllResultData): String{
    val serializedString: String = Json.encodeToString(data)
    return serializedString
}

fun serializeUser(data: PdfMakerUser): String{
    val serializedString: String = Json.encodeToString(data)
    return serializedString
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

        //
        showPdfNotification(context, pdfFile.absolutePath)

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

fun showPdfNotification(context: Context, pdfFilePath: String) {
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
        .setSmallIcon(android.R.drawable.ic_menu_view)
        .setContentTitle("Open PDF")
        .setContentText("Tap to view your PDF file")
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .build()

    // Show the Notification
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.notify(notificationId, notification)
}