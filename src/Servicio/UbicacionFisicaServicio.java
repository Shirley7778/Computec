package Servicio;

import DAO.UbicacionFisicaDAO;
import DAO.DAOFactory;
import Modelo.UbicacionFisica;
import java.util.List;

public class UbicacionFisicaServicio {
    private final UbicacionFisicaDAO ubicacionDAO;
    
    public UbicacionFisicaServicio() {
        this.ubicacionDAO = DAOFactory.getUbicacionFisicaDAO();
    }
    
    public void crearUbicacion(UbicacionFisica ubicacion) throws Exception {
        if (ubicacion.getMiUbicacionFisica() == null || ubicacion.getMiUbicacionFisica().trim().isEmpty()) {
            throw new IllegalArgumentException("La zona de ubicación es obligatoria");
        }
        
        if (ubicacion.getCapacidad() != null && ubicacion.getCapacidad() <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor a 0");
        }
        
        ubicacionDAO.create(ubicacion);
    }
    
    public void actualizarUbicacion(UbicacionFisica ubicacion) throws Exception {
        if (ubicacion.getUbicacionId() <= 0) {
            throw new IllegalArgumentException("ID de ubicación inválido");
        }
        
        if (ubicacion.getMiUbicacionFisica() == null || ubicacion.getMiUbicacionFisica().trim().isEmpty()) {
            throw new IllegalArgumentException("La zona de ubicación es obligatoria");
        }
        
        ubicacionDAO.update(ubicacion);
    }
    
    public void eliminarUbicacion(long ubicacionId) throws Exception {
        if (ubicacionId <= 0) {
            throw new IllegalArgumentException("ID de ubicación inválido");
        }
        
        ubicacionDAO.delete(ubicacionId);
    }
    
    public UbicacionFisica buscarUbicacionPorId(long ubicacionId) throws Exception {
        return ubicacionDAO.findById(ubicacionId);
    }
    
    public UbicacionFisica buscarUbicacionPorNombre(String nombre) throws Exception {
        return ubicacionDAO.findByNombre(nombre);
    }

    public List<UbicacionFisica> listarUbicaciones() throws Exception {
        return ubicacionDAO.findAll();
    }
} 