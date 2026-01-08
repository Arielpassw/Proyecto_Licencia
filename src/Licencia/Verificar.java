package Licencia;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Verificar extends Btn_Regresar_base {
    private JPanel Requisitos;
    private JTextField txtPago;
    private JTextField txtCertificado;
    private JCheckBox CheckBoxMultas;
    private JTextArea txtObservaciones;
    private JButton btnAprobar;
    private JButton btnRechazar;
    private JButton btnRegresar;

    public Verificar(String rolOrigen) {
        super(rolOrigen);

        setTitle("Registro de Solicitante");
        setContentPane(Requisitos);
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        btnRegresar.addActionListener(e -> regresarDashboard());
    }
}