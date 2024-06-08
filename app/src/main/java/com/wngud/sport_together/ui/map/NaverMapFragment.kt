package com.wngud.sport_together.ui.map

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.kakao.sdk.user.Constants.TAG
import com.kakao.sdk.user.UserApiClient
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.wngud.sport_together.R
import com.wngud.sport_together.databinding.FragmentMapBinding
import com.wngud.sport_together.domain.model.Exercise
import com.wngud.sport_together.ui.chatting.ChattingRoomViewModel
import com.wngud.sport_together.ui.mypage.MypageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NaverMapFragment : Fragment(), OnMapReadyCallback {
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

    private lateinit var binding: FragmentMapBinding
    private val mapViewModel: MapViewModel by viewModels()
    private val mypageViewModel: MypageViewModel by viewModels()
    private val chattingRoomViewModel: ChattingRoomViewModel by viewModels()

    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    private val markerList = mutableListOf<Marker>()

    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private val typeList = listOf(
        ExerciseType("자전거", R.drawable.ic_bicycle),
        ExerciseType("등산", R.drawable.ic_hiking),
        ExerciseType("볼링", R.drawable.ic_bowling),
        ExerciseType("배드민턴", R.drawable.ic_badminton),
        ExerciseType("테니스", R.drawable.ic_tennis),
        ExerciseType("탁구", R.drawable.ic_table_tennis),
        ExerciseType("런닝", R.drawable.ic_running),
        ExerciseType("헬스", R.drawable.ic_gym),
        ExerciseType("기타", R.drawable.ic_etc),
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
                requireActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            initMapView()
            setBottomSheet()
            getInfo()
            chattingRoomViewModel.getAllMyChattingRooms()
        }

        return binding.root
    }

    fun getInfo() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패", error)
            } else if (user != null) {
                Log.i(
                    TAG,
                    "사용자 정보 요청 성공" + "\n회원번호: ${user.id}" + "\n이메일: ${user.kakaoAccount?.email}" + "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" + "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}"
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
        val mapFragment =
            fm.findFragmentById(R.id.map) as MapFragment? ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }

        mapFragment.getMapAsync(this)
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
    }

    private fun hasPermission(): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    permission
                ) != PackageManager.PERMISSION_GRANTED
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
        naverMap.minZoom = 10.0
        naverMap.maxZoom = 18.0

        naverMap.setOnMapClickListener { pointF, latLng ->
            Log.i("tag", "${latLng.latitude}, ${latLng.longitude}")
            if (persistentBottomSheet.state == BottomSheetBehavior.STATE_COLLAPSED) {
                persistentBottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }

        naverMap.setOnMapLongClickListener { pointF, latLng ->
            showDialog(latLng)
        }

        initTypeRecyclerView()
        mapViewModel.getAllExercises()
    }

    private fun initTypeRecyclerView() {
        binding.rvTypeMap.run {
            val typeAdapter = TypeAdapter(requireContext(), typeList)
            adapter = typeAdapter
            layoutManager = LinearLayoutManager(requireContext(), HORIZONTAL, false)

            typeAdapter.setItemClickListener(object : TypeAdapter.onItemClickListener {
                override fun onItemClick(position: Int) {
                    removeMarkers()
                    showExerciseOfSelectedType(position)
                }
            })
        }
    }

    private fun showExerciseOfSelectedType(position: Int) {
        val selectedType =
            mapViewModel.uiState.value.exercises.filter { it.type == typeList[position].type }
        showMarkers(selectedType)
        if (markerList.isEmpty()) Snackbar.make(
            requireView(),
            if (typeList[position].type != "기타") "근처에 ${typeList[position].type} 친구는 없네요.." else "근처에 운동 친구는 없네요..",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun removeMarkers() {
        markerList.all {
            it.map = null
            true
        }
        markerList.clear()
    }

    private fun showMarkers(exerciseList: List<Exercise>) {
        for (exercise in exerciseList) {
            val (lat, lng) = exercise.location.split(" ").map { it.toDouble() }
            val marker = Marker()
            markerList.add(marker)
            marker.run {
                setOnClickListener {
                    persistentBottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
                    binding.run {
                        tvTitleBottomSheet.text = exercise.title
                        tvNicknameBottomSheet.text = exercise.nickname
                        mypageViewModel.getUserProfile(exercise.profileImage) {
                            if (it.isSuccessful) {
                                Glide.with(requireView()).load(it.result)
                                    .placeholder(R.drawable.app_icon).error(R.drawable.app_icon)
                                    .into(binding.ivBottomSheet)
                            }
                        }
                        bottomSheetMap.setOnClickListener {
                            val builder = AlertDialog.Builder(requireContext())
                            builder.setTitle("${exercise.nickname}님과 채팅하시겠습니까?")
                                .setPositiveButton("네") { dialog, which ->
                                    val bundle = Bundle()
                                    val room = chattingRoomViewModel.roomList.value.find { it.users.contains(exercise.uid) }!!
                                    bundle.putString("roomId", room.roomId)
                                    bundle.putString("counterUid", exercise.uid)
                                    findNavController().navigate(R.id.nav_chatting, bundle)
                                }.setNegativeButton("아니오") { dialog, which ->

                                }.create().show()
                        }
                    }
                    true
                }
                position = LatLng(lat, lng)
                icon = when (exercise.type) {
                    "자전거" -> OverlayImage.fromResource(R.drawable.ic_pin_bicycle)
                    "등산" -> OverlayImage.fromResource(R.drawable.ic_pin_hiking)
                    "탁구" -> OverlayImage.fromResource(R.drawable.ic_pin_table_tennis)
                    "배드민턴" -> OverlayImage.fromResource(R.drawable.ic_pin_badminton)
                    "볼링" -> OverlayImage.fromResource(R.drawable.ic_pin_bowling)
                    "테니스" -> OverlayImage.fromResource(R.drawable.ic_pin_tennis)
                    "런닝" -> OverlayImage.fromResource(R.drawable.ic_pin_running)
                    "헬스" -> OverlayImage.fromResource(R.drawable.ic_pin_gym)
                    else -> OverlayImage.fromResource(R.drawable.ic_pin_etc)
                }
                captionText = exercise.type
                width = 100
                height = 100
                map = naverMap
            }
        }
    }

    private fun showDialog(latLng: LatLng) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("이곳에서 운동 친구를 모집하겠습니까?").setPositiveButton("네") { dialog, which ->
            findNavController().navigate(
                R.id.nav_recruitment,
                bundleOf("lat" to latLng.latitude, "lng" to latLng.longitude)
            )
        }.setNegativeButton("아니오") { dialog, which ->

        }.create().show()
    }
}

data class ExerciseType(
    val type: String, val image: Int
)