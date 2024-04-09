package com.wngud.sport_together.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.kakao.sdk.common.util.Utility
import com.wngud.sport_together.MainActivity
import com.wngud.sport_together.databinding.ActivitySplashBinding
import com.wngud.sport_together.ui.onboard.OnboardingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val splashViewModel: SplashViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var keyHash = Utility.getKeyHash(this)
        Log.i("KAKAO", keyHash)

        // 2초 후 메인 화면으로 이동
        Handler(Looper.getMainLooper()).postDelayed({
            observeUiEvent()
        }, 1000)
    }

    private fun observeUiEvent() = lifecycleScope.launch {
        splashViewModel.eventFlow.collect { event -> handleEvent(event) }
    }

    private fun handleEvent(event: SplashEvent) = when (event) {
        is SplashEvent.MoveToMain -> moveToMain()
        is SplashEvent.MoveToOnboard -> moveToOnboard()
    }

    private fun moveToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun moveToOnboard() {
        startActivity(Intent(this, OnboardingActivity::class.java))
        finish()
    }
}