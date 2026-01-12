package Licencia;

import DataBase.Conexion;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class Verificar extends Btn_Regresar_base {

    private JPanel Requisitos;
    private JTextField txtPago;
    private JTextField txtCertificado;
    private JCheckBox CheckBoxMultas;
    private JTextArea txtObservaciones;
    private JButton btnAprobar;
    private JButton btnRechazar;
    private JButton btnRegresar;

    private int idTramite;

    public Verificar(String rolOrigen, int idTramite) {
        super(rolOrigen);
        this.idTramite = idTramite;

        setTitle("Verificación de Requisitos");
        setContentPane(Requisitos);
        setSize(650, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        cargarDatos();

        btnRegresar.addActionListener(e -> regresarDashboard());

        // Botón Aprobar
        btnAprobar.addActionListener(e -> aprobarRequisitos());

        // Botón Rechazar
        btnRechazar.addActionListener(e -> rechazarRequisitos());
    }


    private void crearRequisitos() {
        String sql = "INSERT INTO requisitos (id_tramite) VALUES (?)";

        try (Connection con = new Conexion().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idTramite);
            ps.executeUpdate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al crear requisitos",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }
    }


    private void cargarDatos() {
        String sql = "SELECT certificado_medico, pago, multas, observaciones " +
                "FROM requisitos WHERE id_tramite = ?";

        try (Connection con = new Conexion().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idTramite);
            java.sql.ResultSet rs = ps.executeQuery();


            if (rs.next()) {
                txtCertificado.setText(rs.getInt("certificado_medico") == 1 ? "Presentado" : "No presentado");
                txtPago.setText(rs.getInt("pago") == 1 ? "Pagado" : "Pendiente");
                CheckBoxMultas.setSelected(rs.getInt("multas") == 1);
                txtObservaciones.setText(rs.getString("observaciones") == null ? "" : rs.getString("observaciones"));
            } else {
                // Si NO existe, se crea
                crearRequisitos();
                JOptionPane.showMessageDialog(this,
                        "Se ha creado un registro de requisitos para este trámite.",
                        "Aviso", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar requisitos", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }


    private void aprobarRequisitos() {
        actualizarRequisitos(1);
    }

    private void rechazarRequisitos() {
        actualizarRequisitos(0);
    }

    private void actualizarRequisitos(int aprobado) {

        String sql = "UPDATE requisitos SET certificado_medico=?, pago=?, multas=?, observaciones=? WHERE id_tramite=?";
        String sqlEstado = "UPDATE tramite SET estado=? WHERE id_tramite=?";

        try (Connection con = new Conexion().getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             PreparedStatement psEstado = con.prepareStatement(sqlEstado)) {

            ps.setInt(1, txtCertificado.getText().equalsIgnoreCase("Presentado") ? 1 : 0);
            ps.setInt(2, txtPago.getText().equalsIgnoreCase("Pagado") ? 1 : 0);
            ps.setInt(3, CheckBoxMultas.isSelected() ? 1 : 0);
            ps.setString(4, txtObservaciones.getText());
            ps.setInt(5, idTramite);
            ps.executeUpdate();

            if (aprobado == 1) {
                psEstado.setString(1, "aprobado");
                JOptionPane.showMessageDialog(this,
                        "Requisitos aprobados correctamente.\nPuede continuar con los exámenes.");
            } else {
                psEstado.setString(1, "reprobado");
                JOptionPane.showMessageDialog(this,
                        "Requisitos rechazados.\nEl trámite ha sido reprobado.");
            }

            psEstado.setInt(2, idTramite);
            psEstado.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error al actualizar requisitos",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

}
