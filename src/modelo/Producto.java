package modelo;

public class Producto {
    private String nombre;
    private String categoria;
    private String medida;
    private String unidad;
    private double precio;
    private int cantidad;

    public Producto(String nombre, String categoria, String medida,
                    String unidad, double precio, int cantidad) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.medida = medida;
        this.unidad = unidad;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
   
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
   
    public String getMedida() { return medida; }
    public void setMedida(String medida) { this.medida = medida; }
   
    public String getUnidad() { return unidad; }
    public void setUnidad(String unidad) { this.unidad = unidad; }
   
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
   
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
   
    public double getValorTotal() {
        return precio * cantidad;
    }

    public void entradaMercancia(int cantidad) {
        if (cantidad > 0) {
            this.cantidad += cantidad;
        }
    }

    public boolean salidaMercancia(int cantidad) {
        if (cantidad > 0 && this.cantidad >= cantidad) {
            this.cantidad -= cantidad;
            return true;
        }
        return false;
    }

    public String toLinea() {
        return nombre + "|" + categoria + "|" + medida + "|" + unidad + "|" + precio + "|" + cantidad;
    }

    public static Producto desdeLinea(String linea) {
        String[] partes = linea.split("\\|");
        return new Producto(
            partes[0],
            partes[1],
            partes[2],
            partes[3],
            Double.parseDouble(partes[4]),
            Integer.parseInt(partes[5])
        );
    }

    @Override
    public String toString() {
        return String.format("%-20s %-15s %-10s %-8s $%8.2f %6d u",
                nombre, categoria, medida, unidad, precio, cantidad);
    }
}
