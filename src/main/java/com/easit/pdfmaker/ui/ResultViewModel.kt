package com.easit.pdfmaker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easit.pdfmaker.constants.Constant
import com.easit.pdfmaker.kotlinModels.AllResultData
import com.easit.pdfmaker.kotlinModels.PdfMakerHistory
import com.easit.pdfmaker.kotlinModels.PdfMakerUser
import com.easit.pdfmaker.serializeAllResultData
import com.easit.pdfmaker.utils.DateTimeUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ResultViewModel: ViewModel() {

    /**/
    private val _status = MutableStateFlow(Constant.INACTIVE)
    var status = _status.asStateFlow()

    private val _resultData = MutableStateFlow(AllResultData())
    var resultData = _resultData.asStateFlow()

    private val _delayCompleted = MutableStateFlow(ReloadState.INITIAL)
    var delayCompleted = _delayCompleted.asStateFlow()


    //--------------------------------------------------------------------------------------------//
    fun setUserItem(data: AllResultData){
        _resultData.value = data
    }

    fun delayTimer(time: Long) {
        _delayCompleted.value = ReloadState.ACTIVE
        viewModelScope.launch {
            delay(time)
            _delayCompleted.value = ReloadState.COMPLETED
        }
    }

    fun resetDelay(){
        _delayCompleted.value = ReloadState.INITIAL
    }
}