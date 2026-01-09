package Licencia;

import DataBase.Conexion;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;

public class Licencia extends Btn_Regresar_base {

    private JPanel Generar;
    private JTextField txtVencimiento;
    private JTextField txtLicencia;
    private JTextField txtEmision;
    private JButton btnExportar;
    private JButton btnRegresar;
    private JButton btnGuardar;

    private int idTramite;

    public Licencia(String rolOrigen, int idTramite) {
        super(rolOrigen);
        this.idTramite = idTramite;

        setTitle("Licencia");
        setContentPane(Generar);
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        LocalDate hoy = LocalDate.now();
        txtEmision.setText(hoy.toString());
        txtVencimiento.setText(hoy.plusYears(5).toString());

        btnRegresar.addActionListener(e -> regresarDashboard());

        btnGuardar.addActionListener(e -> guardarLicencia());
    }

    private void guardarLicencia() {
        String sql = "INSERT INTO licencia(id_tramite, numero, fecha_emision, fecha_vencimiento) VALUES (?, ?, ?, ?)";
        String sqlEstado = "UPDATE tramite SET estado='licencia_emitida' WHERE id_tramite=?";

        try (Connection con = new Conexion().getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             PreparedStatement psEstado = con.prepareStatement(sqlEstado)) {

            ps.setInt(1, idTramite);
            ps.setString(2, txtLicencia.getText());
            ps.setString(3, txtEmision.getText());
            ps.setString(4, txtVencimiento.getText());
            ps.executeUpdate();

            psEstado.setInt(1, idTramite);
            psEstado.executeUpdate();

            JOptionPane.showMessageDialog(this, "Licencia generada correctamente");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al generar licencia", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
