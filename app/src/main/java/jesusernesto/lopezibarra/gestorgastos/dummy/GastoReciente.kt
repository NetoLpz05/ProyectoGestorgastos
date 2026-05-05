package jesusernesto.lopezibarra.gestorgastos.dummy

import jesusernesto.lopezibarra.gestorgastos.data.enums.CategoriaGasto


data class GastoReciente(val id: Int,
                         val nombre: String,
                         val categoria: CategoriaGasto,
                         val monto: String,
                         val pagadoPor: String,
                         val fecha: String)

val gastosEjemplo = listOf(
    GastoReciente(1, "Cena en La Parrilla",  CategoriaGasto.COMIDA,     "$280.00", "Mateo",  "14 MAY"),
    GastoReciente(2, "Renta de Auto",         CategoriaGasto.TRANSPORTE, "$650.00", "Carlos", "12 MAY"),
    GastoReciente(3, "Bebidas en Coco Bongo", CategoriaGasto.BEBIDAS,    "$270.00", "Ana",    "11 MAY")
)
