package com.wngud.sport_together.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.wngud.sport_together.MainActivity
import com.wngud.sport_together.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            loginViewModel.loginKakao(this@LoginActivity)
        }
        observeUiEvent()
    }

    private fun observeUiEvent() = lifecycleScope.launch {
        loginViewModel.eventFlow.collect { event -> handleEvent(event) }
    }

    private fun handleEvent(event: LoginEvent) = when (event) {
        is LoginEvent.MoveToMain -> moveToMain()//
        is LoginEvent.KakaoLoginFail -> {
            showSnackBar("카카오 로그인에 문제가 발생하였습니다.")
        }
    }

    private fun moveToMain() {
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        finish()
    }

    private fun showSnackBar(text: String) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }
}