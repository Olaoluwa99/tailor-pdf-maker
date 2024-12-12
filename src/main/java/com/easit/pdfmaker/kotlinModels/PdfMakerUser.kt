package com.easit.pdfmaker.kotlinModels

import androidx.annotation.Keep

@Keep
data class PdfMakerUser(
    val id: String? = null,
    val email: String? = null,
    val username: String? = null,
    val profilePictureUrl: String? = null,
    val historyList: List<PdfMakerHistory>? = null,

    val profileTitleNames: List<String> = listOf("Profile 1", "Profile 2", "Profile 3"),
    val profileFullNames: List<String> = defaultListItem,
    val profileRoles: List<String> = defaultListItem,
    val profileEmails: List<String> = defaultListItem,
    val profilePhones: List<String> = defaultListItem,
    val profileLocations: List<String> = defaultListItem,
    val profileLink1: List<String> = defaultListItem,
    val profileLinkCover1: List<String> = defaultListItem,
    val profileLink2: List<String> = defaultListItem,
    val profileLinkCover2: List<String> = defaultListItem,
    val profileObjectives: List<String> = defaultListItem,
    val profileExperiences: List<String> = defaultListItem,
    val profileEducations: List<String> = defaultListItem,
    val profileProjects: List<String> = defaultListItem,
    val profileSkills: List<String> = defaultListItem,
    val profileCertifications: List<String> = defaultListItem,
    val profileHobbies: List<String> = defaultListItem,
    val profileOthers: List<String> = defaultListItem,

    val defaultResumesJson: List<String> = defaultListItem,
    val defaultCoverLettersJson: List<String> = defaultListItem,

    val hasUsedApp: Boolean = false,
)
val defaultListItem = listOf("", "", "")

@Keep
data class PdfMakerHistory(
    val id: String? = null,
    val companyName: String? = null,
    val profileType: String? = null,
    val dateTime: String? = null,
    val text: String? = null,
    val userDetails: String? = null,
    val markdownText: String? = null,
    val jobDescription: String? = null,
    val keywords: String? = null,
    val htmlCoverLetter: String? = null,
    val plainCoverLetter: String? = null,
    val toShow: Boolean = true
)