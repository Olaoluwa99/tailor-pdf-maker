package com.easit.pdfmaker.ui.editResume

import androidx.lifecycle.ViewModel
import com.easit.pdfmaker.constants.Constant
import com.easit.pdfmaker.kotlinModels.User
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class EditResumeViewModel: ViewModel() {
    /**/
    private val db = Firebase.firestore
    private val auth = Firebase.auth

    private val _profileNameTextField = MutableStateFlow("")
    var profileNameTextField = _profileNameTextField.asStateFlow()

    private val _updateSaved = MutableStateFlow(true)
    var updateSaved = _updateSaved.asStateFlow()

    private val _fullNameTextField = MutableStateFlow("")
    var fullNameTextField = _fullNameTextField.asStateFlow()

    private val _fullResume = MutableStateFlow("")
    var fullResume = _fullResume.asStateFlow()

    private val _emailTextField = MutableStateFlow("")
    var emailTextField = _emailTextField.asStateFlow()

    private val _phoneNoTextField = MutableStateFlow("")
    var phoneNoTextField = _phoneNoTextField.asStateFlow()

    private val _locationTextField = MutableStateFlow("")
    var locationTextField = _locationTextField.asStateFlow()

    private val _experienceTextField = MutableStateFlow("")
    var experienceTextField = _experienceTextField.asStateFlow()

    private val _educationTextField = MutableStateFlow("")
    var educationTextField = _educationTextField.asStateFlow()

    private val _projectsTextField = MutableStateFlow("")
    var projectsTextField = _projectsTextField.asStateFlow()

    private val _skillsTextField = MutableStateFlow("")
    var skillsTextField = _skillsTextField.asStateFlow()

    private val _certificationsTextField = MutableStateFlow("")
    var certificationsTextField = _certificationsTextField.asStateFlow()

    private val _hobbiesTextField = MutableStateFlow("")
    var hobbiesTextField = _hobbiesTextField.asStateFlow()

    private val _othersTextField = MutableStateFlow("")
    var othersTextField = _othersTextField.asStateFlow()

    private val _uploadStatus = MutableStateFlow(0)
    var uploadStatus = _uploadStatus.asStateFlow()

    private val _retrievalStatus = MutableStateFlow(Constant.INACTIVE)
    var retrievalStatus = _retrievalStatus.asStateFlow()

    private val _setFileStatus = MutableStateFlow(Constant.INACTIVE)
    var setFileStatus = _setFileStatus.asStateFlow()

    private val _retrieveIdCount = MutableStateFlow(0)
    var retrieveIdCount = _retrieveIdCount.asStateFlow()

    private val _selectedProfileId = MutableStateFlow(1)
    var selectedProfileId = _selectedProfileId.asStateFlow()

    private val _retrievedDetailsSet = MutableStateFlow(false)
    var retrievedDetailsSet = _retrievedDetailsSet.asStateFlow()

    private val _user = MutableStateFlow(User())
    var user = _user.asStateFlow()

    private val _selectedTutorItem = MutableStateFlow(Constant.EXPERIENCES)
    var selectedTutorItem = _selectedTutorItem.asStateFlow()

    private val _selectedSpeechItem = MutableStateFlow(Constant.EXPERIENCES)
    var selectedSpeechItem = _selectedSpeechItem.asStateFlow()

    private val _hasTutorShown = MutableStateFlow(false)
    var hasTutorShown = _hasTutorShown.asStateFlow()


    //--------------------------------------------------------------------------------------------//
    init {
        //retrieveUser()
    }

    /*fun onPhoneNoTextField(text: String) {
        _phoneNoTextField.value = text

        if (profilesList[selectedProfileId.value-1].phone != phoneNoTextField.value){
            _updateSaved.value = false
        }
    }

    fun onLocationTextField(text: String) {
        _locationTextField.value = text

        if (profilesList[selectedProfileId.value-1].location != locationTextField.value){
            _updateSaved.value = false
        }
    }*/
}