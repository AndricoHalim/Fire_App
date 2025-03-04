//package com.andricohalim.fireapp.ui.profile
//
//import android.util.Log
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import com.andricohalim.fireapp.data.repository.FireRepository
//import com.google.firebase.auth.FirebaseAuth
//
//class ProfileViewModel(private val repository: FireRepository) : ViewModel() {
//
//    private val _user = MutableLiveData<Map<String, Any>>()
//    val user: LiveData<Map<String, Any>> get() = _user
//
//    fun loadUserData() {
//        val uid = FirebaseAuth.getInstance().currentUser?.uid
//        if (uid != null) {
//            repository.getUserByUID(uid) { result ->
//                result.onSuccess { data ->
//                    _user.value = data
//                }.onFailure { exception ->
//                    Log.e("ProfileViewModel", "Error loading user data: ${exception.message}")
//                }
//            }
//        } else {
//            Log.e("ProfileViewModel", "No user is currently logged in.")
//        }
//    }
//
//
//    fun logoutUser() {
//        repository.logout()
//    }
//
//
//}