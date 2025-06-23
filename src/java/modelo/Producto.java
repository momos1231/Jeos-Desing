/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author nicolas
 *
/**
 *
 * @author nicolas
 */
public class Producto {
    
    private int idProducto;
    private String nombre;
    private int precio; // <--- ¡CAMBIO AQUÍ! DEBE SER int para coincidir con tu DB
    private String genero;
    private String descripcion;
    private byte[] foto;
    
    private Color color;
    private Categoria categoria;
    private Talla talla;
    private Proveedor proveedor;

    // Constructor que recibe 'precio' como int
    public Producto(int idProducto, String nombre, int precio, String genero, String descripcion, byte[] foto, Color color, Categoria categoria, Talla talla, Proveedor proveedor) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precio = precio; // Asignación de int a int
        this.genero = genero;
        this.descripcion = descripcion;
        this.foto = foto;
        this.color = color;
        this.categoria = categoria;
        this.talla = talla;
        this.proveedor = proveedor;
    }

    // Constructor que recibe 'precio' como int
    public Producto(int idProducto, String nombre, int precio, String genero, String descripcion, byte[] foto) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precio = precio; // Asignación de int a int
        this.genero = genero;
        this.descripcion = descripcion;
        this.foto = foto;
    }

    public Producto() {
    }
    
    // Getters y Setters
    
    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Talla getTalla() {
        return talla;
    }

    public void setTalla(Talla talla) {
        this.talla = talla;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    
    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPrecio() { // <--- ¡CAMBIO AQUÍ! El getter devuelve int
        return precio;
    }

    public void setPrecio(int precio) { // <--- ¡CAMBIO AQUÍ! El setter recibe int
        this.precio = precio;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }
    
}


