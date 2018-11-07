package ar.edu.utn.frsf.dam.isi.laboratorio02;


import android.content.Intent;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.MyDatabase;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoDAO;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;


public class RestoMessagingService extends FirebaseMessagingService {
    private static final String LOGTAG = "android-fcm";
    //private PedidoRepository repositorioPedido;
    private Pedido p;
    private PedidoDAO pedidoDAO;

    public RestoMessagingService() {

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        //repositorioPedido = new PedidoRepository();
        pedidoDAO = MyDatabase.getInstance(this).getPedidoDAO();
        p = new Pedido();
        if (remoteMessage.getNotification() != null) {
            String titulo = remoteMessage.getNotification().getTitle();
            String texto = remoteMessage.getNotification().getBody();
            int pedidoId = Integer.parseInt(titulo);
            p = pedidoDAO.buscarPorID(pedidoId);
            if(texto.equals("listo")) {
                p.setEstado(Pedido.Estado.LISTO);
                pedidoDAO.update(p);
                Intent i = new Intent(getApplicationContext(),EstadoPedidoReceiver.class);
                i.setAction(EstadoPedidoReceiver.ESTADO_LISTO);
                i.putExtra("idPedido",p.getId());
                getApplicationContext().sendBroadcast(i);
            }
        }
    }
}
