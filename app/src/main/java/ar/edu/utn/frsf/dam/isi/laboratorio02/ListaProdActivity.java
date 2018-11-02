package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.CategoriaDAO;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.MyDatabase;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoDAO;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;

public class ListaProdActivity extends AppCompatActivity {
    private Spinner spinner;
    private ArrayAdapter<Categoria> adapterCategoria;
    private ProductoRepository productoRepository;
    private ListView lstProductos;
    private ArrayAdapter<Producto> adapterLstProductos;
    private Categoria cat;
    private EditText edtProdCantidad;
    private Button btnProdAddPedido;
    private Producto producto;
    private CategoriaDAO categoriaDAO;
    private ProductoDAO productoDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_productos);

        productoRepository = new ProductoRepository();
        spinner = (Spinner) findViewById(R.id.spinner);
        lstProductos = (ListView) findViewById(R.id.lstProductos);
        edtProdCantidad = (EditText) findViewById(R.id.edtProdCantidad);
        btnProdAddPedido = (Button) findViewById(R.id.btnProdAddPedido);

        categoriaDAO = MyDatabase.getInstance(this).getCategoriaDAO();
        productoDAO = MyDatabase.getInstance(this).getProductoDAO();

      /*  adapterCategoria = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, productoRepository.getCategorias());
        spinner.setAdapter(adapterCategoria);
        Bundle extras=getIntent().getExtras();
        if(extras!=null) {
            int valor=extras.getInt("NUEVO_PEDIDO");
            if(valor==1) {
                edtProdCantidad.setEnabled(true);
                btnProdAddPedido.setEnabled(true);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        lstProductos.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
                        cat = (Categoria) parent.getItemAtPosition(position);
                        adapterLstProductos = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_single_choice, productoRepository.buscarPorCategoria(cat));
                        lstProductos.setAdapter(adapterLstProductos);
                        lstProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                producto = (Producto) parent.getItemAtPosition(position);
                            }
                        });
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) { }
                });
            }
        }
        else {
            edtProdCantidad.setEnabled(false);
            btnProdAddPedido.setEnabled(false);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    cat = (Categoria) parent.getItemAtPosition(position);
                    adapterLstProductos = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, productoRepository.buscarPorCategoria(cat));
                    lstProductos.setAdapter(adapterLstProductos);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) { }
            });
        }*/

        Bundle extras=getIntent().getExtras();
        if(extras==null) {
            edtProdCantidad.setEnabled(false);
            btnProdAddPedido.setEnabled(false);
        }
        else{
            int valor = extras.getInt("NUEVO_PEDIDO");
            if (valor == 1) {
                lstProductos.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
            }
        }

        Runnable r = new Runnable() {
            @Override
            public void run(){
                final List<Categoria> cats = categoriaDAO.getAll();
                runOnUiThread(new Runnable() {
                    //CategoriaRest catRest = new CategoriaRest();
                    //Categoria[] cats = catRest.listarTodas().toArray(new Categoria[0]);
                    @Override
                    public void run() {
                        adapterCategoria = new ArrayAdapter<Categoria>(ListaProdActivity.this, android.R.layout.simple_spinner_dropdown_item, cats);
                        spinner.setAdapter(adapterCategoria);
                        spinner.setSelection(0);
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                final Categoria c = (Categoria)parent.getItemAtPosition(position);
                                //adapterLstProductos.clear();
                                Runnable r = new Runnable() {
                                    @Override
                                    public void run() {
                                        //adapterLstProductos.addAll(productoDAO.buscarPorCategoria(c.getId()));
                                        //adapterLstProductos.notifyDataSetChanged();
                                        final List<Producto> p = productoDAO.buscarPorCategoria(c.getId());
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                adapterLstProductos = new ArrayAdapter<>(ListaProdActivity.this, android.R.layout.simple_list_item_single_choice, p);
                                                lstProductos.setAdapter(adapterLstProductos);
                                            }
                                        });
                                    }
                                };
                                Thread t = new Thread(r);
                                t.start();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {}
                        });
                    }
                });
            }
        };

        Thread hiloCargarCombo = new Thread (r);
        hiloCargarCombo.start();

        lstProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                producto = (Producto) parent.getItemAtPosition(position);
            }
        });

        btnProdAddPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PedidoActivity.class);
                i.putExtra("cantidad", Integer.valueOf(edtProdCantidad.getText().toString()));
                i.putExtra("idProducto",producto.getId());
                setResult(Activity.RESULT_OK,i);
                finish();
            }
        });
    }
}