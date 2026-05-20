package com.pablo.ducky

import com.google.common.truth.Truth.assertThat
import com.pablo.ducky.data.model.Autor
import com.pablo.ducky.data.model.AutorWrapper
import com.pablo.ducky.data.model.Copia
import com.pablo.ducky.data.model.Libro
import org.junit.Test

class LibroModelTest {

    private fun libro(
        autores: List<AutorWrapper> = emptyList(),
        copias: List<Copia> = emptyList()
    ) = Libro(1, "978-0-00", "Titulo Test", null, null, null, null, null, null, null, null, null, autores, copias)

    @Test
    fun autoresString_sinAutores_retornaVacio() {
        assertThat(libro().autoresString()).isEmpty()
    }

    @Test
    fun autoresString_unAutor_retornaSuNombre() {
        val autores = listOf(AutorWrapper(Autor(1, "Garcia Marquez")))
        assertThat(libro(autores).autoresString()).isEqualTo("Garcia Marquez")
    }

    @Test
    fun autoresString_dosAutores_separadosPorComa() {
        val autores = listOf(
            AutorWrapper(Autor(1, "Autor A")),
            AutorWrapper(Autor(2, "Autor B"))
        )
        assertThat(libro(autores).autoresString()).isEqualTo("Autor A, Autor B")
    }

    @Test
    fun disponibles_todasDisponibles_retornaCuentaTotal() {
        val copias = listOf(
            Copia(1, "Disponible", null, null),
            Copia(2, "Disponible", null, null)
        )
        assertThat(libro(copias = copias).disponibles()).isEqualTo(2)
    }

    @Test
    fun disponibles_ningunaDisponible_retornaCero() {
        val copias = listOf(
            Copia(1, "Prestado", null, null),
            Copia(2, "Prestado", null, null)
        )
        assertThat(libro(copias = copias).disponibles()).isEqualTo(0)
    }

    @Test
    fun disponibles_algunasDisponibles_retornaCuentaCorrecta() {
        val copias = listOf(
            Copia(1, "Disponible", null, null),
            Copia(2, "Prestado", null, null),
            Copia(3, "Disponible", null, null)
        )
        assertThat(libro(copias = copias).disponibles()).isEqualTo(2)
    }

    @Test
    fun estaDisponible_conAlMenosUnaDisponible_retornaTrue() {
        val copias = listOf(Copia(1, "Disponible", null, null))
        assertThat(libro(copias = copias).estaDisponible()).isTrue()
    }

    @Test
    fun estaDisponible_sinCopiasDisponibles_retornaFalse() {
        val copias = listOf(Copia(1, "Prestado", null, null))
        assertThat(libro(copias = copias).estaDisponible()).isFalse()
    }

    @Test
    fun estaDisponible_listaCopiaVacia_retornaFalse() {
        assertThat(libro().estaDisponible()).isFalse()
    }
}
