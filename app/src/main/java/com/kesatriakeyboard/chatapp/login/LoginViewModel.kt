package com.kesatriakeyboard.chatapp.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val _loginResult by lazy { MutableLiveData<Status>() }
    val loginResult: LiveData<Status> get() = _loginResult

    fun isLogin(): Boolean {
        val session = firebaseAuth.currentUser
        return session != null
    }

    fun doLogin(email: String, password: String) {
        _loginResult.value = Status.LOADING
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    _loginResult.value = Status.SUCCESS
                } else {
                    _loginResult.value = Status.ERROR
                }
            }
    }
}