package Licencia;

import javax.swing.*;
import java.awt.*;

public class Administrador extends JFrame{
    private JPanel Admin;
    private JButton btnResgistar;
    private JButton btnVerificar;
    private JButton btnExamenes;
    private JButton btnTramites;
    private JTextField txtAdministrador;
    private JButton btnCerrar;
    private JButton btnDetalles;
    private JButton btnLicencia;
    private JButton btnUsuarios;
    private JButton btnReportes;


        public Administrador() {
            setTitle("Administrador");
            setContentPane(Admin);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            pack();
            setLocationRelativeTo(null);
        }


}


