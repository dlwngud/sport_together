package com.wngud.sport_together.ui.onboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.wngud.sport_together.R
import com.wngud.sport_together.databinding.FragmentOnboardingBinding

const val ARG_PARM1 = "param1"
const val ARG_PARM2 = "param2"
const val ARG_PARM3 = "param3"

class OnboardingFragment : Fragment() {

    private lateinit var binding: FragmentOnboardingBinding
    private var descript: String? = null
    private var lottieResId: Int? = null
    private var page: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnboardingBinding.inflate(layoutInflater, container, false)

        arguments?.let {
            descript = it.getString(ARG_PARM1)
            lottieResId = it.getInt(ARG_PARM2)
            page = it.getInt(ARG_PARM3)
        }
        setOnboarding()

        return binding.root
    }

    private fun setOnboarding() {
        binding.run {
            when (page) {
                OnboardingPage.FIRST_PAGE.position -> showFirstPage()
                OnboardingPage.SECOND_PAGE.position -> showSecondPage()
                else -> showThirdPage()
            }
        }
    }

    private fun showFirstPage() {
        with(binding) {
            lottieOnboarding.apply {
                lottieResId?.let { setAnimation(it) }
                playAnimation()
            }
            descript?.let {
                tvOnboarding.text = it
            }
        }
    }

    private fun showSecondPage() {
        with(binding) {
            lottieOnboarding.apply {
                lottieResId?.let { setAnimation(it) }
                playAnimation()
            }
            descript?.let {
                tvOnboarding.text = it
            }
        }
    }

    private fun showThirdPage() {
        with(binding) {
            lottieOnboarding.apply {
                lottieResId?.let { setAnimation(it) }
                playAnimation()
            }
            descript?.let {
                tvOnboarding.text = it
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(descripts: String, lottieResId: Int, page: Int): Fragment {
            return OnboardingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARM1, descripts)
                    putInt(ARG_PARM2, lottieResId)
                    putInt(ARG_PARM3, page)
                }
            }
        }
    }
}