package jesusernesto.lopezibarra.gestorgastos.data

import jesusernesto.lopezibarra.gestorgastos.data.enums.CategoriaGrupo


data class Grupo(    val id: Int,
                     val nombre: String,
                     val miembros: Int,
                     val iconoCategoria: CategoriaGrupo
)

val gruposEjemplo = listOf(
    Grupo(1, "Pareja", 2, CategoriaGrupo.PAREJA),
    Grupo(2, "Viajes", 6, CategoriaGrupo.VIAJES),
    Grupo(3, "Departamento", 3, CategoriaGrupo.CASA)
)
