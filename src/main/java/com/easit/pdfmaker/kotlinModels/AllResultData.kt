package com.easit.pdfmaker.kotlinModels

import kotlinx.serialization.Serializable

@Serializable
data class AllResultData(
    val resume: ResumeData? = null,
    val coverLetter: CoverLetterData? = null,
    val keywords: List<String> = emptyList()
)

@Serializable
data class CoverLetterData(
    val name: String,
    val role: String,
    val location: String,
    val date: String = "PLACEHOLDER_DATE",

    val companyName: String = "PLACEHOLDER_NAME",
    val companyAddress: String = "PLACEHOLDER_ADDRESS",
    val companyLocation: String = "PLACEHOLDER_LOCATION",

    val mainContent: String,
    val closingSalutation: String
)


@Serializable
data class ResumeData(
    val name: String,
    val role: String,
    val location: String,
    val phone: String,
    val email: String,
    val link1: String? = null,
    val linkCover1: String? = null,
    val link2: String? = null,
    val linkCover2: String? = null,
    val objective: String? = null,
    val experienceList: List<ExperienceItem>? = null,
    val educationList: List<EducationItem>? = null,
    val projectList: List<ProjectItem>? = null,
    val skillsList: List<String>,
    val softSkillsList: List<String>? = null,
    val certificationList: List<String>? = null,
    val hobbiesList: List<String>? = null
)

@Serializable
data class ExperienceItem(
    val experienceRole: String,
    val experienceCompanyName: String,
    val experienceCompanyLocation: String,
    val experienceWorkDate: String,
    val experienceItemsList: List<String>
)

@Serializable
data class EducationItem(
    val degreeEarned: String,
    val schoolName: String,
    val schoolLocation: String,
    val graduatedDate: String
)

@Serializable
data class ProjectItem(
    val projectName: String,
    val projectDetail: String
)