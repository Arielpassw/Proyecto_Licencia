package Licencia;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Gestion extends Btn_Regresar_base {
    private JPanel Gestionar;
    private JTable tableDatos;
    private JTextField txtFiltar;
    private JButton btnRegresar;

    public  Gestion(String rolOrigen) {
        super(rolOrigen);

        setTitle("Gestión Trámites");
        setContentPane(Gestionar);
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // boton regresar
        btnRegresar.addActionListener(e -> regresarDashboard());
    }
}