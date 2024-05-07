package com.wngud.sport_together

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.wngud.sport_together.ui.login.LoginUiState
import java.text.DecimalFormat

object DataBindingAdapter {


    @JvmStatic
    @BindingAdapter("showOnLoading")
    fun showOnLoading(view: View, uiState: LoginUiState) {
        if (uiState == LoginUiState.Loading) {
            view.visibility = View.VISIBLE
            return
        }
        view.visibility = View.INVISIBLE
    }

    @JvmStatic
    @BindingAdapter("hideOnLoading")
    fun hideOnLoading(view: View, uiState: LoginUiState) {
        if (uiState == LoginUiState.Loading) {
            view.visibility = View.INVISIBLE
            return
        }
        view.visibility = View.VISIBLE
    }

    @JvmStatic
    @BindingAdapter("followingCount")
    fun formattedFollow(view: TextView, followingCount: Int) {
        val numberFormatter = DecimalFormat("###,###")
        val text = "팔로잉 ${numberFormatter.format(followingCount)}"
        view.text = text
    }

    @JvmStatic
    @BindingAdapter("followerCount")
    fun formattedFollower(view: TextView, followerCount: Int) {
        val numberFormatter = DecimalFormat("###,###")
        val text = "팔로워 ${numberFormatter.format(followerCount)}"
        view.text = text
    }
}