package com.wngud.sport_together

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.wngud.sport_together.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setBottomNavigation()
        setStatusBar()
    }

    private fun setBottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container_main) as NavHostFragment
        val navController = navHostFragment.findNavController()
        binding.bottomNavigationMain.setupWithNavController(navController)

        setBottomNavigationVisibility(navController)
    }

    private fun setBottomNavigationVisibility(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.nav_search -> {
                    binding.bottomNavigationMain.visibility = View.GONE
                }

                else -> {
                    binding.bottomNavigationMain.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setStatusBar() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    // <a href="https://www.flaticon.com/kr/free-icons/" title="믿음 아이콘">믿음 아이콘 제작자: Freepik - Flaticon</a>
    // https://www.flaticon.com/kr/free-icon/high-five_1534439?term=2%EB%AA%85&page=1&position=2&origin=search&related_id=1534439
}