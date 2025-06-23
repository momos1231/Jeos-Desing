/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

public class MetodoPago {
    private int idMetodoPago;
    private String nombre_metodo_pago;

    public MetodoPago() {}

    public MetodoPago(int idMetodoPago, String nombre_metodo_pago) {
        this.idMetodoPago = idMetodoPago;
        this.nombre_metodo_pago = nombre_metodo_pago;
    }

    public int getIdMetodoPago() {
        return idMetodoPago;
    }

    public void setIdMetodoPago(int idMetodoPago) {
        this.idMetodoPago = idMetodoPago;
    }

    public String getNombre_metodo_pago() {
        return nombre_metodo_pago;
    }

    public void setNombre_metodo_pago(String nombre_metodo_pago) {
        this.nombre_metodo_pago = nombre_metodo_pago;
    }
}