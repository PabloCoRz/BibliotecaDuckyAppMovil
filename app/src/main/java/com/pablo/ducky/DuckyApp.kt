package com.pablo.ducky

import android.app.Application
import com.pablo.ducky.data.local.BibliotecaDatabase

class DuckyApp : Application() {
    val database by lazy { BibliotecaDatabase.getDatabase(this) }
}
