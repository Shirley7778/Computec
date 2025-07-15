package Vista;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */

/**
 *
 * @author SHIRLEY
 */
public class Form_ControlStock extends javax.swing.JPanel {

    /** Creates new form JPanel_ControlStock */
    private void cargarTablaStock() {
        try {
            Servicio.SistemaFacade facade = new Servicio.SistemaFacade();
            java.util.List<Modelo.Stock> lista = facade.listarStock();
            java.util.Collections.reverse(lista); // Mostrar recientes primero
            String[] titulos = {"ID", "Producto", "Categoría", "Precio", "Stock Actual", "Stock Mínimo"};
            javax.swing.table.DefaultTableModel modelo = new javax.swing.table.DefaultTableModel(null, titulos);
            for (Modelo.Stock s : lista) {
                Object[] fila = new Object[6];
                fila[0] = s.getStockId();
                fila[1] = s.getProducto().getNombre();
                fila[2] = s.getProducto().getCategoria();
                fila[3] = s.getProducto().getPrecio();
                fila[4] = s.getCantidadActual();
                fila[5] = s.getMinimo();
                modelo.addRow(fila);
            }
            tbl_ControlStock.setModel(modelo);
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error al cargar stock: " + e.getMessage());
        }
    }

    private void cargarUbicaciones() {
        try {
            Servicio.UbicacionFisicaServicio ubicacionServicio = new Servicio.UbicacionFisicaServicio();
            java.util.List<Modelo.UbicacionFisica> ubicaciones = ubicacionServicio.listarUbicaciones();
            cmb_ubicacion.removeAllItems();
            for (Modelo.UbicacionFisica u : ubicaciones) {
                cmb_ubicacion.addItem(u.getMiUbicacionFisica());
            }
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error al cargar ubicaciones: " + e.getMessage());
        }
    }

    // Método para habilitar/deshabilitar campos de edición
    private void setCamposHabilitados(boolean habilitado) {
        txtNombreProducto.setEnabled(habilitado);
        cmbCategoria.setEnabled(habilitado);
        cmb_ubicacion.setEnabled(habilitado);
        txtStockInicial.setEnabled(habilitado);
        txtStockMinimo.setEnabled(habilitado);
        txtPrecio.setEnabled(habilitado);
        btn_guardar.setEnabled(habilitado);
        btn_crear.setEnabled(habilitado);
    }

    // Método para limpiar los campos
    private void limpiarCampos() {
        txtNombreProducto.setText("");
        cmbCategoria.setSelectedIndex(0);
        cmb_ubicacion.setSelectedIndex(0);
        txtStockInicial.setText("");
        txtStockMinimo.setText("");
        txtPrecio.setText("");
    }

    // Lógica para cargar datos de la fila seleccionada
    private void cargarDatosDeFilaSeleccionada() {
        int fila = tbl_ControlStock.getSelectedRow();
        if (fila >= 0) {
            txtNombreProducto.setText(tbl_ControlStock.getValueAt(fila, 1).toString());
            cmbCategoria.setSelectedItem(tbl_ControlStock.getValueAt(fila, 2).toString());
            txtPrecio.setText(tbl_ControlStock.getValueAt(fila, 3).toString());
            txtStockInicial.setText(tbl_ControlStock.getValueAt(fila, 4).toString());
            txtStockMinimo.setText(tbl_ControlStock.getValueAt(fila, 5).toString());
            setCamposHabilitados(true);
            btn_guardar.setEnabled(true);
        }
    }

