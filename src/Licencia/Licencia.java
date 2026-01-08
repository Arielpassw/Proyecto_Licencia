package Licencia;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Licencia extends Btn_Regresar_base {
    private JPanel Generar;
    private JTextField txtVencimiento;
    private JTextField txtLicencia;
    private JTextField txtEmision;
    private JButton btnExportar;
    private JButton btnRegresar;
    private JButton btnGuardar;

    public Licencia(String rolOrigen) {
        super(rolOrigen);

        setTitle("Licencia");
        setContentPane(Generar);
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        btnRegresar.addActionListener(e -> regresarDashboard());
    }
}