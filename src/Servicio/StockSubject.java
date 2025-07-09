package Servicio;

public interface StockSubject {
    void addObserver(InventarioObserver o);
    void removeObserver(InventarioObserver o);
}
