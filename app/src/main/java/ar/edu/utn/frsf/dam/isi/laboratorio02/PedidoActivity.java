package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.MyDatabase;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoDAO;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoDetalleDAO;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoDAO;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoConDetalles;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoDetalle;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;
import ar.edu.utn.frsf.dam.isi.laboratorio02.ConfiguracionActivity;

public class PedidoActivity extends AppCompatActivity{
    private Pedido unPedido;
    private PedidoDetalle unPedidoDetalle;
    //private ProductoRepository repositorioProducto;
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
    private PedidoDAO pedidoDAO;
    private ProductoDAO productoDAO;
    private PedidoDetalleDAO pedidoDetalleDAO;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nuevo_pedido);

        rbRetira = (RadioButton) findViewById(R.id.rbRetira);
        rbEnviar = (RadioButton) findViewById(R.id.rbEnviar);
        edtPedidoDireccion = (EditText) findViewById(R.id.edtPedidoDireccion);
        edtPedidoCorreo = (EditText) findViewById(R.id.edtPedidoCorreo);
        edtPedidoHoraEntrega = (EditText) findViewById(R.id.edtPedidoHoraEntrega);
        btnPedidoAddProducto = (Button) findViewById(R.id.btnPedidoAddProducto);
        btnPedidoHacerPedido = (Button) findViewById(R.id.btnPedidoHacerPedido);
        lstPedidoItems = (ListView) findViewById(R.id.lstPedidoItems);
        //repositorioPedido = new PedidoRepository();

        pedidoDAO = MyDatabase.getInstance(this).getPedidoDAO();
        productoDAO = MyDatabase.getInstance(this).getProductoDAO();
        pedidoDetalleDAO = MyDatabase.getInstance(this).getPedidoDetalleDAO();

        Intent i1 = getIntent();
        Integer idPedido = 0;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String Email = prefs.getString("edtPreference1","Email");
        Boolean retira = prefs.getBoolean("cbPreference1",false);
        if(!Email.equals("Email")){
            edtPedidoCorreo.setText(Email);
        }
        if(retira.equals(true)){
            rbRetira.setChecked(true);
        }

        if(i1.getExtras()!=null) {
            idPedido = i1.getExtras().getInt("idPedidoSeleccionado");
        }
        if(idPedido>0) {
            final Integer finalIdPedido = idPedido;
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    final PedidoConDetalles unPedidoConDetalles = pedidoDAO.buscarPorIDConDetalle(finalIdPedido);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            edtPedidoCorreo.setText(unPedidoConDetalles.pedido.getMailContacto());
                            edtPedidoDireccion.setText(unPedidoConDetalles.pedido.getDireccionEnvio());
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                            edtPedidoHoraEntrega.setText(sdf.format(unPedidoConDetalles.pedido.getFecha()));
                            rbEnviar.setChecked(!unPedidoConDetalles.pedido.getRetirar());
                            rbRetira.setChecked(unPedidoConDetalles.pedido.getRetirar());
                            if(rbRetira.isChecked()) {
                                edtPedidoDireccion.setEnabled(false);
                            }
                            adapterLstPedidoItems = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_single_choice, unPedidoConDetalles.detalle);
                            lstPedidoItems.setAdapter(adapterLstPedidoItems);

                            edtPedidoCorreo.setEnabled(false);
                            edtPedidoDireccion.setEnabled(false);
                            edtPedidoHoraEntrega.setEnabled(false);
                            rbEnviar.setEnabled(false);
                            rbRetira.setEnabled(false);
                            btnPedidoAddProducto.setEnabled(false);
                            btnPedidoHacerPedido.setEnabled(false);
                            lstPedidoItems.setChoiceMode(ListView.CHOICE_MODE_NONE);
                            //lstPedidoItems.setEnabled(false);
                        }
                    });
                }
            };
            Thread t = new Thread(r);
            t.start();
        } else {
            unPedido = new Pedido();
            adapterLstPedidoItems = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_single_choice, unPedido.getDetalle());
            lstPedidoItems.setAdapter(adapterLstPedidoItems);
        }


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

        lblTotalPedido = (TextView) findViewById(R.id.lblTotalPedido);

        btnPedidoAddProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(getApplicationContext(), ListaProdActivity.class);
                i.putExtra("NUEVO_PEDIDO",1);
                startActivityForResult(i, CODIGO);
            }
        });

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
                //repositorioPedido.guardarPedido(unPedido);
                //unPedido = new Pedido();

                unPedidoDetalle.setPedido(unPedido);

                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        pedidoDetalleDAO.insert(unPedidoDetalle);
                        pedidoDAO.insert(unPedido);
                        try { Thread.currentThread().sleep(5000);
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        List<Pedido> lista = pedidoDAO.getAll();
                        for(Pedido p:lista){
                            if(p.getEstado().equals(Pedido.Estado.REALIZADO)) {
                                p.setEstado(Pedido.Estado.ACEPTADO);
                                pedidoDAO.update(p);
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
                        Intent intent = new Intent(PedidoActivity.this, HistorialActivity.class);
                        startActivity(intent);
                    }
                };
                Thread unHilo = new Thread(r);
                unHilo.start();
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CODIGO) {
                extraID = data.getExtras().getInt("idProducto");
                //repositorioProducto = new ProductoRepository();
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        producto = productoDAO.buscarPorID(extraID);
                        extraCantidad = data.getExtras().getInt("cantidad");
                        unPedidoDetalle = new PedidoDetalle(extraCantidad, producto);
                        unPedidoDetalle.setPedido(unPedido);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapterLstPedidoItems.notifyDataSetChanged();
                                lblTotalPedido.setText("Total del pedido: $" + unPedido.total().toString());
                            }
                        });
                    }
                };
                Thread t = new Thread(r);
                t.start();
            }
        }
    }
}