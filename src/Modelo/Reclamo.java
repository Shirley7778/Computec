package Modelo;

import java.time.LocalDate;

public class Reclamo {
    private long reclamoId;
    private Cliente cliente;
    private Pedido pedido;
    private Usuario usuario;
    private String tipo;
    private String prioridad;
    private String estado;
    private LocalDate fechaVencimiento;
    private String descripcion;

    public Reclamo() {}

    public Reclamo(long reclamoId, Cliente cliente, Pedido pedido, Usuario usuario,
                   String tipo, String prioridad, String estado,
                   LocalDate fechaVencimiento, String descripcion) {
        this.reclamoId = reclamoId;
        this.cliente = cliente;
        this.pedido = pedido;
        this.usuario = usuario;
        this.tipo = tipo;
        this.prioridad = prioridad;
        this.estado = estado;
        this.fechaVencimiento = fechaVencimiento;
        this.descripcion = descripcion;
    }

    public long getReclamoId() {
        return reclamoId;
    }

    public void setReclamoId(long reclamoId) {
        this.reclamoId = reclamoId;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
