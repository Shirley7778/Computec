package Servicio;

import DAO.ReclamoDAO;
import DAO.DAOFactory;
import Modelo.Reclamo;
import java.util.List;

/**
 * Servicio para manejar la lógica de negocio de los reclamos
 * Siguiendo el principio de Responsabilidad Única (SRP)
 */
public class ReclamoServicio {
    private final ReclamoDAO reclamoDAO;
    
    public ReclamoServicio() {
        this.reclamoDAO = DAOFactory.getReclamoDAO();
    }
    
    /**
     * Crear un nuevo reclamo
     */
    public void crearReclamo(Reclamo reclamo) throws Exception {
        validarReclamo(reclamo);
        reclamoDAO.create(reclamo);
    }
    
    /**
     * Actualizar un reclamo existente
     */
    public void actualizarReclamo(Reclamo reclamo) throws Exception {
        validarReclamo(reclamo);
        reclamoDAO.update(reclamo);
    }
    
    /**
     * Obtener todos los reclamos
     */
    public List<Reclamo> listarReclamos() throws Exception {
        return reclamoDAO.findAll();
    }
    
    /**
     * Obtener reclamo por ID
     */
    public Reclamo obtenerReclamoPorId(long id) throws Exception {
        return reclamoDAO.findById(id);
    }
    
    /**
     * Obtener reclamos por cliente
     */
    public List<Reclamo> obtenerReclamosPorCliente(long clienteId) throws Exception {
        return reclamoDAO.findByClienteId(clienteId);
    }
    
    /**
     * Obtener reclamos por RUC/DNI
     */
    public List<Reclamo> obtenerReclamosPorRucDni(String rucDni) throws Exception {
        return reclamoDAO.findByRucDni(rucDni);
    }
    
    /**
     * Obtener reclamos por estado
     */
    public List<Reclamo> obtenerReclamosPorEstado(String estado) throws Exception {
        return reclamoDAO.findByEstado(estado);
    }
    
    /**
     * Cambiar estado de un reclamo
     */
    public void cambiarEstadoReclamo(long reclamoId, String nuevoEstado) throws Exception {
        validarEstado(nuevoEstado);
        reclamoDAO.updateEstado(reclamoId, nuevoEstado);
    }
    
    /**
     * Cerrar un reclamo (cambiar estado a "Cerrado")
     */
    public void cerrarReclamo(long reclamoId) throws Exception {
        reclamoDAO.updateEstado(reclamoId, "Cerrado");
    }
    
    /**
     * Eliminar un reclamo
     */
    public void eliminarReclamo(long id) throws Exception {
        reclamoDAO.delete(id);
    }
    
    /**
     * Validar datos del reclamo antes de crear/actualizar
     */
    private void validarReclamo(Reclamo reclamo) throws Exception {
        if (reclamo.getCliente() == null) {
            throw new Exception("El cliente es obligatorio");
        }
        if (reclamo.getUsuario() == null) {
            throw new Exception("El usuario asignado es obligatorio");
        }
        if (reclamo.getTipo() == null || reclamo.getTipo().trim().isEmpty()) {
            throw new Exception("El tipo de reclamo es obligatorio");
        }
        if (reclamo.getPrioridad() == null || reclamo.getPrioridad().trim().isEmpty()) {
            throw new Exception("La prioridad es obligatoria");
        }
        if (reclamo.getEstado() == null || reclamo.getEstado().trim().isEmpty()) {
            throw new Exception("El estado es obligatorio");
        }
        if (reclamo.getDescripcion() == null || reclamo.getDescripcion().trim().isEmpty()) {
            throw new Exception("La descripción es obligatoria");
        }
    }
    
    /**
     * Validar estado del reclamo
     */
    private void validarEstado(String estado) throws Exception {
        String[] estadosValidos = {"Abierto", "En proceso", "Resuelto", "Cerrado"};
        boolean estadoValido = false;
        for (String est : estadosValidos) {
            if (est.equals(estado)) {
                estadoValido = true;
                break;
            }
        }
        if (!estadoValido) {
            throw new Exception("Estado no válido. Estados válidos: Abierto, En proceso, Resuelto, Cerrado");
        }
    }
} 