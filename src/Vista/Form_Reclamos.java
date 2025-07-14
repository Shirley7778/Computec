package Vista;

import Servicio.SistemaFacade;
import Modelo.Reclamo;
import Modelo.Cliente;
import Modelo.Pedido;
import Modelo.Usuario;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.Date;

public class Form_Reclamos extends javax.swing.JPanel {

    private SistemaFacade facade;
    private List<Cliente> clientes;
    private List<Pedido> pedidosConfirmados;
    private List<Usuario> usuariosSoporte;
    private List<Reclamo> reclamosGlobal;
    private Reclamo reclamoSeleccionado;
    private boolean modoEdicion = false;

    public Form_Reclamos() {
        initComponents();
        facade = new SistemaFacade();
        inicializarFormulario();
        cargarTablaReclamos();
        deshabilitarCampos();
    }

    private void inicializarFormulario() {
        try {
            // Cargar clientes
            clientes = facade.listarClientes();
            cmbCliente.removeAllItems();
            cmbCliente.addItem("--Seleccione--");
            for (Cliente cliente : clientes) {
                cmbCliente.addItem(cliente.getNombreEmpresa() != null ? 
                    cliente.getNombreEmpresa() : cliente.getRucDni());
            }

            // Cargar usuarios con rol soporte
            usuariosSoporte = facade.obtenerUsuariosSoporte();
            cmbAsignar.removeAllItems();
            cmbAsignar.addItem("--Seleccione--");
            for (Usuario usuario : usuariosSoporte) {
                cmbAsignar.addItem(usuario.getNombre() + " " + usuario.getApellido());
            }

            // Configurar listeners
            configurarListeners();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al inicializar formulario: " + e.getMessage());
        }
    }

