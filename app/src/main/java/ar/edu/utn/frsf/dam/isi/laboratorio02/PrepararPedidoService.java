package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.MyDatabase;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoDAO;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

public class PrepararPedidoService extends IntentService {
    //private PedidoRepository repositorioPedido;
    private PedidoDAO pedidoDAO;
    public PrepararPedidoService() {
        super("PreparaPedidoService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        //repositorioPedido = new PedidoRepository();
        pedidoDAO = MyDatabase.getInstance(this).getPedidoDAO();
        try{
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<Pedido> lista = pedidoDAO.getAll();
        for(Pedido p:lista){
            if(p.getEstado().equals(Pedido.Estado.ACEPTADO)) {
                p.setEstado(Pedido.Estado.EN_PREPARACION);
                pedidoDAO.update(p);
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
