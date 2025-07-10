import Utilidades.EncriptacionUtil;
import DAO.impl.UsuarioDAOImpl;
import Modelo.Usuario;

/**
 * Script de prueba para verificar el sistema de login
 */
public class TestLogin {
    
    public static void main(String[] args) {
        System.out.println("=== PRUEBA DEL SISTEMA DE LOGIN ===");
        
        // Prueba 1: Verificar encriptación SHA256
        System.out.println("\n1. Prueba de encriptación SHA256:");
        String password = "admin";
        String hash = EncriptacionUtil.encriptarSHA256(password);
        System.out.println("Contraseña: " + password);
        System.out.println("Hash SHA256: " + hash);
        
        // Verificar que la contraseña coincide
        boolean esValida = EncriptacionUtil.verificarPassword(password, hash);
        System.out.println("Verificación: " + (esValida ? "CORRECTO" : "INCORRECTO"));
        
        // Prueba 2: Verificar contraseñas de usuarios de prueba
        System.out.println("\n2. Prueba de contraseñas de usuarios:");
        String[] passwords = {"admin", "cliente", "almacenero", "soporte"};
        String[] hashes = {
            "75e6c848206f01c6b1d7455e319c96471b49ae69bde97316b4ef724147bec385",
            "785c07307d5160984c6a90c8c0a384cada6ce31cbfc09a76c6b2afe06f79bdbc",
            "d6c025ab876840151b08da4486c16274933e12de0d1ca1e7966f8a17b8545aef",
            "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92"
        };
        
        for (int i = 0; i < passwords.length; i++) {
            boolean valida = EncriptacionUtil.verificarPassword(passwords[i], hashes[i]);
            System.out.println("Usuario " + (i+1) + " (" + passwords[i] + "): " + 
                             (valida ? "CORRECTO" : "INCORRECTO"));
        }
        
        // Prueba 3: Información de usuarios de prueba
        System.out.println("\n3. Información de usuarios de prueba:");
        System.out.println("Nombre: Ujens, Contraseña: admin, Rol: Admin");
        System.out.println("Nombre: Ushirley, Contraseña: cliente, Rol: Atencion Cliente");
        System.out.println("Nombre: Uliliana, Contraseña: almacenero, Rol: Almacenero");
        System.out.println("Nombre: Ujuanito, Contraseña: soporte, Rol: Soporte");
        
        System.out.println("\n=== PRUEBA COMPLETADA ===");
        System.out.println("Para usar el sistema:");
        System.out.println("1. Ejecutar la aplicación");
        System.out.println("2. Usar cualquiera de los usuarios de prueba");
        System.out.println("3. Navegar por los módulos según los permisos");
    }
} 