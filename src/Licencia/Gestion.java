package Licencia;

import DataBase.Conexion;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Gestion extends Btn_Regresar_base {

    private JPanel Gestionar;
    private JTable tableDatos;
    private JTextField txtFiltrar;
    private JButton btnRegresar;
    private JButton btnVerDetalle;
    private JButton btnMarcarReq;
    private JButton btnRegistrarExamen;
    private JButton btnGenerarLicencia;

    public Gestion(String rolOrigen) {
        super(rolOrigen);

        setTitle("Gestión Trámites");
        setContentPane(Gestionar);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

        // Cargar tabla inicialmente
        cargarTabla("");

        // Botón regresar
        btnRegresar.addActionListener(e -> regresarDashboard());

        // Filtrar en tiempo real mientras escribes
        txtFiltrar.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                filtrar();
            }

            public void removeUpdate(DocumentEvent e) {
                filtrar();
            }

            public void changedUpdate(DocumentEvent e) {
                filtrar();
            }

            private void filtrar() {
                String texto = txtFiltrar.getText().trim();
                cargarTabla(texto);
            }
        });

        // Botones de acciones (implementación futura)
        btnVerDetalle.addActionListener(e -> verDetalle());
        btnMarcarReq.addActionListener(e -> marcarRequisitos());
        btnRegistrarExamen.addActionListener(e -> registrarExamen());
        btnGenerarLicencia.addActionListener(e -> generarLicencia());
    }

    private void cargarTabla(String filtro) {
        DefaultTableModel modelo = new DefaultTableModel();
        tableDatos.setModel(modelo);

        modelo.addColumn("ID");
        modelo.addColumn("Cédula");
        modelo.addColumn("Nombre");
        modelo.addColumn("Tipo");
        modelo.addColumn("Fecha Solicitud");
        modelo.addColumn("Estado");

        String sql = "SELECT t.id_tramite, s.cedula, s.nombre, s.tipos_licencia, t.fecha_solicitud, t.estado " +
                "FROM tramite t JOIN solicitante s ON t.id_solicitante = s.id_solicitante";

        // Filtro dinámico por estado, nombre o cédula
        if (!filtro.isEmpty()) {
            sql += " WHERE t.estado LIKE ? OR s.nombre LIKE ? OR s.cedula LIKE ?";
        }

        try (Connection con = new Conexion().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (!filtro.isEmpty()) {
                String filtroSQL = "%" + filtro + "%";
                ps.setString(1, filtroSQL);
                ps.setString(2, filtroSQL);
                ps.setString(3, filtroSQL);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                modelo.addRow(new Object[]{
                        rs.getInt("id_tramite"),
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("tipos_licencia"),
                        rs.getDate("fecha_solicitud"),
                        rs.getString("estado")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar trámites", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Métodos placeholder para acciones
    private void verDetalle() {
        int fila = tableDatos.getSelectedRow();
        if (fila != -1) {
            int idTramite = (int) tableDatos.getValueAt(fila, 0);
            new Verificar("gestion", idTramite); // Abrir ventana de Verificar
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un trámite primero.");
        }
    }

    private void marcarRequisitos() {
        JOptionPane.showMessageDialog(this, "Función de marcar requisitos aún no implementada.");
    }

    private void registrarExamen() {
        JOptionPane.showMessageDialog(this, "Función de registrar examen aún no implementada.");
    }

    private void generarLicencia() {
        JOptionPane.showMessageDialog(this, "Función de generar licencia aún no implementada.");
    }
}
