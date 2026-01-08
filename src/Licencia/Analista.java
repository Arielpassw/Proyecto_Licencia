package Licencia;

import DataBase.Conexion;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Analista extends JFrame {

    private JPanel Analista;
    private JButton btnResgistar;
    private JButton btnTramites;
    private JButton btnVerificar;
    private JButton btnDetalles;
    private JButton btnExamenes;
    private JButton btnLicencia;
    private JButton btnCerrar;
    private JTable table1;

    public Analista() {
        setTitle("Analista");
        setContentPane(Analista);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);

        // Cargar datos en la tabla
        cargarTabla();

        // Bloquear edición de la tabla
        table1.setDefaultEditor(Object.class, null);

        btnResgistar.addActionListener(e -> {
            new Registro("ANALISTA").setVisible(true);
            setVisible(false);
        });

        btnVerificar.addActionListener(e -> {
            new Verificar("ANALISTA").setVisible(true);
            setVisible(false);
        });

        btnExamenes.addActionListener(e -> {
            new Examenes("ANALISTA").setVisible(true);
            setVisible(false);
        });

        btnTramites.addActionListener(e -> {
            new Gestion("ANALISTA").setVisible(true);
            setVisible(false);
        });

        btnDetalles.addActionListener(e -> {
            new Detalles("ANALISTA").setVisible(true);
            setVisible(false);
        });

        btnLicencia.addActionListener(e -> {
            new Licencia("ANALISTA").setVisible(true);
            setVisible(false);
        });

        btnCerrar.addActionListener(e -> System.exit(0));
    }

    // MÉTODO PARA CARGAR TABLA
    private void cargarTabla() {

        DefaultTableModel modelo = new DefaultTableModel();
        table1.setModel(modelo);

        modelo.addColumn("ID");
        modelo.addColumn("Cédula");
        modelo.addColumn("Nombre");
        modelo.addColumn("Tipo");
        modelo.addColumn("Fecha Solicitud");
        modelo.addColumn("Estado");
        modelo.addColumn("Cert. Médico");

        String sql =
                "SELECT t.id_tramite, s.cedula, s.nombre, s.tipos_licencia, " +
                        "t.fecha_solicitud, t.estado, IFNULL(r.certificado_medico, 0) " +
                        "FROM tramite t " +
                        "JOIN solicitante s ON t.id_solicitante = s.id_solicitante " +
                        "LEFT JOIN requisitos r ON t.id_tramite = r.id_tramite";

        try (Connection con = new Conexion().getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                modelo.addRow(new Object[]{
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getDate(5),
                        rs.getString(6),
                        rs.getBoolean(7) ? "Sí" : "No"
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar la tabla",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
