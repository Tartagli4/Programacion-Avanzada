# Trabajo Práctico - Formulario Swing

**Materia:** Programación Avanzada  
**Tema:** Librería Swing - Formularios y validaciones

# Alumno
Juan Tartaglia

## Descripción

Proyecto Java Swing desarrollado para cargar y validar un formulario de contacto. Incluye validaciones a nivel de caracteres mientras el usuario escribe y validaciones finales al presionar el botón **Validar**.

## Campos del formulario

- Nombre: máximo 20 caracteres alfabéticos.
- Apellido: máximo 20 caracteres alfabéticos.
- DNI: 8 dígitos numéricos, entre 10.000.000 y 60.000.000.
- Pasaporte: 1 letra de A-Z y 8 dígitos numéricos, con número entre 10.000.000 y 60.000.000.
- Teléfono: más de 6 dígitos, permitiendo números y los caracteres `+()-`.
- Código Postal: 4 dígitos numéricos.
- Domicilio: máximo 50 caracteres.

## Validaciones implementadas

- Nombre y Apellido solo aceptan letras y espacios.
- DNI solo acepta números y máximo 8 caracteres.
- Pasaporte acepta una letra inicial y hasta 8 números; la letra se transforma a mayúscula automáticamente.
- Teléfono solo acepta números, espacios y símbolos permitidos.
- Código Postal solo acepta números y máximo 4 caracteres.
- Domicilio se limita a 50 caracteres.
- DNI y Pasaporte son excluyentes: cuando se completa uno, el otro se deshabilita.
- Al validar, se informa si existen errores o si la carga es correcta.


## Clase principal

```java
edu.uda.tpformulario.Main
```
