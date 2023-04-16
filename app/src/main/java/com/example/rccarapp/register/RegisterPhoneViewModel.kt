package com.example.rccarapp.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterPhoneViewModel:ViewModel() {

    private var _phoneNumber = MutableLiveData("")
    val phoneNumber = _phoneNumber
}