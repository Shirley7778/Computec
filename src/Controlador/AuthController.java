package Controlador;

import DAO.UsuarioDAO;
import DAO.impl.UsuarioDAOImpl;
import Modelo.Usuario;
import Utilidades.EncriptacionUtil;
import javax.swing.JOptionPane;

public class AuthController {
    private final UsuarioDAO usuarioDAO;
    private static Usuario usuarioActual;
    
    public AuthController() {
        this.usuarioDAO = new UsuarioDAOImpl();
    }
    
    /**
     * Autentica un usuario con nombre y contraseña
     * @param nombre El nombre del usuario
     * @param password La contraseña en texto plano
     * @return true si la autenticación es exitosa, false en caso contrario
     */
    public boolean autenticar(String nombre, String password) {
        try {
            if (nombre == null || nombre.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor ingrese su nombre", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            if (password == null || password.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor ingrese su contraseña", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            // Buscar usuario por nombre
            Usuario usuario = ((UsuarioDAOImpl) usuarioDAO).findByNombre(nombre);
            
            if (usuario == null) {
                JOptionPane.showMessageDialog(null, "Usuario no encontrado", "Error de Autenticación", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            // Verificar contraseña usando SHA256
            if (EncriptacionUtil.verificarPassword(password, usuario.getContrasena())) {
                usuarioActual = usuario;
                JOptionPane.showMessageDialog(null, "Bienvenido " + usuario.getNombre() + " " + usuario.getApellido(), 
                                           "Login Exitoso", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Contraseña incorrecta", "Error de Autenticación", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al autenticar: " + e.getMessage(), 
                                       "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Obtiene el usuario actualmente autenticado
     * @return El usuario actual o null si no hay usuario autenticado
     */
    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }
    
    /**
     * Cierra la sesión del usuario actual
     */
    public static void cerrarSesion() {
        usuarioActual = null;
    }
    
    /**
     * Verifica si hay un usuario autenticado
     * @return true si hay usuario autenticado, false en caso contrario
     */
    public static boolean hayUsuarioAutenticado() {
        return usuarioActual != null;
    }
} 