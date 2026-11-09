package servicio;

import modelo.Producto;
import java.util.List;

public class Graficador {
   
    public static void graficarStock(List<Producto> productos) {
        System.out.println("\n📊 === GRÁFICO DE STOCK ===");
        System.out.println("Cantidad por producto:\n");
       
        int maxBarra = 30;
        int maxCantidad = 1;
       
        for (Producto p : productos) {
            if (p.getCantidad() > maxCantidad) {
                maxCantidad = p.getCantidad();
            }
        }
       
        for (Producto p : productos) {
            int longitud = (p.getCantidad() * maxBarra) / maxCantidad;
            String barra = "█".repeat(longitud);
            String nombre = p.getNombre();
            if (nombre.length() > 15) nombre = nombre.substring(0, 12) + "...";
            System.out.printf("%-15s |%s %d%n", nombre, barra, p.getCantidad());
        }
        System.out.println();
    }
   
    public static void graficarPorCategoria(List<Producto> productos) {
        System.out.println("\n📊 === PRODUCTOS POR CATEGORÍA ===");
       
        java.util.Map<String, Integer> conteo = new java.util.HashMap<>();
        for (Producto p : productos) {
            String cat = p.getCategoria().isBlank() ? "Sin categoría" : p.getCategoria();
            conteo.put(cat, conteo.getOrDefault(cat, 0) + 1);
        }
       
        int maxBarra = 30;
        int maxValor = 1;
        for (int valor : conteo.values()) {
            if (valor > maxValor) maxValor = valor;
        }
       
        System.out.println();
        for (java.util.Map.Entry<String, Integer> entrada : conteo.entrySet()) {
            int longitud = (entrada.getValue() * maxBarra) / maxValor;
            String barra = "█".repeat(longitud);
            System.out.printf("%-18s |%s %d%n", entrada.getKey(), barra, entrada.getValue());
        }
        System.out.println();
    }
   
    public static void graficarValorTotal(List<Producto> productos) {
        System.out.println("\n💰 === VALOR TOTAL POR PRODUCTO ===");
        System.out.println("(Precio × Cantidad)\n");
       
        int maxBarra = 30;
        double maxValor = 1.0;
       
        for (Producto p : productos) {
            double total = p.getPrecio() * p.getCantidad();
            if (total > maxValor) maxValor = total;
        }
       
        for (Producto p : productos) {
            double total = p.getPrecio() * p.getCantidad();
            int longitud = (int) ((total / maxValor) * maxBarra);
            if (longitud < 1 && total > 0) longitud = 1;
            String barra = "█".repeat(longitud);
            String nombre = p.getNombre();
            if (nombre.length() > 15) nombre = nombre.substring(0, 12) + "...";
            System.out.printf("%-15s |%s $%.2f%n", nombre, barra, total);
        }
        System.out.println();
    }
}
