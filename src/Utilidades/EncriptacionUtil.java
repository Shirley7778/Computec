package Utilidades;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class EncriptacionUtil {
    
    /**
     * Encripta una cadena usando SHA256
     */
    public static String encriptarSHA256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error al encriptar", e);
        }
    }
    
    /**
     * Verifica si una contraseña coincide con su hash
     */
    public static boolean verificarPassword(String password, String hash) {
        String passwordHash = encriptarSHA256(password);
        return passwordHash.equals(hash);
    }
} 