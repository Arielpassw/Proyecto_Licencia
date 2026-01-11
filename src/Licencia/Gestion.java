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

        setTitle("Gestión de Trámites");
        setContentPane(Gestionar);
        setSize(950, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

        cargarTabla("");

        btnRegresar.addActionListener(e -> regresarDashboard());

        // Filtro en tiempo real
        txtFiltrar.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filtrar(); }
            public void removeUpdate(DocumentEvent e) { filtrar(); }
            public void changedUpdate(DocumentEvent e) { filtrar(); }

            private void filtrar() {
                cargarTabla(txtFiltrar.getText().trim());
            }
        });

        // Ver Detalles
        btnVerDetalle.addActionListener(e -> abrirDetalles());

        // Marcar Requisitos (se mantiene como placeholder)
        btnMarcarReq.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "Función de requisitos se maneja desde Detalles")
        );

        // Registrar Examen
        btnRegistrarExamen.addActionListener(e -> abrirExamen());

        // Generar Licencia
        btnGenerarLicencia.addActionListener(e -> abrirLicencia());
    }

    //  TABLA

    private void cargarTabla(String filtro) {
        DefaultTableModel modelo = new DefaultTableModel();
        tableDatos.setModel(modelo);

        modelo.addColumn("ID");
        modelo.addColumn("Cédula");
        modelo.addColumn("Nombre");
        modelo.addColumn("Tipo Licencia");
        modelo.addColumn("Fecha Solicitud");
        modelo.addColumn("Estado");

        String sql = """
                SELECT t.id_tramite, s.cedula, s.nombre, s.tipos_licencia,
                       t.fecha_solicitud, t.estado
                FROM tramite t
                JOIN solicitante s ON t.id_solicitante = s.id_solicitante
                """;

        if (!filtro.isEmpty()) {
            sql += " WHERE s.nombre LIKE ? OR s.cedula LIKE ? OR t.estado LIKE ?";
        }

        try (Connection con = new Conexion().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (!filtro.isEmpty()) {
                String f = "%" + filtro + "%";
                ps.setString(1, f);
                ps.setString(2, f);
                ps.setString(3, f);
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
            JOptionPane.showMessageDialog(this,
                    "Error al cargar trámites",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // ACCIONES

    private int getIdSeleccionado() {
        int fila = tableDatos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un trámite",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return -1;
        }
        return (int) tableDatos.getValueAt(fila, 0);
    }

    private void abrirDetalles() {
        int id = getIdSeleccionado();
        if (id != -1) {
            new Detalles(rolOrigen, id).setVisible(true);
            setVisible(false);
        }
    }

    private void abrirExamen() {
        int id = getIdSeleccionado();
        if (id != -1) {
            new Examenes(rolOrigen, id).setVisible(true);
            setVisible(false);
        }
    }

    private void abrirLicencia() {
        int id = getIdSeleccionado();
        if (id != -1) {
            new Licencia(rolOrigen, id).setVisible(true);
            setVisible(false);
        }
    }
}
