package com.iftikhar.khan

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.iftikhar.khan.databinding.ActivitySplashBinding

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    private val handler = Handler(Looper.getMainLooper())
    private var splashRunnable: Runnable? = null

    private var avatarAnimator: ObjectAnimator? = null
    private var loadingAnimator: ObjectAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startSplashAnimations()
        scheduleNavigation()
    }

    private fun scheduleNavigation() {
        splashRunnable = Runnable {
            if (!isFinishing && !isDestroyed) {
                navigateToMain()
            }
        }
        handler.postDelayed(splashRunnable!!, SPLASH_DELAY_MS)
    }

    private fun navigateToMain() {
        cancelAnimations()

        startActivity(Intent(this, MainActivity::class.java))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(
                OVERRIDE_TRANSITION_OPEN,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
        } else {
            @Suppress("DEPRECATION")
            overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
        }

        finish()
    }

    private fun startSplashAnimations() {
        // Initial view states
        binding.mtrlCardId.apply {
            alpha = 0f
            scaleX = 0.7f
            scaleY = 0.7f
        }

        binding.imageSle.alpha = 0f
        binding.text.apply {
            alpha = 0f
            translationY = 25f
        }
        binding.loadingText.alpha = 0f

        // Logo card animation
        val logoAnimation = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(binding.mtrlCardId, View.ALPHA, 0f, 1f),
                ObjectAnimator.ofFloat(binding.mtrlCardId, View.SCALE_X, 0.7f, 1f),
                ObjectAnimator.ofFloat(binding.mtrlCardId, View.SCALE_Y, 0.7f, 1f)
            )
            duration = LOGO_ANIMATION_DURATION_MS
            interpolator = AccelerateDecelerateInterpolator()
        }

        logoAnimation.doOnEnd {
            if (isFinishing || isDestroyed) return@doOnEnd

            // Profile avatar fade and horizontal float
            binding.imageSle.animate()
                .alpha(1f)
                .setDuration(AVATAR_FADE_DURATION_MS)
                .start()

            val travelDistance = AVATAR_TRAVEL_DP * resources.displayMetrics.density

            avatarAnimator = ObjectAnimator.ofFloat(
                binding.imageSle,
                View.TRANSLATION_X,
                -travelDistance,
                travelDistance
            ).apply {
                duration = AVATAR_FLOAT_DURATION_MS
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
                interpolator = AccelerateDecelerateInterpolator()
                start()
            }

            // App title animation
            AnimatorSet().apply {
                playTogether(
                    ObjectAnimator.ofFloat(binding.text, View.ALPHA, 0f, 1f),
                    ObjectAnimator.ofFloat(binding.text, View.TRANSLATION_Y, 25f, 0f)
                )
                duration = TITLE_ANIMATION_DURATION_MS
                interpolator = AccelerateDecelerateInterpolator()
                start()
            }

            // Subtitle fade
            binding.loadingText.animate()
                .alpha(1f)
                .setDuration(SUBTITLE_FADE_DURATION_MS)
                .start()
        }

        logoAnimation.start()

        // Progress bar animation
        binding.loadingTrack.post {
            if (isFinishing || isDestroyed) return@post

            val distance = (binding.loadingTrack.width - binding.loadingBar.width).toFloat()
            if (distance > 0f) {
                loadingAnimator = ObjectAnimator.ofFloat(
                    binding.loadingBar,
                    View.TRANSLATION_X,
                    0f,
                    distance
                ).apply {
                    duration = LOADING_ANIMATION_DURATION_MS
                    repeatCount = ObjectAnimator.INFINITE
                    repeatMode = ObjectAnimator.REVERSE
                    interpolator = AccelerateDecelerateInterpolator()
                    start()
                }
            }
        }
    }

    private fun cancelAnimations() {
        avatarAnimator?.cancel()
        avatarAnimator = null
        loadingAnimator?.cancel()
        loadingAnimator = null
    }

    override fun onDestroy() {
        splashRunnable?.let { handler.removeCallbacks(it) }
        cancelAnimations()
        super.onDestroy()
    }

    companion object {
        private const val SPLASH_DELAY_MS = 2500L
        private const val LOGO_ANIMATION_DURATION_MS = 700L
        private const val AVATAR_FADE_DURATION_MS = 400L
        private const val AVATAR_FLOAT_DURATION_MS = 850L
        private const val AVATAR_TRAVEL_DP = 35f
        private const val TITLE_ANIMATION_DURATION_MS = 650L
        private const val SUBTITLE_FADE_DURATION_MS = 600L
        private const val LOADING_ANIMATION_DURATION_MS = 900L
    }
}
