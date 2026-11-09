package ui;

import modelo.Producto;
import servicio.Inventario;
import java.util.List;
import java.util.Scanner;

public class MenuPrincipal {
    private Inventario inventario;
    private Scanner entrada;

    public MenuPrincipal() {
        inventario = new Inventario();
        entrada = new Scanner(System.in);
    }

    public void mostrar() {
        System.out.println("\n".repeat(2));
        System.out.println("======================================");
        System.out.println("   INVENTARIO DE PRODUCTOS - TRUPER   ");
        System.out.println("======================================");
    }

    public void iniciar() {
        int opcion;
        do {
            mostrar();
            System.out.println("\n======= MENÚ PRINCIPAL =======");
            System.out.println("1. 📦 Agregar nuevo producto");
            System.out.println("2. 📋 Ver todos los productos");
            System.out.println("3. 🔍 Buscar producto");
            System.out.println("4. ✏️  Editar producto");
            System.out.println("5. 🗑️  Eliminar producto");
            System.out.println("6. 📊 Resumen del inventario");
            System.out.println("7. 💾 Salir y guardar");
            System.out.println("===============================");
            System.out.print("Elige una opción: ");
           
            while (!entrada.hasNextInt()) {
                System.out.print("Ingresa un número: ");
                entrada.next();
            }
            opcion = entrada.nextInt();
            entrada.nextLine();

            switch (opcion) {
                case 1 -> agregarProducto();
                case 2 -> mostrarTodos();
                case 3 -> buscarProducto();
                case 4 -> editarProducto();
                case 5 -> eliminarProducto();
                case 6 -> mostrarResumen();
                case 7 -> guardarYSalir();
                default -> System.out.println("❌ Opción no válida");
            }
        } while (opcion != 7);
    }

    private void agregarProducto() {
        System.out.println("\n--- AGREGAR PRODUCTO ---");
        System.out.print("Nombre: ");
        String nombre = entrada.nextLine();
        System.out.print("Medida: ");
        String medida = entrada.nextLine();
        System.out.print("Unidad: ");
        String unidad = entrada.nextLine();
        System.out.print("Precio: $");
        double precio = leerDouble();
        System.out.print("Cantidad: ");
        int cantidad = leerEntero();

        inventario.agregarProducto(new Producto(nombre, medida, unidad, precio, cantidad));
    }

    private void mostrarTodos() {
        System.out.println("\n--- LISTA DE PRODUCTOS ---");
        List<Producto> lista = inventario.obtenerTodos();
        if (lista.isEmpty()) {
            System.out.println("📭 El inventario está vacío");
            return;
        }
        System.out.printf("%-4s %-20s %-10s %-8s %-10s %-8s %-10s%n",
                "Nº", "NOMBRE", "MEDIDA", "UNIDAD", "PRECIO", "CANT", "TOTAL");
        System.out.println("-".repeat(75));
        for (int i = 0; i < lista.size(); i++) {
            Producto p = lista.get(i);
            System.out.printf("%-4d %-20s %-10s %-8s $%8.2f %6d u $%10.2f%n",
                    i + 1, p.getNombre(), p.getMedida(), p.getUnidad(),
                    p.getPrecio(), p.getCantidad(), p.getValorTotal());
        }
    }

    private void buscarProducto() {
        System.out.println("\n--- BUSCAR PRODUCTO ---");
        System.out.print("Nombre del producto: ");
        String nombre = entrada.nextLine();
        Producto p = inventario.buscarPorNombre(nombre);
        if (p != null) {
            System.out.println("✅ Encontrado:");
            System.out.println("Nombre:  " + p.getNombre());
            System.out.println("Medida:  " + p.getMedida());
            System.out.println("Unidad:  " + p.getUnidad());
            System.out.println("Precio:  $" + p.getPrecio());
            System.out.println("Cantidad: " + p.getCantidad() + " unidades");
            System.out.printf("Total:   $%.2f%n", p.getValorTotal());
        } else {
            System.out.println("❌ No encontrado");
        }
    }

    private void editarProducto() {
        System.out.println("\n--- EDITAR PRODUCTO ---");
        System.out.print("Nombre del producto a editar: ");
        String nombre = entrada.nextLine();
        Producto p = inventario.buscarPorNombre(nombre);
        if (p == null) {
            System.out.println("❌ No encontrado");
            return;
        }
        System.out.println("(Deja vacío y presiona Enter para no cambiar)");
        System.out.print("Nuevo nombre: ");
        String nuevoNombre = entrada.nextLine();
        if (!nuevoNombre.isBlank()) p.setNombre(nuevoNombre);
       
        System.out.print("Nueva medida: ");
        String nuevaMedida = entrada.nextLine();
        if (!nuevaMedida.isBlank()) p.setMedida(nuevaMedida);
       
        System.out.print("Nuevo precio: $");
        String precioStr = entrada.nextLine();
        if (!precioStr.isBlank()) p.setPrecio(Double.parseDouble(precioStr));
       
        System.out.print("Nueva cantidad: ");
        String cantStr = entrada.nextLine();
        if (!cantStr.isBlank()) p.setCantidad(Integer.parseInt(cantStr));
       
        System.out.println("✅ Producto actualizado");
    }

    private void eliminarProducto() {
        System.out.println("\n--- ELIMINAR PRODUCTO ---");
        System.out.print("Nombre del producto: ");
        String nombre = entrada.nextLine();
        if (inventario.eliminarProducto(nombre)) {
            System.out.println("✅ Producto eliminado");
        } else {
            System.out.println("❌ No encontrado");
        }
    }

    private void mostrarResumen() {
        System.out.println("\n--- RESUMEN DEL INVENTARIO ---");
        System.out.println("Total de productos distintos: " + inventario.getCantidadTotal());
        System.out.printf("Valor total del inventario: $%.2f%n", inventario.getValorTotalInventario());
    }

    private void guardarYSalir() {
        inventario.guardarDatos();
        System.out.println("\n👋 ¡Gracias por usar el Sistema de Inventario Truper!");
    }

    private double leerDouble() {
        while (!entrada.hasNextDouble()) {
            System.out.print("Por favor ingresa un número válido: ");
            entrada.next();
        }
        double valor = entrada.nextDouble();
        entrada.nextLine();
        return valor;
    }

    private int leerEntero() {
        while (!entrada.hasNextInt()) {
            System.out.print("Por favor ingresa un número válido: ");
            entrada.next();
        }
        int valor = entrada.nextInt();
        entrada.nextLine();
        return valor;
    }
}
