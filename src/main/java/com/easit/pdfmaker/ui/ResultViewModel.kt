package com.easit.pdfmaker.ui

import androidx.lifecycle.ViewModel
import com.easit.pdfmaker.constants.Constant
import com.easit.pdfmaker.kotlinModels.AllResultData
import com.easit.pdfmaker.kotlinModels.PdfMakerHistory
import com.easit.pdfmaker.kotlinModels.PdfMakerUser
import com.easit.pdfmaker.serializeAllResultData
import com.easit.pdfmaker.utils.DateTimeUtil
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ResultViewModel: ViewModel() {
    /**/
    private val db = Firebase.firestore
    private val auth = Firebase.auth

    private val _dateTimeMilli = MutableStateFlow(100000000000000)
    var dateTimeMilli = _dateTimeMilli.asStateFlow()

    private val _resumeId = MutableStateFlow("")
    var resumeId = _resumeId.asStateFlow()

    private val _profileName = MutableStateFlow("")
    var profileName = _profileName.asStateFlow()

    private val _userDetailText = MutableStateFlow("")
    var userDetailText = _userDetailText.asStateFlow()

    private val _jobDescription = MutableStateFlow("")
    var jobDescription = _jobDescription.asStateFlow()

    private val _retrievalStatus = MutableStateFlow(Constant.INACTIVE)
    var retrievalStatus = _retrievalStatus.asStateFlow()

    private val _status = MutableStateFlow(0)
    var status = _status.asStateFlow()

    private val _user = MutableStateFlow(PdfMakerUser())
    var user = _user.asStateFlow()

    private val _resultData = MutableStateFlow(AllResultData())
    var resultData = _resultData.asStateFlow()


    //--------------------------------------------------------------------------------------------//
    fun setUserItem(user: PdfMakerUser, data: AllResultData, defaultProfileName: String, defaultJobDescription: String, defaultUserDetailText: String){
        _user.value = user
        _resultData.value = data
        _profileName.value = defaultProfileName
        _userDetailText.value = defaultUserDetailText
        _jobDescription.value = defaultJobDescription
    }

    private fun addToHistory() {
        val localDateTime = DateTimeUtil.now()
        _dateTimeMilli.value = DateTimeUtil.toEpochMillis(localDateTime)
        val stringDateTime = DateTimeUtil.formatNoteDate(localDateTime)
        _resumeId.value = "RES-${dateTimeMilli.value}"

        val history = PdfMakerHistory(
            id = resumeId.value,
            companyName = "BAS-${dateTimeMilli.value}",
            profileType = profileName.value,
            dateTime = stringDateTime,
            text = serializeAllResultData(resultData.value),
            markdownText = "",
            jobDescription = jobDescription.value,
            keywords = "",
            userDetails = userDetailText.value,
            htmlCoverLetter = "",
            plainCoverLetter = ""
        )
        db
            .collection(Constant.USER)
            .document(auth.uid!!)
            .update("historyList", FieldValue.arrayUnion(history))
            .addOnSuccessListener {
                _status.value = Constant.SUCCESS
            }
            .addOnFailureListener { _status.value = Constant.FAILED_HISTORY }//Unable to save to History
    }

}