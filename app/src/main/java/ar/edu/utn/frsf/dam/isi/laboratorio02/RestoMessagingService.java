package ar.edu.utn.frsf.dam.isi.laboratorio02;


import android.content.Intent;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;


public class RestoMessagingService extends FirebaseMessagingService {
    private static final String LOGTAG = "android-fcm";
    private PedidoRepository repositorioPedido;
    private Pedido p;
    public RestoMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        repositorioPedido = new PedidoRepository();
        p = new Pedido();
        if (remoteMessage.getNotification() != null) {
            String titulo = remoteMessage.getNotification().getTitle();
            String texto = remoteMessage.getNotification().getBody();
            int pedidoId = Integer.parseInt(titulo);
            p = repositorioPedido.buscarPorId(pedidoId);
            if(texto.equals("listo")) {
                p.setEstado(Pedido.Estado.LISTO);

                Intent i = new Intent(getApplicationContext(),EstadoPedidoReceiver.class);
                i.setAction(EstadoPedidoReceiver.ESTADO_LISTO);
                i.putExtra("idPedido",p.getId());
                getApplicationContext().sendBroadcast(i);
            }
        }
    }
}
