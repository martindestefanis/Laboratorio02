package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.view.View;
import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

public class PedidoAdapter extends ArrayAdapter<Pedido> {
    private Context ctx;
    private List<Pedido> datos;
    private PedidoHolder pedidoHolder;

    public PedidoAdapter(Context context,List<Pedido> objects) {
        super(context, 0, objects);
        this.ctx = context;
        this.datos = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(this.ctx);
        View fila_historial = convertView;
        if (fila_historial == null) {
            fila_historial = inflater.inflate(R.layout.fila_historial, parent, false);
        }
        pedidoHolder = (PedidoHolder) fila_historial.getTag();
        if (pedidoHolder == null) {
            pedidoHolder = new PedidoHolder(fila_historial);
            fila_historial.setTag(pedidoHolder);
        }
        final Pedido pedido = (Pedido) super.getItem(position);
        pedidoHolder.tvMailPedido.setText("Contacto: " + pedido.getMailContacto());
        pedidoHolder.tvHoraEntrega.setText("Fecha de entrega: " + pedido.getFecha().toString());
        pedidoHolder.tvPrecio.setText("A pagar: $" + pedido.total().toString());
        pedidoHolder.tvCantidadItems.setText("Items: " + pedido.getDetalle().size());
        pedidoHolder.estado.setText("Estado: " + pedido.getEstado().toString());
        if (pedido.getRetirar()) {
            pedidoHolder.tipoEntrega.setImageResource(R.drawable.retira);
        }
        else {
            pedidoHolder.tipoEntrega.setImageResource(R.drawable.envio);
        }

        pedidoHolder.btnCancelar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int indice = (int) view.getTag();
                        Pedido pedidoSeleccionado = datos.get(indice);
                        if( pedidoSeleccionado.getEstado().equals(Pedido.Estado.REALIZADO) ||
                                pedidoSeleccionado.getEstado().equals(Pedido.Estado.ACEPTADO) ||
                                pedidoSeleccionado.getEstado().equals(Pedido.Estado.EN_PREPARACION)) {
                            pedidoSeleccionado.setEstado(Pedido.Estado.CANCELADO);
                            PedidoAdapter.this.notifyDataSetChanged();
                            return;
                        }
                    }
                }
        );

        switch (pedido.getEstado()){
            case LISTO:
                pedidoHolder.estado.setTextColor(Color.DKGRAY);
                break;
            case ENTREGADO:
                pedidoHolder.estado.setTextColor(Color.BLUE);
                break;
            case CANCELADO:
                pedidoHolder.estado.setTextColor(Color.RED);
                break;
            case RECHAZADO:
                pedidoHolder.estado.setTextColor(Color.RED);
                break;
            case ACEPTADO:
                pedidoHolder.estado.setTextColor(Color.GREEN);
                break;
            case EN_PREPARACION:
                pedidoHolder.estado.setTextColor(Color.MAGENTA);
                break;
            case REALIZADO:
                pedidoHolder.estado.setTextColor(Color.BLUE);
                break;
        }

        pedidoHolder.btnCancelar.setTag(position);

        pedidoHolder.btnVerDetalle.setTag(position);
        pedidoHolder.btnVerDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ctx, PedidoActivity.class);
                i.putExtra("idPedidoSeleccionado", pedido.getId());
                Log.d("Database", "idPedidoSeleccionado salida " + pedido.getId().toString());
                ctx.startActivity(i);
            }
        });

        return fila_historial;
    }
}