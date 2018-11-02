package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoDetalle;


@Dao
public interface PedidoDetalleDAO {
    @Query("SELECT * FROM PedidoDetalle")
    List<PedidoDetalle> getAll();

    @Query("SELECT * FROM PedidoDetalle WHERE ID_PEDIDO_DETALLE = :PedDetID")
    PedidoDetalle buscarPorID(Integer PedDetID);

    @Insert
    long insert(PedidoDetalle pedidoDetalle);

    @Update
    void update(PedidoDetalle pedidoDetalle);

    @Delete
    void delete(PedidoDetalle pedidoDetalle);
}
