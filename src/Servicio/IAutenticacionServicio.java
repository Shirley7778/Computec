package Servicio;

import Modelo.Usuario;

public interface IAutenticacionServicio {
    Usuario autenticar(String username, String password) throws Exception;
    boolean cambiarContrasena(long idUsuario, String contrasenaActual, String nuevaContrasena) throws Exception;
    boolean validarContrasena(String contrasena) throws Exception;
} 