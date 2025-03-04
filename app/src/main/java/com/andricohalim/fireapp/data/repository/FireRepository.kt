package com.andricohalim.fireapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.andricohalim.fireapp.data.response.FireResponse
import com.andricohalim.fireapp.data.response.LoginResponse
import com.andricohalim.fireapp.data.response.RegisterResponse
import com.andricohalim.fireapp.data.retrofit.ApiService
import com.andricohalim.fireapp.util.UserPreference
import com.google.gson.Gson
import com.andricohalim.fireapp.data.response.Result
import com.andricohalim.fireapp.data.retrofit.UserModel
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class FireRepository (
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {
    fun register(username: String, email: String, password: String, location: String): LiveData<Result<RegisterResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val registerResponse = apiService.register(username, email, password, location)
                emit(Result.Success(registerResponse))
            } catch (e: HttpException) {
                val error = e.response()?.errorBody()?.string()
                val errorRes = Gson().fromJson(error, RegisterResponse::class.java)
                Log.d(TAG, "register: ${e.message.toString()}")
                emit(Result.Error(errorRes.message))
            } catch (e: Exception){
                emit(Result.Error("Internet Connection Problem"))
            }
        }

    fun login(email: String, password: String): LiveData<Result<LoginResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val loginResponse = apiService.login(email, password)
                emit(Result.Success(loginResponse))

            } catch (e: HttpException) {
                val error = e.response()?.errorBody()?.string()
                val errorRes = Gson().fromJson(error, LoginResponse::class.java)
                Log.d(TAG, "login: ${e.message.toString()}")
                emit(Result.Error(errorRes.message))
            }catch (e: Exception){
                emit(Result.Error("Internet Connection Problem"))
            }

        }

    fun getLatestData(): LiveData<Result<FireResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val storyResponse = apiService.getLatestData()
                val responseJson = Gson().toJson(storyResponse)
                Log.d("FireRepository", "Latest Data Response: $responseJson") // 🔥 Cek JSON Response
                emit(Result.Success(storyResponse))
            } catch (e: HttpException) {
                val error = e.response()?.errorBody()?.string()
                Log.e("FireRepository", "Latest Data Error: $error") // 🔥 Cek Error Response JSON
                val errorRes = Gson().fromJson(error, FireResponse::class.java)
                emit(Result.Error(errorRes.message))
            } catch (e: Exception) {
                emit(Result.Error("Internet Connection Problem"))
            }
        }


    suspend fun logout(){
        userPreference.logout()
    }
    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    companion object {
        private const val TAG = "UserRepository"
    }
}