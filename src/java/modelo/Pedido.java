/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.time.LocalDateTime; // Usar LocalDateTime para fecha_hora si es DATETIME

public class Pedido {
    private int idPedido;
    private int cantidadItems; // Mapeado a 'cantidad' en tu DB
    private double total; // Mapeado a 'precio' en tu DB (DOUBLE)
    private Cliente cliente;
    private Empleado empleado;
    // Podrías añadir fecha_hora si tu tabla Pedido la tuviera, pero no está en la imagen
    // private LocalDateTime fechaHoraPedido; // Si existiera en tu DB

    public Pedido() {}

    public Pedido(int idPedido, int cantidadItems, double total, Cliente cliente, Empleado empleado) {
        this.idPedido = idPedido;
        this.cantidadItems = cantidadItems;
        this.total = total;
        this.cliente = cliente;
        this.empleado = empleado;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getCantidadItems() {
        return cantidadItems;
    }

    public void setCantidadItems(int cantidadItems) {
        this.cantidadItems = cantidadItems;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }
    // Si tuvieras fecha_hora en la tabla pedido
    // public LocalDateTime getFechaHoraPedido() { return fechaHoraPedido; }
    // public void setFechaHoraPedido(LocalDateTime fechaHoraPedido) { this.fechaHoraPedido = fechaHoraPedido; }
}