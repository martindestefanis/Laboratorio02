package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.view.View;
import android.widget.Toast;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoDetalle;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;

public class PedidoActivity extends AppCompatActivity{
    private Pedido unPedido,unPedido2;
    private PedidoDetalle unPedidoDetalle;
    private PedidoRepository repositorioPedido;
    private ProductoRepository repositorioProducto;
    private RadioButton rbRetira, rbEnviar;
    private EditText edtPedidoDireccion, edtPedidoCorreo, edtPedidoHoraEntrega;
    private ListView lstPedidoItems;
    private ArrayAdapter<PedidoDetalle> adapterLstPedidoItems;
    private Button btnPedidoAddProducto, btnPedidoHacerPedido, btnPedidoVolver;
    private int extraID, extraCantidad;
    private Producto producto;
    private Intent i;
    private static final int CODIGO = 999;
    private TextView lblTotalPedido;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nuevo_pedido);

        rbRetira = (RadioButton) findViewById(R.id.rbRetira);
        rbEnviar = (RadioButton) findViewById(R.id.rbEnviar);
        edtPedidoDireccion = (EditText) findViewById(R.id.edtPedidoDireccion);
        edtPedidoCorreo = (EditText) findViewById(R.id.edtPedidoCorreo);
        edtPedidoHoraEntrega = (EditText) findViewById(R.id.edtPedidoHoraEntrega);
        repositorioPedido = new PedidoRepository();

        Intent i1 = getIntent();
        Integer idPedido = 0;
        if(i1.getExtras()!=null) {
            idPedido = i1.getExtras().getInt("idPedidoSeleccionado");
        }
        if(idPedido>0) {
            unPedido = repositorioPedido.buscarPorId(idPedido);
            edtPedidoCorreo.setText(unPedido.getMailContacto());
            edtPedidoDireccion.setText(unPedido.getDireccionEnvio());
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            edtPedidoHoraEntrega.setText(sdf.format(unPedido.getFecha()));
            rbEnviar.setChecked(!unPedido.getRetirar());
            rbRetira.setChecked(unPedido.getRetirar());
            if(rbRetira.isChecked()) {
                edtPedidoDireccion.setEnabled(false);
            }
        } else {
            unPedido = new Pedido();
        }
        /*
        Ver lo de que cuando se clickea la notificacion se abra PedidoActivity con los datos, no anda

        Intent i2=getIntent();
        Integer idPedido1=0;
        if(i2.getExtras()!=null) {
            idPedido1 = i1.getExtras().getInt("idPed");
        }
        if(idPedido1>0){
            idPedido1 = i1.getExtras().getInt("idPed");
            unPedido2 = repositorioPedido.buscarPorId(idPedido);
            edtPedidoCorreo.setText(unPedido2.getMailContacto());
            edtPedidoDireccion.setText(unPedido2.getDireccionEnvio());
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            edtPedidoHoraEntrega.setText(sdf.format(unPedido.getFecha()));
            rbEnviar.setChecked(!unPedido2.getRetirar());
            rbRetira.setChecked(unPedido2.getRetirar());
            if(rbRetira.isChecked()) {
                edtPedidoDireccion.setEnabled(false);
            }
        } else {
            unPedido2 = new Pedido();
        }
        */

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
        lblTotalPedido = (TextView) findViewById(R.id.lblTotalPedido);

        btnPedidoAddProducto = (Button) findViewById(R.id.btnPedidoAddProducto);
        btnPedidoAddProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(getApplicationContext(), ListaProdActivity.class);
                i.putExtra("NUEVO_PEDIDO",1);
                startActivityForResult(i, CODIGO);
            }
        });

        btnPedidoHacerPedido = (Button) findViewById(R.id.btnPedidoHacerPedido);
        btnPedidoHacerPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GregorianCalendar hora = new GregorianCalendar();
                int valorHora = 0, valorMinuto = 0;
                if(edtPedidoCorreo.getText().toString().isEmpty()) {
                    Toast tstCamposVacios = Toast.makeText(getApplicationContext(), "Debe ingresar un e-mail", Toast.LENGTH_LONG);
                    tstCamposVacios.show();
                    return;
                }
                if (!rbEnviar.isChecked() && !rbRetira.isChecked()) {
                    Toast tstCamposVacios = Toast.makeText(getApplicationContext(), "Debe elegir si retira el pedido o si se lo envía", Toast.LENGTH_LONG);
                    tstCamposVacios.show();
                    return;
                }
                if (rbEnviar.isChecked()) {
                    if (edtPedidoDireccion.getText().toString().isEmpty()) {
                        Toast tstCamposVacios = Toast.makeText(getApplicationContext(), "Debe ingresar una dirección", Toast.LENGTH_LONG);
                        tstCamposVacios.show();
                        return;
                    }
                }
                if (edtPedidoHoraEntrega.getText().toString().isEmpty()){
                    Toast tstCamposVacios = Toast.makeText(getApplicationContext(), "Debe ingresar una hora de entrega", Toast.LENGTH_LONG);
                    tstCamposVacios.show();
                    return;
                }
                else {
                    String[] horaIngresada = edtPedidoHoraEntrega.getText().toString().split(":");
                    valorHora = Integer.valueOf(horaIngresada[0]);
                    valorMinuto = Integer.valueOf(horaIngresada[1]);
                    if(valorHora<0 || valorHora>23){
                        Toast.makeText(PedidoActivity.this,"La hora ingresada ("+valorHora+") es incorrecta", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(valorMinuto <0 || valorMinuto >59){
                        Toast.makeText(PedidoActivity.this, "Los minutos ("+valorMinuto+") son incorrectos", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                hora.set(Calendar.HOUR_OF_DAY, valorHora);
                hora.set(Calendar.MINUTE, valorMinuto);
                hora.set(Calendar.SECOND, Integer.valueOf(0));
                unPedido.setFecha(hora.getTime());
                unPedido.setEstado(Pedido.Estado.REALIZADO);
                unPedido.setDireccionEnvio(edtPedidoDireccion.getText().toString());
                unPedido.setMailContacto(edtPedidoCorreo.getText().toString());
                if (rbRetira.isChecked()) {
                    unPedido.setRetirar(true);
                }
                else {
                    unPedido.setRetirar(false);
                }
                repositorioPedido.guardarPedido(unPedido);
                unPedido = new Pedido();


                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        try { Thread.currentThread().sleep(10000);
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // buscar pedidos no aceptados y aceptarlos utomáticamente
                        List<Pedido> lista = repositorioPedido.getLista();
                        for(Pedido p:lista){
                            if(p.getEstado().equals(Pedido.Estado.REALIZADO)) {
                                p.setEstado(Pedido.Estado.ACEPTADO);

                                Intent intent = new Intent(getApplicationContext(),EstadoPedidoReceiver.class);
                                intent.setAction(EstadoPedidoReceiver.ESTADO_ACEPTADO);
                                intent.putExtra("idPedido",p.getId());
                                getApplicationContext().sendBroadcast(intent);
                            }

                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(PedidoActivity.this,"Informacion de pedidos actualizada!",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                };
                Thread unHilo = new Thread(r);
                unHilo.start();

                Intent intent = new Intent(PedidoActivity.this, HistorialActivity.class);
                startActivity(intent);

            }
        });
        btnPedidoVolver = (Button) findViewById(R.id.btnPedidoVolver);
        btnPedidoVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PedidoActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CODIGO) {
                extraID=data.getExtras().getInt("idProducto");
                repositorioProducto = new ProductoRepository();
                producto = repositorioProducto.buscarPorId(extraID);
                extraCantidad = data.getExtras().getInt("cantidad");
                unPedidoDetalle = new PedidoDetalle(extraCantidad, producto);
                unPedidoDetalle.setPedido(unPedido);
                adapterLstPedidoItems.notifyDataSetChanged();
                lblTotalPedido.setText("Total del pedido: $" + unPedido.total());
            }
        }
    }
}