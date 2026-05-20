package com.pablo.ducky

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.pablo.ducky.data.local.SessionManager
import com.pablo.ducky.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHost.navController

        val session = SessionManager(this)
        binding.tvDrawerEmail.text = session.getEmail() ?: ""

        binding.drawerPrestamos.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            navController.navigate(R.id.action_to_prestamos)
        }

        binding.drawerMultas.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            navController.navigate(R.id.action_to_multas)
        }

        binding.drawerCerrarSesion.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            session.clearSession()
            navController.navigate(
                R.id.loginFragment, null,
                NavOptions.Builder().setPopUpTo(R.id.nav_graph, true).build()
            )
        }
    }

    fun openDrawer() {
        val session = SessionManager(this)
        binding.tvDrawerEmail.text = session.getEmail() ?: ""
        binding.drawerLayout.openDrawer(GravityCompat.START)
    }
}
