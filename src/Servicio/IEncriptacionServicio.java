package Servicio;

public interface IEncriptacionServicio {
    String encriptar(String texto) throws Exception;
    boolean verificar(String texto, String hash) throws Exception;
} 