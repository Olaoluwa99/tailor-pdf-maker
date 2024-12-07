package com.easit.pdfmaker.javaModels;

import android.util.Log;

import com.easit.pdfmaker.javaModels.data.EducationItem;
import com.easit.pdfmaker.javaModels.data.ExperienceItem;
import com.easit.pdfmaker.javaModels.data.ProjectItem;
import com.easit.pdfmaker.javaModels.data.ResumeItem;
import com.lowagie.text.Anchor;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.List;
import com.lowagie.text.ListItem;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

public class MakeResume {
    private final String mPath;

    // Constructor that accepts a Context as argument
    public MakeResume(String path) {
        this.mPath = path;
    }

    public void makeResume(ResumeItem item) {
        Document document = new Document(PageSize.A4);
        float multiItemSpacing = 10f;//5f;
        float headerSpacingAfter = 7.5f;
        float headerSpacingAfterExperience = 7.5f;
        float headerSpacingBefore = 10f;
        //ResumeItem item = createSampleResumeItem();

        try {
            PdfWriter.getInstance(document, Files.newOutputStream(Paths.get(mPath)));
            document.open();

            /*Paragraph mains = new Paragraph("This is the Header");
            document.add(mains);*/

            //TODO - HEADER
            Paragraph contactItem = createContactDetailsSection(
                    item.getName(), item.getJobRole(), item.getPhone(),
                    item.getEmail(), item.getLocation(),
                    item.getLinkCover1(), item.getLinkCover2(),
                    item.getLink1(), item.getLink2()
            );
            contactItem.setAlignment("Center");
            document.add(contactItem);

            //TODO - OBJECTIVE
            if(item.getObjectiveText() != null){
                Paragraph objectiveHeader = createHeader("OBJECTIVE");
                objectiveHeader.setSpacingBefore(headerSpacingBefore);
                objectiveHeader.setSpacingAfter(headerSpacingAfter);
                document.add(objectiveHeader);
                //
                Paragraph objectiveItem = createObjectiveSection(item.getObjectiveText());
                objectiveItem.setAlignment(Element.ALIGN_JUSTIFIED);
                document.add(objectiveItem);
            }

            //TODO _ Change into interior function like Education
            //TODO - EXPERIENCE
            if(item.getExperienceItemList() != null){
                Paragraph experienceHeader = createHeader("EXPERIENCE");
                experienceHeader.setSpacingBefore(headerSpacingBefore);
                experienceHeader.setSpacingAfter(headerSpacingAfterExperience);
                document.add(experienceHeader);
                for (int i = 0; i <= item.getExperienceItemList().size()-1; i++) {
                    Paragraph experienceText = createExperienceSection(item.getExperienceItemList().get(i));
                    if (i != item.getExperienceItemList().size()-1) experienceText.setSpacingAfter(multiItemSpacing);
                    document.add(experienceText);
                }
            }

            //TODO - TECHNICAL SKILLS
            if(item.getSkillsItemList() != null){
                Paragraph skillsHeader = createHeader("TECHNICAL SKILLS");
                skillsHeader.setSpacingBefore(headerSpacingBefore);
                document.add(skillsHeader);

                boolean isLong = false;
                for (int i = 0; i <= item.getSkillsItemList().size()-1; i++) {
                    isLong = item.getSkillsItemList().get(i).length() > 24;
                }
                if (isLong){
                    document.add(createSingleColumnSection(item.getSkillsItemList()));
                }else{
                    document.add(createDualColumnSection(item.getSkillsItemList()));
                }
            }

            //TODO - SOFT SKILLS
            if(item.getSoftSkillsItemList() != null){
                Paragraph softSkillsHeader = createHeader("SOFT SKILLS");
                softSkillsHeader.setSpacingBefore(headerSpacingBefore);
                document.add(softSkillsHeader);
                document.add(createDualColumnSection(item.getSoftSkillsItemList()));
            }

            //TODO - EDUCATION
            if(item.getEducationItemList() != null){
                Paragraph educationHeader = createHeader("EDUCATION");
                educationHeader.setSpacingBefore(headerSpacingBefore);
                educationHeader.setSpacingAfter(headerSpacingAfter);
                document.add(educationHeader);
                //
                for (int i = 0; i <= item.getEducationItemList().size()-1; i++) {
                    Paragraph educationText = createEducationSection(item.getEducationItemList().get(i));
                    if (i != item.getEducationItemList().size()-1) educationText.setSpacingAfter(multiItemSpacing);
                    document.add(educationText);
                }

            }

            //TODO - PROJECT
            if(item.getProjectItemList() != null){
                Paragraph projectsHeader = createHeader("PROJECTS");
                projectsHeader.setSpacingBefore(headerSpacingBefore);
                projectsHeader.setSpacingAfter(headerSpacingAfter);
                document.add(projectsHeader);
                //
                for (int i = 0; i <= item.getProjectItemList().size()-1; i++) {
                    Paragraph projectSectionItem = createProjectsSection(item.getProjectItemList().get(i));
                    projectSectionItem.setAlignment(Element.ALIGN_JUSTIFIED);
                    if (i != item.getProjectItemList().size()-1) projectSectionItem.setSpacingAfter(multiItemSpacing);
                    document.add(projectSectionItem);
                }
            }


            //TODO - CERTIFICATIONS
            if(item.getCertificationItemList() != null){
                Paragraph certificationsHeader = createHeader("CERTIFICATIONS");
                certificationsHeader.setSpacingBefore(headerSpacingBefore);
                document.add(certificationsHeader);
                //
                document.add(createSingleColumnSection(item.getCertificationItemList()));
            }


            //TODO - HOBBIES
            if(item.getHobbiesItemList() != null){
                Paragraph hobbiesHeader = createHeader("HOBBIES");
                hobbiesHeader.setSpacingBefore(headerSpacingBefore);
                document.add(hobbiesHeader);
                //
                document.add(createDualColumnSection(item.getHobbiesItemList()));
            }


        } catch (DocumentException | IOException de) {
            System.err.println(de.getMessage());
            Log.e("Start", Objects.requireNonNull(de.getMessage()));
        }
        document.close();
    }


