package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PedidoHolder extends AppCompatActivity {
    Button btnCancelar;
    TextView tvMailPedido;
    TextView tvHoraEntrega;
    TextView tvCantidadItems;
    TextView tvPrecio;
    TextView estado;
    ImageView tipoEntrega;

    public PedidoHolder(View base) {
        this.btnCancelar = (Button) base.findViewById(R.id.btnCancelar);
        this.tvMailPedido = (TextView) base.findViewById(R.id.tvEmail);
        this.tvHoraEntrega = (TextView) base.findViewById(R.id.tvFechaEntrega);
        this.tvCantidadItems = (TextView) base.findViewById(R.id.tvItems);
        this.tvPrecio = (TextView) base.findViewById(R.id.tvAPagar);
        this.estado = (TextView) base.findViewById(R.id.lblEstado);
        this.tipoEntrega = (ImageView) base.findViewById(R.id.ivTipoEntrega);
    }
}