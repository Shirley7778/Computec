package Modelo;

import java.time.LocalDateTime;

public class MovimientoInventario {
    private long movimientoId;
    private Stock stock;
    private Pedido pedido;
    private String tipo;
    private int cantidad;
    private String motivo;
    private String referencia;
    private String observaciones;
    private LocalDateTime fecha;
    private String ubicacionProveedor;
    private UbicacionFisica ubicacionFisica;

    public MovimientoInventario() {}

    public MovimientoInventario(long movimientoId, Stock stock, Pedido pedido,
                                String tipo, int cantidad, String motivo,
                                String referencia, String observaciones,
                                LocalDateTime fecha, String ubicacionProveedor,
                                UbicacionFisica ubicacionFisica) {
        this.movimientoId = movimientoId;
        this.stock = stock;
        this.pedido = pedido;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.motivo = motivo;
        this.referencia = referencia;
        this.observaciones = observaciones;
        this.fecha = fecha;
        this.ubicacionProveedor = ubicacionProveedor;
        this.ubicacionFisica = ubicacionFisica;
    }

    public long getMovimientoId() {
        return movimientoId;
    }

    public void setMovimientoId(long movimientoId) {
        this.movimientoId = movimientoId;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getUbicacionProveedor() {
        return ubicacionProveedor;
    }

    public void setUbicacionProveedor(String ubicacionProveedor) {
        this.ubicacionProveedor = ubicacionProveedor;
    }

    public UbicacionFisica getUbicacionFisica() {
        return ubicacionFisica;
    }

    public void setUbicacionFisica(UbicacionFisica ubicacionFisica) {
        this.ubicacionFisica = ubicacionFisica;
    }
}
