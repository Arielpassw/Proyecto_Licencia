package Licencia;
import DataBase.Conexion;
import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;

public class Registro extends Btn_Regresar_base {

    private JPanel Registrar;
    private JTextField textCedula;
    private JTextField textFecha;
    private JTextField textNombre;
    private JButton btnGuardar;
    private JButton btnLimpiar;
    private JButton btnRegresar;
    private JComboBox comboTipo;


    public Registro(String rolOrigen) {
        super(rolOrigen);

        setTitle("Registro Solicitante");
        setContentPane(Registrar);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(650, 420);
        setLocationRelativeTo(null);

        // nueva conexion con la base de datos
        new Conexion().getConexion();

        // boton regresar
        btnRegresar.addActionListener(e -> regresarDashboard());

        //boton guardar
        btnGuardar.addActionListener(e -> {
            String cedula = textCedula.getText().trim();
            String nombre = textNombre.getText().trim();
            String tipoLicencia = comboTipo.getSelectedItem().toString();

            if (cedula.isEmpty() || nombre.isEmpty() || tipoLicencia.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Complete todos los campos",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (comboTipo.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this,
                        "Seleccione un tipo de licencia",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String sqlSolicitante = """
        INSERT INTO solicitante (cedula, nombre, tipos_licencia)
        VALUES (?, ?, ?)
    """;

            String sqlTramite = """
        INSERT INTO tramite (id_solicitante, fecha_solicitud, estado)
        VALUES (?, CURDATE(), 'pendiente')
    """;
            Conexion conexion = new Conexion();
            try (Connection con = conexion.getConexion()) {
                if (con == null) {
                    JOptionPane.showMessageDialog(this,
                            "No se pudo conectar a la base de datos",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                con.setAutoCommit(false); // Transacción
                //  Insertar solicitante
                PreparedStatement psSolicitante =
                        con.prepareStatement(sqlSolicitante, Statement.RETURN_GENERATED_KEYS);
                psSolicitante.setString(1, cedula);
                psSolicitante.setString(2, nombre);
                psSolicitante.setString(3, tipoLicencia);
                psSolicitante.executeUpdate();
                ResultSet rs = psSolicitante.getGeneratedKeys();
                int idSolicitante;
                if (rs.next()) {
                    idSolicitante = rs.getInt(1);
                } else {
                    throw new SQLException("No se pudo obtener el ID del solicitante");
                }
                //  Insertar trámite
                PreparedStatement psTramite = con.prepareStatement(sqlTramite);
                psTramite.setInt(1, idSolicitante);
                psTramite.executeUpdate();
                con.commit(); //  Confirmar
                JOptionPane.showMessageDialog(this,
                        "Solicitante y trámite registrados correctamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);

                btnLimpiar.doClick();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Error al guardar el registro",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // boton limpiar
        btnLimpiar.addActionListener(e -> {
            textCedula.setText("");
            textNombre.setText("");
            textFecha.setText("");
        });


    }


}
