import android.content.Context
import com.andricohalim.fireapp.data.model.DataFire
import com.andricohalim.fireapp.data.model.User
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.andricohalim.fireapp.util.Reference
import kotlinx.coroutines.tasks.await

class FireRepository(context: Context) {
    private val auth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore by lazy {
        Firebase.firestore
    }

    fun getUser():String? = auth.uid

    fun login(
        email: String, password: String,
        onSuccess: (AuthResult) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener(onSuccess)
            .addOnFailureListener(onFailure)
    }

    fun register(
        user: User,
        email: String,
        password: String,
        onResult: (Void?, Exception?) -> Unit,
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            db.collection(Reference.COLLECTION).document(it.user!!.uid)
                .set(user)
                .addOnSuccessListener { documentReference ->
                    onResult(documentReference, null)
                }
                .addOnFailureListener { e ->
                    onResult(null, e)
                }
        }.addOnFailureListener {
            onResult(null, it)
        }
    }

    fun logout() {
        auth.signOut()
    }

    fun getUserData(onResult: (User?, Exception?) -> Unit) {
        db.collection(Reference.COLLECTION).document(auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val user = documentSnapshot.toObject(User::class.java)
                onResult(user, null)
            }
            .addOnFailureListener { e ->
                onResult(null, e)
            }
    }

    suspend fun getAllLatestSensorData(): List<DataFire> {
        return try {
            val deviceSnapshots = db.collection(Reference.COLLECTION).get().await()
            deviceSnapshots.documents.flatMap { deviceDoc ->
                val deviceId = deviceDoc.id
                println("Processing deviceId: $deviceId") // Debug log
                val querySnapshot = db.collection(Reference.COLLECTION)
                    .document(deviceId)
                    .collection(Reference.DATA_ALAT)
                    .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                    .limit(1)
                    .get()
                    .await()

                querySnapshot.documents.map { documentSnapshot ->
                    DataFire(
                        flameDetected = documentSnapshot.getString("FlameDetected"),
                        hum = documentSnapshot.getDouble("Humidity"),
                        mqValue = documentSnapshot.getString("MQValue"),
                        temp = documentSnapshot.getDouble("Temperature"),
                        timestamp = documentSnapshot.getString("timestamp"),
                        deviceId = deviceId
                    )
                }
            }
        } catch (e: Exception) {
            println("Error: ${e.message}") // Debug log
            emptyList()
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