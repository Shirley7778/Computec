package Servicio;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class SHA256EncriptacionServicio implements IEncriptacionServicio {
    
    private static final String ALGORITHM = "SHA-256";
    private static final String SALT_SEPARATOR = ":";
    
    @Override
    public String encriptar(String texto) throws Exception {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("El texto a encriptar no puede ser nulo o vacío");
        }
        
        try {
            // Generar salt aleatorio
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            
            // Combinar texto y salt
            String textoConSalt = texto + Base64.getEncoder().encodeToString(salt);
            
            // Crear hash SHA-256
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            byte[] hash = md.digest(textoConSalt.getBytes("UTF-8"));
            
            // Codificar hash en Base64
            String hashBase64 = Base64.getEncoder().encodeToString(hash);
            
            // Retornar salt + hash
            return Base64.getEncoder().encodeToString(salt) + SALT_SEPARATOR + hashBase64;
            
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("Error al encriptar: " + e.getMessage());
        }
    }
    
    @Override
    public boolean verificar(String texto, String hashCompleto) throws Exception {
        if (texto == null || hashCompleto == null) {
            return false;
        }
        
        try {
            // Separar salt y hash
            String[] partes = hashCompleto.split(SALT_SEPARATOR);
            if (partes.length != 2) {
                return false;
            }
            
            String saltBase64 = partes[0];
            String hashBase64 = partes[1];
            
            // Decodificar salt
            byte[] salt = Base64.getDecoder().decode(saltBase64);
            
            // Combinar texto con el mismo salt
            String textoConSalt = texto + Base64.getEncoder().encodeToString(salt);
            
            // Crear hash del texto ingresado
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            byte[] hashCalculado = md.digest(textoConSalt.getBytes("UTF-8"));
            String hashCalculadoBase64 = Base64.getEncoder().encodeToString(hashCalculado);
            
            // Comparar hashes
            return hashCalculadoBase64.equals(hashBase64);
            
        } catch (Exception e) {
            throw new Exception("Error al verificar hash: " + e.getMessage());
        }
    }
} 