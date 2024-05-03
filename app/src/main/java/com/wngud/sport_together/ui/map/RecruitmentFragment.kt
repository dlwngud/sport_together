package com.wngud.sport_together.ui.map

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.type.LatLng
import com.wngud.sport_together.App
import com.wngud.sport_together.R
import com.wngud.sport_together.databinding.FragmentRecruitmentBinding
import com.wngud.sport_together.domain.model.Exercise
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.lang.reflect.Array
import java.util.Locale

@AndroidEntryPoint
class RecruitmentFragment : Fragment() {

    private val mapViewModel: MapViewModel by viewModels()
    private lateinit var binding: FragmentRecruitmentBinding

    private val typeList = arrayOf(
        "자전거",
        "등산",
        "볼링",
        "배드민턴",
        "테니스",
        "탁구",
        "런닝",
        "헬스",
        "기타",
    )

    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecruitmentBinding.inflate(layoutInflater, container, false)

        val lat: Double = arguments?.getDouble("lat") ?: 0.0
        val lng: Double = arguments?.getDouble("lng") ?: 0.0
        binding.run {
            etRecruitmentLocation.setText(getAddress(lat, lng, requireContext()))
            (otfRecruitmentType.editText as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        typeList
                    )
                )
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                binding.btnRecruitment.setOnClickListener {
                    val exercise = Exercise(
                        uid = App.auth.currentUser!!.uid,
                        type = binding.autoCompleteTvRecruitment.text.toString(),
                        location = "$lat $lng",
                        title = binding.etRecruitmentTitle.text.toString()
                    )
                    saveExercise(exercise)
                }
            }
        }

        return binding.root
    }

    private fun backPress() {
        findNavController().popBackStack()
    }

    private fun saveExercise(exercise: Exercise) {
        mapViewModel.saveExercise(exercise)
        backPress()
    }

    private fun getAddress(lat: Double, lng: Double, context: Context): String {
        return try {
            val list = Geocoder(context, Locale.KOREA).getFromLocation(lat, lng, 1)!!
            list[0].getAddressLine(0)
        } catch (e: Exception) {
            "오류"
        }
    }
}