package com.udacity.project4.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map

class AuthenticationViewModel : ViewModel() {

    val authentication = FireBaseLiveData().map { firebaseUser ->
        if (firebaseUser != null)
            AuthenticationState.AUTHENTICATED
        else
            AuthenticationState.UNAUTHENTICATED
    }


    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED
    }
}