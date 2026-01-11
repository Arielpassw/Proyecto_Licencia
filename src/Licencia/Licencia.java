package Licencia;

import DataBase.Conexion;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;

public class Licencia extends Btn_Regresar_base {

    private JPanel Generar;
    private JTextField txtVencimiento;
    private JTextField txtLicencia;
    private JTextField txtEmision;
    private JButton btnRegresar;
    private JButton btnGuardar;
    private JButton btnExportar;
    private JButton btnCargarFoto;
    private JLabel lblFoto;
    private JTextField txtNombre;
    private JTextField txtTipo;

    private int idTramite;

    // NUEVO (foto)
    private String rutaFoto;

    public Licencia(String rolOrigen, int idTramite) {
        super(rolOrigen);
        this.idTramite = idTramite;

        setTitle("Generación de Licencia");
        setContentPane(Generar);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Campos automáticos
        LocalDate hoy = LocalDate.now();
        txtEmision.setText(hoy.toString());
        txtVencimiento.setText(hoy.plusYears(5).toString());

        // La cédula y el nombre no debe editarse
        txtLicencia.setEditable(false);
        txtNombre.setEditable(false);

        // Cargar automáticamente los datos del solicitante
        cargarDatosSolicitante();

        // Botón Regresar
        btnRegresar.addActionListener(e -> regresarDashboard());

        // Botón Guardar Licencia
        btnGuardar.addActionListener(e -> {
            int confirmar = JOptionPane.showConfirmDialog(
                    this,
                    "¿Desea generar la licencia con estos datos?\n\n" +
                            "Nombre" + txtVencimiento.getText() + "\n" +
                            "Cédula: " + txtLicencia.getText() + "\n" +
                            "Tipo: " + txtTipo.getText() + "\n" +
                            "Emisión: " + txtEmision.getText() + "\n" +
                            "Vencimiento: " + txtVencimiento.getText(),
                    "Confirmar Licencia",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmar == JOptionPane.YES_OPTION) {
                guardarLicencia();
            }
        });

        // Botón Exportar PDF
        btnExportar.addActionListener(e -> {
            try {
                JFileChooser chooser = new JFileChooser();
                chooser.setSelectedFile(new File("licencia.pdf"));

                if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                    generarPDF(chooser.getSelectedFile());
                    JOptionPane.showMessageDialog(this,
                            "PDF generado correctamente",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al generar el PDF",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Botón Cargar Foto
        btnCargarFoto.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                rutaFoto = file.getAbsolutePath();

                ImageIcon icon = new ImageIcon(rutaFoto);
                java.awt.Image img = icon.getImage().getScaledInstance(
                        lblFoto.getWidth(),
                        lblFoto.getHeight(),
                        java.awt.Image.SCALE_SMOOTH
                );
                lblFoto.setIcon(new ImageIcon(img));
            }
        });
    }

    // Cargar Datos
    private void cargarDatosSolicitante() {
        String sql = """
        SELECT s.cedula, s.nombre , s.tipos_licencia
        FROM tramite t
        JOIN solicitante s ON t.id_solicitante = s.id_solicitante
        WHERE t.id_tramite = ?
    """;

        try (Connection con = new Conexion().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idTramite);
            var rs = ps.executeQuery();

            if (rs.next()) {
                txtLicencia.setText(rs.getString("cedula"));
                txtNombre.setText(rs.getString("nombre")); // <-- aquí asignamos el nombre
                txtTipo.setText(rs.getString("tipos_licencia"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error al cargar los datos del solicitante",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    // Guardar Licencia
    private void guardarLicencia() {
        String sqlLicencia = """
            INSERT INTO licencia 
            (id_tramite, numero_licencia, fecha_emision, fecha_vencimiento)
            VALUES (?, ?, ?, ?)
        """;

        String sqlEstado = """
            UPDATE tramite SET estado = 'licencia_emitida'
            WHERE id_tramite = ?
        """;

        try (Connection con = new Conexion().getConexion();
             PreparedStatement psLic = con.prepareStatement(sqlLicencia);
             PreparedStatement psEstado = con.prepareStatement(sqlEstado)) {

            psLic.setInt(1, idTramite);
            psLic.setString(2, txtLicencia.getText());
            psLic.setString(3, txtEmision.getText());
            psLic.setString(4, txtVencimiento.getText());
            psLic.executeUpdate();

            psEstado.setInt(1, idTramite);
            psEstado.executeUpdate();

            JOptionPane.showMessageDialog(this,
                    "Licencia generada correctamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error al guardar la licencia",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Generar PDF
    private void generarPDF(File file) throws Exception {
        Document document = new Document(PageSize.A4, 30, 30, 30, 30);
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();

        Font titulo = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Font subtitulo = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
        Font texto = new Font(Font.FontFamily.HELVETICA, 9);
        Font textoBold = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);

        String nombres = txtNombre.getText();
        String nacionalidad = "ECUATORIANA";
        String tipoLicencia = txtTipo.getText();

        // Encabezado
        PdfPTable encabezado = new PdfPTable(1);
        encabezado.setWidthPercentage(100);

        PdfPCell cellEnc = new PdfPCell();
        cellEnc.setBorder(Rectangle.NO_BORDER);
        cellEnc.setHorizontalAlignment(Element.ALIGN_CENTER);

        cellEnc.addElement(new Paragraph("REPÚBLICA DEL ECUADOR", subtitulo));
        cellEnc.addElement(new Paragraph("LICENCIA DE CONDUCIR", titulo));
        cellEnc.addElement(new Paragraph("Agencia Nacional de Tránsito\n\n", texto));

        encabezado.addCell(cellEnc);
        document.add(encabezado);

        // Cuerpo
        PdfPTable cuerpo = new PdfPTable(2);
        cuerpo.setWidthPercentage(100);
        cuerpo.setWidths(new float[]{60, 40});

        PdfPCell datos = new PdfPCell();
        datos.setBorder(Rectangle.NO_BORDER);

        datos.addElement(new Paragraph("APELLIDOS Y NOMBRES", textoBold));
        datos.addElement(new Paragraph(nombres + "\n", texto));

        datos.addElement(new Paragraph("CÉDULA", textoBold));
        datos.addElement(new Paragraph(txtLicencia.getText() + "\n", texto));

        datos.addElement(new Paragraph("NACIONALIDAD", textoBold));
        datos.addElement(new Paragraph(nacionalidad + "\n", texto));

        datos.addElement(new Paragraph("TIPO DE LICENCIA", textoBold));
        datos.addElement(new Paragraph(tipoLicencia + "\n", texto));

        datos.addElement(new Paragraph("FECHA DE EMISIÓN", textoBold));
        datos.addElement(new Paragraph(txtEmision.getText() + "\n", texto));

        datos.addElement(new Paragraph("FECHA DE VENCIMIENTO", textoBold));
        datos.addElement(new Paragraph(txtVencimiento.getText(), texto));

        cuerpo.addCell(datos);

        // Foto
        PdfPCell fotoCell;
        if (rutaFoto != null) {
            Image foto = Image.getInstance(rutaFoto);
            foto.scaleToFit(120, 150);
            fotoCell = new PdfPCell(foto);
        } else {
            fotoCell = new PdfPCell(new Paragraph("SIN FOTO", texto));
        }
        fotoCell.setBorder(Rectangle.BOX);
        fotoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        fotoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cuerpo.addCell(fotoCell);

        document.add(cuerpo);

        // Pie
        PdfPTable pie = new PdfPTable(2);
        pie.setWidthPercentage(100);
        pie.setSpacingBefore(25);

        PdfPCell firma = new PdfPCell(new Paragraph("________________________\nFirma del Titular", texto));
        firma.setBorder(Rectangle.NO_BORDER);
        firma.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell huella = new PdfPCell(new Paragraph("HUELLA DIGITAL", texto));
        huella.setBorder(Rectangle.NO_BORDER);
        huella.setHorizontalAlignment(Element.ALIGN_CENTER);

        pie.addCell(firma);
        pie.addCell(huella);

        document.add(pie);

        document.close();
    }

}
