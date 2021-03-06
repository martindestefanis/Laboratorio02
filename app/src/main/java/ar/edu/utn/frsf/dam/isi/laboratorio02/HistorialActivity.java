package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.MyDatabase;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoDAO;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoConDetalles;

public class HistorialActivity extends AppCompatActivity{

    private Button btnHistorialNuevo, btnhistorialMenu;
    private ListView lstHistorialPedidos;
    PedidoAdapter pedidoAdapter;
    PedidoDAO pedidoDAO;
    List<PedidoConDetalles> pedidosConDetalles;
    List<Pedido> pedidos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historial_pedidos);

        lstHistorialPedidos = (ListView) findViewById(R.id.lstHistorialPedidos);
        //PedidoRepository pedidoRepository = new PedidoRepository();
        //pedidoAdapter = new PedidoAdapter(HistorialActivity.this,pedidoRepository.getLista());
        pedidoDAO = MyDatabase.getInstance(this).getPedidoDAO();

        Runnable r = new Runnable() {
            @Override
            public void run() {
                pedidosConDetalles = pedidoDAO.buscarPedidosConDetalles();
                for(PedidoConDetalles pedConDet : pedidosConDetalles){
                    Pedido p = pedConDet.pedido;
                    p.setDetalle(pedConDet.detalle);
                    pedidos.add(p);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pedidoAdapter = new PedidoAdapter(HistorialActivity.this, pedidos);
                        lstHistorialPedidos.setAdapter(pedidoAdapter);
                    }
                });
            }
        };
        Thread t = new Thread(r);
        t.start();

        btnHistorialNuevo = (Button) findViewById(R.id.btnHistorialNuevo);
        btnhistorialMenu = (Button) findViewById(R.id.btnhistorialMenu);
        btnHistorialNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HistorialActivity.this, PedidoActivity.class);
                startActivity(i);
            }
        });
        btnhistorialMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HistorialActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
}