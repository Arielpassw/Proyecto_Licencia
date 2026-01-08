package Licencia;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Detalles extends Btn_Regresar_base {
    private JPanel Busqueda;
    private JButton guardarRegistrosButton;
    private JButton btnRegresar;
    private JButton generarLicenciaButton;
    private JButton guardarNotasExamenButton;
    private JTable table1;

    public  Detalles(String rolOrigen) {
        super(rolOrigen);

        setTitle("Detalles");
        setContentPane(Busqueda);
        setSize(700, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // boton regresar
        btnRegresar.addActionListener(e -> regresarDashboard());
    }
}