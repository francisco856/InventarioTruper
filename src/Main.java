import modelo.Producto;
import servicio.Inventario;
import servicio.ExportadorCSV;
import servicio.ExportadorPDF;
import servicio.Graficador;
import java.io.*;
import java.util.Scanner;
import java.util.List;

public class Main {
    private static final String CARPETA_DATOS = "datos";
    private static final String ARCHIVO_DATOS = CARPETA_DATOS + "/inventario.txt";
   
    private static void cargarDatos(Inventario inventario) {
        File carpeta = new File(CARPETA_DATOS);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
       
        File archivo = new File(ARCHIVO_DATOS);
        if (!archivo.exists()) {
            return;
        }
       
        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                if (!linea.isBlank()) {
                    Producto p = Producto.desdeLinea(linea);
                    inventario.agregarProducto(p);
                }
            }
        } catch (IOException e) {
            System.out.println("⚠️ No se pudieron cargar los datos anteriores.");
        }
    }
   
    private static void guardarDatos(Inventario inventario) {
        File carpeta = new File(CARPETA_DATOS);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
       
        try (PrintWriter escritor = new PrintWriter(new FileWriter(ARCHIVO_DATOS))) {
            for (Producto p : inventario.listarTodos()) {
                escritor.println(p.toLinea());
            }
            System.out.println("✅ Datos guardados correctamente.");
        } catch (IOException e) {
            System.out.println("❌ Error al guardar los datos.");
        }
    }
   
    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        Inventario inventario = new Inventario();
        cargarDatos(inventario);
        boolean salir = false;

        while (!salir) {
            System.out.println("=====================================");
            System.out.println("   INVENTARIO DE PRODUCTOS - TRUPER");
            System.out.println("=====================================");
            System.out.println();
            System.out.println("        === MENÚ PRINCIPAL ===");
            System.out.println("1. 📦 Agregar nuevo producto");
            System.out.println("2. 📋 Ver todos los productos");
            System.out.println("3. 🔍 Buscar por nombre");
            System.out.println("4. 🏷️ Buscar por categoría");
            System.out.println("5. 📥 Entrada de mercancía");
            System.out.println("6. 📤 Salida de mercancía");
            System.out.println("7. ✏️ Editar producto");
            System.out.println("8. 🗑️ Eliminar producto");
            System.out.println("9. 📊 Resumen del inventario");
            System.out.println("10. 📈 Ver gráficos");
            System.out.println("11. 💾 Exportar a CSV/Excel");
            System.out.println("12. 📄 Exportar a Reporte");
            System.out.println("13. 🚪 Salir y guardar");
            System.out.println();
            System.out.print("Elige una opción: ");

            int opcion;
            try {
                opcion = Integer.parseInt(entrada.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ Por favor escribe un número válido.");
                System.out.println();
                continue;
            }

            System.out.println();

            switch (opcion) {
                case 1:
                    System.out.println("=== Agregar Nuevo Producto ===");
                    System.out.print("Nombre: ");
                    String nombre = entrada.nextLine();
                    System.out.print("Categoría: ");
                    String categoria = entrada.nextLine();
                    System.out.print("Medida: ");
                    String medida = entrada.nextLine();
                    System.out.print("Unidad: ");
                    String unidad = entrada.nextLine();
                    System.out.print("Precio: $");
                    double precio = Double.parseDouble(entrada.nextLine());
                    System.out.print("Cantidad: ");
                    int cantidad = Integer.parseInt(entrada.nextLine());

                    Producto nuevo = new Producto(nombre, categoria, medida, unidad, precio, cantidad);
                    inventario.agregarProducto(nuevo);
                    System.out.println("✅ Producto agregado con éxito.");
                    break;

                case 2:
                    System.out.println("=== Lista de Productos ===");
                    List<Producto> lista = inventario.listarTodos();
                    if (lista.isEmpty()) {
                        System.out.println("📭 El inventario está vacío.");
                    } else {
                        System.out.printf("%-20s %-15s %-10s %-8s %-10s %8s%n",
                                "NOMBRE", "CATEGORÍA", "MEDIDA", "UNIDAD", "PRECIO", "CANTIDAD");
                        System.out.println("---------------------------------------------------------------------------");
                        for (Producto p : lista) {
                            System.out.println(p);
                        }
                    }
                    break;

                case 3:
                    System.out.println("=== Buscar por Nombre ===");
                    System.out.print("Nombre del producto: ");
                    String buscarNom = entrada.nextLine();
                    Producto encontrado = inventario.buscarPorNombre(buscarNom);
                    if (encontrado != null) {
                        System.out.println("✅ Producto encontrado:");
                        System.out.println(encontrado);
                    } else {
                        System.out.println("❌ Producto no encontrado.");
                    }
                    break;

                case 4:
                    System.out.println("=== Buscar por Categoría ===");
                    System.out.print("Nombre de la categoría: ");
                    String buscarCat = entrada.nextLine();
                    List<Producto> resultados = inventario.buscarPorCategoria(buscarCat);
                    if (resultados.isEmpty()) {
                        System.out.println("❌ No hay productos en esa categoría.");
                    } else {
                        System.out.println("✅ Encontrados " + resultados.size() + " producto(s):");
                        System.out.printf("%-20s %-10s %-8s %-10s%n",
                                "NOMBRE", "MEDIDA", "UNIDAD", "CANTIDAD");
                        System.out.println("----------------------------------------------");
                        for (Producto p : resultados) {
                            System.out.printf("%-20s %-10s %-8s %d u%n",
                                    p.getNombre(), p.getMedida(), p.getUnidad(), p.getCantidad());
                        }
                    }
                    break;

                case 5:
                    System.out.println("=== Entrada de Mercancía ===");
                    System.out.print("Nombre del producto: ");
                    String nombreEntrada = entrada.nextLine();
                    System.out.print("Cantidad a agregar: ");
                    int cantEntrada = Integer.parseInt(entrada.nextLine());

                    if (inventario.entradaMercancia(nombreEntrada, cantEntrada)) {
                        System.out.println("✅ Entrada registrada. Stock actualizado.");
                    } else {
                        System.out.println("❌ No se pudo realizar la entrada. Verifica el nombre.");
                    }
                    break;

                case 6:
                    System.out.println("=== Salida de Mercancía ===");
                    System.out.print("Nombre del producto: ");
                    String nombreSalida = entrada.nextLine();
                    System.out.print("Cantidad a retirar: ");
                    int cantSalida = Integer.parseInt(entrada.nextLine());

                    if (inventario.salidaMercancia(nombreSalida, cantSalida)) {
                        System.out.println("✅ Salida registrada. Stock actualizado.");
                    } else {
                        System.out.println("❌ No hay suficiente stock o el producto no existe.");
                    }
                    break;

                case 7:
                    System.out.println("=== Editar Producto ===");
                    System.out.print("Nombre del producto a editar: ");
                    String nombreEditar = entrada.nextLine();
                    Producto prodEditar = inventario.buscarPorNombre(nombreEditar);
                    if (prodEditar != null) {
                        System.out.print("Nuevo nombre (dejar vacío para no cambiar): ");
                        String nuevoNombre = entrada.nextLine();
                        if (!nuevoNombre.isBlank()) prodEditar.setNombre(nuevoNombre);

                        System.out.print("Nueva categoría (dejar vacío para no cambiar): ");
                        String nuevaCat = entrada.nextLine();
                        if (!nuevaCat.isBlank()) prodEditar.setCategoria(nuevaCat);

                        System.out.print("Nuevo precio: $");
                        String precioStr = entrada.nextLine();
                        if (!precioStr.isBlank()) prodEditar.setPrecio(Double.parseDouble(precioStr));

                        System.out.println("✅ Producto actualizado.");
                    } else {
                        System.out.println("❌ Producto no encontrado.");
                    }
                    break;

                case 8:
                    System.out.println("=== Eliminar Producto ===");
                    System.out.print("Nombre del producto a eliminar: ");
                    String nombreEliminar = entrada.nextLine();
                    if (inventario.eliminarProducto(nombreEliminar)) {
                        System.out.println("✅ Producto eliminado.");
                    } else {
                        System.out.println("❌ Producto no encontrado.");
                    }
                    break;

                case 9:
                    System.out.println("=== Resumen del Inventario ===");
                    List<Producto> todos = inventario.listarTodos();
                    double valorTotal = 0;
                    for (Producto p : todos) {
                        valorTotal += p.getValorTotal();
                    }
                    System.out.println("Total de productos distintos: " + inventario.getCantidadTotal());
                    System.out.printf("Valor total del inventario: $%.2f%n", valorTotal);
                    break;

                case 10:
                    System.out.println("=== GRÁFICOS DEL INVENTARIO ===");
                    System.out.println("1. 📊 Stock por producto");
                    System.out.println("2. 🏷️ Productos por categoría");
                    System.out.println("3. 💰 Valor total por producto");
                    System.out.print("Elige tipo de gráfico: ");
                    int tipoGrafico;
                    try {
                        tipoGrafico = Integer.parseInt(entrada.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("❌ Número inválido.");
                        break;
                    }
                   
                    List<Producto> listaGrafico = inventario.listarTodos();
                    if (listaGrafico.isEmpty()) {
                        System.out.println("📭 No hay productos para graficar.");
                    } else {
                        switch (tipoGrafico) {
                            case 1:
                                Graficador.graficarStock(listaGrafico);
                                break;
                            case 2:
                                Graficador.graficarPorCategoria(listaGrafico);
                                break;
                            case 3:
                                Graficador.graficarValorTotal(listaGrafico);
                                break;
                            default:
                                System.out.println("❌ Opción no válida.");
                        }
                    }
                    break;

                case 11:
                    System.out.println("=== Exportar a CSV ===");
                    System.out.print("Nombre del archivo (ej: inventario.csv): ");
                    String nombreArchivoCSV = entrada.nextLine();
                    List<Producto> paraExportar = inventario.listarTodos();
                    if (ExportadorCSV.exportar(paraExportar, nombreArchivoCSV)) {
                        System.out.println("✅ Exportado con éxito en: datos/" + nombreArchivoCSV);
                    } else {
                        System.out.println("❌ Error al exportar el archivo.");
                    }
                    break;

                case 12:
                    System.out.println("=== Exportar a Reporte ===");
                    System.out.print("Nombre del archivo (ej: reporte.txt): ");
                    String nombreReporte = entrada.nextLine();
                    List<Producto> paraReporte = inventario.listarTodos();
                    if (ExportadorPDF.exportar(paraReporte, nombreReporte)) {
                        System.out.println("✅ Reporte generado en: datos/" + nombreReporte);
                        System.out.println("💡 Puedes abrirlo con cualquier editor de texto o imprimirlo.");
                    } else {
                        System.out.println("❌ Error al generar el reporte.");
                    }
                    break;

                case 13:
                    guardarDatos(inventario);
                    System.out.println("✅ ¡Guardado! Saliendo del programa...");
                    salir = true;
                    break;

                default:
                    System.out.println("❌ Opción no válida. Intenta de nuevo.");
                    break;
            }

            if (!salir) {
                System.out.println();
                System.out.print("Presiona Enter para continuar...");
                entrada.nextLine();
            }
        }
        entrada.close();
    }
}
