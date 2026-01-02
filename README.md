# TailoR PDF Maker 📄✨

![Kotlin](https://img.shields.io/badge/Kotlin-2.0-purple?style=flat&logo=kotlin)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-UI-green?style=flat&logo=android)
![OpenPDF](https://img.shields.io/badge/PDF%20Generation-OpenPDF-blue?style=flat)

> **Note:** This repository allows public access to the PDF generation module used in **TailoR Ai**. It serves as a showcase of dynamic document generation and formatting using Android & Kotlin.

## 📱 About TailoR Ai & This Module

**TailoR Ai** is a comprehensive mobile application designed to streamline the job application process. While the core application handles complex AI-driven logic, this specific module handles the **visual construction and rendering** of professional resumes and cover letters.

This module demonstrates how to bridge the gap between Modern Android UI (Jetpack Compose) and raw PDF byte-stream generation.

### 🎨 Key Features Showcase

| Dynamic Styling | Layout Engine | Content Control |
| :---: | :---: | :---: |
| <img src="YOUR_IMAGE_LINK_HERE" width="200"> | <img src="YOUR_IMAGE_LINK_HERE" width="200"> | <img src="YOUR_IMAGE_LINK_HERE" width="200"> |
| **Real-time Color Injection:** Modify accent colors and link styles on the fly. | **Smart Flow Layouts:** Switch between Grid, Column, or FlowRow implementation for skills. | **Section Toggling:** Dynamically include or exclude sections (Experience, Hobbies) without breaking the PDF structure. |

## 🛠️ Technical Implementation: OpenPDF in Kotlin

Generating PDFs in Android can be tricky. This project utilizes **OpenPDF**, a Java-based library, but adapts it to a "Kotlin-first" approach.

### Why OpenPDF?
While Android has native `PdfDocument` classes, they essentially draw a Canvas (like a bitmap). OpenPDF allows for:
* Text selection in the final output (crucial for ATS scanners).
* Hyperlink support.
* Metadata injection.

### 💡 Developer Tip: managing OpenPDF in Android

If you are looking to implement PDF generation in your own Android apps, here is a pattern used in this project to clean up the verbose Java syntax:

**1. Create Extension Functions for Structure**
Instead of adding elements line-by-line, create wrappers to handle spacing and styling automatically.

```kotlin
// Example: Extension function to add a styled paragraph
private fun Document.addStyledParagraph(
    text: String, 
    font: Font, 
    spacingAfter: Float = 10f
) {
    val paragraph = Paragraph(text, font).apply {
        alignment = Element.ALIGN_LEFT
        this.spacingAfter = spacingAfter
    }
    this.add(paragraph)
}
2. Handling Layouts (The "Flow Row" Challenge) Standard PDFs write top-to-bottom. To achieve the "Skill Tags" look (seen in the screenshots), you must use a PdfPTable with specific border settings.

Kotlin

// Pseudo-code logic used in this repo for Skill Chips
val table = PdfPTable(3) // 3 Columns
table.widthPercentage = 100f

skillsList.forEach { skill ->
    val cell = PdfPCell(Phrase(skill, chipFont)).apply {
        backgroundColor = BaseColor.LIGHT_GRAY
        border = Rectangle.NO_BORDER
        padding = 5f
        // distinct styling to make it look like a UI Chip
    }
    table.addCell(cell)
}
document.add(table)
3. Previewing in Jetpack Compose Since OpenPDF generates a file/stream, you cannot "preview" it natively in a Compose View. This project generates the PDF to a temporary cache file and renders it using a Bitmap renderer or a native PDF Viewer intent for the immediate feedback loop shown in the app.

🚀 Getting Started
Clone the repository:

Bash

git clone [https://github.com/Olaoluwa99/tailor-pdf-maker.git](https://github.com/Olaoluwa99/tailor-pdf-maker.git)
Open in Android Studio: Ensure you are using the latest version (Koala/Ladybug) compatible with the Compose version used.

Run the Sample: The app module contains a sample activity that launches the PDF builder interface.

🤝 Contribution & License
This is a sub-repository of the private TailoR Ai project.

Issues regarding PDF rendering bugs are welcome.

Pull requests improving the OpenPDF-to-Kotlin styling wrappers are appreciated.

Built with ❤️ using Kotlin and Jetpack Compose.
