package datos;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ArchivoManager {
    private final String carpetaDatos;
    private final String nombreArchivo;

    public ArchivoManager(String carpeta, String archivo) {
        this.carpetaDatos = carpeta;
        this.nombreArchivo = archivo;
        asegurarCarpetaExiste();
    }

    private void asegurarCarpetaExiste() {
        File carpeta = new File(System.getProperty("user.home"), carpetaDatos);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
    }

    public File getArchivo() {
        return new File(System.getProperty("user.home"),
                        carpetaDatos + File.separator + nombreArchivo);
    }

    public List<String> leerTodasLineas() {
        List<String> lineas = new ArrayList<>();
        File archivo = getArchivo();
        if (!archivo.exists()) return lineas;

        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    lineas.add(linea);
                }
            }
        } catch (IOException e) {
            System.out.println("⚠ Error al leer: " + e.getMessage());
        }
        return lineas;
    }

    public void guardarLineas(List<String> lineas) {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(getArchivo()))) {
            for (String linea : lineas) {
                escritor.write(linea);
                escritor.newLine();
            }
            System.out.println("✅ Datos guardados correctamente");
        } catch (IOException e) {
            System.out.println("❌ Error al guardar: " + e.getMessage());
        }
    }
}
