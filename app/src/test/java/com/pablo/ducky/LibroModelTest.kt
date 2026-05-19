package com.pablo.ducky

import com.google.common.truth.Truth.assertThat
import com.pablo.ducky.data.model.Autor
import com.pablo.ducky.data.model.AutorWrapper
import com.pablo.ducky.data.model.Copia
import com.pablo.ducky.data.model.Libro
import com.pablo.ducky.data.local.LibroEntity
import org.junit.Test

/**
 * Pruebas unitarias del dominio y modelo de datos.
 * Se ejecutan en la JVM local (sin emulador).
 *
 * Cubren:
 *  - Lógica de disponibilidad de copias
 *  - Serialización de autores
 *  - Cálculo de copias disponibles
 *  - Casos borde (sin copias, sin autores, portada nula)
 *  - Entidad Room: campos y valores por defecto
 */
class LibroModelTest {

    // ─── Helpers ────────────────────────────────────────────────────────────

    private fun makeLibro(
        copias: List<Copia> = emptyList(),
        autores: List<AutorWrapper> = emptyList(),
        portadaUrl: String? = null
    ) = Libro(
        id = 1,
        isbn = "000-0000000000",
        titulo = "Libro de Prueba",
        subtitulo = null,
        editorial = "Editorial Test",
        edicion = "1a",
        anioPub = 2024,
        numPaginas = 100,
        categoria = "Test",
        idioma = "Español",
        descripcion = "Descripción de prueba",
        portadaUrl = portadaUrl,
        autores = autores,
        copias = copias
    )

    private fun copia(estado: String) = Copia(0, estado, "A", "1")

    // ─── Prueba 1: libro sin copias no está disponible ───────────────────────

    @Test
    fun `libro sin copias no esta disponible`() {
        val libro = makeLibro(copias = emptyList())
        assertThat(libro.estaDisponible()).isFalse()
    }

    // ─── Prueba 2: libro con copia disponible sí está disponible ─────────────

    @Test
    fun `libro con una copia disponible esta disponible`() {
        val libro = makeLibro(copias = listOf(copia("Disponible")))
        assertThat(libro.estaDisponible()).isTrue()
    }

    // ─── Prueba 3: libro con solo copias prestadas no está disponible ─────────

    @Test
    fun `libro con todas las copias prestadas no esta disponible`() {
        val copias = listOf(copia("Prestada"), copia("Prestada"))
        val libro = makeLibro(copias = copias)
        assertThat(libro.estaDisponible()).isFalse()
    }

    // ─── Prueba 4: disponibles() cuenta solo copias con estado "Disponible" ───

    @Test
    fun `disponibles cuenta correctamente copias disponibles`() {
        val copias = listOf(
            copia("Disponible"),
            copia("Disponible"),
            copia("Prestada"),
            copia("Dañada")
        )
        val libro = makeLibro(copias = copias)
        assertThat(libro.disponibles()).isEqualTo(2)
    }

    // ─── Prueba 5: disponibles() retorna 0 con lista vacía ───────────────────

    @Test
    fun `disponibles retorna 0 cuando no hay copias`() {
        val libro = makeLibro(copias = emptyList())
        assertThat(libro.disponibles()).isEqualTo(0)
    }

    // ─── Prueba 6: autoresString() con un autor ────────────────────────────

    @Test
    fun `autoresString retorna nombre del unico autor`() {
        val autores = listOf(AutorWrapper(Autor(1, "Larson, Ron")))
        val libro = makeLibro(autores = autores)
        assertThat(libro.autoresString()).isEqualTo("Larson, Ron")
    }

    // ─── Prueba 7: autoresString() con múltiples autores separados por coma ──

    @Test
    fun `autoresString une multiples autores con coma`() {
        val autores = listOf(
            AutorWrapper(Autor(1, "Ross, Michael H.")),
            AutorWrapper(Autor(2, "Pawlina, Wojciech"))
        )
        val libro = makeLibro(autores = autores)
        assertThat(libro.autoresString()).isEqualTo("Ross, Michael H., Pawlina, Wojciech")
    }

    // ─── Prueba 8: autoresString() con lista vacía retorna cadena vacía ───────

    @Test
    fun `autoresString con lista vacia retorna cadena vacia`() {
        val libro = makeLibro(autores = emptyList())
        assertThat(libro.autoresString()).isEmpty()
    }

    // ─── Prueba 9: portadaUrl puede ser nula sin causar error ────────────────

    @Test
    fun `libro con portadaUrl nula no lanza excepcion`() {
        val libro = makeLibro(portadaUrl = null)
        assertThat(libro.portadaUrl).isNull()
        // Verificamos que las operaciones normales siguen funcionando
        assertThat(libro.titulo).isEqualTo("Libro de Prueba")
    }

    // ─── Prueba 10: LibroEntity almacena valores correctos ───────────────────

    @Test
    fun `LibroEntity almacena correctamente todos los campos`() {
        val entity = LibroEntity(
            id = 5,
            isbn = "978-6071512684",
            titulo = "Principios de Economía",
            subtitulo = null,
            editorial = "CENGAGE",
            edicion = "7a",
            anioPub = 2015,
            numPaginas = 848,
            categoria = "Economía",
            idioma = "Español",
            descripcion = "Introducción a economía",
            portadaUrl = null,
            autoresStr = "Mankiw, N. Gregory",
            totalCopias = 2,
            copiasDisponibles = 0,
            pasillo = "B",
            estante = "2"
        )

        assertThat(entity.id).isEqualTo(5)
        assertThat(entity.titulo).isEqualTo("Principios de Economía")
        assertThat(entity.copiasDisponibles).isEqualTo(0)
        assertThat(entity.totalCopias).isEqualTo(2)
        assertThat(entity.autoresStr).isEqualTo("Mankiw, N. Gregory")
        assertThat(entity.pasillo).isEqualTo("B")
        assertThat(entity.portadaUrl).isNull()
    }
}
