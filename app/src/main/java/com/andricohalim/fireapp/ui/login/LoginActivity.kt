package com.andricohalim.fireapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.andricohalim.fireapp.MainActivity
import com.andricohalim.fireapp.data.ViewModelFactory
import com.andricohalim.fireapp.databinding.ActivityLoginBinding
import com.andricohalim.fireapp.ui.register.RegisterActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (isUserLoggedIn()) {
            navigateToMainActivity()
            return
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            binding.progressBar.visibility = android.view.View.VISIBLE

            loginViewModel.loginUser(email, password)
        }

        binding.tvRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        loginViewModel.loginResult.observe(this) { (success, message) ->
            if (success) {
                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                navigateToMainActivity()
            } else {
                Toast.makeText(this, "Error: $message", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isUserLoggedIn(): Boolean {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser != null
    }

    private fun navigateToMainActivity() {
        val mainIntent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(mainIntent)
        finish()
    }
}
