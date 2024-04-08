package com.wngud.sport_together.ui.onboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wngud.sport_together.databinding.ActivityOnboardingBinding
import com.wngud.sport_together.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            btnOnboarding.setOnClickListener {
                startActivity(Intent(this@OnboardingActivity, LoginActivity::class.java))
                finish()
            }
        }
    }
}