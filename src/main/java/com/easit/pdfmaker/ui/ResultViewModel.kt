package com.easit.pdfmaker.ui

import androidx.lifecycle.ViewModel
import com.easit.pdfmaker.constants.Constant
import com.easit.pdfmaker.kotlinModels.AllResultData
import com.easit.pdfmaker.kotlinModels.PdfMakerHistory
import com.easit.pdfmaker.kotlinModels.PdfMakerUser
import com.easit.pdfmaker.serializeAllResultData
import com.easit.pdfmaker.utils.DateTimeUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ResultViewModel: ViewModel() {

    /**/
    private val _status = MutableStateFlow(Constant.INACTIVE)
    var status = _status.asStateFlow()

    private val _resultData = MutableStateFlow(AllResultData())
    var resultData = _resultData.asStateFlow()


    //--------------------------------------------------------------------------------------------//
    fun setUserItem(data: AllResultData){
        _resultData.value = data
    }
}