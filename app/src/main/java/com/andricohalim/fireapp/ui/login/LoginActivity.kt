package com.andricohalim.fireapp.ui.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.andricohalim.fireapp.MainActivity
import com.andricohalim.fireapp.R
import com.andricohalim.fireapp.data.ViewModelFactory
import com.andricohalim.fireapp.data.response.LoginResult
import com.andricohalim.fireapp.databinding.ActivityLoginBinding
import com.andricohalim.fireapp.ui.register.RegisterActivity
import com.andricohalim.fireapp.data.response.Result
import com.andricohalim.fireapp.data.retrofit.UserModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupView()
        setupAction()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.tvRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            loginUser(email, password)
        }
    }

    private fun loginUser(email: String, password: String) {
        loginViewModel.loginUser(email, password).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    setupAction()
                    AlertDialog.Builder(this).apply {
                        setTitle(getString(R.string.sukses_login))
                        setMessage(getString(R.string.login_berhasil))
                        setPositiveButton(getString(R.string.lanjut)) { _, _ ->
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        show()
                    }
                    saveSession(result.data.loginResult)
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Log.e("DEBUG_LOGIN", "Login gagal: ${result.error}") // 🔍 Tambahkan log error
                }
            }
        }
    }


    private fun saveSession(token: LoginResult) {
        loginViewModel.saveSession(UserModel(token.token))
        Log.d("Token disimpan", token.token)
    }

}