package jesusernesto.lopezibarra.gestorgastos.dummy

import jesusernesto.lopezibarra.gestorgastos.R
data class Transaccion(
    val id: Int, val title: String, val category: String,
    val date: String, val amount: Double, val paymentMethod: String = "Efectivo",
    val iconEmoji: String = "🏠")

data class GastosPorCategoria(val name: String, val amount: Double, val iconEmoji: String)

data class Presupuesto(
    val category: String, val assigned: Double,
    val spent: Double) {
        val restante get() = assigned - spent
        val porcentaje get() = if (assigned > 0) (spent / assigned * 100).toInt() else 0
    }

data class Grupo(
    val id: Int, val name: String, val type: String,
    val members: Int, val emoji: String, val balance: Double)

data class UserProfile(val nombre: String, val correo: String, val telefono: String, val pfp: Int)

object DummyData {
    val userActual = UserProfile(nombre = "Luis Pérez", correo = "luis.perez08@gmail.com", telefono = "+52 6442819732", pfp = R.drawable.dummypfp)

    val transacciones = listOf(
        Transaccion(1, "Renta casa cdmx", "Hogar", "1 marzo", -52_000.0, "Efectivo", "🏠"),
        Transaccion(2, "Rolex presidente", "Compras", "6 marzo", -405_000.0, "Tarjeta Débito", "🛍️"),
        Transaccion(3, "Ganancia fletes", "Ingreso", "8 marzo", 200_000.0, "Efectivo", "💰"),
        Transaccion(4, "Netflix", "Entretenimiento", "3 marzo", -250.0, "Tarjeta Crédito", "🎬"),
        Transaccion(5, "Uber Eats", "Comida", "5 marzo", -480.0, "Efectivo", "🍔"),
        Transaccion(6, "Gasolina", "Transporte", "4 marzo", -800.0, "Efectivo", "🚗"),
        Transaccion(7, "Salario marzo", "Ingreso", "1 marzo", 549_359.0, "Transferencia", "💰"),
    )

    val gastosPorCategoria = listOf(
        GastosPorCategoria("Hogar", 172_209.60, "🏠"),
        GastosPorCategoria("Comida", 125_864.00, "🍔"),
        GastosPorCategoria("Transporte", 75_518.40, "🚗"),
        GastosPorCategoria("Compras", 503_546.60, "🛍️"),
    )

    val balanceMensual = 245_903.0
    val ingresoMensual = 749_359.0
    val gastoMensual = 503_456.0
    val mesActual = "Marzo 2026"

    val listapresupuestos = listOf(
        Presupuesto("Comida", 3_200.0, 1_420.0),
        Presupuesto("Entretenimiento", 1_850.0, 1_850.0),
        Presupuesto("Transporte", 1_800.0, 1_000.0),
        Presupuesto("Otros", 1_600.0, 1_000.0),
    )

    val ingreso = 50_000.0
    val gastos = listOf(
        Pair("Renta (Vivienda)", -10_000.0),
        Pair("Internet (Otros)", -580.0),
        Pair("Netflix (Entretenimiento)", -250.0),
    )

    val grupos = listOf(
        Grupo(1, "Pareja", "Pareja", 2, "💑", 3_400.0),
        Grupo(2, "Viajes", "Viaje", 6, "✈️", 12_500.0),
        Grupo(3, "Familia", "Familiar", 4, "👨‍👩‍👧‍👦", 8_200.0),
    )

    val tiposDeGrupo = listOf(
        "👨‍👩‍👧‍👦" to "Familiar",
        "💼" to "Trabajo",
        "💑" to "Pareja",
        "🎓" to "Escuela",
        "🎉" to "Evento",
        "✈️" to "Viaje",
    )

    val categorias = listOf(
        "🏠" to "Vivienda",
        "🎬" to "Entretenimiento",
        "🚗" to "Transporte",
        "🍔" to "Alimentación",
        "❤️" to "Salud",
        "📦" to "Otros",
    )

    val formasPago = listOf(
        Triple("💵", "Efectivo", "DINERO FÍSICO"),
        Triple("💳", "Tarjeta de Débito", "6572 5249 4291"),
        Triple("💳", "Tarjeta de Crédito", "**** 7422"),
        Triple("💳", "Tarjeta de Débito", "**** 8802"),
    )
}
