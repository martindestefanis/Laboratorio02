package ar.edu.utn.frsf.dam.isi.laboratorio02.modelo;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

public class PedidoConDetalles {
    @Embedded
    public Pedido pedido;

    @Relation(parentColumn = "ID_PEDIDO", entityColumn = "ID_PEDIDO_DETALLE", entity = PedidoDetalle.class)
    public List<PedidoDetalle> detalle;
}
