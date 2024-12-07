package com.easit.pdfmaker.javaModels.data;

public class ProjectItem {
    // Fields
    private String projectTitle;
    private String projectDetail;

    // Constructor
    public ProjectItem(String projectTitle, String projectDetail) {
        this.projectTitle = projectTitle;
        this.projectDetail = projectDetail;
    }

    // Getter methods
    public String getProjectTitle() {
        return projectTitle;
    }

    public String getProjectDetail() {
        return projectDetail;
    }


    // Setter methods
    public void setProjectTitle(String projectDetail) {
        this.projectTitle = projectTitle;
    }

    public void setProjectDetail(String projectDetail) {
        this.projectDetail = projectDetail;
    }
}
