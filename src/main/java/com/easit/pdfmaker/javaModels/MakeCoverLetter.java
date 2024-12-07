package com.easit.pdfmaker.javaModels;

import android.util.Log;

import com.easit.pdfmaker.javaModels.data.CoverLetterItem;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class MakeCoverLetter {
    private final String mPath;

    // Constructor that accepts a Context as argument
    public MakeCoverLetter(String path) {
        this.mPath = path;
    }

    public void makeCoverLetter(CoverLetterItem item) {
        Document document = new Document(PageSize.A4);
        float spacing = 10f;

        try {
            PdfWriter.getInstance(document, Files.newOutputStream(Paths.get(mPath)));
            document.open();

            Paragraph name = createHeader(item.getName());
            name.setAlignment(Element.ALIGN_RIGHT);
            document.add(name);

            Paragraph role = createMain(item.getRole());
            role.setAlignment(Element.ALIGN_RIGHT);
            document.add(role);

            Paragraph location = createMain(item.getLocation());
            location.setAlignment(Element.ALIGN_RIGHT);
            document.add(location);

            Paragraph date = createMain(item.getDate());
            date.setAlignment(Element.ALIGN_RIGHT);
            document.add(date);

            //
            document.add(createHeader("Hiring Manager"));
            document.add(createMain(item.getCompanyName()));
            document.add(createMain(item.getCompanyLocation()));
            document.add(createMain(item.getCompanyAddress()));

            //
            Paragraph salutation = createMain("Dear Hiring Manager,");
            salutation.setSpacingBefore(spacing);
            salutation.setSpacingAfter(spacing);
            document.add(salutation);

            //
            Paragraph mainContent = createMain(item.getMainContent());
            mainContent.setAlignment(Element.ALIGN_JUSTIFIED);
            mainContent.setSpacingAfter(spacing);
            document.add(mainContent);

            //
            document.add(createMain(item.getClosingSalutation()));
            //document.add(createMain(item.getName()));





        } catch (DocumentException | IOException de) {
            System.err.println(de.getMessage());
            Log.e("Start", Objects.requireNonNull(de.getMessage()));
        }
        document.close();
    }

    public static Paragraph createHeader(String text) {
        Font timesNewRomanBold = FontFactory.getFont(FontFactory.TIMES_BOLD, 12);
        return new Paragraph(text, timesNewRomanBold);
    }

    public static Paragraph createMain(String text) {
        Font timesNewRomanPlain = FontFactory.getFont(FontFactory.TIMES, 12);
        return new Paragraph(text, timesNewRomanPlain);
    }
}
