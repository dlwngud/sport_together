package com.wngud.sport_together.ui.onboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.wngud.sport_together.R
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

        binding.vpOnboarding.adapter = ScreenSlidePagerAdapter(this)
        binding.wormDotsIndicatorOnboarding.attachTo(binding.vpOnboarding)

        setButtonTextToPageTransition()
        setViewPagePosition()
    }

    private fun setButtonTextToPageTransition() {
        binding.run {
            vpOnboarding.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    btnOnboarding.text = when(position) {
                        OnboardingPage.THIRD_PAGE.position -> "시작하기"
                        else -> "다음"
                    }
                }
            })
        }
    }

    private fun setViewPagePosition() {
        binding.run {
            btnOnboarding.setOnClickListener {
                vpOnboarding.run {
                    if (currentItem == END_PAGE) {
                        startActivity(Intent(this@OnboardingActivity, LoginActivity::class.java))
                        finish()
                    } else {
                        currentItem += 1
                    }
                }
            }
        }
    }

    private inner class ScreenSlidePagerAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {
        override fun getItemCount(): Int = PAGES_NUMBER
        override fun createFragment(position: Int): Fragment {
            return when (position) {
                OnboardingPage.FIRST_PAGE.position -> {
                    OnboardingFragment.newInstance(
                        descripts = "운동 부족으로 고민이 많으신가요?",
                        lottieResId = R.raw.ani_worry,
                        page = OnboardingPage.FIRST_PAGE.position
                    )
                }

                OnboardingPage.SECOND_PAGE.position -> {
                    OnboardingFragment.newInstance(
                        descripts = "운동친구가 없어서 지루하거나\n혼자 운동하기 어려운가요?",
                        lottieResId = R.raw.ani_alone,
                        page = OnboardingPage.SECOND_PAGE.position
                    )
                }

                else -> {
                    OnboardingFragment.newInstance(
                        descripts = "이제 걱정 끝!\n운동하는 친구들을 쉽게 찾아 봐요!",
                        lottieResId = R.raw.ani_together,
                        page = OnboardingPage.THIRD_PAGE.position
                    )
                }
            }
        }
    }

    companion object {
        private const val PAGES_NUMBER = 3
        private const val END_PAGE = 2
    }
}

enum class OnboardingPage(val position: Int) {
    FIRST_PAGE(0),
    SECOND_PAGE(1),
    THIRD_PAGE(2),
}