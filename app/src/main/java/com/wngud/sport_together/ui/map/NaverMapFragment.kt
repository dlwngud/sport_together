package com.wngud.sport_together.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kakao.sdk.user.Constants.TAG
import com.kakao.sdk.user.UserApiClient
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.wngud.sport_together.R
import com.wngud.sport_together.databinding.FragmentMapBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NaverMapFragment : Fragment(), OnMapReadyCallback {
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

    private lateinit var binding: FragmentMapBinding

    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap

    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private lateinit var persistentBottomSheet: BottomSheetBehavior<View>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(layoutInflater, container, false)

        binding.searchMap.setOnClickListener {
            it.findNavController().navigate(R.id.nav_search)
        }

        if (!hasPermission()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                permissions,
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            initMapView()
            setBottomSheet()
            getInfo()
        }

        return binding.root
    }

    fun getInfo() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패", error)
            } else if (user != null) {
                Log.i(
                    TAG, "사용자 정보 요청 성공" +
                            "\n회원번호: ${user.id}" +
                            "\n이메일: ${user.kakaoAccount?.email}" +
                            "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                            "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}"
                )
            }
        }
    }

    private fun setBottomSheet() {
        persistentBottomSheet = BottomSheetBehavior.from(binding.bottomSheetMap)
        persistentBottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
        persistentBottomSheet.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })
    }

    private fun initMapView() {
        val fm = childFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }

        mapFragment.getMapAsync(this)
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
    }

    private fun hasPermission(): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(requireContext(), permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        naverMap.locationSource = locationSource
        binding.compassView.map = naverMap

        naverMap.uiSettings.isLocationButtonEnabled = true
        naverMap.uiSettings.isCompassEnabled = false
        naverMap.locationTrackingMode = LocationTrackingMode.Follow

        naverMap.setOnMapClickListener { pointF, latLng ->
            Log.i("tag", "${latLng.latitude}, ${latLng.longitude}")
            if (persistentBottomSheet.state == BottomSheetBehavior.STATE_COLLAPSED) {
                persistentBottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }

        val marker = Marker()
        marker.run {
            setOnClickListener {
                persistentBottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
                true
            }
            position = LatLng(37.3714281566433, 127.25022443158156)
            icon = OverlayImage.fromResource(R.drawable.ic_gym)
            width = 80
            height = 80
            map = naverMap
        }
    }
}