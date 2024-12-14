package com.andricohalim.fireapp.data.repository

import android.content.Context
import android.util.Log
import com.andricohalim.fireapp.data.model.DataFire
import com.andricohalim.fireapp.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.andricohalim.fireapp.util.Reference

class FireRepository(context: Context) {
    private val auth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore by lazy {
        Firebase.firestore
    }

    fun registerUser(user: User, onResult: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                    val appInfo = mapOf("accountType" to "Pantauin Damkar")
                    db.collection("Damkar").document(userId).set(
                        mapOf(
                            "email" to user.email,
                            "password" to user.password,
                            "username" to user.username,
                            "accountType" to appInfo["accountType"]
                        )
                    ).addOnSuccessListener {
                        onResult(true, null)
                    }.addOnFailureListener { e ->
                        onResult(false, e.message)
                    }
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

    fun loginUser(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                    val userDoc = db.collection("Damkar").document(userId)

                    userDoc.get().addOnSuccessListener { documentSnapshot ->
                        val accountType = documentSnapshot.getString("accountType")
                        val currentAccount = "Pantauin Damkar"

                        if (accountType != currentAccount) {
                            onResult(false, "Akun Tidak Valid")
                        } else {
                            onResult(true, null)
                        }
                    }
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

    fun isUserLoggedIn(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }

    fun getUserByUID(uid: String, callback: (Result<Map<String, Any>>) -> Unit) {
        val userDocRef = db.collection("Damkar").document(uid)
        userDocRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    callback(Result.success(documentSnapshot.data ?: emptyMap()))
                } else {
                    callback(Result.failure(Exception("Data Tidak Ditemukan")))
                }
            }
            .addOnFailureListener { exception ->
                callback(Result.failure(exception))
            }
    }

    fun logout() {
        auth.signOut()
    }

    fun listenToAllDevicesRealtime(onDataChanged: (List<DataFire>) -> Unit, onError: (Exception) -> Unit) {
        db.collection(Reference.COLLECTION).addSnapshotListener { snapshot, error ->
            if (error != null) {
                onError(error)
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
                val allDevicesData = mutableListOf<DataFire>()
                snapshot.documents.forEach { deviceDoc ->
                    val deviceId = deviceDoc.id
                    db.collection(Reference.COLLECTION)
                        .document(deviceId)
                        .collection(Reference.DATA_ALAT)
                        .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                        .limit(1)
                        .addSnapshotListener { querySnapshot, queryError ->
                            if (queryError != null) {
                                onError(queryError)
                                return@addSnapshotListener
                            }

                            querySnapshot?.documents?.forEach { documentSnapshot ->
                                val data = DataFire(
                                    flameDetected = documentSnapshot.getString("FlameDetected"),
                                    hum = documentSnapshot.getDouble("Humidity"),
                                    mqValue = documentSnapshot.getString("MQValue"),
                                    temp = documentSnapshot.getDouble("Temperature"),
                                    timestamp = documentSnapshot.getTimestamp("timestamp"),
                                    deviceId = deviceId
                                )
                                allDevicesData.add(data)
                            }
                            onDataChanged(allDevicesData)
                        }
                }
            }
        }
    }

    fun getLocationForDevice(deviceId: String, onResult: (String?, Exception?) -> Unit) {
        db.collection(Reference.COLLECTION)
            .document(deviceId)
            .collection(Reference.DATA_USER)
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.isEmpty) {
                    onResult(null, Exception("No DataUser found for deviceId: $deviceId"))
                    return@addOnSuccessListener
                }

                for (document in snapshot.documents) {
                    val location = document.getString("location")
                    if (location != null) {
                        Log.d("Firestore", "Location found: $location")
                        onResult(location, null)
                        return@addOnSuccessListener
                    }
                }
                onResult(null, Exception("Location not found"))
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error getting location: ${e.message}")
                onResult(null, e)
            }
    }

    companion object {
        @Volatile
        private var instances: FireRepository? = null

        fun getInstance(context: Context):FireRepository =
            instances ?: synchronized(this){
                instances ?:FireRepository(context)
                    .also {
                        instances = it
                    }
            }
    }
}
