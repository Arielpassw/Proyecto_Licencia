package Licencia;

import DataBase.Conexion;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Reportes extends Btn_Regresar_base {

    private JPanel Reporte;
    private JTable tableReporte;
    private JTextField txtFiltrar;
    private JButton btnDetalles;
    private JButton btnBuscar;
    private JButton btnExportar;
    private JButton btnRegresar;
    private JTextArea textArea;

    private DefaultTableModel modelo;

    public Reportes(String rolOrigen) {
        super(rolOrigen);

        setTitle("Reportes - Administrador");
        setSize(950, 650);
        setContentPane(Reporte);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        modelo = new DefaultTableModel();
        modelo.addColumn("ID Trámite");
        modelo.addColumn("Cédula");
        modelo.addColumn("Nombre");
        modelo.addColumn("Tipo Licencia");
        modelo.addColumn("Fecha Solicitud");
        modelo.addColumn("Estado");
        modelo.addColumn("Número Licencia");
        modelo.addColumn("Fecha Emisión");

        tableReporte.setModel(modelo);
        tableReporte.setDefaultEditor(Object.class, null);

        // Configuración del JTextArea
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        // Buscar
        btnBuscar.addActionListener(e -> cargarReporte());

        // Filtrar mientras escribe
        txtFiltrar.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { cargarReporte(); }
            public void removeUpdate(DocumentEvent e) { cargarReporte(); }
            public void changedUpdate(DocumentEvent e) { cargarReporte(); }
        });

        // Ver detalle
        btnDetalles.addActionListener(e -> verDetalle());

        // Exportar CSV
        btnExportar.addActionListener(e -> exportarCSV());

        // Regresar
        btnRegresar.addActionListener(e -> regresarDashboard());

        cargarReporte();
    }

    // CARGAR REPORTE
    private void cargarReporte() {

        modelo.setRowCount(0);

        String sql =
                "SELECT t.id_tramite, s.cedula, s.nombre, s.tipos_licencia, " +
                        "t.fecha_solicitud, t.estado, " +
                        "l.numero_licencia, l.fecha_emision " +
                        "FROM tramite t " +
                        "JOIN solicitante s ON t.id_solicitante = s.id_solicitante " +
                        "LEFT JOIN licencia l ON t.id_tramite = l.id_tramite " +
                        "WHERE s.cedula LIKE ?";

        int pendientes = 0, aprobados = 0, reprobados = 0, emitidas = 0;

        try (Connection con = new Conexion().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + txtFiltrar.getText().trim() + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                modelo.addRow(new Object[]{
                        rs.getInt("id_tramite"),
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("tipos_licencia"),
                        rs.getDate("fecha_solicitud"),
                        rs.getString("estado"),
                        rs.getString("numero_licencia"),
                        rs.getDate("fecha_emision")
                });

                switch (rs.getString("estado")) {
                    case "pendiente":
                        pendientes++;
                        break;
                    case "aprobado":
                        aprobados++;
                        break;
                    case "reprobado":
                        reprobados++;
                        break;
                    case "licencia_emitida":
                        emitidas++;
                        break;
                }

            }

            // ACTUALIZAR RESUMEN EN EL JTextArea
            textArea.setText(
                            "Pendientes: " + pendientes + "\n" +
                            "Aprobados: " + aprobados + "\n" +
                            "Reprobados: " + reprobados + "\n" +
                            "Licencias Emitidas: " + emitidas + "\n\n" +
                            "Total de Trámites: " + modelo.getRowCount()
            );

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error al cargar reporte",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    //  VER DETALLE
    private void verDetalle() {

        int fila = tableReporte.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro");
            return;
        }

        String detalle =
                "ID Trámite: " + modelo.getValueAt(fila, 0) +
                        "\nCédula: " + modelo.getValueAt(fila, 1) +
                        "\nNombre: " + modelo.getValueAt(fila, 2) +
                        "\nTipo Licencia: " + modelo.getValueAt(fila, 3) +
                        "\nFecha Solicitud: " + modelo.getValueAt(fila, 4) +
                        "\nEstado: " + modelo.getValueAt(fila, 5) +
                        "\nNúmero Licencia: " + modelo.getValueAt(fila, 6) +
                        "\nFecha Emisión: " + modelo.getValueAt(fila, 7);

        JOptionPane.showMessageDialog(this, detalle,
                "Detalle del Trámite",
                JOptionPane.INFORMATION_MESSAGE);
    }

    //  EXPORTAR CSV
    private void exportarCSV() {

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Guardar reporte");

        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {

            try (FileWriter fw = new FileWriter(chooser.getSelectedFile() + ".csv")) {

                for (int i = 0; i < modelo.getColumnCount(); i++) {
                    fw.write(modelo.getColumnName(i) + ",");
                }
                fw.write("\n");

                for (int i = 0; i < modelo.getRowCount(); i++) {
                    for (int j = 0; j < modelo.getColumnCount(); j++) {
                        fw.write(
                                (modelo.getValueAt(i, j) == null ? "" : modelo.getValueAt(i, j)) + ","
                        );
                    }
                    fw.write("\n");
                }

                JOptionPane.showMessageDialog(this,
                        "Reporte exportado correctamente",
                        "Exportar",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
