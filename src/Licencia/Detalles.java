package Licencia;

import DataBase.Conexion;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Detalles extends Btn_Regresar_base {

    private JPanel Busqueda;
    private JButton guardarRequisitosButton;
    private JButton btnRegresar;
    private JButton generarLicenciaButton;
    private JButton guardarNotasExamenButton;
    private JTable table1;

    private int idTramite;

    public Detalles(String rolOrigen, int idTramite) {
        super(rolOrigen);
        this.idTramite = idTramite;

        setTitle("Detalle de Trámite");
        setContentPane(Busqueda);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Cargar datos
        cargarDatos();

        // Botón regresar al dashboard
        btnRegresar.addActionListener(e -> regresarDashboard());

        // Botón Guardar Requisitos
        guardarRequisitosButton.addActionListener(e -> {
            new Verificar(rolOrigen, this.idTramite).setVisible(true);
            dispose();
        });

        // Botón Guardar Notas de Examen
        guardarNotasExamenButton.addActionListener(e -> {
            new Examenes(rolOrigen, this.idTramite).setVisible(true);
            dispose();
        });

        // Botón Generar Licencia (solo si requisitos aprobados y examen aprobado)
        generarLicenciaButton.addActionListener(e -> {
            if (licenciaDisponible()) {
                new Licencia(rolOrigen, this.idTramite).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se puede generar la licencia. Asegúrese de que los requisitos estén aprobados y el examen esté aprobado.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    private void cargarDatos() {
        DefaultTableModel modelo = new DefaultTableModel();
        table1.setModel(modelo);

        modelo.addColumn("Campo");
        modelo.addColumn("Valor");

        String sql = "SELECT s.cedula, s.nombre, s.tipos_licencia, " +
                "r.certificado_medico, r.pago, r.multas, r.observaciones, " +
                "e.nota_teorica, e.nota_practica, e.resultado, " +
                "l.numero_licencia, l.fecha_emision, l.fecha_vencimiento " +
                "FROM solicitante s " +
                "LEFT JOIN tramite t ON s.id_solicitante = t.id_solicitante " +
                "LEFT JOIN requisitos r ON t.id_tramite = r.id_tramite " +
                "LEFT JOIN examen e ON t.id_tramite = e.id_tramite " +
                "LEFT JOIN licencia l ON t.id_tramite = l.id_tramite " +
                "WHERE t.id_tramite = ?";

        try (Connection con = new Conexion().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idTramite);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                modelo.addRow(new Object[]{"Cédula", rs.getString("cedula")});
                modelo.addRow(new Object[]{"Nombre", rs.getString("nombre")});
                modelo.addRow(new Object[]{"Tipo Licencia", rs.getString("tipos_licencia")});

                modelo.addRow(new Object[]{"Certificado Médico", rs.getInt("certificado_medico") == 1 ? "Presentado" : "No presentado"});
                modelo.addRow(new Object[]{"Pago", rs.getInt("pago") == 1 ? "Pagado" : "Pendiente"});
                modelo.addRow(new Object[]{"Multas", rs.getInt("multas") == 1 ? "Sí" : "No"});
                modelo.addRow(new Object[]{"Observaciones", rs.getString("observaciones") == null ? "" : rs.getString("observaciones")});

                modelo.addRow(new Object[]{"Nota Teórica", rs.getObject("nota_teorica") == null ? "No registrada" : rs.getDouble("nota_teorica")});
                modelo.addRow(new Object[]{"Nota Práctica", rs.getObject("nota_practica") == null ? "No registrada" : rs.getDouble("nota_practica")});
                modelo.addRow(new Object[]{"Resultado Examen", rs.getString("resultado") == null ? "No registrado" : rs.getString("resultado")});

                modelo.addRow(new Object[]{"Número Licencia", rs.getString("numero_licencia") == null ? "No generada" : rs.getString("numero_licencia")});
                modelo.addRow(new Object[]{"Fecha Emisión Licencia", rs.getDate("fecha_emision")});
                modelo.addRow(new Object[]{"Fecha Vencimiento Licencia", rs.getDate("fecha_vencimiento")});
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar detalles", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Verifica si se puede generar la licencia
    private boolean licenciaDisponible() {
        try (Connection con = new Conexion().getConexion();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT r.certificado_medico, r.pago, r.multas, e.resultado " +
                             "FROM requisitos r " +
                             "LEFT JOIN examen e ON r.id_tramite = e.id_tramite " +
                             "WHERE r.id_tramite = ?")) {

            ps.setInt(1, idTramite);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                boolean requisitosOk = rs.getInt("certificado_medico") == 1 &&
                        rs.getInt("pago") == 1 &&
                        rs.getInt("multas") == 0;
                boolean examenAprobado = "APROBADO".equals(rs.getString("resultado"));
                return requisitosOk && examenAprobado;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
