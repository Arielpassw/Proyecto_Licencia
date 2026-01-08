package Licencia;

import javax.swing.*;

public class Usuarios extends Btn_Regresar_base {
    private JPanel Gestion;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JTextField textField6;
    private JButton btnGuardar;
    private JButton btnActualizar;
    private JButton activarDesactivarPendienteButton;
    private JButton btnBuscar;
    private JButton btnLimpiar;
    private JButton btnRegresar;

    public Usuarios(String rolOrigen) {
        super(rolOrigen);

        setTitle("Usuarios");
        setContentPane(Gestion);
        setSize(600,400);
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        btnRegresar.addActionListener(e -> regresarDashboard());
    }
}
