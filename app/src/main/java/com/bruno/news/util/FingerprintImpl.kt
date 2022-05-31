package com.bruno.news.util

import android.content.Context
import android.util.Log
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import com.bruno.news.R
import java.util.concurrent.Executors
import javax.inject.Inject

class FingerprintImpl @Inject constructor(
    private val biometricManager: BiometricManager,
    private val context: Context,
) : Fingerprint {

    private val executor = Executors.newSingleThreadExecutor()
    private val biometricPromptInfo by lazy {
        BiometricPrompt.PromptInfo.Builder()
            .setTitle(context.getString(R.string.dialog_title))
            .setDescription(context.getString(R.string.dialog_description))
            .setNegativeButtonText(context.getString(R.string.dialog_cancel))
            .build()
    }

    override fun authenticate(activity: FragmentActivity) {
        if (fingerprintIsAvailable().not()) return
        val biometricPrompt = BiometricPrompt(activity, executor, biometricPromptCallback())
        biometricPrompt.authenticate(biometricPromptInfo)

    }

    private fun biometricPromptCallback() = object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            Log.d("Fingerprint", "onAuthenticationSucceeded: Succcess")
        }
    }

    private fun fingerprintIsAvailable(): Boolean {
        return biometricManager.canAuthenticate(BIOMETRIC_WEAK) == BIOMETRIC_SUCCESS
    }
}