    private void configurarListeners() {
        // Listener para cmbCliente
        cmbCliente.addActionListener(e -> {
            if (cmbCliente.getSelectedIndex() > 0) {
                Cliente clienteSeleccionado = clientes.get(cmbCliente.getSelectedIndex() - 1);
                txtRuc.setText(clienteSeleccionado.getRucDni());
                cargarPedidosConfirmados(clienteSeleccionado.getIdCliente());
            } else {
                txtRuc.setText("");
                cmbNumeroPedido.removeAllItems();
                cmbNumeroPedido.addItem("--Seleccione--");
            }
        });

        // Listener para selección en tabla
        tbl_reclamo.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = tbl_reclamo.getSelectedRow();
                if (fila != -1) {
                    cargarReclamoSeleccionado(fila);
                }
            }
        });
    }

    private void cargarPedidosConfirmados(long clienteId) {
        try {
            pedidosConfirmados = facade.obtenerPedidosConfirmadosPorCliente(clienteId);
            cmbNumeroPedido.removeAllItems();
            cmbNumeroPedido.addItem("--Seleccione--");
            for (Pedido pedido : pedidosConfirmados) {
                cmbNumeroPedido.addItem(pedido.getNumeroPedido());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar pedidos: " + e.getMessage());
        }
    }

    private void cargarTablaReclamos() {
        try {
            String[] titulos = {"ID", "Cliente", "N° Pedido", "Tipo", "Prioridad", "Estado", "Asignado a", "Fecha Vencimiento"};
            DefaultTableModel modelo = new DefaultTableModel(null, titulos);
            tbl_reclamo.setModel(modelo);

            reclamosGlobal = facade.listarReclamos();
            for (Reclamo reclamo : reclamosGlobal) {
                Object[] fila = new Object[8];
                fila[0] = reclamo.getReclamoId();
                fila[1] = reclamo.getCliente().getNombreEmpresa() != null ? 
                    reclamo.getCliente().getNombreEmpresa() : reclamo.getCliente().getRucDni();
                fila[2] = reclamo.getPedido() != null ? reclamo.getPedido().getNumeroPedido() : "N/A";
                fila[3] = reclamo.getTipo();
                fila[4] = reclamo.getPrioridad();
                fila[5] = reclamo.getEstado();
                fila[6] = reclamo.getUsuario().getNombre() + " " + reclamo.getUsuario().getApellido();
                fila[7] = reclamo.getFechaVencimiento() != null ? reclamo.getFechaVencimiento().toString() : "N/A";
                modelo.addRow(fila);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar reclamos: " + e.getMessage());
        }
    }

    private void cargarReclamoSeleccionado(int fila) {
        try {
            long reclamoId = (Long) tbl_reclamo.getValueAt(fila, 0);
            reclamoSeleccionado = facade.obtenerReclamoPorId(reclamoId);
            
            if (reclamoSeleccionado != null) {
                // Cargar cliente
                for (int i = 0; i < cmbCliente.getItemCount(); i++) {
                    String item = cmbCliente.getItemAt(i);
                    if (item.equals(reclamoSeleccionado.getCliente().getNombreEmpresa() != null ? 
                        reclamoSeleccionado.getCliente().getNombreEmpresa() : 
                        reclamoSeleccionado.getCliente().getRucDni())) {
                        cmbCliente.setSelectedIndex(i);
                        break;
                    }
                }
                
                txtRuc.setText(reclamoSeleccionado.getCliente().getRucDni());
                
                // Cargar pedido si existe
                if (reclamoSeleccionado.getPedido() != null) {
                    cargarPedidosConfirmados(reclamoSeleccionado.getCliente().getIdCliente());
                    for (int i = 0; i < cmbNumeroPedido.getItemCount(); i++) {
                        if (cmbNumeroPedido.getItemAt(i).equals(reclamoSeleccionado.getPedido().getNumeroPedido())) {
                            cmbNumeroPedido.setSelectedIndex(i);
                            break;
                        }
                    }
                }
                
                // Cargar tipo de reclamo
                cmbTipoReclamo.setSelectedItem(reclamoSeleccionado.getTipo());
                
                // Cargar prioridad
                cmbPrioridad.setSelectedItem(reclamoSeleccionado.getPrioridad());
                
                // Cargar estado
                cmbEstado.setSelectedItem(reclamoSeleccionado.getEstado());
                
                // Cargar usuario asignado
                for (int i = 0; i < cmbAsignar.getItemCount(); i++) {
                    String item = cmbAsignar.getItemAt(i);
                    if (item.equals(reclamoSeleccionado.getUsuario().getNombre() + " " + 
                        reclamoSeleccionado.getUsuario().getApellido())) {
                        cmbAsignar.setSelectedIndex(i);
                        break;
                    }
                }
                
                // Cargar fecha de vencimiento
                if (reclamoSeleccionado.getFechaVencimiento() != null) {
                    dtcFechaVencimiento.setDate(Date.from(reclamoSeleccionado.getFechaVencimiento()
                        .atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()));
                }
                
                // Cargar descripción
                txtDescripcionProblema.setText(reclamoSeleccionado.getDescripcion());
                
                // Verificar si el reclamo está cerrado
                if ("Cerrado".equals(reclamoSeleccionado.getEstado())) {
                    modoEdicion = false;
                    deshabilitarCampos();
                    JOptionPane.showMessageDialog(this, 
                        "Este reclamo está cerrado y no se puede editar.", 
                        "Reclamo Cerrado", 
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    modoEdicion = true;
                    habilitarCampos();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar reclamo: " + e.getMessage());
        }
    }

    private void habilitarCampos() {
        cmbCliente.setEnabled(true);
        txtRuc.setEnabled(true);
        btn_buscar.setEnabled(true);
        cmbNumeroPedido.setEnabled(true);
        cmbTipoReclamo.setEnabled(true);
        cmbPrioridad.setEnabled(true);
        cmbEstado.setEnabled(true);
        dtcFechaVencimiento.setEnabled(true);
        cmbAsignar.setEnabled(true);
        txtDescripcionProblema.setEnabled(true);
        btn_crear.setEnabled(true);
        btn_guardar.setEnabled(modoEdicion);
    }

    private void deshabilitarCampos() {
        cmbCliente.setEnabled(false);
        txtRuc.setEnabled(false);
        btn_buscar.setEnabled(false);
        cmbNumeroPedido.setEnabled(false);
        cmbTipoReclamo.setEnabled(false);
        cmbPrioridad.setEnabled(false);
        cmbEstado.setEnabled(false);
        dtcFechaVencimiento.setEnabled(false);
        cmbAsignar.setEnabled(false);
        txtDescripcionProblema.setEnabled(false);
        btn_crear.setEnabled(false);
        btn_guardar.setEnabled(false);
    }

    private void limpiarCampos() {
        cmbCliente.setSelectedIndex(0);
        txtRuc.setText("");
        cmbNumeroPedido.removeAllItems();
        cmbNumeroPedido.addItem("--Seleccione--");
        cmbTipoReclamo.setSelectedIndex(0);
        cmbPrioridad.setSelectedIndex(0);
        cmbEstado.setSelectedIndex(0);
        dtcFechaVencimiento.setDate(null);
        cmbAsignar.setSelectedIndex(0);
        txtDescripcionProblema.setText("");
        reclamoSeleccionado = null;
        modoEdicion = false;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbl_titulo = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_reclamo = new javax.swing.JTable();
        lbl_ingreseDni = new javax.swing.JLabel();
        btn_crear = new javax.swing.JButton();
        btn_nuevo = new javax.swing.JButton();
        btn_guardar = new javax.swing.JButton();
        btn_eliminar = new javax.swing.JButton();
        txt_ingreseDni = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        cmbCliente = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        txtRuc = new javax.swing.JTextField();
        btn_buscar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        cmbNumeroPedido = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        cmbTipoReclamo = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        cmbPrioridad = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        cmbEstado = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        cmbAsignar = new javax.swing.JComboBox<>();
        dtcFechaVencimiento = new com.toedter.calendar.JDateChooser();
        jLabel9 = new javax.swing.JLabel();
        txtDescripcionProblema = new javax.swing.JTextField();

        setBackground(new java.awt.Color(51, 51, 51));

        lbl_titulo.setFont(new java.awt.Font("Microsoft YaHei", 1, 24)); // NOI18N
        lbl_titulo.setForeground(new java.awt.Color(255, 255, 255));
        lbl_titulo.setText("Gestión de Reclamos");

        tbl_reclamo.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tbl_reclamo);

        lbl_ingreseDni.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        lbl_ingreseDni.setForeground(new java.awt.Color(255, 255, 255));
        lbl_ingreseDni.setText("Buscar reclamo:");

        btn_crear.setBackground(new java.awt.Color(21, 21, 21));
        btn_crear.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        btn_crear.setForeground(new java.awt.Color(255, 255, 255));
        btn_crear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Crear.png"))); // NOI18N
        btn_crear.setText("Crear");
        btn_crear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_crearActionPerformed(evt);
            }
        });

        btn_nuevo.setBackground(new java.awt.Color(21, 21, 21));
        btn_nuevo.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        btn_nuevo.setForeground(new java.awt.Color(255, 255, 255));
        btn_nuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Nuevo1.png"))); // NOI18N
        btn_nuevo.setText("Nuevo");
        btn_nuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_nuevoActionPerformed(evt);
            }
        });

        btn_guardar.setBackground(new java.awt.Color(21, 21, 21));
        btn_guardar.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        btn_guardar.setForeground(new java.awt.Color(255, 255, 255));
        btn_guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Guardar.png"))); // NOI18N
        btn_guardar.setText("Guardar");
        btn_guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_guardarActionPerformed(evt);
            }
        });

        btn_eliminar.setBackground(new java.awt.Color(21, 21, 21));
        btn_eliminar.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        btn_eliminar.setForeground(new java.awt.Color(255, 255, 255));
        btn_eliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/Eliminar.png"))); // NOI18N
        btn_eliminar.setText("Eliminar");
        btn_eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_eliminarActionPerformed(evt);
            }
        });

        txt_ingreseDni.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_ingreseDniActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Cliente:");

        cmbCliente.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel2.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("RUC:");

        btn_buscar.setBackground(new java.awt.Color(21, 21, 21));
        btn_buscar.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        btn_buscar.setForeground(new java.awt.Color(255, 255, 255));
        btn_buscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/lupa.png"))); // NOI18N
        btn_buscar.setText("Buscar");
        btn_buscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_buscarActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Número Pedido: ");

        cmbNumeroPedido.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel4.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Tipo de Reclamo:");

        cmbTipoReclamo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Seleccione--", "Producto defectuoso", "Entrega tardia", "Facturacion", "Producto incorrecto", "Otros" }));

        jLabel5.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Prioridad:");

        cmbPrioridad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Seleccione--", "Alta", "Media", "Baja" }));

        jLabel6.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Estado:");

        cmbEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Seleccione--", "Abierto", "En proceso", "Resuelto" }));

        jLabel7.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Fecha de Vencimiento:");

        jLabel8.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Asignar a:");

        cmbAsignar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel9.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Descripción de Problema:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btn_nuevo)
                                .addGap(55, 55, 55)
                                .addComponent(btn_guardar)
                                .addGap(55, 55, 55)
                                .addComponent(btn_eliminar)
                                .addGap(477, 477, 477)
                                .addComponent(btn_crear, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 1069, Short.MAX_VALUE)
                                .addComponent(lbl_ingreseDni, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txt_ingreseDni, javax.swing.GroupLayout.Alignment.LEADING))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addGap(18, 18, 18)
                                        .addComponent(cmbTipoReclamo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addGap(18, 18, 18)
                                        .addComponent(cmbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtRuc, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btn_buscar)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel3)
                                        .addGap(18, 18, 18)
                                        .addComponent(cmbNumeroPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addGap(18, 18, 18)
                                        .addComponent(cmbPrioridad, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel6)
                                        .addGap(18, 18, 18)
                                        .addComponent(cmbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel8)
                                        .addGap(18, 18, 18)
                                        .addComponent(cmbAsignar, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(18, 18, 18)
                                .addComponent(dtcFechaVencimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtDescripcionProblema, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(426, 426, 426)
                        .addComponent(lbl_titulo)))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(lbl_titulo)
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cmbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(txtRuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_buscar)
                    .addComponent(jLabel3)
                    .addComponent(cmbNumeroPedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cmbTipoReclamo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(cmbPrioridad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(cmbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(cmbAsignar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(37, 37, 37)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(dtcFechaVencimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7))
                                .addGap(14, 14, 14))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(16, 16, 16)))
                        .addComponent(btn_crear)
                        .addGap(20, 20, 20)
                        .addComponent(lbl_ingreseDni))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(txtDescripcionProblema, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(txt_ingreseDni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_nuevo)
                    .addComponent(btn_guardar)
                    .addComponent(btn_eliminar))
                .addGap(25, 25, 25))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btn_crearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_crearActionPerformed
        try {
            if (validarCampos()) {
                Reclamo reclamo = crearReclamoDesdeCampos();
                facade.crearReclamo(reclamo);
                JOptionPane.showMessageDialog(this, "Reclamo creado exitosamente");
                limpiarCampos();
                deshabilitarCampos();
                cargarTablaReclamos();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al crear reclamo: " + e.getMessage());
        }
    }//GEN-LAST:event_btn_crearActionPerformed

    private void txt_ingreseDniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_ingreseDniActionPerformed
        buscarReclamosPorRuc();
    }//GEN-LAST:event_txt_ingreseDniActionPerformed

    private void btn_nuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nuevoActionPerformed
        limpiarCampos();
        habilitarCampos();
        modoEdicion = false;
        btn_guardar.setEnabled(false);
    }//GEN-LAST:event_btn_nuevoActionPerformed

    private void btn_guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardarActionPerformed
        try {
            if (reclamoSeleccionado != null && validarCampos()) {
                // Verificar que el reclamo no esté cerrado
                if ("Cerrado".equals(reclamoSeleccionado.getEstado())) {
                    JOptionPane.showMessageDialog(this, "No se puede editar un reclamo cerrado", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                actualizarReclamoDesdeCampos();
                facade.actualizarReclamo(reclamoSeleccionado);
                JOptionPane.showMessageDialog(this, "Reclamo actualizado exitosamente");
                limpiarCampos();
                deshabilitarCampos();
                cargarTablaReclamos();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar reclamo: " + e.getMessage());
        }
    }//GEN-LAST:event_btn_guardarActionPerformed

    private void btn_eliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_eliminarActionPerformed
        try {
            if (reclamoSeleccionado != null) {
                // Verificar que el reclamo no esté ya cerrado
                if ("Cerrado".equals(reclamoSeleccionado.getEstado())) {
                    JOptionPane.showMessageDialog(this, "Este reclamo ya está cerrado", 
                        "Información", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                
                int confirmacion = JOptionPane.showConfirmDialog(this, 
                    "¿Está seguro de cerrar el reclamo?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (confirmacion == JOptionPane.YES_OPTION) {
                    facade.cerrarReclamo(reclamoSeleccionado.getReclamoId());
                    JOptionPane.showMessageDialog(this, "Reclamo cerrado exitosamente");
                    limpiarCampos();
                    deshabilitarCampos();
                    cargarTablaReclamos();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un reclamo para cerrar");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cerrar reclamo: " + e.getMessage());
        }
    }//GEN-LAST:event_btn_eliminarActionPerformed

    private void btn_buscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_buscarActionPerformed
        buscarClientePorRuc();
    }//GEN-LAST:event_btn_buscarActionPerformed

    private void buscarClientePorRuc() {
        try {
            String ruc = txtRuc.getText().trim();
            if (ruc.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese un RUC/DNI para buscar");
                return;
            }
            
            Cliente cliente = facade.buscarClientePorRuc(ruc);
            if (cliente != null) {
                // Seleccionar el cliente en el combo
                for (int i = 0; i < cmbCliente.getItemCount(); i++) {
                    String item = cmbCliente.getItemAt(i);
                    if (item.equals(cliente.getNombreEmpresa() != null ? 
                        cliente.getNombreEmpresa() : cliente.getRucDni())) {
                        cmbCliente.setSelectedIndex(i);
                        break;
                    }
                }
                cargarPedidosConfirmados(cliente.getIdCliente());
            } else {
                JOptionPane.showMessageDialog(this, "Cliente no encontrado");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al buscar cliente: " + e.getMessage());
        }
    }

    private void buscarReclamosPorRuc() {
        try {
            String ruc = txt_ingreseDni.getText().trim();
            if (ruc.isEmpty()) {
                cargarTablaReclamos();
                return;
            }
            
            List<Reclamo> reclamos = facade.obtenerReclamosPorRucDni(ruc);
            String[] titulos = {"ID", "Cliente", "N° Pedido", "Tipo", "Prioridad", "Estado", "Asignado a", "Fecha Vencimiento"};
            DefaultTableModel modelo = new DefaultTableModel(null, titulos);
            tbl_reclamo.setModel(modelo);
            
            for (Reclamo reclamo : reclamos) {
                Object[] fila = new Object[8];
                fila[0] = reclamo.getReclamoId();
                fila[1] = reclamo.getCliente().getNombreEmpresa() != null ? 
                    reclamo.getCliente().getNombreEmpresa() : reclamo.getCliente().getRucDni();
                fila[2] = reclamo.getPedido() != null ? reclamo.getPedido().getNumeroPedido() : "N/A";
                fila[3] = reclamo.getTipo();
                fila[4] = reclamo.getPrioridad();
                fila[5] = reclamo.getEstado();
                fila[6] = reclamo.getUsuario().getNombre() + " " + reclamo.getUsuario().getApellido();
                fila[7] = reclamo.getFechaVencimiento() != null ? reclamo.getFechaVencimiento().toString() : "N/A";
                modelo.addRow(fila);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al buscar reclamos: " + e.getMessage());
        }
    }

    private boolean validarCampos() {
        if (cmbCliente.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente");
            return false;
        }
        if (cmbTipoReclamo.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un tipo de reclamo");
            return false;
        }
        if (cmbPrioridad.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una prioridad");
            return false;
        }
        if (cmbEstado.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un estado");
            return false;
        }
        if (cmbAsignar.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario para asignar");
            return false;
        }
        if (txtDescripcionProblema.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese una descripción del problema");
            return false;
        }
        return true;
    }

    private Reclamo crearReclamoDesdeCampos() {
        Reclamo reclamo = new Reclamo();
        
        // Cliente
        Cliente cliente = clientes.get(cmbCliente.getSelectedIndex() - 1);
        reclamo.setCliente(cliente);
        
        // Pedido (opcional)
        if (cmbNumeroPedido.getSelectedIndex() > 0) {
            Pedido pedido = pedidosConfirmados.get(cmbNumeroPedido.getSelectedIndex() - 1);
            reclamo.setPedido(pedido);
        }
        
        // Usuario
        Usuario usuario = usuariosSoporte.get(cmbAsignar.getSelectedIndex() - 1);
        reclamo.setUsuario(usuario);
        
        // Otros campos
        reclamo.setTipo(cmbTipoReclamo.getSelectedItem().toString());
        reclamo.setPrioridad(cmbPrioridad.getSelectedItem().toString());
        reclamo.setEstado(cmbEstado.getSelectedItem().toString());
        reclamo.setDescripcion(txtDescripcionProblema.getText().trim());
        
        // Fecha de vencimiento
        if (dtcFechaVencimiento.getDate() != null) {
            reclamo.setFechaVencimiento(dtcFechaVencimiento.getDate().toInstant()
                .atZone(java.time.ZoneId.systemDefault()).toLocalDate());
        }
        
        return reclamo;
    }

    private void actualizarReclamoDesdeCampos() {
        // Verificar que el reclamo no esté cerrado antes de actualizar
        if ("Cerrado".equals(reclamoSeleccionado.getEstado())) {
            throw new IllegalStateException("No se puede editar un reclamo cerrado");
        }
        
        // Cliente
        Cliente cliente = clientes.get(cmbCliente.getSelectedIndex() - 1);
        reclamoSeleccionado.setCliente(cliente);
        
        // Pedido (opcional)
        if (cmbNumeroPedido.getSelectedIndex() > 0) {
            Pedido pedido = pedidosConfirmados.get(cmbNumeroPedido.getSelectedIndex() - 1);
            reclamoSeleccionado.setPedido(pedido);
        } else {
            reclamoSeleccionado.setPedido(null);
        }
        
        // Usuario
        Usuario usuario = usuariosSoporte.get(cmbAsignar.getSelectedIndex() - 1);
        reclamoSeleccionado.setUsuario(usuario);
        
        // Otros campos
        reclamoSeleccionado.setTipo(cmbTipoReclamo.getSelectedItem().toString());
        reclamoSeleccionado.setPrioridad(cmbPrioridad.getSelectedItem().toString());
        reclamoSeleccionado.setEstado(cmbEstado.getSelectedItem().toString());
        reclamoSeleccionado.setDescripcion(txtDescripcionProblema.getText().trim());
        
        // Fecha de vencimiento
        if (dtcFechaVencimiento.getDate() != null) {
            reclamoSeleccionado.setFechaVencimiento(dtcFechaVencimiento.getDate().toInstant()
                .atZone(java.time.ZoneId.systemDefault()).toLocalDate());
        } else {
            reclamoSeleccionado.setFechaVencimiento(null);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_buscar;
    private javax.swing.JButton btn_crear;
    private javax.swing.JButton btn_eliminar;
    private javax.swing.JButton btn_guardar;
    private javax.swing.JButton btn_nuevo;
    private javax.swing.JComboBox<String> cmbAsignar;
    private javax.swing.JComboBox<String> cmbCliente;
    private javax.swing.JComboBox<String> cmbEstado;
    private javax.swing.JComboBox<String> cmbNumeroPedido;
    private javax.swing.JComboBox<String> cmbPrioridad;
    private javax.swing.JComboBox<String> cmbTipoReclamo;
    private com.toedter.calendar.JDateChooser dtcFechaVencimiento;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbl_ingreseDni;
    private javax.swing.JLabel lbl_titulo;
    private javax.swing.JTable tbl_reclamo;
    private javax.swing.JTextField txtDescripcionProblema;
    private javax.swing.JTextField txtRuc;
    private javax.swing.JTextField txt_ingreseDni;
    // End of variables declaration//GEN-END:variables
}
