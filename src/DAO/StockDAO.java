package DAO;

import Modelo.Stock;

public interface StockDAO extends Dao<Stock> {
   Stock findByProductoNombre(String nombre) throws Exception;
}
