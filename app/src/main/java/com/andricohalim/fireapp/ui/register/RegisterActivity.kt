package com.andricohalim.fireapp.ui.register

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.andricohalim.fireapp.MainActivity
import com.andricohalim.fireapp.R
import com.andricohalim.fireapp.data.response.Result
import com.andricohalim.fireapp.data.ViewModelFactory
import com.andricohalim.fireapp.databinding.ActivityRegisterBinding
import com.andricohalim.fireapp.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var progressBar: ProgressBar
    private val registerViewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        progressBar = findViewById(R.id.progressBar)

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

    private fun registerUser(username: String, email: String, password: String, location: String) {
        registerViewModel.registerUser(username, email, password, location).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    setupAction()
                    binding.progressBar.visibility = View.GONE

//                    AlertDialog.Builder(this).apply {
//                        setTitle(getString(R.string.akun_dibuat))
//                        setMessage(
//                            getString(
//                                R.string.akun_dengan_berhasil_dibuat_login_sekarang,
//                                email
//                            ))
//                        setPositiveButton(context.getString(R.string.lanjut)) { _, _ ->
//                            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
//                            startActivity(intent)
//                            finish()
//                        }
//                        show()
//                    }
                }

                is Result.Error -> {
                    progressBar.visibility = View.GONE
//                    AlertDialog.Builder(this).apply {
//                        setTitle(context.getString(R.string.error))
//                        setMessage(result.error)
//                        setPositiveButton(context.getString(R.string.ok)) { p0, _ ->
//                            p0.dismiss()
//                        }
//                    }.create().show()
                }
            }
        }
    }

    private fun setupAction() {
        binding.tvLogin.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnRegister.setOnClickListener {

            val name = binding.etUsername.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val location = binding.etLocation.text.toString()

            registerUser(name, email, password, location)
        }

        binding.etUsername.addTextChangedListener {
            binding.etUsername.error = null
        }
        binding.etEmail.addTextChangedListener {
            binding.etEmail.error = null
        }
        binding.etPassword.addTextChangedListener {
            binding.etPassword.error = null
        }
    }

}