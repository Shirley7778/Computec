package Vista;

import menu.componentes.evento.EventMenu;
import Vista.Form_Inicio;
import Vista.Form_Ubicacion;
import Vista.Form_Reclamos;
import Vista.Form_Clientes;
import Vista.Form_Pedidos;
import Vista.Form_ControlStock;
import Vista.Form_Movimientos;
import Vista.Form_Reporte;
import Controlador.AuthController;
import Modelo.Usuario;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JOptionPane;

public class Main extends javax.swing.JFrame {

    private Usuario usuarioActual;
    private int permisoId;

    public Main(int permiso_id) {
        this.permisoId = permiso_id;
        this.usuarioActual = AuthController.getUsuarioActual();
        
        initComponents();
        getContentPane().setBackground(new Color(0, 0, 0, 0));
        
        // Configurar el menú según los permisos del usuario
        configurarMenu();
        
        // Mostrar la pantalla de inicio por defecto
        showForm(new Form_Inicio());
    }

    private void configurarMenu() {
        EventMenu event = new EventMenu() {
            @Override
            public void selected(int index) {
                switch (index) {
                    case 0: // Inicio
                        showForm(new Form_Inicio());
                        break;
                    case 1: // Clientes
                        if (tienePermiso("Cliente")) {
                            showForm(new Form_Clientes());
                        } else {
                            JOptionPane.showMessageDialog(null, "No tiene permisos para acceder a Clientes", 
                                                       "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
                        }
                        break;
                    case 2: // Pedidos
                        if (tienePermiso("Pedido")) {
                            showForm(new Form_Pedidos());
                        } else {
                            JOptionPane.showMessageDialog(null, "No tiene permisos para acceder a Pedidos", 
                                                       "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
                        }
                        break;
                    case 3: // Control de Stock
                        if (tienePermiso("ControlStock")) {
                            showForm(new Form_ControlStock());
                        } else {
                            JOptionPane.showMessageDialog(null, "No tiene permisos para acceder a Control de Stock", 
                                                       "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
                        }
                        break;
                    case 4: // Movimientos
                        if (tienePermiso("Movimiento")) {
                            showForm(new Form_Movimientos());
                        } else {
                            JOptionPane.showMessageDialog(null, "No tiene permisos para acceder a Movimientos", 
                                                       "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
                        }
                        break;
                    case 5: // Reclamos
                        if (tienePermiso("Reclamo")) {
                            showForm(new Form_Reclamos());
                        } else {
                            JOptionPane.showMessageDialog(null, "No tiene permisos para acceder a Reclamos", 
                                                       "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
                        }
                        break;
                    case 6: // Ubicación
                        if (tienePermiso("Ubicacion")) {
                            showForm(new Form_Ubicacion());
                        } else {
                            JOptionPane.showMessageDialog(null, "No tiene permisos para acceder a Ubicación", 
                                                       "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
                        }
                        break;
                    case 7: // Reportes
                        if (tienePermiso("Reporte")) {
                            showForm(new Form_Reporte());
                        } else {
                            JOptionPane.showMessageDialog(null, "No tiene permisos para acceder a Reportes", 
                                                       "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
                        }
                        break;
                    case 8: // Cerrar Sesión
                        cerrarSesion();
                        break;
                }
            }
        };
        menu1.initMenu(event);
    }
    
    private boolean tienePermiso(String permiso) {
        if (usuarioActual == null || usuarioActual.getPermiso() == null) {
            return false;
        }
        
        String acceso = usuarioActual.getPermiso().getAcceso();
        if (acceso == null) {
            return false;
        }
        
        return acceso.contains(permiso);
    }
    
    private void cerrarSesion() {
        int opcion = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro que desea cerrar sesión?", 
            "Cerrar Sesión", 
            JOptionPane.YES_NO_OPTION);
            
        if (opcion == JOptionPane.YES_OPTION) {
            AuthController.cerrarSesion();
            this.dispose();
            
            // Abrir nuevamente la pantalla de login
            Form_InicioSesion login = new Form_InicioSesion();
            login.setVisible(true);
        }
    }

    private void showForm(Component com) {
        body.removeAll();
        body.add(com);
        body.revalidate();
        body.repaint();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        roundPanel1 = new menu.componentes.swing.RoundPanel();
        menu1 = new Vista.Menu();
        body = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        roundPanel1.setBackground(new java.awt.Color(25, 25, 25));

        body.setBackground(new java.awt.Color(51, 51, 51));
        body.setOpaque(false);
        body.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout roundPanel1Layout = new javax.swing.GroupLayout(roundPanel1);
        roundPanel1.setLayout(roundPanel1Layout);
        roundPanel1Layout.setHorizontalGroup(
            roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(menu1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(body, javax.swing.GroupLayout.DEFAULT_SIZE, 1121, Short.MAX_VALUE)
                .addGap(10, 10, 10))
        );
        roundPanel1Layout.setVerticalGroup(
            roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(body, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(menu1, javax.swing.GroupLayout.DEFAULT_SIZE, 677, Short.MAX_VALUE))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(roundPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(roundPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // Iniciar con la pantalla de login
                Form_InicioSesion login = new Form_InicioSesion();
                login.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel body;
    private Vista.Menu menu1;
    private menu.componentes.swing.RoundPanel roundPanel1;
    // End of variables declaration//GEN-END:variables
}
