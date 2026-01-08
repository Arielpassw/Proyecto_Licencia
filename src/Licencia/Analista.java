package Licencia;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Analista extends JFrame {

    private JPanel Analista;
    private JButton btnResgistar;
    private JButton btnTramites;
    private JButton btnVerificar;
    private JButton btnDetalles;
    private JButton btnExamenes;
    private JButton btnLicencia;
    private JTextField textAnalista;
    private JButton btnCerrar;

    public Analista() {
        setTitle("Analista");
        setContentPane(Analista);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1980, 720);
        setLocationRelativeTo(null);

        btnResgistar.addActionListener(e -> {
            new Registro("ANALISTA").setVisible(true);
            setVisible(false);
        });


        btnVerificar.addActionListener(e -> {
            Verificar verificar = new Verificar("ANALISTA");
            verificar.setVisible(true);
            setVisible(false);
        });

        btnExamenes.addActionListener(e -> {
            Examenes examenes = new Examenes("ANALISTA");
            examenes.setVisible(true);
            setVisible(false);
        });

        btnTramites.addActionListener(e -> {
            Gestion gestion = new Gestion("ANALISTA");
            gestion.setVisible(true);
            setVisible(false);
        });

        btnDetalles.addActionListener(e -> {
            Detalles detalles = new Detalles("ANALISTA");
            detalles.setVisible(true);
            setVisible(false);
        });

        btnLicencia.addActionListener(e -> {
            Licencia licencia = new Licencia("ANALISTA");
            licencia.setVisible(true);
            setVisible(false);
        });

        // boton cerrar
        btnCerrar.addActionListener(e -> {
            System.exit(0);
        });
    }
}
