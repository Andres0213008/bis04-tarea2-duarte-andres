package coopetica.logica;

import coopetica.datos.AsociadoArchivoDAO;
import coopetica.modelo.Asociado;
import java.io.IOException;
import java.util.ArrayList;

public class AsociadoServicio {

    /** Mensaje que devuelve registrar cuando el asociado se guardo bien. */
    public static final String OK = "OK";

    private AsociadoArchivoDAO dao;

    public AsociadoServicio() throws IOException {
        this.dao = new AsociadoArchivoDAO();
    }

    public ArrayList<Asociado> listar() throws IOException {
        return dao.listar();
    }

    public String registrar(Asociado a)
            throws IOException {

        if (a.getCedula().isEmpty() || a.getNombre().isEmpty()) {
            return "La cedula y el nombre son obligatorios.";
        }


        if (a.getSaldoAportes() < 0) {
            return "El saldo no puede ser negativo.";
        }

        for (Asociado asociado : dao.listar()) {
            if (asociado.getCedula().equals(a.getCedula())) {
                return "Ya existe un asociado con esa cedula.";
            }
        }

        dao.agregar(a);
        return OK;
    }

    /**
     * Registra un asociado a partir de los textos tal como los escribio la
     * persona usuaria. Limpia espacios, convierte el saldo a numero y aplica
     * todas las validaciones. Devuelve OK o el mensaje de error.
     *
     * @param cedula     texto del campo cedula
     * @param nombre     texto del campo nombre
     * @param saldoTexto texto del campo saldo de aportes
     * @return "OK" si se registro, o el mensaje que explica por que no
     * @throws IOException si falla la lectura o escritura del archivo
     */
    public String registrar(String cedula, String nombre, String saldoTexto)
            throws IOException {

        String c = cedula.trim();
        String n = nombre.trim();
        String s = saldoTexto.trim();

        if (c.isEmpty() || n.isEmpty() || s.isEmpty()) {
            return "La cedula, el nombre y el saldo son obligatorios.";
        }

        // La coma separa los campos en el archivo: no puede ir dentro de un dato.
        if (c.contains(",") || n.contains(",")) {
            return "La cedula y el nombre no pueden contener comas.";
        }

        double saldo;
        try {
            saldo = Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return "El saldo debe ser un numero.";
        }
        if (Double.isNaN(saldo) || Double.isInfinite(saldo)) {
            return "El saldo debe ser un numero.";
        }

        return registrar(new Asociado(c, n, saldo));
    }

    /**
     * Cuenta los asociados registrados.
     *
     * @return cantidad de asociados guardados
     * @throws IOException si falla la lectura del archivo
     */
    public int cantidadAsociados() throws IOException {
        return dao.listar().size();
    }

    /**
     * Suma el saldo de aportes de todos los asociados.
     *
     * @return total de aportes (0 si no hay asociados)
     * @throws IOException si falla la lectura del archivo
     */
    public double totalAportes() throws IOException {
        double total = 0;
        for (Asociado asociado : dao.listar()) {
            total += asociado.getSaldoAportes();
        }
        return total;
    }

    /**
     * Busca un asociado por su cedula. Ignora espacios al inicio y al final.
     *
     * @param cedula cedula a buscar
     * @return el asociado encontrado, o null si no existe
     * @throws IOException si falla la lectura del archivo
     */
    public Asociado buscarPorCedula(String cedula) throws IOException {
        String buscada = cedula.trim();
        for (Asociado asociado : dao.listar()) {
            if (asociado.getCedula().equals(buscada)) {
                return asociado;
            }
        }
        return null;
    }
}
