package Licencia;

import javax.swing.*;

public class Detalles extends Btn_Regresar_base {

    private JPanel Busqueda;
    private JButton guardarRegistrosButton;
    private JButton btnRegresar;
    private JButton generarLicenciaButton;
    private JButton guardarNotasExamenButton;
    private JTable table1;

    private int idTramite;

    // Constructor CORRECTO: recibe rol y idTramite
    public Detalles(String rolOrigen, int idTramite) {
        super(rolOrigen);
        this.idTramite = idTramite;

        setTitle("Detalle de Trámite");
        setContentPane(Busqueda);
        setSize(700, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Botón regresar al dashboard
        btnRegresar.addActionListener(e -> regresarDashboard());

        // Botón Verificar requisitos
        guardarRegistrosButton.addActionListener(e -> {
            new Verificar(rolOrigen, this.idTramite).setVisible(true);
            dispose(); // Cierra esta ventana
        });

        // Botón Guardar notas de examen
        guardarNotasExamenButton.addActionListener(e -> {
            new Examenes(rolOrigen, this.idTramite).setVisible(true);
            dispose();
        });

        // Botón Generar Licencia
        generarLicenciaButton.addActionListener(e -> {
            new Licencia(rolOrigen, this.idTramite).setVisible(true);
            dispose();
        });
    }
}
