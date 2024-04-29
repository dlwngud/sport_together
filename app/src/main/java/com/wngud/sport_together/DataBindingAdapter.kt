package com.wngud.sport_together

import android.view.View
import androidx.databinding.BindingAdapter
import com.wngud.sport_together.ui.login.LoginUiState

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
}