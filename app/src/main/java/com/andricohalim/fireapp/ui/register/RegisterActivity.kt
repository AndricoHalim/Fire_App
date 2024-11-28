package com.andricohalim.fireapp.ui.register

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.andricohalim.fireapp.MainActivity
import com.andricohalim.fireapp.R
import com.andricohalim.fireapp.databinding.ActivityLoginBinding
import com.andricohalim.fireapp.databinding.ActivityRegisterBinding
import com.andricohalim.fireapp.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.apply {
            tvLogin.setOnClickListener {
                val loginIntent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(loginIntent)
                finish()
            }
        }
    }
}