    public static Paragraph createContactDetailsSection(String iName,
                                                        String iJobRole,
                                                        String iPhone, String iEmail,
                                                        String iLocation,
                                                        String iLinkCover1, String iLinkCover2,
                                                        String iLink1, String iLink2
    ) {
        Font timesNewRomanName = FontFactory.getFont(FontFactory.TIMES_BOLD, 24);
        Font timesNewRomanRole = FontFactory.getFont(FontFactory.TIMES, 16);
        Font timesNewRomanPlain = FontFactory.getFont(FontFactory.TIMES, 12);
        Font linkFont = FontFactory.getFont(FontFactory.TIMES, 12, Font.UNDERLINE, new Color(0, 0, 255)); // Blue color for hyperlink

        //
        Anchor emailLink = new Anchor(iEmail, linkFont);
        emailLink.setReference("mailto:" + iEmail);
        //START
        //Name
        Paragraph name = new Paragraph(iName, timesNewRomanName);
        name.setAlignment("Center");
        //name.setSpacingAfter(7.5f);
        //Role
        Paragraph role = new Paragraph(iJobRole, timesNewRomanRole);
        role.setAlignment("Center");
        role.setSpacingAfter(2.5f);
        //Contact
        Paragraph contact = new Paragraph();
        contact.add(new Chunk(iPhone + " | ", timesNewRomanPlain));
        contact.add(emailLink);
        contact.setAlignment("Center");
        //Location
        Paragraph location = new Paragraph(iLocation, timesNewRomanPlain);
        location.setAlignment("Center");


        //Liinks
        Paragraph links = new Paragraph();
        if (iLink1 != null && iLinkCover1 != null){
            Anchor link1 = new Anchor(iLink1, linkFont);
            link1.setReference(iLink1);
            links.add(new Chunk(iLinkCover1 + ": ", timesNewRomanPlain));
            links.add(link1);
        }
        if (iLink2 != null && iLinkCover2 != null){
            Anchor link2 = new Anchor(iLink2, linkFont);
            link2.setReference(iLink2);
            //
            if(iLink1 != null && iLinkCover1 != null){
                links.add(new Chunk(" | ", timesNewRomanPlain));
            }
            links.add(new Chunk(iLinkCover2 + ": ", timesNewRomanPlain));
            links.add(link2);
        }
        links.setAlignment("Center");
        links.setSpacingAfter(10f);

        /*links.add(new Chunk(iLinkCover1 + ": ", timesNewRomanPlain));
        links.add(link1);
        links.add(new Chunk(" | ", timesNewRomanPlain));
        links.add(new Chunk(iLinkCover2 + ": ", timesNewRomanPlain));
        links.add(link2);
        links.setAlignment("Center");
        links.setSpacingAfter(10f);*/

        //CLOSE
        Paragraph section = new Paragraph();
        section.add(name);
        section.add(role);
        section.add(contact);
        section.add(location);

        Boolean link1State = iLink1 != null && iLinkCover1 != null;
        Boolean link2State = iLink2 != null && iLinkCover2 != null;
        if (link1State || link2State){
            section.add(links);
        }


        return section;
    }