    // Lógica para actualizar producto y stock
    private void btn_guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardarActionPerformed
        try {
            int fila = tbl_ControlStock.getSelectedRow();
            if (fila < 0) {
                javax.swing.JOptionPane.showMessageDialog(this, "Seleccione una fila para actualizar.");
                return;
            }
            long stockId = Long.parseLong(tbl_ControlStock.getValueAt(fila, 0).toString());
            String nombreProducto = txtNombreProducto.getText().trim();
            String categoria = cmbCategoria.getSelectedItem().toString();
            java.math.BigDecimal precio = new java.math.BigDecimal(txtPrecio.getText().trim());
            int cantidad = Integer.parseInt(txtStockInicial.getText().trim());
            int minimo = Integer.parseInt(txtStockMinimo.getText().trim());
            // Actualizar producto
            Servicio.SistemaFacade facade = new Servicio.SistemaFacade();
            Modelo.Stock stock = null;
            for (Modelo.Stock s : facade.listarStock()) {
                if (s.getStockId() == stockId) {
                    stock = s;
                    break;
                }
            }
            if (stock != null) {
                Modelo.Producto producto = stock.getProducto();
                producto.setNombre(nombreProducto);
                producto.setCategoria(categoria);
                producto.setPrecio(precio);
                stock.setCantidadActual(cantidad);
                stock.setMinimo(minimo);
                // Actualizar en BD
                DAO.ProductoDAO productoDAO = DAO.DAOFactory.getProductoDAO();
                productoDAO.update(producto);
                DAO.StockDAO stockDAO = DAO.DAOFactory.getStockDAO();
                stockDAO.update(stock);
                javax.swing.JOptionPane.showMessageDialog(this, "Datos actualizados correctamente.");
                cargarTablaStock();
                setCamposHabilitados(false);
                limpiarCampos();
            }
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error al actualizar: " + e.getMessage());
        }
    }//GEN-LAST:event_btn_guardarActionPerformed

    // Evento para habilitar campos al presionar btn_nuevo
    private void btn_nuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nuevoActionPerformed
        setCamposHabilitados(true);
        limpiarCampos();
        btn_guardar.setEnabled(false);
    }//GEN-LAST:event_btn_nuevoActionPerformed

    // Evento para cargar datos al seleccionar una fila
    private void tbl_ControlStockMouseClicked(java.awt.event.MouseEvent evt) {
        cargarDatosDeFilaSeleccionada();
    }

    // Método para filtrar la tabla de stock por nombre de producto
    private void filtrarTablaStockPorNombre(String nombre) {
        try {
            Servicio.SistemaFacade facade = new Servicio.SistemaFacade();
            java.util.List<Modelo.Stock> lista = facade.listarStock();
            java.util.Collections.reverse(lista); // Mostrar recientes primero
            String[] titulos = {"ID", "Producto", "Categoría", "Precio", "Stock Actual", "Stock Mínimo"};
            javax.swing.table.DefaultTableModel modelo = new javax.swing.table.DefaultTableModel(null, titulos);
            for (Modelo.Stock s : lista) {
                if (s.getProducto().getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                    Object[] fila = new Object[6];
                    fila[0] = s.getStockId();
                    fila[1] = s.getProducto().getNombre();
                    fila[2] = s.getProducto().getCategoria();
                    fila[3] = s.getProducto().getPrecio();
                    fila[4] = s.getCantidadActual();
                    fila[5] = s.getMinimo();
                    modelo.addRow(fila);
                }
            }
            tbl_ControlStock.setModel(modelo);
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error al filtrar stock: " + e.getMessage());
        }
    }

    // Listener para búsqueda en tiempo real
    private void txt_buscarDniKeyReleased(java.awt.event.KeyEvent evt) {
        String texto = txt_buscar.getText();
        filtrarTablaStockPorNombre(texto);
    }

    // Llama a cargarTablaStock y cargarUbicaciones en el constructor
    public Form_ControlStock() {
        initComponents();
        cargarTablaStock();
        cargarUbicaciones();
        setCamposHabilitados(false);
        btn_guardar.setEnabled(false);
        tbl_ControlStock.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_ControlStockMouseClicked(evt);
            }
        });
        txt_buscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_buscarDniKeyReleased(evt);
            }
        });
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel9 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtNombreProducto = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cmbCategoria = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtStockInicial = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtStockMinimo = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtPrecio = new javax.swing.JTextField();
        btn_crear = new javax.swing.JButton();
        lbl_ingresarDNI = new javax.swing.JLabel();
        txt_buscar = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_ControlStock = new javax.swing.JTable();
        btn_nuevo = new javax.swing.JButton();
        btn_guardar = new javax.swing.JButton();
        cmb_ubicacion = new javax.swing.JComboBox<>();

        setBackground(new java.awt.Color(51, 51, 51));
        setPreferredSize(new java.awt.Dimension(1125, 552));

        jLabel9.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Control de Stock");

        jLabel1.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Nombre Producto:");

        jLabel3.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Categoría:");

        cmbCategoria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Seleccionar--", "Electronico", "Periferico", "Mueble", "Accesorios", "Almacenamiento", "Redes", "Cables", "Pantallas" }));

        jLabel4.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Ubicación Física:");

        jLabel2.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Stock Inicial:");

        jLabel5.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Stock Mínimo:");

        jLabel6.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Precio:");

        btn_crear.setBackground(new java.awt.Color(25, 25, 25));
        btn_crear.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        btn_crear.setForeground(new java.awt.Color(255, 255, 255));
        btn_crear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Crear.png"))); // NOI18N
        btn_crear.setText("Crear");
        btn_crear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_crearActionPerformed(evt);
            }
        });

        lbl_ingresarDNI.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        lbl_ingresarDNI.setForeground(new java.awt.Color(255, 255, 255));
        lbl_ingresarDNI.setText("Buscar producto:");

        txt_buscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_buscarActionPerformed(evt);
            }
        });

        tbl_ControlStock.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tbl_ControlStock);

        btn_nuevo.setBackground(new java.awt.Color(25, 25, 25));
        btn_nuevo.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        btn_nuevo.setForeground(new java.awt.Color(255, 255, 255));
        btn_nuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Nuevo1.png"))); // NOI18N
        btn_nuevo.setText("Nuevo");
        btn_nuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_nuevoActionPerformed(evt);
            }
        });

        btn_guardar.setBackground(new java.awt.Color(25, 25, 25));
        btn_guardar.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        btn_guardar.setForeground(new java.awt.Color(255, 255, 255));
        btn_guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Guardar.png"))); // NOI18N
        btn_guardar.setText("Guardar");
        btn_guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_guardarActionPerformed(evt);
            }
        });

        cmb_ubicacion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_buscar)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(15, 15, 15)
                        .addComponent(txtStockInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(txtStockMinimo, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 131, Short.MAX_VALUE)
                        .addComponent(btn_crear, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btn_nuevo)
                                .addGap(62, 62, 62)
                                .addComponent(btn_guardar))
                            .addComponent(lbl_ingresarDNI)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addGap(18, 18, 18)
                                        .addComponent(cmbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cmb_ubicacion, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(115, 115, 115)
                        .addComponent(btn_crear))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(cmbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(cmb_ubicacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtStockInicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(txtStockMinimo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(45, 45, 45)
                .addComponent(lbl_ingresarDNI)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_nuevo)
                    .addComponent(btn_guardar))
                .addGap(19, 19, 19))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btn_crearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_crearActionPerformed
        try {
            // 1. Crear producto
            Modelo.Producto producto = new Modelo.Producto();
            producto.setNombre(txtNombreProducto.getText().trim());
            producto.setCategoria(cmbCategoria.getSelectedItem().toString());
            producto.setPrecio(new java.math.BigDecimal(txtPrecio.getText().trim()));

            // 2. Crear stock
            Modelo.Stock stock = new Modelo.Stock();
            stock.setCantidadActual(Integer.parseInt(txtStockInicial.getText().trim()));
            stock.setMinimo(Integer.parseInt(txtStockMinimo.getText().trim()));

            // 3. Crear movimiento inventario
            Modelo.MovimientoInventario movimiento = new Modelo.MovimientoInventario();
            movimiento.setTipo("Entrada");
            movimiento.setCantidad(stock.getCantidadActual());
            // Ubicación física
            Modelo.UbicacionFisica ubicacion = new Modelo.UbicacionFisica();
            int idx = cmb_ubicacion.getSelectedIndex();
            if (idx >= 0 && cmb_ubicacion.getItemCount() > 0) {
                ubicacion.setUbicacionId(idx + 1L); 
                movimiento.setUbicacionFisica(ubicacion);
            } else {
                movimiento.setUbicacionFisica(null);
            }
            // Los demás campos pueden ser null
            movimiento.setMotivo("Compra Producto");
            movimiento.setReferencia(null);
            movimiento.setObservaciones(null);
            movimiento.setUbicacionProveedor(null);
            movimiento.setPedido(null);

            // 4. Registrar todo usando la fachada
            Servicio.SistemaFacade facade = new Servicio.SistemaFacade();
            facade.registrarProductoConStockYMovimiento(producto, stock, movimiento);

            // 5. Refrescar tabla
            cargarTablaStock();

            javax.swing.JOptionPane.showMessageDialog(this, "Producto y stock registrados correctamente.");
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }//GEN-LAST:event_btn_crearActionPerformed

    private void txt_buscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_buscarActionPerformed

    }//GEN-LAST:event_txt_buscarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_crear;
    private javax.swing.JButton btn_guardar;
    private javax.swing.JButton btn_nuevo;
    private javax.swing.JComboBox<String> cmbCategoria;
    private javax.swing.JComboBox<String> cmb_ubicacion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbl_ingresarDNI;
    private javax.swing.JTable tbl_ControlStock;
    private javax.swing.JTextField txtNombreProducto;
    private javax.swing.JTextField txtPrecio;
    private javax.swing.JTextField txtStockInicial;
    private javax.swing.JTextField txtStockMinimo;
    private javax.swing.JTextField txt_buscar;
    // End of variables declaration//GEN-END:variables

}
