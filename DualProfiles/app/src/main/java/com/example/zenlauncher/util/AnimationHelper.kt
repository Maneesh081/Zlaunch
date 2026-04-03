package com.example.zenlauncher.util

import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation

object AnimationHelper {

    fun fadeIn(view: View, duration: Long = 400, onEnd: (() -> Unit)? = null) {
        view.alpha = 0f
        view.visibility = View.VISIBLE
        view.animate()
            .alpha(1f)
            .setDuration(duration)
            .withEndAction { onEnd?.invoke() }
            .start()
    }

    fun fadeOut(view: View, duration: Long = 400, onEnd: (() -> Unit)? = null) {
        view.animate()
            .alpha(0f)
            .setDuration(duration)
            .withEndAction {
                view.visibility = View.GONE
                onEnd?.invoke()
            }
            .start()
    }

    fun crossFade(view: View, duration: Long = 500, onMid: () -> Unit) {
        view.animate()
            .alpha(0f)
            .setDuration(duration / 2)
            .withEndAction {
                onMid()
                view.animate()
                    .alpha(1f)
                    .setDuration(duration / 2)
                    .start()
            }
            .start()
    }

    fun pulse(view: View) {
        view.animate()
            .scaleX(1.02f)
            .scaleY(1.02f)
            .setDuration(2000)
            .withEndAction {
                view.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(2000)
                    .withEndAction { pulse(view) }
                    .start()
            }
            .start()
    }

    fun slideUp(view: View, duration: Long = 300) {
        view.translationY = 100f
        view.alpha = 0f
        view.visibility = View.VISIBLE
        view.animate()
            .translationY(0f)
            .alpha(1f)
            .setDuration(duration)
            .start()
    }
}