    public static Paragraph createHeader(String text) {
        Font timesNewRomanBold = FontFactory.getFont(FontFactory.TIMES_BOLD, 12);
        Paragraph header = new Paragraph(text, timesNewRomanBold);
        header.setSpacingAfter(5f);  // Add some spacing after the header
        return header;
    }

    public static Paragraph createObjectiveSection(String text) {
        Font timesNewRomanPlain = FontFactory.getFont(FontFactory.TIMES, 12);

        Paragraph objectiveText = new Paragraph(text, timesNewRomanPlain);
        //
        Paragraph section = new Paragraph();
        section.add(objectiveText);

        return section;
    }

    public static Paragraph createExperienceSection(ExperienceItem experienceItem) {
        Font timesNewRomanBold = FontFactory.getFont(FontFactory.TIMES_BOLD, 12);
        Font timesNewRomanPlain = FontFactory.getFont(FontFactory.TIMES, 12);
        Font timesNewRomanItalics = FontFactory.getFont(FontFactory.TIMES_ITALIC, 12);

        // SET 2
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);  // Make table span the full width of the page
        table.setWidths(new int[]{50, 50});  // Set column widths (50% for left, 50% for right)
        table.setSpacingBefore(3f);  // No space before the table
        table.setSpacingAfter(3f);  // No space after the table
        //
        PdfPCell leftCell = new PdfPCell(new Phrase(0f, experienceItem.getJobRole(), timesNewRomanBold));
        leftCell.setBorder(PdfPCell.NO_BORDER);  // Remove the border
        leftCell.setHorizontalAlignment(Element.ALIGN_LEFT);  // Align text to the left
        leftCell.setPadding(0f);  // Remove padding inside the cell
        table.addCell(leftCell);
        //
        PdfPCell rightCell = new PdfPCell(new Phrase(0f, experienceItem.getWorkDate(), timesNewRomanPlain));
        rightCell.setBorder(PdfPCell.NO_BORDER);  // Remove the border
        rightCell.setHorizontalAlignment(Element.ALIGN_RIGHT);  // Align text to the right
        rightCell.setPadding(0f);  // Remove padding inside the cell
        table.addCell(rightCell);


        //SET 3
        Chunk boldLocation = new Chunk(experienceItem.getCompanyName() + " |", timesNewRomanBold);
        Chunk regularLocation = new Chunk(" " + experienceItem.getCompanyLocation(), timesNewRomanItalics);
        Paragraph locationText = new Paragraph();
        locationText.add(boldLocation);
        locationText.add(regularLocation);


        //SET 4
        List bulletList = new List(List.UNORDERED);
        bulletList.setSymbolIndent(10);
        bulletList.setListSymbol("•");
        for (int i = 0; i <= experienceItem.getExperienceList().size()-1; i++) {
            ListItem item = new ListItem("  " + experienceItem.getExperienceList().get(i), timesNewRomanPlain);
            item.setAlignment("Justify");
            bulletList.add(item);
        }

        //CLOSE
        Paragraph section = new Paragraph();
        //section.add(experienceHeader);
        section.add(table);
        section.add(locationText);
        section.add(bulletList);

