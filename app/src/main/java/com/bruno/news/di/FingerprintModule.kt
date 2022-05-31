package com.bruno.news.di

import android.app.Application
import android.content.Context
import androidx.biometric.BiometricManager
import com.bruno.news.util.Fingerprint
import com.bruno.news.util.FingerprintImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
class FingerprintModule {

    @Provides
    fun provideBiometricManager(context: Application): BiometricManager {
        return BiometricManager.from(context)
    }

    @Provides
    fun provideFingerprint(biometricManager: BiometricManager, context: Application): Fingerprint {
        return FingerprintImpl(biometricManager, context)
    }

}