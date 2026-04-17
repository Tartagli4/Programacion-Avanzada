/**
 * PATRON DE ESTRUCTURA: Adapter
 *
 * Permite que interfaces incompatibles trabajen juntas.
 * Actua como un "traductor" entre dos clases con interfaces distintas.
 *
 * Caso de uso: un sistema nuevo que reproduce audio en formato MP3 y WAV
 * debe poder reproducir tambien archivos en formato antiguo VLC y MP4,
 * sin modificar las clases existentes.
 */

// ── Interfaz que el sistema nuevo espera ────────────────────────────────────
interface ReproductorMedia {
    void reproducir(String tipoArchivo, String nombreArchivo);
}

// ── Interfaz del sistema antiguo (incompatible) ──────────────────────────────
interface ReproductorAvanzado {
    void reproducirVlc(String nombreArchivo);
    void reproducirMp4(String nombreArchivo);
}

// ── Implementacion del sistema antiguo ──────────────────────────────────────
class ReproductorVlc implements ReproductorAvanzado {
    @Override
    public void reproducirVlc(String nombreArchivo) {
        System.out.println("[VLC] Reproduciendo: " + nombreArchivo);
    }
    @Override
    public void reproducirMp4(String nombreArchivo) {
        // No soportado en esta clase
    }
}

class ReproductorMp4 implements ReproductorAvanzado {
    @Override
    public void reproducirVlc(String nombreArchivo) {
        // No soportado en esta clase
    }
    @Override
    public void reproducirMp4(String nombreArchivo) {
        System.out.println("[MP4] Reproduciendo: " + nombreArchivo);
    }
}

// ── Adapter: traduce la interfaz nueva a la antigua ──────────────────────────
class MediaAdapter implements ReproductorMedia {

    private ReproductorAvanzado reproductorAvanzado;

    public MediaAdapter(String tipoArchivo) {
        if (tipoArchivo.equalsIgnoreCase("vlc")) {
            reproductorAvanzado = new ReproductorVlc();
        } else if (tipoArchivo.equalsIgnoreCase("mp4")) {
            reproductorAvanzado = new ReproductorMp4();
        }
    }

    @Override
    public void reproducir(String tipoArchivo, String nombreArchivo) {
        if (tipoArchivo.equalsIgnoreCase("vlc")) {
            reproductorAvanzado.reproducirVlc(nombreArchivo);
        } else if (tipoArchivo.equalsIgnoreCase("mp4")) {
            reproductorAvanzado.reproducirMp4(nombreArchivo);
        }
    }
}

// ── Reproductor moderno que usa el Adapter para formatos no nativos ──────────
class ReproductorAudio implements ReproductorMedia {

    private MediaAdapter adapter;

    @Override
    public void reproducir(String tipoArchivo, String nombreArchivo) {
        // Formatos nativos: no necesita adapter
        if (tipoArchivo.equalsIgnoreCase("mp3")) {
            System.out.println("[MP3] Reproduciendo: " + nombreArchivo);
        } else if (tipoArchivo.equalsIgnoreCase("wav")) {
            System.out.println("[WAV] Reproduciendo: " + nombreArchivo);
        } else if (tipoArchivo.equalsIgnoreCase("vlc")
                || tipoArchivo.equalsIgnoreCase("mp4")) {
            // Delega al Adapter para formatos incompatibles
            adapter = new MediaAdapter(tipoArchivo);
            adapter.reproducir(tipoArchivo, nombreArchivo);
        } else {
            System.out.println("[ERROR] Formato no soportado: " + tipoArchivo);
        }
    }
}

// ── Demo ─────────────────────────────────────────────────────────────────────
public class ReproductorAdapter {
    public static void main(String[] args) {
        System.out.println("=== PATRON ADAPTER ===\n");

        ReproductorAudio reproductor = new ReproductorAudio();

        reproductor.reproducir("mp3", "cancion.mp3");
        reproductor.reproducir("wav", "sonido.wav");
        reproductor.reproducir("vlc", "pelicula.vlc");
        reproductor.reproducir("mp4", "video.mp4");
        reproductor.reproducir("avi", "archivo.avi");

        System.out.println("\nConclusion: el reproductor moderno maneja todos los");
        System.out.println("formatos sin modificar las clases existentes.");
    }
}
