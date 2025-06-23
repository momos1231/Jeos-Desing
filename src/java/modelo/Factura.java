/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.time.LocalDate; // Para fecha_factura (DATE)

public class Factura {
    private int idFactura;
    private String fechaFactura; // Usamos String para coincidir con el formato que obtienes de la DB
    private double monto; // Tu campo monto en factura es INT en el MER pero DOUBLE en la imagen. Lo dejaré como double por consistencia con cálculos.
    private Pedido pedido;

    public Factura() {}

    public Factura(int idFactura, String fechaFactura, double monto, Pedido pedido) {
        this.idFactura = idFactura;
        this.fechaFactura = fechaFactura;
        this.monto = monto;
        this.pedido = pedido;
    }

    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public String getFechaFactura() {
        return fechaFactura;
    }

    public void setFechaFactura(String fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
}