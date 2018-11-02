package ar.edu.utn.frsf.dam.isi.laboratorio02.modelo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class PedidoDetalle {

    private static int ID_DETALLE =1;
    @PrimaryKey
    @ColumnInfo(name = "ID_PEDIDO_DETALLE")
    private Integer id;
    private Integer cantidad;
    @Embedded(prefix = "prod_")
    private Producto producto;
    @Embedded(prefix = "ped_")
    private Pedido pedido;

    public PedidoDetalle(Integer cantidad, Producto producto) {
        id=ID_DETALLE++;
        this.cantidad = cantidad;
        this.producto = producto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
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
        pedido.agregarDetalle(this);
    }

    @Override
    public String toString() {
        return producto.getNombre() + "( $"+producto.getPrecio()+")"+ cantidad;
    }
}
