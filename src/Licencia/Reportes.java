package Licencia;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Reportes extends Btn_Regresar_base{
    private JPanel Reporte;
    private JTable tableReporte;
    private JTextField txtFiltrar;
    private JComboBox comboBox1;
    private JButton btnDetalles;
    private JButton btnBuscar;
    private JButton btnExportar;
    private JButton btnRegresar;

    public Reportes(String rolOrigen) {
        super(rolOrigen);

        setTitle("Reportes");
        setSize(600,400);
        setVisible(true);
        setContentPane(Reporte);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        btnRegresar.addActionListener(e -> regresarDashboard());
    }
}
