package Licencia;

import javax.swing.*;

public class Reportes extends Btn_Regresar_base{
    private JPanel Reporte;
    private JTable tableReporte;
    private JTextField txtFiltrar;
    private JComboBox comboLicencia;
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
