package com.bruno.news.util

import androidx.fragment.app.FragmentActivity

interface Fingerprint {

    fun authenticate(activity: FragmentActivity)
}