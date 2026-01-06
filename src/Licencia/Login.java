package Licencia;

import DataBase.Conexion;

import javax.swing.*;
import java.sql.*;

public class Login extends JFrame {

    private JPanel Login;
    private JTextField textUser;
    private JPasswordField passwordField;
    private JButton ingresarButton;

    private int intentos = 0;

    public Login() {

        setTitle("Login");
        setContentPane(Login);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

        ingresarButton.addActionListener(e -> autenticar());
    }

    private void autenticar() {

        String usuario = textUser.getText().trim();
        String contraseña = new String(passwordField.getPassword()).trim();

        // Validaciones básicas
        if (usuario.isEmpty() || contraseña.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Ingrese usuario y contraseña");
            return;
        }

        Conexion conexion = new Conexion();

        String sql = "SELECT rol, estado FROM usuarios WHERE usuario=? AND contraseña=?";

        try (Connection con = conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario);
            ps.setString(2, contraseña);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String rolBD = rs.getString("rol")
                        .trim()
                        .toUpperCase();

                String estado = rs.getString("estado");

                // Usuario inactivo
                if (!estado.equalsIgnoreCase("Activo")) {
                    JOptionPane.showMessageDialog(this,
                            "Usuario inactivo. Contacte al administrador.");
                    return;
                }

                // Login correcto
                abrirMenuSegunRol(rolBD);
                dispose();

            } else {
                intentos++;
                JOptionPane.showMessageDialog(this,
                        "Credenciales incorrectas (" + intentos + "/3)");

                if (intentos >= 3) {
                    ingresarButton.setEnabled(false);
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error de conexión con la base de datos");
        }
    }

    private void abrirMenuSegunRol(String rol) {

        switch (rol) {
            case "ADMIN":
            case "ADMINISTRADOR":
                new Administrador().setVisible(true);
                break;

            case "ANALISTA":
                new Modulos().setVisible(true);
                break;

            default:
                JOptionPane.showMessageDialog(this,
                        "Rol no reconocido: " + rol);
        }
    }
}
