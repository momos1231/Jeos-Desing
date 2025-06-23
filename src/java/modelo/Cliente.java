/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Administrador
 */

public class Cliente {
    private int idCliente;
    private String nombre;
    private String direccion;
    private String email;
    private int edad;

    public Cliente() {
    }

    public Cliente(int idCliente, String nombre, String direccion, String email, int edad) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.direccion = direccion;
        this.email = email;
        this.edad = edad;
    }

    // Getters y Setters
    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    @Override
    public String toString() {
        return "Cliente{" +
               "idCliente=" + idCliente +
               ", nombre='" + nombre + '\'' +
               ", direccion='" + direccion + '\'' +
               ", email='" + email + '\'' +
               ", edad=" + edad +
               '}';
    }
}