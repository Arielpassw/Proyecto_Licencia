package Licencia;

import DataBase.Conexion;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class Examenes extends Btn_Regresar_base {

    private JPanel Registro;
    private JTextField txtTeorica;
    private JTextField txtPractica;
    private JButton btnGuardar;
    private JButton btnRegresar;

    private int idTramite;

    public Examenes(String rolOrigen, int idTramite) {
        super(rolOrigen);
        this.idTramite = idTramite;

        setTitle("Exámenes");
        setContentPane(Registro);
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        btnRegresar.addActionListener(e -> regresarDashboard());

        btnGuardar.addActionListener(e -> guardarNotas());

    }

    private void guardarNotas() {
        try {
            double notaTeorica = Double.parseDouble(txtTeorica.getText());
            double notaPractica = Double.parseDouble(txtPractica.getText());

            String sqlInsert = "INSERT INTO examen(id_tramite, nota_teorica, nota_practica, resultado) VALUES (?, ?, ?, ?)";
            String resultado = (notaTeorica >= 14 && notaPractica >= 14) ? "aprobado" : "reprobado";
            String sqlEstado = "UPDATE tramite SET estado=? WHERE id_tramite=?";

            try (Connection con = new Conexion().getConexion();
                 PreparedStatement ps = con.prepareStatement(sqlInsert);
                 PreparedStatement psEstado = con.prepareStatement(sqlEstado)) {

                ps.setInt(1, idTramite);
                ps.setDouble(2, notaTeorica);
                ps.setDouble(3, notaPractica);
                ps.setString(4, resultado);
                ps.executeUpdate();

                psEstado.setString(1, resultado);
                psEstado.setInt(2, idTramite);
                psEstado.executeUpdate();

                JOptionPane.showMessageDialog(this, "Notas registradas. Resultado: " + resultado);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese notas válidas", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al guardar notas", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
