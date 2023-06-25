package com.kimkazandi.common.utils

import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.kimkazandi.common.states.BaseState

class Utils {
    companion object {
        fun checkState(
            activity: FragmentActivity?,
            state: BaseState,
            onLoading: Unit? = null,
            onSuccess: (() -> Unit)? = null,
        ) {
            when (state) {
                is BaseState.Loading -> {
                    onLoading
                }
                is BaseState.Success -> {
                    if (state.message != null) {
                        Toast.makeText(
                            activity?.applicationContext, state.message.toString(), Toast.LENGTH_LONG
                        ).show()
                    }
                    if (onSuccess != null) onSuccess()
                }
                is BaseState.Error -> {
                    Toast.makeText(
                        activity?.applicationContext, state.message.toString(), Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}