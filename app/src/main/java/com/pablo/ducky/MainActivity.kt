package com.pablo.ducky

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pablo.ducky.databinding.ActivityMainBinding

/**
 * Única Activity de la aplicación.
 * Aloja el NavHostFragment que gestiona toda la navegación entre fragments.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
