package Licencia;

import DataBase.Conexion;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Gestion extends Btn_Regresar_base {

    private JPanel Gestionar;
    private JTable tableDatos;
    private JTextField txtFiltrar;
    private JButton btnRegresar;
    private JButton btnFiltrar;

    public Gestion(String rolOrigen) {
        super(rolOrigen);

        setTitle("Gestión Trámites");
        setContentPane(Gestionar);
        setSize(700, 400);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        cargarTabla();

        btnRegresar.addActionListener(e -> regresarDashboard());

        btnFiltrar.addActionListener(e -> cargarTabla(txtFiltrar.getText()));
    }

    private void cargarTabla() {
        cargarTabla("");
    }

    private void cargarTabla(String estadoFiltro) {
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

        if (!estadoFiltro.isEmpty()) sql += " WHERE t.estado=?";

        try (Connection con = new Conexion().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (!estadoFiltro.isEmpty()) ps.setString(1, estadoFiltro);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                modelo.addRow(new Object[]{
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getDate(5),
                        rs.getString(6)
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar trámites", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
