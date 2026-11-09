package servicio;

import java.util.ArrayList;
import java.util.List;
import modelo.Producto;

public class Inventario {
    private List<Producto> productos;

    public Inventario() {
        productos = new ArrayList<>();
    }

    public void agregarProducto(Producto p) {
        productos.add(p);
    }

    public boolean eliminarProducto(String nombre) {
        return productos.removeIf(p -> p.getNombre().equalsIgnoreCase(nombre));
    }

    public Producto buscarPorNombre(String nombre) {
        for (Producto p : productos) {
            if (p.getNombre().equalsIgnoreCase(nombre)) {
                return p;
            }
        }
        return null;
    }

    public List<Producto> buscarPorCategoria(String categoria) {
        List<Producto> resultado = new ArrayList<>();
        for (Producto p : productos) {
            if (p.getCategoria().equalsIgnoreCase(categoria)) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    public boolean entradaMercancia(String nombre, int cantidad) {
        Producto p = buscarPorNombre(nombre);
        if (p != null && cantidad > 0) {
            p.entradaMercancia(cantidad);
            return true;
        }
        return false;
    }

    public boolean salidaMercancia(String nombre, int cantidad) {
        Producto p = buscarPorNombre(nombre);
        if (p != null && cantidad > 0) {
            return p.salidaMercancia(cantidad);
        }
        return false;
    }

    public List<Producto> listarTodos() {
        return productos;
    }

    public int getCantidadTotal() {
        return productos.size();
    }
}
