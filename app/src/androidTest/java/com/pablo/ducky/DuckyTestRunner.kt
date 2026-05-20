package com.pablo.ducky

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class DuckyTestRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
        return super.newApplication(cl, DuckyApp::class.java.name, context)
    }
}
