package Modelo;

import java.math.BigDecimal;

public class Producto {
    private long productoId;
    private String nombre;
    private String categoria;
    private BigDecimal precio;

    public Producto() {}

    public Producto(long productoId, String nombre, String categoria, BigDecimal precio) {
        this.productoId = productoId;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
    }

    public long getProductoId() {
        return productoId;
    }

    public void setProductoId(long productoId) {
        this.productoId = productoId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }
}