        return section;
    }

    public static Paragraph createEducationSection(EducationItem item) {
        Font timesNewRomanBold = FontFactory.getFont(FontFactory.TIMES_BOLD, 12);
        Font timesNewRomanPlain = FontFactory.getFont(FontFactory.TIMES, 12);
        Font timesNewRomanItalics = FontFactory.getFont(FontFactory.TIMES_ITALIC, 12);
        Paragraph section = new Paragraph();

        //
        Chunk schoolName = new Chunk(item.getSchoolName() + " |", timesNewRomanBold);
        Chunk schoolLocation = new Chunk(" " + item.getSchoolLocation(), timesNewRomanItalics);
        Paragraph schoolDetail = new Paragraph();
        schoolDetail.add(schoolName);
        schoolDetail.add(schoolLocation);
        //
        PdfPTable educationTable = new PdfPTable(2);
        educationTable.setWidthPercentage(100);
        educationTable.setWidths(new int[]{65, 35});
        educationTable.setSpacingBefore(3f);
        //
        PdfPCell schoolDetailItem = new PdfPCell(schoolDetail);
        schoolDetailItem.setBorder(PdfPCell.NO_BORDER);
        schoolDetailItem.setHorizontalAlignment(Element.ALIGN_LEFT);
        schoolDetailItem.setPadding(0f);
        educationTable.addCell(schoolDetailItem);
        //
        PdfPCell graduatedDate = new PdfPCell(new Phrase(0f, item.getGraduatedMonthYear(), timesNewRomanPlain));
        graduatedDate.setBorder(PdfPCell.NO_BORDER);
        graduatedDate.setHorizontalAlignment(Element.ALIGN_RIGHT);
        graduatedDate.setPadding(0f);
        educationTable.addCell(graduatedDate);
        //
        section.add(educationTable);


        //DEGREE DETAILS
        Paragraph degreeDetail = new Paragraph(item.getDegreeDetail(), timesNewRomanPlain);
        degreeDetail.setAlignment("Justify");
        degreeDetail.setSpacingAfter(10f);
        section.add(degreeDetail);

        return section;
    }

    public static Paragraph createEducationSectionOld(java.util.List<EducationItem> item) {
        Font timesNewRomanBold = FontFactory.getFont(FontFactory.TIMES_BOLD, 12);
        Font timesNewRomanPlain = FontFactory.getFont(FontFactory.TIMES, 12);
        Font timesNewRomanItalics = FontFactory.getFont(FontFactory.TIMES_ITALIC, 12);
        Paragraph section = new Paragraph();

        //SET 1
        /*Paragraph educationHeader = new Paragraph("EDUCATION", timesNewRomanBold);
        educationHeader.setSpacingAfter(5f);
        section.add(educationHeader);*/


        for (int i = 0; i <= item.size()-1; i++) {
            //
            Chunk schoolName = new Chunk(item.get(i).getSchoolName() + " |", timesNewRomanBold);
            Chunk schoolLocation = new Chunk(" " + item.get(i).getSchoolLocation(), timesNewRomanItalics);
            Paragraph schoolDetail = new Paragraph();
            schoolDetail.add(schoolName);
            schoolDetail.add(schoolLocation);
            //
            PdfPTable educationTable = new PdfPTable(2);
            educationTable.setWidthPercentage(100);
            educationTable.setWidths(new int[]{65, 35});
            educationTable.setSpacingBefore(3f);
            //
            PdfPCell schoolDetailItem = new PdfPCell(schoolDetail);
            schoolDetailItem.setBorder(PdfPCell.NO_BORDER);
            schoolDetailItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            schoolDetailItem.setPadding(0f);
            educationTable.addCell(schoolDetailItem);
            //
            PdfPCell graduatedDate = new PdfPCell(new Phrase(0f, item.get(i).getGraduatedMonthYear(), timesNewRomanPlain));
            graduatedDate.setBorder(PdfPCell.NO_BORDER);
            graduatedDate.setHorizontalAlignment(Element.ALIGN_RIGHT);
            graduatedDate.setPadding(0f);
            educationTable.addCell(graduatedDate);
            //
            section.add(educationTable);


            //DEGREE DETAILS
            Paragraph degreeDetail = new Paragraph(item.get(i).getDegreeDetail(), timesNewRomanPlain);
            degreeDetail.setAlignment("Justify");
            degreeDetail.setSpacingAfter(10f);
            section.add(degreeDetail);
        }
        return section;
    }

    public static Paragraph createProjectsSection(ProjectItem item) {
        Font timesNewRomanBold = FontFactory.getFont(FontFactory.TIMES_BOLD, 12);
        Font timesNewRomanPlain = FontFactory.getFont(FontFactory.TIMES, 12);
        Paragraph section = new Paragraph();

        //SET
        Paragraph projectTitle = new Paragraph(item.getProjectTitle(), timesNewRomanBold);
        Paragraph projectDetail = new Paragraph(item.getProjectDetail(), timesNewRomanPlain);
        projectDetail.setAlignment("Justify");
        //
        section.add(projectTitle);
        section.add(projectDetail);
        return section;
    }

    public static Paragraph createSingleColumnSection(java.util.List<String> list) {
        Font timesNewRomanBold = FontFactory.getFont(FontFactory.TIMES_BOLD, 12);
        Font timesNewRomanPlain = FontFactory.getFont(FontFactory.TIMES, 12);

        //SET
        List bulletList = new List(List.UNORDERED);
        bulletList.setSymbolIndent(10);
        bulletList.setListSymbol("•");
        for (int i = 0; i <= list.size()-1; i++) {
            ListItem item = new ListItem("  " + list.get(i), timesNewRomanPlain);
            item.setAlignment("Justify");
            bulletList.add(item);
        }

        //CLOSE
        Paragraph section = new Paragraph();
        section.add(bulletList);

        return section;
    }

    public static Paragraph createDualColumnSection(java.util.List<String> dualItem) {
        Font timesNewRomanBold = FontFactory.getFont(FontFactory.TIMES_BOLD, 12);
        Font timesNewRomanPlain = FontFactory.getFont(FontFactory.TIMES, 12);
        java.util.List<String> listA = new ArrayList<>();
        java.util.List<String> listB = new ArrayList<>();
        splitList(dualItem, listA, listB);


        //HOBBIES
        //Paragraph dualHeader = new Paragraph(title, timesNewRomanBold);

        //HOBBY ITEMS
        List dualList1 = new List(List.UNORDERED);
        dualList1.setSymbolIndent(12);
        dualList1.setListSymbol("•");
        for (int i = 0; i <= listA.size()-1; i++) {
            dualList1.add(new ListItem("  " + listA.get(i), timesNewRomanPlain));
        }
        //
        List dualList2 = new List(List.UNORDERED);
        dualList2.setSymbolIndent(12);
        dualList2.setListSymbol("•");
        for (int i = 0; i <= listB.size()-1; i++) {
            dualList2.add(new ListItem("  " + listB.get(i), timesNewRomanPlain));
        }
        //-----------------------------------------------------------------------------
        PdfPTable dualListTable = new PdfPTable(2);
        dualListTable.setHeaderRows(0);
        dualListTable.setWidthPercentage(100);
        //
        PdfPCell dualCell1 = new PdfPCell();
        dualCell1.addElement(dualList1);
        dualCell1.setBorder(PdfPCell.NO_BORDER);
        dualCell1.setPadding(0f);
        dualListTable.addCell(dualCell1);
        //
        PdfPCell dualCell2 = new PdfPCell();
        dualCell2.addElement(dualList2);
        dualCell2.setBorder(PdfPCell.NO_BORDER);
        dualCell2.setPadding(0f);
        dualListTable.addCell(dualCell2);

        //CLOSE
        Paragraph section = new Paragraph();
        //section.add(dualHeader);
        section.add(dualListTable);

        return section;
    }

    public static void splitList(java.util.List<String> originalList, java.util.List<String> listA, java.util.List<String> listB) {
        int middle = (originalList.size() + 1) / 2;
        listA.addAll(originalList.subList(0, middle));
        listB.addAll(originalList.subList(middle, originalList.size()));
    }

    private ResumeItem createSampleResumeItem() {
        java.util.List<ExperienceItem> fullExperienceList = new ArrayList<>();
        java.util.List<String> expItemList = new ArrayList<>();
        expItemList.add("Led the design and execution of over 30 branding campaigns, creating comprehensive visual identities including logos, business cards, and packaging for clients, resulting in a 25% increase in client retention.");
        expItemList.add("Collaborated with cross-functional teams including marketing, copywriters, and production to develop digital marketing materials, contributing to a 40% increase in social media engagement.");
        expItemList.add("Spearheaded the redesign of the company's website, optimizing user experience and aligning the site with the company's evolving brand strategy, resulting in a 20% boost in online lead generation.");
        expItemList.add("Conceptualized and produced event design materials such as large-scale print graphics and interactive digital displays, ensuring consistent brand messaging across all touchpoints.");
        expItemList.add("Managed multiple design projects concurrently, consistently meeting deadlines and maintaining high standards, which resulted in 100% on-time project delivery.");
        ExperienceItem experienceItem = new ExperienceItem(
                "Graphics Designer",
                "Creative Minds Studio",
                "New York, NY",
                "August 2021 – Present",
                expItemList
        );
        fullExperienceList.add(experienceItem);

        //Education
        java.util.List<EducationItem> fullEducationList = new ArrayList<>();
        EducationItem educationItem1 = new EducationItem(
                "New York School of Visual Arts",
                "New York, NY",
                "Graduated: May 2019",
                "Bachelor of Fine Arts in Graphic Design."
        );

        EducationItem educationItem2 = new EducationItem(
                "Olabisi Onabanjo University",
                "Lagos,Nigeria",
                "Graduated: January 2022",
                "Bachelor of Science in Architecture."
        );
        fullEducationList.add(educationItem1);
        fullEducationList.add(educationItem2);

        //PROJECT
        java.util.List<ProjectItem> fullProjectsList = new ArrayList<>();
        ProjectItem projectItem1 = new ProjectItem(
                "Expense Tracker App:",
                "Developed a personal finance app using Kotlin and MVVM to track income, expenses, and set budgets, contributing to a personal project showcasing proficiency in Android development."
        );

        ProjectItem projectItem2 = new ProjectItem(
                "Educational Quiz App:",
                "Designed and developed a quiz app featuring various subjects and difficulty levels, utilizing Kotlin, Jetpack Compose, and Firebase Realtime Database for data storage."
        );
        fullProjectsList.add(projectItem1);
        fullProjectsList.add(projectItem2);

        //Skills
        java.util.List<String> fullSkillsList = new ArrayList<>();
        fullSkillsList.add("Design Software: Adobe Creative Suite (Illustrator, Photoshop, InDesign), Sketch, Figma, Adobe photoshop");
        fullSkillsList.add("Web Design: HTML, CSS, WordPress");
        fullSkillsList.add("Branding: Logo Design, Brand Identity, Typography, Email marketing, Brand guidelines");
        fullSkillsList.add("Branding: Logo Design, Brand Identity, Typography, Email marketing, Brand guidelines");
        fullSkillsList.add("Project Management: Client Communication, Social Media Content Creation");
        fullSkillsList.add("Others: Editing, Research, Google docs");

        //Soft skills
        java.util.List<String> fullSoftSkillsList = new ArrayList<>();
        fullSoftSkillsList.add("Work independently");
        fullSoftSkillsList.add("Attention to detail");
        fullSoftSkillsList.add("Interpersonal skills");
        fullSoftSkillsList.add("Collaborative");
        fullSoftSkillsList.add("Flexible");
        fullSoftSkillsList.add("Brainstorming");
        fullSoftSkillsList.add("Audiences");
        fullSoftSkillsList.add("Problem-solving");
        fullSoftSkillsList.add("Prioritize tasks");
        fullSoftSkillsList.add("Collaboration");

        //Certifications
        java.util.List<String> certificationsList = new ArrayList<>();
        certificationsList.add("Google Analytic Certification - 2023");
        certificationsList.add("Google Ads Certification - 2023");

        //Hobbies
        java.util.List<String> hobbiesList = new ArrayList<>();
        hobbiesList.add("Hiking");
        hobbiesList.add("Acting");
        hobbiesList.add("Travelling");

        return new ResumeItem(
                "Liam Harper ",
                "Graphics Designer ",
                "(555) 123-4567 ",
                "liam.harper@example.com ",
                "123 Creative Avenue, New York, NY 10001 ",
                "Github ",
                "www.github.com/Olaoluwa99 ",
                "Portfolio ",
                "www.google.com ",
                "Innovative and detail-oriented Graphics Designer with 3 years of experience creating compelling visual content. Seeking to leverage my skills in Adobe Creative Suite, branding, and web design to contribute to a dynamic team. ",
                fullExperienceList,
                fullEducationList,
                fullProjectsList,
                fullSkillsList,
                fullSoftSkillsList,
                certificationsList,
                hobbiesList
        );
    }
}
