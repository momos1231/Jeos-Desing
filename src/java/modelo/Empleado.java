/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

// Dependiendo de tu MER, un Empleado puede tener un Rol y Credencial
public class Empleado {
    private int idEmpleado;
    private String nombre;
    private String email;
    private String genero;
    private String fecha_ingreso; // O LocalDate si usas java.time
    private int edad;

    public Empleado() {}

    public Empleado(int idEmpleado, String nombre, String email, String genero, String fecha_ingreso, int edad) {
        this.idEmpleado = idEmpleado;
        this.nombre = nombre;
        this.email = email;
        this.genero = genero;
        this.fecha_ingreso = fecha_ingreso;
        this.edad = edad;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getFecha_ingreso() {
        return fecha_ingreso;
    }

    public void setFecha_ingreso(String fecha_ingreso) {
        this.fecha_ingreso = fecha_ingreso;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }
}