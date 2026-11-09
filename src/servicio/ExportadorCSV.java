package servicio;

import modelo.Producto;
import java.io.*;
import java.util.List;

public class ExportadorCSV {
    private static final String CARPETA_DATOS = "datos";
   
    public static boolean exportar(List<Producto> productos, String nombreArchivo) {
        File carpeta = new File(CARPETA_DATOS);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
       
        File archivo = new File(carpeta, nombreArchivo);
       
        try (PrintWriter escritor = new PrintWriter(new FileWriter(archivo))) {
            escritor.println("Nombre,Categoría,Medida,Unidad,Precio,Cantidad,Valor Total");
           
            for (Producto p : productos) {
                escritor.printf("%s,%s,%s,%s,%.2f,%d,%.2f%n",
                    p.getNombre(),
                    p.getCategoria(),
                    p.getMedida(),
                    p.getUnidad(),
                    p.getPrecio(),
                    p.getCantidad(),
                    p.getPrecio() * p.getCantidad()
                );
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
