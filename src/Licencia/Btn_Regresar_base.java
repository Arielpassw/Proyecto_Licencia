package Licencia;

import javax.swing.*;

public class Btn_Regresar_base extends JFrame {
    protected String rolOrigen;

    public Btn_Regresar_base(String rolOrigen) {
        this.rolOrigen = rolOrigen;
    }

    protected void regresarDashboard() {

        if ("ADMIN".equalsIgnoreCase(rolOrigen)) {
            new Administrador().setVisible(true);

        } else if ("ANALISTA".equalsIgnoreCase(rolOrigen)) {
            new Analista().setVisible(true);

        } else {
            JOptionPane.showMessageDialog(this,
                    "Rol no reconocido",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        dispose();
    }
}
