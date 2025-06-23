/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.time.LocalDate; // Para fecha_pago (DATE)

public class Pago {
    private int idPago;
    private LocalDate fechaPago; // LocalDate para DATE en DB
    private double montoPago; // DECIMAL(10,0) en DB
    private String estado;
    private Factura factura;
    private MetodoPago metodoPago;

    public Pago() {}

    public Pago(int idPago, LocalDate fechaPago, double montoPago, String estado, Factura factura, MetodoPago metodoPago) {
        this.idPago = idPago;
        this.fechaPago = fechaPago;
        this.montoPago = montoPago;
        this.estado = estado;
        this.factura = factura;
        this.metodoPago = metodoPago;
    }

    public int getIdPago() {
        return idPago;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    public double getMontoPago() {
        return montoPago;
    }

    public void setMontoPago(double montoPago) {
        this.montoPago = montoPago;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }
}