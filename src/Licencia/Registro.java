package Licencia;

import javax.swing.*;

public class Registro extends Btn_Regresar_base {

    private JPanel Registrar;
    private JTextField textCedula;
    private JTextField textFecha;
    private JTextField textNombre;
    private JTextField textTipo;
    private JButton btnGuardar;
    private JButton btnLimpiar;
    private JButton btnRegresar;


    public Registro(String rolOrigen) {
        super(rolOrigen);

        setTitle("Registro Solicitante");
        setContentPane(Registrar);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 350);
        setLocationRelativeTo(null);

        btnRegresar.addActionListener(e -> regresarDashboard());
        btnGuardar.addActionListener(e -> {});
        btnLimpiar.addActionListener(e -> {});
    }

}
