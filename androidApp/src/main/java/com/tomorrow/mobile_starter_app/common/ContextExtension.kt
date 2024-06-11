package com.tomorrow.mobile_starter_app.common

import android.content.Context

@Suppress("DEPRECATION")
fun Context.vibratePhone(milliseconds: Long = 500) {
    val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as android.os.Vibrator
    vibrator.vibrate(milliseconds)
}