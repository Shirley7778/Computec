package Controlador;

import DAO.UsuarioDAO;
import DAO.impl.UsuarioDAOImpl;
import Modelo.Usuario;
import javax.swing.JOptionPane;

public class AuthController {
    private final UsuarioDAO usuarioDAO;
    private static Usuario usuarioActual;
    
    public AuthController() {
        this.usuarioDAO = new UsuarioDAOImpl();
    }
    
    /**
     * Autentica un usuario con nombre y contraseña
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

            // Manejo de intentos y bloqueo temporal
            int maxIntentos = 3;
            int minutosBloqueo = 5;
            java.time.LocalDateTime ahora = java.time.LocalDateTime.now();
            if (usuario.getIntentos() >= maxIntentos) {
                if (usuario.getUltimaVez() != null) {
                    java.time.Duration duracion = java.time.Duration.between(usuario.getUltimaVez(), ahora);
                    if (duracion.toMinutes() < minutosBloqueo) {
                        JOptionPane.showMessageDialog(null, "Usuario bloqueado. Intente nuevamente en " + (minutosBloqueo - duracion.toMinutes()) + " minutos.", "Usuario bloqueado", JOptionPane.ERROR_MESSAGE);
                        return false;
                    } else {
                        // Desbloquear usuario
                        usuario.setIntentos(0);
                        usuario.setUltimaVez(null);
                        usuarioDAO.actualizarIntentosYUltimaVez(usuario.getIdUsuario(), 0, null);
                    }
                }
            }

            // Verificar contraseña usando SHA256
            if (Utilidades.EncriptacionUtil.verificarPassword(password, usuario.getContrasena())) {
                usuarioActual = usuario;
                // Resetear intentos y ultima_vez
                usuario.setIntentos(0);
                usuario.setUltimaVez(null);
                usuarioDAO.actualizarIntentosYUltimaVez(usuario.getIdUsuario(), 0, null);
                JOptionPane.showMessageDialog(null, "Bienvenido " + usuario.getNombre() + " " + usuario.getApellido(), 
                                           "Login Exitoso", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                int nuevosIntentos = usuario.getIntentos() + 1;
                usuario.setIntentos(nuevosIntentos);
                usuario.setUltimaVez(ahora);
                usuarioDAO.actualizarIntentosYUltimaVez(usuario.getIdUsuario(), nuevosIntentos, ahora);
                if (nuevosIntentos >= maxIntentos) {
                    JOptionPane.showMessageDialog(null, "Usuario bloqueado por " + minutosBloqueo + " minutos.", "Usuario bloqueado", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Contraseña incorrecta. Intentos restantes: " + (maxIntentos - nuevosIntentos), "Error de Autenticación", JOptionPane.ERROR_MESSAGE);
                }
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