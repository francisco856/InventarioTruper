package servicio;

import modelo.Producto;
import java.io.*;
import java.util.List;

public class ExportadorPDF {
    private static final String CARPETA_DATOS = "datos";

    public static boolean exportar(List<Producto> productos, String nombreArchivo) {
        File carpeta = new File(CARPETA_DATOS);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        if (!nombreArchivo.endsWith(".txt") && !nombreArchivo.endsWith(".pdf")) {
            nombreArchivo += ".txt";
        }

        File archivo = new File(carpeta, nombreArchivo);

        try (PrintWriter escritor = new PrintWriter(new OutputStreamWriter(new FileOutputStream(archivo), "UTF-8"))) {
            escritor.println("============================================================");
            escritor.println("           INVENTARIO DE PRODUCTOS - TRUPER                 ");
            escritor.println("============================================================");
            escritor.println("Fecha: " + java.time.LocalDate.now());
            escritor.println("Hora:  " + java.time.LocalTime.now().withNano(0));
            escritor.println("============================================================");
            escritor.println();

            escritor.printf("%-4s %-20s %-15s %-8s %-8s %-10s %-10s %-12s%n",
                    "N°", "NOMBRE", "CATEGORÍA", "MEDIDA", "UNIDAD", "PRECIO", "CANTIDAD", "TOTAL");
            escritor.println("------------------------------------------------------------" +
                            "------------------------------");

            double granTotal = 0;
            int i = 1;
            for (Producto p : productos) {
                double total = p.getPrecio() * p.getCantidad();
                granTotal += total;
                escritor.printf("%-4d %-20s %-15s %-8s %-8s $%9.2f %9d  $%10.2f%n",
                        i++,
                        p.getNombre(),
                        p.getCategoria(),
                        p.getMedida(),
                        p.getUnidad(),
                        p.getPrecio(),
                        p.getCantidad(),
                        total);
            }

            escritor.println("------------------------------------------------------------" +
                            "------------------------------");
            escritor.printf("%-74s $%10.2f%n", "VALOR TOTAL DEL INVENTARIO:", granTotal);
            escritor.println();
            escritor.println("============================================================");
            escritor.println("                    FIN DEL REPORTE                          ");
            escritor.println("============================================================");

            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
