/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Vista;

import Servicio.SistemaFacade;
import Modelo.Cliente;
import Modelo.Producto;
import Modelo.DetallePedido;
import Modelo.Pedido;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Servicio.StockServicio;
import Servicio.InventarioObserver;

public class Form_Pedidos1 extends javax.swing.JPanel {

    /**
     * Creates new form JPanel_Pedidos1
     */
    private List<Cliente> clientes = new ArrayList<>();
    private List<Producto> productos = new ArrayList<>();
    private List<DetallePedido> detalles = new ArrayList<>();
    private Cliente clienteSeleccionado = null;
    private Producto productoSeleccionado = null;
    private StockServicio stockServicio = new StockServicio();
    private SistemaFacade facade = new SistemaFacade();
    private boolean modoEdicion = false;
    private boolean modoVisualizacion = false;
    
     public Form_Pedidos1() {
        this(false, false);
        mostrarSiguienteNumeroPedido();
    }
    public Form_Pedidos1(boolean modoEdicion) {
        this(modoEdicion, false);
    }
    
    public Form_Pedidos1(boolean modoEdicion, boolean modoVisualizacion) {
        this.modoEdicion = modoEdicion;
        this.modoVisualizacion = modoVisualizacion;
        initComponents();
        cargarEmpresas();
        cargarProductos();
        configurarListeners();
        limpiarCamposProducto();
        limpiarTablaDetalle();
        if (modoVisualizacion) {
            deshabilitarTodo();
        }
        // Registrar observer visual para stock bajo
        stockServicio.addObserver(new InventarioObserver() {
            @Override
            public void actualizado() {
                verificarStockBajo();
                // Mostrar JOptionPane si el stock está bajo
                if (productoSeleccionado != null) {
                    try {
                        Modelo.Stock stock = facade.obtenerStockPorProductoId(productoSeleccionado.getProductoId());
                        if (stock != null && stock.getCantidadActual() <= stock.getMinimo()) {
                            JOptionPane.showMessageDialog(Form_Pedidos1.this, "¡Advertencia! El stock del producto '" + productoSeleccionado.getNombre() + "' está bajo o en el mínimo.");
                        }
                    } catch (Exception e) {}
                }
            }
        });
        
    }

    
    private void cargarEmpresas() {
        try {
            clientes = facade.listarClientes();
            cmbEmpresa.removeAllItems();
            for (Cliente c : clientes) {
                cmbEmpresa.addItem(c.getNombreEmpresa());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar empresas: " + e.getMessage());
        }
    }

    private void cargarProductos() {
        try {
            productos = facade.listarProductos();
            cmbProductos.removeAllItems();
            for (Producto p : productos) {
                cmbProductos.addItem(p.getNombre());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar productos: " + e.getMessage());
        }
    }

    private void deshabilitarTodo() {
        btn_agregarTabla.setEnabled(false);
        btn_eliminar.setEnabled(false);
        btn_limpiar.setEnabled(false);
        btn_crear.setEnabled(false);
        cmbEmpresa.setEnabled(false);
        cmbProductos.setEnabled(false);
        txtCantidad.setEditable(false);
        txtPrecioUnitario.setEditable(false);
        txtStock.setEditable(false);
        txtTotalPro.setEditable(false);
        txtRuc.setEditable(false);
        txtPedido.setEditable(false);
        tbl_detallePedido.setEnabled(false);
        btn_buscar.setEnabled(false);
    }

    private void actualizarTotalPro() {
        try {
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());
            BigDecimal precio = new BigDecimal(txtPrecioUnitario.getText().trim());
            BigDecimal total = precio.multiply(BigDecimal.valueOf(cantidad));
            txtTotalPro.setText(total.toString());
        } catch (Exception e) {
            txtTotalPro.setText("");
        }
    }

    private void configurarListeners() {
        cmbEmpresa.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int idx = cmbEmpresa.getSelectedIndex();
                if (idx >= 0 && idx < clientes.size()) {
                    clienteSeleccionado = clientes.get(idx);
                    txtRuc.setText(clienteSeleccionado.getRucDni());
                }
            }
        });
        cmbProductos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int idx = cmbProductos.getSelectedIndex();
                if (idx >= 0 && idx < productos.size()) {
                    productoSeleccionado = productos.get(idx);
                    txtPrecioUnitario.setText(productoSeleccionado.getPrecio().toString());
                    try {
                        Modelo.Stock stock = facade.obtenerStockPorProductoId(productoSeleccionado.getProductoId());
                        if (stock != null) {
                            txtStock.setText(String.valueOf(stock.getCantidadActual()));
                        } else {
                            txtStock.setText("0");
                        }
                    } catch (Exception ex) {
                        txtStock.setText("0");
                    }
                    actualizarTotalPro();
                    verificarStockBajo(); // Actualiza el color del campo de stock inmediatamente
                }
            }
        });
        txtCantidad.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { actualizarTotalPro(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { actualizarTotalPro(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { actualizarTotalPro(); }
        });
    }

    public void mostrarSiguienteNumeroPedido() {
        try {
            List<Pedido> pedidos = facade.listarPedidos();
            long maxId = 0;
            for (Pedido p : pedidos) {
                if (p.getIdPedido() > maxId) maxId = p.getIdPedido();
            }
            txtPedido.setText(String.valueOf(maxId + 1));
        } catch (Exception e) {
            txtPedido.setText("");
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel9 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        cmbEmpresa = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        txtRuc = new javax.swing.JTextField();
        btn_buscar = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtPedido = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        cmbProductos = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        txtStock = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtPrecioUnitario = new javax.swing.JTextField();
        txtCantidad = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtTotalPro = new javax.swing.JTextField();
        btn_limpiar = new javax.swing.JButton();
        btn_eliminar = new javax.swing.JButton();
        btn_agregarTabla = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_detallePedido = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        txtSubTotal = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtIgv = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtTotalPagar = new javax.swing.JTextField();
        btn_crear = new javax.swing.JButton();

        setBackground(new java.awt.Color(51, 51, 51));

        jLabel9.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Gestión de Pedidos");

        jLabel1.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Empresa");

        cmbEmpresa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbEmpresa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbEmpresaActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("RUC:");

        btn_buscar.setBackground(new java.awt.Color(25, 25, 25));
        btn_buscar.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        btn_buscar.setForeground(new java.awt.Color(255, 255, 255));
        btn_buscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/lupa.png"))); // NOI18N
        btn_buscar.setText("Buscar");
        btn_buscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_buscarActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Pedido");

        jLabel8.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("N°:");

        jLabel12.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Productos:");

        jLabel10.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Cantidad:");

        cmbProductos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel13.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Stock:");

        jLabel14.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Precio Unitario:");

        jLabel11.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Total:");

        btn_limpiar.setBackground(new java.awt.Color(25, 25, 25));
        btn_limpiar.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        btn_limpiar.setForeground(new java.awt.Color(255, 255, 255));
        btn_limpiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/5.png"))); // NOI18N
        btn_limpiar.setText("Limpiar");
        btn_limpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_limpiarActionPerformed(evt);
            }
        });

        btn_eliminar.setBackground(new java.awt.Color(25, 25, 25));
        btn_eliminar.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        btn_eliminar.setForeground(new java.awt.Color(255, 255, 255));
        btn_eliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Eliminar.png"))); // NOI18N
        btn_eliminar.setText("Eliminar");
        btn_eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_eliminarActionPerformed(evt);
            }
        });

        btn_agregarTabla.setBackground(new java.awt.Color(25, 25, 25));
        btn_agregarTabla.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        btn_agregarTabla.setForeground(new java.awt.Color(255, 255, 255));
        btn_agregarTabla.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Nuevo2.png"))); // NOI18N
        btn_agregarTabla.setText("Agregar");
        btn_agregarTabla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_agregarTablaActionPerformed(evt);
            }
        });

        tbl_detallePedido.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tbl_detallePedido);

        jLabel4.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("SubTotal:");

        jLabel5.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("IGV:");

        jLabel6.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Total a Pagar:");

        btn_crear.setBackground(new java.awt.Color(25, 25, 25));
        btn_crear.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        btn_crear.setForeground(new java.awt.Color(255, 255, 255));
        btn_crear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Crear.png"))); // NOI18N
        btn_crear.setText("Agregar Pedido");
        btn_crear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_crearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addGap(467, 467, 467))
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(18, 18, 18)
                        .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel11)
                        .addGap(18, 18, 18)
                        .addComponent(txtTotalPro, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtIgv, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(txtTotalPagar, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btn_crear, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(521, 521, 521)
                                .addComponent(btn_agregarTabla)
                                .addGap(41, 41, 41)
                                .addComponent(btn_eliminar)
                                .addGap(36, 36, 36)
                                .addComponent(btn_limpiar))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addGap(18, 18, 18)
                                        .addComponent(cmbEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel3)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtRuc, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btn_buscar))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel12)
                                        .addGap(18, 18, 18)
                                        .addComponent(cmbProductos, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel14)
                                        .addGap(18, 18, 18)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(172, 172, 172)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel7)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(6, 6, 6)
                                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addComponent(txtPrecioUnitario, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(8, 8, 8)))
                        .addGap(36, 36, 36))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jLabel9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_buscar)
                            .addComponent(txtRuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(cmbEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(cmbProductos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(txtPrecioUnitario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(txtTotalPro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_limpiar)
                    .addComponent(btn_eliminar)
                    .addComponent(btn_agregarTabla))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(txtIgv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(txtTotalPagar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_crear))
                .addGap(24, 24, 24))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btn_buscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_buscarActionPerformed
        // TODO add your handling code here:
        String ruc = txtRuc.getText().trim();
        if (ruc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un RUC para buscar.");
            return;
        }
        try {
            Cliente cliente = facade.buscarClientePorRuc(ruc);
            if (cliente != null) {
                for (int i = 0; i < clientes.size(); i++) {
                    if (clientes.get(i).getRucDni().equalsIgnoreCase(ruc)) {
                        cmbEmpresa.setSelectedIndex(i);
                        return;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró cliente con ese RUC.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al buscar cliente: " + e.getMessage());
        }
    }//GEN-LAST:event_btn_buscarActionPerformed

    private void btn_limpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_limpiarActionPerformed
 
         limpiarTablaDetalle();
        detalles.clear();
        calcularTotales();
    }//GEN-LAST:event_btn_limpiarActionPerformed

    private void btn_eliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_eliminarActionPerformed

         int fila = tbl_detallePedido.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una fila para eliminar.");
            return;
        }
        detalles.remove(fila);
        DefaultTableModel modelo = (DefaultTableModel) tbl_detallePedido.getModel();
        modelo.removeRow(fila);
        calcularTotales();
    }//GEN-LAST:event_btn_eliminarActionPerformed

    private void btn_agregarTablaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_agregarTablaActionPerformed
        // TODO add your handling code here:
        if (productoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto.");
            return;
        }
        int cantidad;
        try {
            cantidad = Integer.parseInt(txtCantidad.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Cantidad inválida.");
            return;
        }
        int stockDisponible = 0;
        int stockMinimo = 0;
        try {
            Modelo.Stock stock = facade.obtenerStockPorProductoId(productoSeleccionado.getProductoId());
            if (stock != null) {
                stockDisponible = stock.getCantidadActual();
                stockMinimo = stock.getMinimo();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al consultar el stock: " + e.getMessage());
            return;
        }
        if (cantidad > stockDisponible) {
            JOptionPane.showMessageDialog(this, "Stock insuficiente para la cantidad seleccionada.");
            return;
        }
        if (stockDisponible <= stockMinimo) {
            stockServicio.notificarObservers(); // Notifica a los observers
            // Eliminar JOptionPane aquí, lo maneja el observer
        }
        BigDecimal precio = productoSeleccionado.getPrecio();
        BigDecimal total = precio.multiply(BigDecimal.valueOf(cantidad));
        DetallePedido detalle = new DetallePedido();
        detalle.setProducto(productoSeleccionado);
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(precio);
        detalle.setTotal(total);
        detalles.add(detalle);
        DefaultTableModel modelo = (DefaultTableModel) tbl_detallePedido.getModel();
        modelo.addRow(new Object[] {
            detalle.getProducto().getNombre(),
            detalle.getCantidad(),
            detalle.getPrecioUnitario(),
            detalle.getTotal()
        });
        calcularTotales();
        limpiarCamposProducto();
    }//GEN-LAST:event_btn_agregarTablaActionPerformed

    private void btn_crearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_crearActionPerformed
        if (clienteSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una empresa.");
            return;
        }
        if (detalles.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Agregue al menos un producto al pedido.");
            return;
        }
        try {
            
            Pedido pedido = new Pedido();
            pedido.setCliente(clienteSeleccionado);
            pedido.setFecha(java.time.LocalDateTime.now());
            pedido.setEstado("Pendiente");
            
            // Calcular totales
            BigDecimal subtotal = BigDecimal.ZERO;
            for (DetallePedido detalle : detalles) {
                subtotal = subtotal.add(detalle.getTotal());
            }
            BigDecimal igv = subtotal.multiply(new BigDecimal("0.18"));
            BigDecimal total = subtotal.add(igv);
            
            pedido.setSubtotal(subtotal);
            pedido.setIgv(igv);
            pedido.setTotal(total);
            
            // Generar número de pedido
            String numeroPedido = facade.generarNumeroPedido();
            pedido.setNumeroPedido(numeroPedido);
            
            // Guardar pedido usando Facade
            facade.crearPedido(pedido, detalles);
            
            JOptionPane.showMessageDialog(this, "Pedido creado exitosamente con número: " + numeroPedido);
            
            // Cerrar el diálogo
            java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.dispose();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al crear pedido: " + e.getMessage());
        }
    }//GEN-LAST:event_btn_crearActionPerformed

    private void cmbEmpresaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbEmpresaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbEmpresaActionPerformed

    private void verificarStockBajo() {
        // Verificación visual de stock bajo
        if (productoSeleccionado != null) {
            try {
                Modelo.Stock stock = facade.obtenerStockPorProductoId(productoSeleccionado.getProductoId());
                if (stock != null && stock.getCantidadActual() <= stock.getMinimo()) {
                    txtStock.setBackground(java.awt.Color.YELLOW);
                    txtStock.setForeground(java.awt.Color.RED);
                } else {
                    txtStock.setBackground(java.awt.Color.WHITE);
                    txtStock.setForeground(java.awt.Color.BLACK);
                }
            } catch (Exception e) {
                // Manejar error silenciosamente
            }
        }
    }
    
    public void cargarDatosPedido(Pedido pedido) {
        if (pedido == null) return;
        try {
            // Cargar cliente
            clienteSeleccionado = pedido.getCliente();
            txtRuc.setText(clienteSeleccionado.getRucDni());
            // Seleccionar cliente en el combo
            for (int i = 0; i < cmbEmpresa.getItemCount(); i++) {
                if (cmbEmpresa.getItemAt(i).equals(clienteSeleccionado.getNombreEmpresa())) {
                    cmbEmpresa.setSelectedIndex(i);
                    break;
                }
            }
            // Cargar número de pedido
            txtPedido.setText(pedido.getNumeroPedido());
            // Cargar detalles del pedido
            List<DetallePedido> detallesPedido = facade.obtenerDetallesPorPedidoId(pedido.getIdPedido());
            detalles.clear();
            detalles.addAll(detallesPedido);
            // Crear modelo temporal y agregar filas
            DefaultTableModel modelo = new DefaultTableModel(new Object[]{"Producto", "Cantidad", "Precio Unitario", "Total"}, 0);
            for (DetallePedido detalle : detalles) {
                modelo.addRow(new Object[] {
                    detalle.getProducto().getNombre(),
                    detalle.getCantidad(),
                    detalle.getPrecioUnitario(),
                    detalle.getTotal()
                });
            }
            tbl_detallePedido.setModel(modelo);
            // Cargar totales
            txtSubTotal.setText(pedido.getSubtotal().toString());
            txtIgv.setText(pedido.getIgv().toString());
            txtTotalPagar.setText(pedido.getTotal().toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos del pedido: " + e.getMessage());
        }
    }

    private void limpiarTablaDetalle() {
        DefaultTableModel modelo = new DefaultTableModel(new Object[]{"Producto", "Cantidad", "Precio Unitario", "Total"}, 0);
        tbl_detallePedido.setModel(modelo);
    }

    private void limpiarCamposProducto() {
        cmbProductos.setSelectedIndex(-1);
        txtCantidad.setText("");
        txtPrecioUnitario.setText("");
        txtStock.setText("");
        txtTotalPro.setText("");
    }

    private void calcularTotales() {
        BigDecimal subtotal = BigDecimal.ZERO;
        for (DetallePedido d : detalles) {
            subtotal = subtotal.add(d.getTotal());
        }
        BigDecimal igv = subtotal.multiply(BigDecimal.valueOf(0.18));
        BigDecimal total = subtotal.add(igv);
        java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
        txtSubTotal.setText(df.format(subtotal));
        txtIgv.setText(df.format(igv));
        txtTotalPagar.setText(df.format(total));
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_agregarTabla;
    private javax.swing.JButton btn_buscar;
    private javax.swing.JButton btn_crear;
    private javax.swing.JButton btn_eliminar;
    private javax.swing.JButton btn_limpiar;
    private javax.swing.JComboBox<String> cmbEmpresa;
    private javax.swing.JComboBox<String> cmbProductos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl_detallePedido;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtIgv;
    private javax.swing.JTextField txtPedido;
    private javax.swing.JTextField txtPrecioUnitario;
    private javax.swing.JTextField txtRuc;
    private javax.swing.JTextField txtStock;
    private javax.swing.JTextField txtSubTotal;
    private javax.swing.JTextField txtTotalPagar;
    private javax.swing.JTextField txtTotalPro;
    // End of variables declaration//GEN-END:variables
}
