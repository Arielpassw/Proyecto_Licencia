package Licencia;

import DataBase.Conexion;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;

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

    private int idTramite;

    public Licencia(String rolOrigen, int idTramite) {
        super(rolOrigen);
        this.idTramite = idTramite;

        setTitle("Generación de Licencia");
        setContentPane(Generar);
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Vista previa automática de fechas
        LocalDate hoy = LocalDate.now();
        txtEmision.setText(hoy.toString());
        txtVencimiento.setText(hoy.plusYears(5).toString());

        // Botón Regresar
        btnRegresar.addActionListener(e -> regresarDashboard());

        // Botón Generar licencia
        btnGuardar.addActionListener(e -> {
            if (txtLicencia.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Debe ingresar el número de licencia",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirmar = JOptionPane.showConfirmDialog(this,
                    "¿Desea generar la licencia con estos datos?\n\n" +
                            "Número: " + txtLicencia.getText() + "\n" +
                            "Emisión: " + txtEmision.getText() + "\n" +
                            "Vencimiento: " + txtVencimiento.getText(),
                    "Confirmar Licencia",
                    JOptionPane.YES_NO_OPTION);

            if (confirmar == JOptionPane.YES_OPTION) {
                guardarLicencia();
            }
        });

        // Botón Exportar PDF
        btnExportar.addActionListener(e -> {
            try {
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Guardar PDF de la Licencia");
                chooser.setSelectedFile(new File("licencia.pdf"));
                int opcion = chooser.showSaveDialog(this);

                if (opcion == JFileChooser.APPROVE_OPTION) {
                    File archivo = chooser.getSelectedFile();
                    generarPDF(archivo);
                    JOptionPane.showMessageDialog(this,
                            "PDF generado correctamente en:\n" + archivo.getAbsolutePath(),
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Error al generar PDF",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void guardarLicencia() {
        String sql = "INSERT INTO licencia(id_tramite, numero_licencia, fecha_emision, fecha_vencimiento, created_by) " +
                "VALUES (?, ?, ?, ?, ?)";
        String sqlEstado = "UPDATE tramite SET estado='licencia_emitida' WHERE id_tramite=?";

        try (Connection con = new Conexion().getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             PreparedStatement psEstado = con.prepareStatement(sqlEstado)) {

            ps.setInt(1, idTramite);
            ps.setString(2, txtLicencia.getText());
            ps.setString(3, txtEmision.getText());
            ps.setString(4, txtVencimiento.getText());
            ps.setNull(5, java.sql.Types.INTEGER); // Puedes reemplazarlo con getIdUsuario() si lo tienes
            ps.executeUpdate();

            psEstado.setInt(1, idTramite);
            psEstado.executeUpdate();

            JOptionPane.showMessageDialog(this,
                    "Licencia generada correctamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error al generar licencia",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generarPDF(File file) throws Exception {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();

        // --- Fuentes ---
        Font titulo = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, BaseColor.BLUE);
        Font subTitulo = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
        Font texto = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

        // --- Logo ---
        try {
            Image logo = Image.getInstance("logo.png"); // Pon tu logo en la carpeta del proyecto
            logo.scaleToFit(100, 100);
            logo.setAlignment(Element.ALIGN_CENTER);
            document.add(logo);
        } catch (Exception e) {
            // Si no hay logo, se ignora
        }

        // --- Título ---
        Paragraph p1 = new Paragraph("LICENCIA DE CONDUCCIÓN", titulo);
        p1.setAlignment(Element.ALIGN_CENTER);
        p1.setSpacingAfter(15);
        document.add(p1);

        // Línea divisoria
        LineSeparator ls = new LineSeparator();
        document.add(new Chunk(ls));
        document.add(Chunk.NEWLINE);

        // --- Datos de la licencia en tabla ---
        PdfPTable tabla = new PdfPTable(2);
        tabla.setWidthPercentage(80);
        tabla.setSpacingBefore(20);
        tabla.setSpacingAfter(20);
        tabla.setHorizontalAlignment(Element.ALIGN_CENTER);

        // Celda: Etiqueta
        PdfPCell c1 = new PdfPCell(new Phrase("Número de Licencia:", subTitulo));
        c1.setBorder(Rectangle.NO_BORDER);
        tabla.addCell(c1);

        PdfPCell c2 = new PdfPCell(new Phrase(txtLicencia.getText(), texto));
        c2.setBorder(Rectangle.NO_BORDER);
        tabla.addCell(c2);

        tabla.addCell(new PdfPCell(new Phrase("Fecha de Emisión:", subTitulo))).setBorder(Rectangle.NO_BORDER);
        tabla.addCell(new PdfPCell(new Phrase(txtEmision.getText(), texto))).setBorder(Rectangle.NO_BORDER);

        tabla.addCell(new PdfPCell(new Phrase("Fecha de Vencimiento:", subTitulo))).setBorder(Rectangle.NO_BORDER);
        tabla.addCell(new PdfPCell(new Phrase(txtVencimiento.getText(), texto))).setBorder(Rectangle.NO_BORDER);

        document.add(tabla);

        // Mensaje final
        Paragraph p2 = new Paragraph("¡Licencia emitida correctamente!", subTitulo);
        p2.setAlignment(Element.ALIGN_CENTER);
        document.add(p2);

        document.close();
    }
}
