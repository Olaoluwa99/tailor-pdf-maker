package com.easit.pdfmaker.javaModels.data;

import com.easit.pdfmaker.javaModels.data.EducationItem;
import com.easit.pdfmaker.javaModels.data.ExperienceItem;
import com.easit.pdfmaker.javaModels.data.ProjectItem;

import java.util.List;

public class ResumeItem {
    // Contact
    private String name;
    private String jobRole;
    private String phone;
    private String email;
    private String location;
    private String linkCover1;
    private String link1;
    private String linkCover2;
    private String link2;
    private String objectiveText;
    private List<ExperienceItem> experienceItemList;
    private List<EducationItem> educationItemList;
    private List<ProjectItem> projectItemList;
    private List<String> skillsItemList;
    private List<String> softSkillsItemList;
    private List<String> certificationItemList;
    private List<String> hobbiesItemList;

    // TODO - Constructor
    public ResumeItem(String name,
                      String jobRole,
                      String phone,
                      String email,
                      String location,
                      String linkCover1,
                      String link1,
                      String linkCover2,
                      String link2,
                      String objectiveText,
                      List<ExperienceItem> experienceItemList,
                      List<EducationItem> educationItemList,
                      List<ProjectItem> projectItemList,
                      List<String> skillsItemList,
                      List<String> softSkillsItemList,
                      List<String> certificationItemList,
                      List<String> hobbiesItemList
    ) {
        this.name = name;
        this.jobRole = jobRole;
        this.phone = phone;
        this.email = email;
        this.location = location;
        this.linkCover1 = linkCover1;
        this.link1 = link1;
        this.linkCover2 = linkCover2;
        this.link2 = link2;
        this.objectiveText = objectiveText;
        this.experienceItemList = experienceItemList;
        this.educationItemList = educationItemList;
        this.projectItemList = projectItemList;
        this.skillsItemList = skillsItemList;
        this.softSkillsItemList = softSkillsItemList;
        this.certificationItemList = certificationItemList;
        this.hobbiesItemList = hobbiesItemList;
    }

    // TODO - Getter methods
    public String getName() {
        return name;
    }

    public String getJobRole() {
        return jobRole;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getLocation() {
        return location;
    }

    public String getLinkCover1() {
        return linkCover1;
    }

    public String getLink1() {
        return link1;
    }

    public String getLinkCover2() {
        return linkCover2;
    }

    public String getLink2() {
        return link2;
    }

    public String getObjectiveText() {
        return objectiveText;
    }

    public List<ExperienceItem> getExperienceItemList() {
        return experienceItemList;
    }

    public List<EducationItem> getEducationItemList() {
        return educationItemList;
    }

    public List<ProjectItem> getProjectItemList() {
        return projectItemList;
    }

    public List<String> getSkillsItemList() {
        return skillsItemList;
    }

    public List<String> getSoftSkillsItemList() {
        return softSkillsItemList;
    }

    public List<String> getCertificationItemList() {
        return certificationItemList;
    }

    public List<String> getHobbiesItemList() {
        return hobbiesItemList;
    }


    // TODO - Setter methods
    public void setName(String schoolName) {
        this.name = name;
    }

    public void setJobRole(String jobRole) {
        this.jobRole = jobRole;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setLinkCover1(String linkCover1) {
        this.linkCover1 = linkCover1;
    }

    public void setLink1(String link1) {
        this.link1 = link1;
    }

    public void setLinkCover2(String linkCover2) {
        this.linkCover2 = linkCover2;
    }

    public void setLink2(String link2) {
        this.link2 = link2;
    }

    public void setObjectiveText(String objectiveText) {
        this.objectiveText = objectiveText;
    }

    public void setExperienceItemList(List<ExperienceItem> experienceItemList) {
        this.experienceItemList = experienceItemList;
    }

    public void setEducationItemList(List<EducationItem> educationItemList) {
        this.educationItemList = educationItemList;
    }

    public void setProjectItemList(List<ProjectItem> projectItemList) {
        this.projectItemList = projectItemList;
    }

    public void setSkillsItemList(List<String> skillsItemList) {
        this.skillsItemList = skillsItemList;
    }

    public void setSoftSkillsItemList(List<String> softSkillsItemList) {
        this.softSkillsItemList = softSkillsItemList;
    }

    public void setCertificationItemList(List<String> certificationItemList) {
        this.certificationItemList = certificationItemList;
    }

    public void setHobbiesItemList(List<String> hobbiesItemList) {
        this.hobbiesItemList = hobbiesItemList;
    }
}
