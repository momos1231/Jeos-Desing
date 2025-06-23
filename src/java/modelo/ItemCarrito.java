/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
// Archivo: modelo/ItemCarrito.java
package modelo;

public class ItemCarrito {
    private Producto producto;
    private int cantidad;
    private String talla; // Nuevo campo para la talla
    private double subtotal;

    // Constructor vacío
    public ItemCarrito() {
    }

    // Constructor con parámetros
    public ItemCarrito(Producto producto, int cantidad, String talla) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.talla = talla;
        calcularSubtotal(); // Calcula el subtotal al crear el item
    }

    // Getters y Setters
    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
        calcularSubtotal(); // Recalcula si el producto cambia
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        calcularSubtotal(); // Recalcula si la cantidad cambia
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
        // No afecta subtotal directamente a menos que el precio dependa de la talla
        // Si el precio depende de la talla, deberías recalcular el precio del producto aquí
    }

    public double getSubtotal() {
        return subtotal;
    }

    // Método para calcular el subtotal
    private void calcularSubtotal() {
        if (this.producto != null) {
            this.subtotal = this.cantidad * this.producto.getPrecio();
        } else {
            this.subtotal = 0.0;
        }
    }
}