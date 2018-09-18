package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.Activity;
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
    private Intent i;
    private static final int CODIGO = 999;

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
        lstPedidoItems = (ListView) findViewById(R.id.lstPedidoItems);
        adapterLstPedidoItems = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_single_choice, unPedido.getDetalle());
        lstPedidoItems.setAdapter(adapterLstPedidoItems);

        btnPedidoAddProducto = (Button) findViewById(R.id.btnPedidoAddProducto);
        btnPedidoAddProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(getApplicationContext(), ListaProdActivity.class);
                startActivityForResult(i, CODIGO);
            }
        });

        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == CODIGO) {
                    extraID = data.getExtras.getInt("idProducto");
                    repositorioProducto = new ProductoRepository();
                    producto = repositorioProducto.buscarPorId(extraID);
                    extraCantidad = data.getExtras.getInt("cantidad");
                    unPedidoDetalle = new PedidoDetalle(extraCantidad, producto);
                }
            }
        }
    }
}