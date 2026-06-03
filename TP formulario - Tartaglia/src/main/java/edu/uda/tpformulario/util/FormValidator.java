package edu.uda.tpformulario.util;

import java.util.ArrayList;
import java.util.List;

public class FormValidator {
    private static final int MIN_DOCUMENTO = 10_000_000;
    private static final int MAX_DOCUMENTO = 60_000_000;

    public static List<String> validarFormulario(
            String nombre,
            String apellido,
            String dni,
            String pasaporte,
            String telefono,
            String codigoPostal,
            String domicilio
    ) {
        List<String> errores = new ArrayList<>();

        validarCampoObligatorio(errores, nombre, "El nombre es obligatorio.");
        validarCampoObligatorio(errores, apellido, "El apellido es obligatorio.");
        validarCampoObligatorio(errores, telefono, "El teléfono es obligatorio.");
        validarCampoObligatorio(errores, codigoPostal, "El código postal es obligatorio.");
        validarCampoObligatorio(errores, domicilio, "El domicilio es obligatorio.");

        if (!nombre.isBlank() && !nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]{1,20}")) {
            errores.add("El nombre solo puede contener letras y hasta 20 caracteres.");
        }

        if (!apellido.isBlank() && !apellido.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]{1,20}")) {
            errores.add("El apellido solo puede contener letras y hasta 20 caracteres.");
        }

        if (dni.isBlank() && pasaporte.isBlank()) {
            errores.add("Debe ingresar DNI o Pasaporte.");
        }

        if (!dni.isBlank() && !pasaporte.isBlank()) {
            errores.add("DNI y Pasaporte son excluyentes. Complete solo uno de los dos campos.");
        }

        if (!dni.isBlank()) {
            validarDni(errores, dni);
        }

        if (!pasaporte.isBlank()) {
            validarPasaporte(errores, pasaporte);
        }

        validarTelefono(errores, telefono);
        validarCodigoPostal(errores, codigoPostal);

        if (!domicilio.isBlank() && domicilio.length() > 50) {
            errores.add("El domicilio no puede superar los 50 caracteres.");
        }

        return errores;
    }

    private static void validarCampoObligatorio(List<String> errores, String valor, String mensaje) {
        if (valor == null || valor.isBlank()) {
            errores.add(mensaje);
        }
    }

    private static void validarDni(List<String> errores, String dni) {
        if (!dni.matches("\\d{8}")) {
            errores.add("El DNI debe contener exactamente 8 dígitos numéricos.");
            return;
        }

        int numero = Integer.parseInt(dni);
        if (numero < MIN_DOCUMENTO || numero > MAX_DOCUMENTO) {
            errores.add("El DNI debe estar entre 10.000.000 y 60.000.000.");
        }
    }

    private static void validarPasaporte(List<String> errores, String pasaporte) {
        if (!pasaporte.matches("[A-Z]\\d{8}")) {
            errores.add("El pasaporte debe tener 1 letra de A-Z seguida de 8 números. Ejemplo: N39392288.");
            return;
        }

        int numero = Integer.parseInt(pasaporte.substring(1));
        if (numero < MIN_DOCUMENTO || numero > MAX_DOCUMENTO) {
            errores.add("La parte numérica del pasaporte debe estar entre 10.000.000 y 60.000.000.");
        }
    }

    private static void validarTelefono(List<String> errores, String telefono) {
        if (telefono.isBlank()) {
            return;
        }

        if (!telefono.matches("[0-9+()\\- ]+")) {
            errores.add("El teléfono solo puede contener números y los caracteres +()-.");
            return;
        }

        long cantidadDigitos = telefono.chars().filter(Character::isDigit).count();
        if (cantidadDigitos <= 6) {
            errores.add("El teléfono debe tener más de 6 dígitos numéricos.");
        }
    }

    private static void validarCodigoPostal(List<String> errores, String codigoPostal) {
        if (!codigoPostal.isBlank() && !codigoPostal.matches("\\d{4}")) {
            errores.add("El código postal debe contener exactamente 4 dígitos numéricos.");
        }
    }
}
