package com.easit.pdfmaker.javaModels.data;

public class EducationItem {
    // Fields
    private String schoolName;
    private String schoolLocation;
    private String graduatedMonthYear;
    private String degreeDetail;

    // Constructor
    public EducationItem(String schoolName, String schoolLocation, String graduatedMonthYear, String degreeDetail) {
        this.schoolName = schoolName;
        this.schoolLocation = schoolLocation;
        this.graduatedMonthYear = graduatedMonthYear;
        this.degreeDetail = degreeDetail;
    }

    // Getter methods
    public String getSchoolName() {
        return schoolName;
    }

    public String getSchoolLocation() {
        return schoolLocation;
    }

    public String getGraduatedMonthYear() {
        return graduatedMonthYear;
    }

    public String getDegreeDetail() {
        return degreeDetail;
    }


    // Setter methods
    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public void setSchoolLocation(String schoolLocation) {
        this.schoolLocation = schoolLocation;
    }

    public void setGraduatedMonthYear(String graduatedMonthYear) {
        this.graduatedMonthYear = graduatedMonthYear;
    }

    public void setDegreeDetail(String degreeDetail) {
        this.degreeDetail = degreeDetail;
    }
}
