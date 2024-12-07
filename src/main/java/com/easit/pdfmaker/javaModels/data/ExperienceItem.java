package com.easit.pdfmaker.javaModels.data;

import java.util.List;

public class ExperienceItem {
    // Fields
    private String jobRole;
    private String companyName;
    private String companyLocation;
    private String workDate;
    private List<String> experienceList;

    // Constructor
    public ExperienceItem(String jobRole, String companyName, String companyLocation, String workDate,  List<String> experienceList) {
        this.jobRole = jobRole;
        this.companyName = companyName;
        this.companyLocation = companyLocation;
        this.workDate = workDate;
        this.experienceList = experienceList;
    }

    // Getter methods
    public String getJobRole() {
        return jobRole;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyLocation() {
        return companyLocation;
    }

    public String getWorkDate() {
        return workDate;
    }

    public List<String> getExperienceList() {
        return experienceList;
    }


    // Setter methods
    public void setJobRole(String jobRole) {
        this.jobRole = jobRole;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setCompanyLocation(String companyLocation) {
        this.companyLocation = companyLocation;
    }

    public void setWorkDate(String workDate) {
        this.workDate = workDate;
    }

    public void setExperienceList(List<String> experienceList) {
        this.experienceList = experienceList;
    }
}
