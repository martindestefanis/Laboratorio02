package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.view.View;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoDetalle;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;

public class PedidoActivity extends AppCompatActivity{
    private Pedido unPedido;
    private PedidoDetalle unPedidoDetalle;
    private PedidoRepository repositorioPedido;
    private ProductoRepository repositorioProducto;
    private RadioButton rbRetira, rbEnviar;
    private EditText edtPedidoDireccion;
    private ListView lstPedidoItems;
    private ArrayAdapter<PedidoDetalle> adapterLstPedidoItems;
    private Button btnPedidoAddProducto;
    private Bundle extras;
    private int extraID, extraCantidad;
    private Producto producto;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nuevo_pedido);

        rbRetira = (RadioButton) findViewById(R.id.rbRetira);
        rbEnviar = (RadioButton) findViewById(R.id.rbEnviar);
        edtPedidoDireccion = (EditText) findViewById(R.id.edtPedidoDireccion);

        rbRetira.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtPedidoDireccion.setEnabled(false);
            }
        });
        rbEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtPedidoDireccion.setEnabled(true);
            }
        });

        unPedido = new Pedido();

        btnPedidoAddProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ListaProdActivity.class);
                startActivity(i);
                extras = getIntent().getExtras();
                extraID = extras.getInt("idProducto");
                extraCantidad = extras.getInt("cantidad");
                repositorioProducto = new ProductoRepository();
                producto = repositorioProducto.buscarPorId(extraID);
                unPedidoDetalle = new PedidoDetalle(extraCantidad, producto);

                lstPedidoItems = (ListView) findViewById(R.id.lstPedidoItems);
                adapterLstPedidoItems = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_single_choice, unPedido.getDetalle());
                lstPedidoItems.setAdapter(adapterLstPedidoItems);
            }
        });

    }
}