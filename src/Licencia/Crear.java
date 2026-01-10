package Licencia;

import DataBase.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
/*
 MAPEO CAMPOS:
 userText  -> usuario
 passwText -> contraseña
 nameText  -> nombre_completo
 cedulaText-> cedula
*/

public class Crear extends Btn_Regresar_base {

    private JPanel crear_user;
    private JComboBox comboRol;
    private JTextField userText; // nombre_completo
    private JTextField passwText; // cedula
    private JTextField nameText; // usuario
    private JTextField cedulaText; // contraseña
    private JTextField activoDesactivoTextField; // estado
    private JButton btnRegresar;
    private JButton guardarButton;
    private JButton actualizarButton;
    private JRadioButton activoRadioButton;
    private JButton buscarButton;
    private JButton limpiarButton;

    public Crear(String rolOrigen) {
        super(rolOrigen);

        setTitle("Crear Usuario");
        setContentPane(crear_user);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Conexión inicial
        new Conexion().getConexion();

        // BOTÓN REGRESAR
        btnRegresar.addActionListener(e -> regresarDashboard());

        // BOTÓN GUARDAR (INSERT CORRECTO)
        guardarButton.addActionListener(e -> {

            String sql = "INSERT INTO usuarios (usuario, contraseña, nombre_completo, estado, rol, cedula) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            try (Connection con = new Conexion().getConexion();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, userText.getText().trim());   // usuario
                ps.setString(2, passwText.getText().trim()); // contraseña
                ps.setString(3, nameText.getText().trim());  // nombre completo
                ps.setString(4, activoDesactivoTextField.getText().trim()); // estado
                ps.setString(5, comboRol.getSelectedItem().toString()); // rol
                ps.setString(6, cedulaText.getText().trim()); // cedula

                ps.executeUpdate();

                JOptionPane.showMessageDialog(this,
                        "Usuario creado correctamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);

                limpiarCampos();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al guardar usuario",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });


        // BOTÓN BUSCAR
        buscarButton.addActionListener(e -> {

            String criterio = userText.getText().trim();
            if (criterio.isEmpty()) {
                criterio = cedulaText.getText().trim();
            }

            String sql = "SELECT * FROM usuarios WHERE usuario LIKE ? OR cedula LIKE ?";

            try (Connection con = new Conexion().getConexion();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, "%" + criterio + "%");
                ps.setString(2, "%" + criterio + "%");

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    userText.setText(rs.getString("usuario"));
                    passwText.setText(rs.getString("contraseña"));
                    nameText.setText(rs.getString("nombre_completo"));
                    cedulaText.setText(rs.getString("cedula"));
                    comboRol.setSelectedItem(rs.getString("rol"));
                    activoDesactivoTextField.setText(rs.getString("estado"));
                    activoRadioButton.setSelected(
                            rs.getString("estado").equals("Activo")
                    );
                } else {
                    JOptionPane.showMessageDialog(this, "Usuario no encontrado");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // BOTÓN ACTUALIZAR
        actualizarButton.addActionListener(e -> {

            String sql = "UPDATE usuarios SET usuario=?, contraseña=?, nombre_completo=?, estado=?, rol=? " +
                    "WHERE cedula=?";

            try (Connection con = new Conexion().getConexion();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, userText.getText().trim());
                ps.setString(2, passwText.getText().trim());
                ps.setString(3, nameText.getText().trim());
                ps.setString(4, activoDesactivoTextField.getText().trim());
                ps.setString(5, comboRol.getSelectedItem().toString());
                ps.setString(6, cedulaText.getText().trim());

                int filas = ps.executeUpdate();

                if (filas > 0) {
                    JOptionPane.showMessageDialog(this, "Usuario actualizado correctamente");
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo actualizar el usuario");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });


        // RADIO BUTTON ACTIVO / INACTIVO
        activoRadioButton.addActionListener(e -> {
            if (activoRadioButton.isSelected()) {
                activoDesactivoTextField.setText("Activo");
            } else {
                activoDesactivoTextField.setText("Inactivo");
            }
        });

        // BOTÓN LIMPIAR
        limpiarButton.addActionListener(e -> limpiarCampos());

        // Estado por defecto
        activoDesactivoTextField.setText("Activo");
        activoRadioButton.setSelected(true);
    }

    private void limpiarCampos() {
        userText.setText("");
        passwText.setText("");
        nameText.setText("");
        cedulaText.setText("");
        activoDesactivoTextField.setText("Activo");
        comboRol.setSelectedIndex(0);
        activoRadioButton.setSelected(true);
    }
}
