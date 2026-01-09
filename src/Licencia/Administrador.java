package Licencia;

import DataBase.Conexion;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Administrador extends JFrame {

    private JPanel Admin;
    private JButton btnResgistar;
    private JButton btnVerificar;
    private JButton btnExamenes;
    private JButton btnTramites;
    private JButton btnCerrar;
    private JButton btnDetalles;
    private JButton btnLicencia;
    private JButton btnUsuarios;
    private JButton btnReportes;
    private JTable table1;

    public Administrador() {
        setTitle("Administrador");
        setContentPane(Admin);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);

        cargarTablaAdmin();
        table1.setDefaultEditor(Object.class, null);

        btnResgistar.addActionListener(e -> {
            new Registro("ADMIN").setVisible(true);
            setVisible(false);
        });

        btnVerificar.addActionListener(e -> abrirSeleccionado(Verificar.class));

        btnExamenes.addActionListener(e -> abrirSeleccionado(Examenes.class));

        btnTramites.addActionListener(e -> new Gestion("ADMIN").setVisible(true));

        btnDetalles.addActionListener(e -> abrirSeleccionado(Detalles.class));

        btnLicencia.addActionListener(e -> abrirSeleccionado(Licencia.class));

        btnUsuarios.addActionListener(e -> new Usuarios("ADMIN").setVisible(true));

        btnReportes.addActionListener(e -> new Reportes("ADMIN").setVisible(true));

        btnCerrar.addActionListener(e -> System.exit(0));
    }

    private void abrirSeleccionado(Class<?> clase) {
        int fila = table1.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un trámite de la tabla",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idTramite = Integer.parseInt(table1.getValueAt(fila, 0).toString());

        try {
            if(clase == Detalles.class)
                new Detalles("ADMIN", idTramite).setVisible(true);
            else if(clase == Verificar.class)
                new Verificar("ADMIN", idTramite).setVisible(true);
            else if(clase == Examenes.class)
                new Examenes("ADMIN", idTramite).setVisible(true);
            else if(clase == Licencia.class)
                new Licencia("ADMIN", idTramite).setVisible(true);

            setVisible(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void cargarTablaAdmin() {
        DefaultTableModel modelo = new DefaultTableModel();
        table1.setModel(modelo);

        modelo.addColumn("ID");
        modelo.addColumn("Cédula");
        modelo.addColumn("Nombre");
        modelo.addColumn("Tipo");
        modelo.addColumn("Fecha Solicitud");
        modelo.addColumn("Estado");
        modelo.addColumn("Exámenes");
        modelo.addColumn("Cert. Médico");
        modelo.addColumn("Observaciones");

        String sql =
                "SELECT t.id_tramite, s.cedula, s.nombre, s.tipos_licencia, " +
                        "t.fecha_solicitud, t.estado, e.resultado, r.observaciones, " +
                        "IFNULL(r.certificado_medico,0) " +
                        "FROM tramite t " +
                        "JOIN solicitante s ON t.id_solicitante = s.id_solicitante " +
                        "LEFT JOIN examen e ON t.id_tramite = e.id_tramite " +
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
                        rs.getString(7) == null ? "Pendiente" : rs.getString(7),
                        rs.getBoolean(9) ? "Sí" : "No",
                        rs.getString(8) == null ? "" : rs.getString(8)
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar tabla del Administrador",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
