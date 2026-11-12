package coopetica.presentacion;

import coopetica.logica.AsociadoServicio;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                AsociadoServicio servicio = new AsociadoServicio();
                VentanaPrincipal ventana = new VentanaPrincipal(servicio);
                ventana.setVisible(true);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null,
                        "No se pudo preparar el archivo de datos: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
