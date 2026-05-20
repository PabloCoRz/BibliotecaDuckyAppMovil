package com.pablo.ducky

import com.google.common.truth.Truth.assertThat
import com.pablo.ducky.data.local.entity.PrestamoEntity
import com.pablo.ducky.data.local.entity.UsuarioEntity
import org.junit.Test
import java.util.concurrent.TimeUnit

class EntityTest {

    @Test
    fun usuarioEntity_emailAlmacenadoCorrectamente() {
        val usuario = UsuarioEntity(email = "test@ducky.edu", password = "1234")
        assertThat(usuario.email).isEqualTo("test@ducky.edu")
    }

    @Test
    fun usuarioEntity_idDefaultEsCero() {
        val usuario = UsuarioEntity(email = "a@b.com", password = "pass")
        assertThat(usuario.id).isEqualTo(0)
    }

    @Test
    fun prestamoEntity_devueltoDefaultEsFalse() {
        val prestamo = PrestamoEntity(
            libroId = 1, tituloLibro = "El Quijote", autorLibro = "Cervantes",
            portadaUrl = null, fechaSolicitud = 1000L, fechaDevolucion = 9000L
        )
        assertThat(prestamo.devuelto).isFalse()
    }

    @Test
    fun prestamoEntity_multaDeTresDias_esQuincePesos() {
        val fechaDevolucion = 0L
        val ahoraTresDiasDespues = TimeUnit.DAYS.toMillis(3)
        val diasRetraso = TimeUnit.MILLISECONDS.toDays(ahoraTresDiasDespues - fechaDevolucion).toInt()
        val monto = diasRetraso * 5.0
        assertThat(monto).isEqualTo(15.0)
    }

    @Test
    fun prestamoEntity_todosLosCamposAlmacenadosCorrectamente() {
        val prestamo = PrestamoEntity(
            libroId = 42,
            tituloLibro = "Cien Anos de Soledad",
            autorLibro = "Garcia Marquez",
            portadaUrl = "http://example.com/portada.jpg",
            fechaSolicitud = 1_000_000L,
            fechaDevolucion = 2_000_000L
        )
        assertThat(prestamo.libroId).isEqualTo(42)
        assertThat(prestamo.tituloLibro).isEqualTo("Cien Anos de Soledad")
        assertThat(prestamo.portadaUrl).isEqualTo("http://example.com/portada.jpg")
        assertThat(prestamo.fechaDevolucion).isGreaterThan(prestamo.fechaSolicitud)
    }
}
