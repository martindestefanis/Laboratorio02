package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;


public class PrepararPedidoService extends IntentService {
    private PedidoRepository repositorioPedido;
    public PrepararPedidoService() {
        super("PreparaPedidoService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        repositorioPedido = new PedidoRepository();
        try{
            Thread.sleep(20000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<Pedido> lista = repositorioPedido.getLista();
        for(Pedido p:lista){
            if(p.getEstado().equals(Pedido.Estado.ACEPTADO)) {
                p.setEstado(Pedido.Estado.EN_PREPARACION);

                Intent i = new Intent(getApplicationContext(),EstadoPedidoReceiver.class);
                i.setAction(EstadoPedidoReceiver.ESTADO_EN_PREPARACION);
                i.putExtra("idPedido",p.getId());
                getApplicationContext().sendBroadcast(i);
            }

        }


    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
