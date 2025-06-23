
package modelo;

public class Proveedor {
    private int idProveedor;
    private String nombre_proveedor; // Coincide con tu MER
    private String correo; // Coincide con tu MER

    public Proveedor() {}

    public Proveedor(int idProveedor, String nombre_proveedor, String correo) {
        this.idProveedor = idProveedor;
        this.nombre_proveedor = nombre_proveedor;
        this.correo = correo;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getNombre_proveedor() {
        return nombre_proveedor;
    }

    public void setNombre_proveedor(String nombre_proveedor) {
        this.nombre_proveedor = nombre_proveedor;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}