package Licencia;

import DataBase.Conexion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Reportes extends Btn_Regresar_base {

    private JPanel Reporte;
    private JTable tableReporte;
    private JTextField txtFiltrar; // cédula
    private JButton btnDetalles;
    private JButton btnBuscar;
    private JButton btnExportar;
    private JButton btnRegresar;

    private DefaultTableModel modelo;

    public Reportes(String rolOrigen) {
        super(rolOrigen);

        setTitle("Reportes");
        setSize(700, 400);
        setVisible(true);
        setContentPane(Reporte);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // TABLA
        modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Fecha");
        modelo.addColumn("Estado");
        modelo.addColumn("Tipo Licencia");
        modelo.addColumn("Cédula");

        tableReporte.setModel(modelo);

        btnRegresar.addActionListener(e -> regresarDashboard());

        // 🔎 BOTÓN BUSCAR
        btnBuscar.addActionListener(e -> cargarReporte());

        // 👁 VER DETALLE
        btnDetalles.addActionListener(e -> verDetalle());

        // 📤 EXPORTAR CSV
        btnExportar.addActionListener(e -> exportarCSV());
    }

    // ================= BUSCAR =================
    private void cargarReporte() {

        modelo.setRowCount(0);

        String sql = "SELECT id, fecha, estado, tipo_licencia, cedula " +
                "FROM licencias WHERE cedula LIKE ?";

        int totalActivo = 0;
        int totalInactivo = 0;
        int totalLicencias = 0;

        try (Connection con = new Conexion().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + txtFiltrar.getText().trim() + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                modelo.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getDate("fecha"),
                        rs.getString("estado"),
                        rs.getString("tipo_licencia"),
                        rs.getString("cedula")
                });

                totalLicencias++;

                if (rs.getString("estado").equalsIgnoreCase("Activo")) {
                    totalActivo++;
                } else {
                    totalInactivo++;
                }
            }

            JOptionPane.showMessageDialog(this,
                    "Totales:\n" +
                            "Activos: " + totalActivo +
                            "\nInactivos: " + totalInactivo +
                            "\nTotal licencias: " + totalLicencias,
                    "Resumen",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= VER DETALLE =================
    private void verDetalle() {

        int fila = tableReporte.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro");
            return;
        }

        String detalle =
                "ID: " + modelo.getValueAt(fila, 0) +
                        "\nFecha: " + modelo.getValueAt(fila, 1) +
                        "\nEstado: " + modelo.getValueAt(fila, 2) +
                        "\nTipo Licencia: " + modelo.getValueAt(fila, 3) +
                        "\nCédula: " + modelo.getValueAt(fila, 4);

        JOptionPane.showMessageDialog(this, detalle, "Detalle Licencia",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // ================= EXPORTAR CSV =================
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
                        fw.write(modelo.getValueAt(i, j).toString() + ",");
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
