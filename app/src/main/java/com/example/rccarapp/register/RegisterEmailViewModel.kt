package com.example.rccarapp.register

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterEmailViewModel : ViewModel() {

    private val _userEmailId = MutableLiveData("")
    val userEmailId = _userEmailId
    private val _userPassword = MutableLiveData("")
    val userPassword = _userPassword

    fun validateEmailAddress(email:String):Boolean{
      return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}