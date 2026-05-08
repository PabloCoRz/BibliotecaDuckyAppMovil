package com.pablo.ducky.data.repository

import com.pablo.ducky.data.model.Autor
import com.pablo.ducky.data.model.AutorWrapper
import com.pablo.ducky.data.model.Copia
import com.pablo.ducky.data.model.Libro
import kotlinx.coroutines.delay

class LibroRepository {

    suspend fun buscar(query: String): Result<List<Libro>> {
        // simulate a small network delay
        delay(300)

        val todos = librosFalsos()

        if (query.isBlank()) return Result.success(todos)

        val filtrados = todos.filter { libro ->
            libro.titulo.contains(query, ignoreCase = true) ||
                    libro.autoresString().contains(query, ignoreCase = true) ||
                    libro.isbn.contains(query, ignoreCase = true) ||
                    libro.categoria?.contains(query, ignoreCase = true) == true ||
                    libro.editorial?.contains(query, ignoreCase = true) == true
        }

        return Result.success(filtrados)
    }

    private fun librosFalsos(): List<Libro> = listOf(
        Libro(
            id = 1,
            isbn = "978-6075228280",
            titulo = "Cálculo Tomo I",
            subtitulo = "Trascendentes tempranas",
            editorial = "CENGAGE",
            edicion = "10a",
            anioPub = 2019,
            numPaginas = 658,
            categoria = "Matemáticas",
            idioma = "Español",
            descripcion = "Cálculo de una variable con enfoque en trascendentes tempranas. Incluye ejercicios y aplicaciones.",
            portadaUrl = "https://m.media-amazon.com/images/I/71HU6XkiZ5L._AC_UF1000,1000_QL80_.jpg",
            autores = listOf(AutorWrapper(Autor(1, "Larson, Ron"))),
            copias = listOf(
                Copia(1, "Disponible", "A", "3"),
                Copia(2, "Disponible", "A", "3"),
                Copia(3, "Prestada",   "A", "3"),
            )
        ),
        Libro(
            id = 2,
            isbn = "978-6075228297",
            titulo = "Cálculo Tomo II",
            subtitulo = "Trascendentes tempranas",
            editorial = "CENGAGE",
            edicion = "10a",
            anioPub = 2019,
            numPaginas = 720,
            categoria = "Matemáticas",
            idioma = "Español",
            descripcion = "Continuación del cálculo de una variable, introducción a cálculo multivariable.",
            portadaUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRelM6LMu35axJE1r0EdzImysxE1f2nxGXXoA&s",
            autores = listOf(AutorWrapper(Autor(1, "Larson, Ron"))),
            copias = listOf(
                Copia(4, "Disponible", "A", "3"),
                Copia(5, "Prestada",   "A", "3"),
            )
        ),
        Libro(
            id = 3,
            isbn = "978-9702610540",
            titulo = "Álgebra Lineal",
            subtitulo = null,
            editorial = "Prentice Hall",
            edicion = "8a",
            anioPub = 2013,
            numPaginas = 559,
            categoria = "Matemáticas",
            idioma = "Español",
            descripcion = "Introducción al álgebra lineal con aplicaciones a ciencias e ingeniería.",
            portadaUrl = "https://m.media-amazon.com/images/I/61jm7YLfqbL._AC_UF1000,1000_QL80_.jpg",
            autores = listOf(AutorWrapper(Autor(2, "Lay, David C."))),
            copias = listOf(
                Copia(6, "Disponible", "A", "4"),
            )
        ),
        Libro(
            id = 4,
            isbn = "978-6073238298",
            titulo = "Histología",
            subtitulo = "Texto y atlas con biología celular y molecular",
            editorial = "Wolters Kluwer",
            edicion = "7a",
            anioPub = 2016,
            numPaginas = 930,
            categoria = "Medicina",
            idioma = "Español",
            descripcion = "Atlas completo de histología con correlaciones clínicas y preguntas de repaso.",
            portadaUrl = "https://m.media-amazon.com/images/I/511Dg1gyX+L._SX342_SY445_ML2_.jpg",
            autores = listOf(
                AutorWrapper(Autor(3, "Ross, Michael H.")),
                AutorWrapper(Autor(4, "Pawlina, Wojciech"))
            ),
            copias = listOf(
                Copia(7, "Disponible", "C", "1"),
                Copia(8, "Disponible", "C", "1"),
                Copia(9, "Dañada",     "C", "1"),
            )
        ),
        Libro(
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
            descripcion = "Introducción a los principios micro y macroeconómicos con casos prácticos.",
            portadaUrl = null,
            autores = listOf(AutorWrapper(Autor(5, "Mankiw, N. Gregory"))),
            copias = listOf(
                Copia(10, "Prestada", "B", "2"),
                Copia(11, "Prestada", "B", "2"),
            )
        ),
        Libro(
            id = 6,
            isbn = "978-0132350884",
            titulo = "Clean Code",
            subtitulo = "A Handbook of Agile Software Craftsmanship",
            editorial = "Prentice Hall",
            edicion = "1a",
            anioPub = 2008,
            numPaginas = 431,
            categoria = "Tecnología",
            idioma = "Inglés",
            descripcion = "Guía práctica para escribir código limpio, mantenible y profesional.",
            portadaUrl = null,
            autores = listOf(AutorWrapper(Autor(6, "Martin, Robert C."))),
            copias = listOf(
                Copia(12, "Disponible", "D", "5"),
                Copia(13, "Disponible", "D", "5"),
            )
        ),
        Libro(
            id = 7,
            isbn = "978-9681653149",
            titulo = "Cien años de soledad",
            subtitulo = null,
            editorial = "Real Academia Española",
            edicion = "Edición conmemorativa",
            anioPub = 2007,
            numPaginas = 496,
            categoria = "Literatura",
            idioma = "Español",
            descripcion = "La obra maestra del realismo mágico latinoamericano.",
            portadaUrl = null,
            autores = listOf(AutorWrapper(Autor(7, "García Márquez, Gabriel"))),
            copias = listOf(
                Copia(14, "Disponible", "E", "1"),
            )
        ),
        Libro(
            id = 8,
            isbn = "978-6074383843",
            titulo = "Derecho Civil",
            subtitulo = "Parte General",
            editorial = "Porrúa",
            edicion = "40a",
            anioPub = 2021,
            numPaginas = 712,
            categoria = "Derecho",
            idioma = "Español",
            descripcion = "Texto clásico del derecho civil mexicano, actualizado con la legislación vigente.",
            portadaUrl = null,
            autores = listOf(AutorWrapper(Autor(8, "Rojina Villegas, Rafael"))),
            copias = listOf(
                Copia(15, "Disponible", "F", "2"),
                Copia(16, "Perdida",    "F", "2"),
            )
        ),
    )
}