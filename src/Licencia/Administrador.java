package Licencia;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

            // boton registrar
            btnResgistar.addActionListener(e -> {
                new Registro("ADMIN").setVisible(true);
                setVisible(false);
            });

            btnVerificar.addActionListener(e -> {
                Verificar verificar = new Verificar("ADMIN");
                verificar.setVisible(true);
                setVisible(false);
            });

            btnExamenes.addActionListener(e -> {
                Examenes examenes = new Examenes("ADMIN");
                examenes.setVisible(true);
                setVisible(false);
            });

            btnTramites.addActionListener(e -> {
                Gestion gestion = new Gestion("ADMIN");
                gestion.setVisible(true);
                setVisible(false);
            });

            btnDetalles.addActionListener(e -> {
                Detalles detalles = new Detalles("ADMIN");
                detalles.setVisible(true);
                setVisible(false);
            });

            btnLicencia.addActionListener(e -> {
                Licencia licencia = new Licencia("ADMIN");
                licencia.setVisible(true);
                setVisible(false);
            });

            btnUsuarios.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Usuarios usuarios = new Usuarios("ADMIN");
                    usuarios.setVisible(true);
                    setVisible(false);
                }
            });

            btnReportes.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Reportes reportes = new Reportes("ADMIN");
                    reportes.setVisible(true);
                    setVisible(false);
                }
            });

            // boton cerrar
            btnCerrar.addActionListener(e -> {
                System.exit(0);
            });


        }


}


