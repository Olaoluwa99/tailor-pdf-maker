package com.easit.pdfmaker.javaModels.data;

public class CoverLetterItem {
    // Contact
    private String name;
    private String role;
    private String location;
    private String date;

    private String companyName;
    private String companyAddress;
    private String companyLocation;

    private String mainContent;
    private String closingSalutation;

    // TODO - Constructor
    public CoverLetterItem(String name,
                           String role,
                           String location,
                           String date,

                           String companyName,
                           String companyAddress,
                           String companyLocation,

                           String mainContent,
                           String closingSalutation
    ) {
        this.name = name;
        this.role = role;
        this.location = location;
        this.date = date;

        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.companyLocation = companyLocation;

        this.mainContent = mainContent;
        this.closingSalutation = closingSalutation;
    }

    // TODO - Getter methods
    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public String getCompanyLocation() {
        return companyLocation;
    }

    public String getMainContent() {
        return mainContent;
    }

    public String getClosingSalutation() {
        return closingSalutation;
    }


    // TODO - Setter methods
    public void setName(String name) {
        this.name = name;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public void setCompanyLocation(String companyLocation) {
        this.companyLocation = companyLocation;
    }

    public void setMainContent(String mainContent) {
        this.mainContent = mainContent;
    }

    public void setClosingSalutation(String closingSalutation) {
        this.closingSalutation = closingSalutation;
    }
}
