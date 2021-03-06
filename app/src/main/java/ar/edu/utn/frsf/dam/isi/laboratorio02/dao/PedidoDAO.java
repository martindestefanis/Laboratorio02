package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoConDetalles;

@Dao
public interface PedidoDAO {
    @Query("SELECT * FROM Pedido")
    List<Pedido> getAll();

    @Query("SELECT * FROM Pedido WHERE ID_PEDIDO = :PedID")
    Pedido buscarPorID(long PedID);

    @Query("SELECT * FROM Pedido WHERE ID_PEDIDO = :PedID")
    PedidoConDetalles buscarPorIDConDetalle(Integer PedID);

    @Query("SELECT * FROM Pedido")
    List<PedidoConDetalles> buscarPedidosConDetalles();

    @Insert
    long insert(Pedido pedido);

    @Update
    void update(Pedido pedido);

    @Delete
    void delete(Pedido pedido);
}
