package Licencia;

import javax.swing.*;

public class Modulos extends JFrame {
    private JPanel Analista;
    private JButton btnResgistar;
    private JButton btnTramites;
    private JButton btnVerificar;
    private JButton btnDetalles;
    private JButton btnExamenes;
    private JButton btnLicencia;
    private JTextField textAnalista;
    private JButton btnCerrar;

    public Modulos() {
        setTitle("Analista");
        setContentPane(Analista);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }
}
