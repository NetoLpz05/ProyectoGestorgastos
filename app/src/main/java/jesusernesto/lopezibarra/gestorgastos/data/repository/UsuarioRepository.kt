package jesusernesto.lopezibarra.gestorgastos.data.repository

import jesusernesto.lopezibarra.gestorgastos.data.dao.UsuarioDao
import jesusernesto.lopezibarra.gestorgastos.data.entity.UsuarioEntity
import java.security.MessageDigest

sealed class AuthResult {
    data class Exito(val usuario: UsuarioEntity) : AuthResult()
    data class Error(val mensaje: String) : AuthResult()
}

class UsuarioRepository(private val dao: UsuarioDao) {

    private fun hashContrasena(contrasena: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(contrasena.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    suspend fun registrar(
        nombre: String,
        apellido: String,
        email: String,
        contrasena: String,
        fechaNacimiento: String,
        genero: String
    ): AuthResult {

        if (nombre.isBlank() || apellido.isBlank()) {
            return AuthResult.Error("El nombre y apellido son obligatorios")
        }
        if (!email.contains("@") || !email.contains(".")) {
            return AuthResult.Error("Correo electrónico inválido")
        }
        if (contrasena.length < 6) {
            return AuthResult.Error("La contraseña debe tener al menos 6 caracteres")
        }

        return try {
            val nuevoUsuario = UsuarioEntity(
                nombre = nombre.trim(),
                apellido = apellido.trim(),
                email = email.trim().lowercase(),
                contrasena = hashContrasena(contrasena),
                fechaNacimiento = fechaNacimiento,
                genero = genero
            )
            val id = dao.insertar(nuevoUsuario)
            AuthResult.Exito(nuevoUsuario.copy(idUsuario = id.toInt()))

        } catch (e: Exception) {
            AuthResult.Error("Este correo ya está registrado")
        }
    }

    suspend fun login(email: String, contrasena: String): AuthResult {

        if (email.isBlank() || contrasena.isBlank()) {
            return AuthResult.Error("Completa todos los campos")
        }

        val usuario = dao.buscarPorEmail(email.trim().lowercase())
            ?: return AuthResult.Error("No existe una cuenta con ese correo")

        val hashIngresado = hashContrasena(contrasena)
        if (usuario.contrasena != hashIngresado) {
            return AuthResult.Error("Contraseña incorrecta")
        }

        return AuthResult.Exito(usuario)
    }

    suspend fun obtenerUsuario(id: Int): UsuarioEntity? {
        return dao.buscarPorId(id)
    }

    suspend fun actualizarPerfil(usuario: UsuarioEntity): AuthResult {
        return try {
            dao.actualizar(usuario)
            AuthResult.Exito(usuario)
        } catch (e: Exception) {
            AuthResult.Error("No se pudo actualizar el perfil")
        }
    }

    suspend fun cambiarTema(id: Int, tema: String) {
        dao.actualizarTema(id, tema)
    }

    suspend fun cambiarBiometria(id: Int, activa: Boolean) {
        dao.actualizarBiometria(id, activa)
    }
}