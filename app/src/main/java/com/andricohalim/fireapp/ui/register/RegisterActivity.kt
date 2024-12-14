package com.andricohalim.fireapp.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.andricohalim.fireapp.MainActivity
import com.andricohalim.fireapp.R
import com.andricohalim.fireapp.data.ViewModelFactory
import com.andricohalim.fireapp.data.model.User
import com.andricohalim.fireapp.databinding.ActivityRegisterBinding
import com.andricohalim.fireapp.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerViewModel.checkUserLoginStatus()

        registerViewModel.isUserLoggedIn.observe(this, Observer { isLoggedIn ->
            if (isLoggedIn) {
                navigateToMainActivity()
            } else {
                setContentView(R.layout.activity_register)
                setupRegister()
            }
        })
    }

    private fun setupRegister() {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val username = binding.etUsername.text.toString()
            val user = User(email = email, password = password, username = username)

            registerViewModel.registerUser(user)
        }
        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        registerViewModel.registerResult.observe(this) { (success, message) ->
            if (success) {
                navigateToMainActivity()
            } else {
                Toast.makeText(this, "$message", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
