package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;


public class EstadoPedidoReceiver extends BroadcastReceiver {
    public static final String ESTADO_ACEPTADO="ar.edu.utn.frsf.dam.isi.laboratorio02.ESTADO_ACEPTADO";
    public static final String ESTADO_EN_PREPARACION="ar.edu.utn.frsf.dam.isi.laboratorio02.ESTADO_EN_PREPARACION";
    public static final String ESTADO_LISTO="ar.edu.utn.frsf.dam.isi.laboratorio02.ESTADO_LISTO";
    @Override
    public void onReceive(Context context, Intent intent) {
        PedidoRepository pedidoRepository = new PedidoRepository();
        Pedido pedido = pedidoRepository.buscarPorId(intent.getExtras().getInt("idPedido"));

        if(intent.getAction().equals(ESTADO_ACEPTADO)){
            Toast.makeText(context,"Pedido para " + pedido.getMailContacto() + " ha cambiado de estado a " + pedido.getEstado() ,Toast.LENGTH_LONG).show();
            Intent destino= new Intent(context,PedidoActivity.class);
            destino.putExtra("idPedidoSeleccionado",pedido.getId());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,destino,PendingIntent.FLAG_UPDATE_CURRENT);

            Notification notification = new NotificationCompat.Builder(context,"CANAL01")
                    //Ver icono
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("Tu pedido fue aceptado")
                    .setStyle(new NotificationCompat.InboxStyle()
                            .addLine("El costo será de: $"+pedido.total())
                            .addLine("Previsto el envio para: "+pedido.getFecha()))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();


            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(pedido.getId(), notification);
        }
        if(intent.getAction().equals(ESTADO_EN_PREPARACION)){
            Intent destino= new Intent(context,HistorialActivity.class);
            destino.putExtra("idPedidoSeleccionado",pedido.getId());
            intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,destino,PendingIntent.FLAG_UPDATE_CURRENT);

            Notification notification = new NotificationCompat.Builder(context,"CANAL01")
                    //Ver icono
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("Tu pedido esta en preparación")
                    .setStyle(new NotificationCompat.InboxStyle()
                            .addLine("El costo será de: $"+pedido.total())
                            .addLine("Previsto el envio para: "+pedido.getFecha()))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();


            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(pedido.getId(), notification);
            Toast.makeText(context,"Pedido para " + pedido.getMailContacto() + " ha cambiado de estado a " + pedido.getEstado() ,Toast.LENGTH_LONG).show();
        }
        if(intent.getAction().equals(ESTADO_LISTO)){
            Intent destino= new Intent(context,PedidoActivity.class);
            destino.putExtra("idPedidoSeleccionado",pedido.getId());
            intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,destino,PendingIntent.FLAG_UPDATE_CURRENT);

            Notification notification = new NotificationCompat.Builder(context,"CANAL01")
                    //Ver icono
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("Tu pedido esta LISTO")
                    .setStyle(new NotificationCompat.InboxStyle()
                            .addLine("El costo será de: $"+pedido.total()))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();


            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(pedido.getId(), notification);
            Toast.makeText(context,"Pedido para " + pedido.getMailContacto() + " ha cambiado de estado a " + pedido.getEstado() ,Toast.LENGTH_LONG).show();
        }
    }
}
