package Modelo;

public class Stock {
    private long stockId;
    private Producto producto;
    private int cantidadActual;
    private int minimo;

    public Stock() {}

    public Stock(long stockId, Producto producto, int cantidadActual, int minimo) {
        this.stockId = stockId;
        this.producto = producto;
        this.cantidadActual = cantidadActual;
        this.minimo = minimo;
    }

    public long getStockId() {
        return stockId;
    }

    public void setStockId(long stockId) {
        this.stockId = stockId;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidadActual() {
        return cantidadActual;
    }

    public void setCantidadActual(int cantidadActual) {
        this.cantidadActual = cantidadActual;
    }

    public int getMinimo() {
        return minimo;
    }

    public void setMinimo(int minimo) {
        this.minimo = minimo;
    }
}
