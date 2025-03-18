package com.example.promptpayapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.promptpayapp.data.ResultClass
import com.example.promptpayapp.data.User
import com.example.promptpayapp.data.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    // We need to initialize the userRepository which is dependant on both Firebase and Firestore
    private val userRepository: UserRepository

    // Create an instance of UserRepository within the init block providing it's dependencies
    init {
        userRepository = UserRepository(
            FirebaseAuth.getInstance(),
            Injection.instance()
        )
        checkUserLoginState() // ✅ Check login state when ViewModel initializes
    }

    private val _currentUser = MutableLiveData<User?>()
    val currentUser: LiveData<User?> get() = _currentUser

    // authResult holder for both login and signup because they will be in different processes at all times
    private val _authResult = MutableLiveData<ResultClass<Boolean>>()
    val authResult: LiveData<ResultClass<Boolean>> get() = _authResult

    // ✅ Check if user is already logged in on app launch
    private fun checkUserLoginState() {
        val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        if(firebaseUser != null) {
            loadCurrentUser() // ✅ Load user data if authenticated
        }
    }

    // Launch the signup from the UserRepository by passing the value to authResult
    fun signUp(email: String, password: String, firstName: String, balance: Double) {
        viewModelScope.launch {
            _authResult.value = userRepository.signUp(email, password, firstName, balance)
        }
    }

    // In AuthViewModel we can prepare the function for use and hold the result value using the authResult.
    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = userRepository.login(email, password)
            _authResult.value = result

            if (result is ResultClass.Success) {
                loadCurrentUser() // ✅ Automatically load user after successful login
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            userRepository.signOut()
            _currentUser.postValue(null) // Reset user data on logout
        }
    }

    fun loadCurrentUser() {
        viewModelScope.launch {
            val firebaseUser = FirebaseAuth.getInstance().currentUser
            if (firebaseUser != null) { // ✅ Only fetch user if logged in
                when (val result = userRepository.getCurrentUser()) {
                    is ResultClass.Success -> _currentUser.postValue(result.data)
                    is ResultClass.Error -> {
                        _currentUser.postValue(null) // Reset user on failure
                    }
                }
            } else {
                _currentUser.postValue(null) // No user logged in
            }
        }
    }
}

// Singleton for Firestore instance
// We will call this class injection just because we are trying to prove a single instance of Firestore which is part of what Dependency Injection does.
object Injection {
    private val instance: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    fun instance(): FirebaseFirestore {
        return instance
    }
}