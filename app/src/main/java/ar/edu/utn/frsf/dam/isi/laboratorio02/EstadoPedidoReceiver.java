package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;


public class EstadoPedidoReceiver extends BroadcastReceiver {
    public static final String ESTADO_ACEPTADO=".EstadoPedidoReciever.ESTADO_ACEPTADO";
    @Override
    public void onReceive(Context context, Intent intent) {
        PedidoRepository pedidoRepository = new PedidoRepository();
        Pedido pedido = pedidoRepository.buscarPorId(intent.getExtras().getInt("idPedido"));

        if(intent.getAction().equals(ESTADO_ACEPTADO)){
            Toast.makeText(context,"Pedido para " + pedido.getMailContacto() + " ha cambiado de estado a " + pedido.getEstado() ,Toast.LENGTH_LONG).show();
        }
    }
}
