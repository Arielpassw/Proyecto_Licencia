package Licencia;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Examenes extends Btn_Regresar_base {
    private JPanel Registro;
    private JTextField txtTeorica;
    private JTextField txtPractica;
    private JButton btnGuardar;
    private JButton btnRegresar;

    public  Examenes(String rolOrigen) {
        super(rolOrigen);

        setTitle("Examenes");
        setContentPane(Registro);
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        btnRegresar.addActionListener(e -> regresarDashboard());
    }
}