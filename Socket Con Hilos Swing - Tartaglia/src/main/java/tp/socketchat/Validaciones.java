package tp.socketchat;

/**
 * Clase de utilidades para validar los datos ingresados en las interfaces Swing.
 */
public final class Validaciones {

    private Validaciones() {
    }

    public static String validarUsuario(String usuario) {
        if (usuario == null || usuario.trim().isEmpty()) {
            return "El usuario no puede estar vacío.";
        }

        String limpio = usuario.trim();

        if (limpio.length() < 3) {
            return "El usuario debe tener al menos 3 caracteres.";
        }

        if (limpio.length() > 15) {
            return "El usuario no puede superar los 15 caracteres.";
        }

        if (limpio.contains(" ")) {
            return "El usuario no puede contener espacios.";
        }

        if (!limpio.matches("[A-Za-zÁÉÍÓÚáéíóúÑñ0-9_-]+")) {
            return "El usuario solo puede contener letras, números, guion medio y guion bajo.";
        }

        return null;
    }

    public static String validarHost(String host) {
        if (host == null || host.trim().isEmpty()) {
            return "El host no puede estar vacío.";
        }
        return null;
    }

    public static String validarPuerto(String puertoTexto) {
        if (puertoTexto == null || puertoTexto.trim().isEmpty()) {
            return "El puerto no puede estar vacío.";
        }

        if (!puertoTexto.trim().matches("\\d+")) {
            return "El puerto debe contener solo números.";
        }

        int puerto;
        try {
            puerto = Integer.parseInt(puertoTexto.trim());
        } catch (NumberFormatException e) {
            return "El puerto ingresado no es válido.";
        }

        if (puerto < 1024 || puerto > 65535) {
            return "El puerto debe estar entre 1024 y 65535.";
        }

        return null;
    }

    public static String validarMensaje(String mensaje) {
        if (mensaje == null || mensaje.trim().isEmpty()) {
            return "El mensaje no puede estar vacío.";
        }

        if (mensaje.length() > 300) {
            return "El mensaje no puede superar los 300 caracteres.";
        }

        return null;
    }

    public static int parsearPuerto(String puertoTexto) {
        return Integer.parseInt(puertoTexto.trim());
    }
}
