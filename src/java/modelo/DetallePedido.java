/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

public class DetallePedido {
    // Asumo que no hay un ID propio para DetallePedido, es una tabla de relación
    // Las claves primarias serían idProducto e idPedido juntos.
    private Producto producto;
    private Pedido pedido;

    public DetallePedido() {}

    public DetallePedido(Producto producto, Pedido pedido) {
        this.producto = producto;
        this.pedido = pedido;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
}