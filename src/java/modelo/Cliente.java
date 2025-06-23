/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

public class Cliente {
    private Long idCliente;
    private String nombre;
    private String direccion; // Coincide con tu DB
    private String email;     // Coincide con tu DB
    private int edad;         // Coincide con tu DB

    // Constructor vacío
    public Cliente() {
    }

    // Constructor con todos los campos de tu DB
    public Cliente(Long idCliente, String nombre, String direccion, String email, int edad) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.direccion = direccion;
        this.email = email;
        this.edad = edad;
    }

    // Getters y Setters
    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
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

    public String getEmail() { // Getter para 'email'
        return email;
    }

    public void setEmail(String email) { // Setter para 'email'
        this.email = email;
    }

    public int getEdad() { // Getter para 'edad'
        return edad;
    }

    public void setEdad(int edad) { // Setter para 'edad'
        this.edad = edad;
     }
    }