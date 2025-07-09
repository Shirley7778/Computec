package Modelo;

public class UbicacionFisica {
    private long ubicacionId;
    private String miUbicacionFisica;
    private Integer capacidad;
    private String tipo;
    private String estado;
    private String descripcion;

    public UbicacionFisica() {}

    public UbicacionFisica(long ubicacionId, String miUbicacionFisica, Integer capacidad,
                           String tipo, String estado, String descripcion) {
        this.ubicacionId = ubicacionId;
        this.miUbicacionFisica = miUbicacionFisica;
        this.capacidad = capacidad;
        this.tipo = tipo;
        this.estado = estado;
        this.descripcion = descripcion;
    }

    public long getUbicacionId() {
        return ubicacionId;
    }

    public void setUbicacionId(long ubicacionId) {
        this.ubicacionId = ubicacionId;
    }

    public String getMiUbicacionFisica() {
        return miUbicacionFisica;
    }

    public void setMiUbicacionFisica(String miUbicacionFisica) {
        this.miUbicacionFisica = miUbicacionFisica;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
