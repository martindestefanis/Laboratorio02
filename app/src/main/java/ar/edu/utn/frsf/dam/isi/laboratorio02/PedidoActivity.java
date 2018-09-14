package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;

public class PedidoActivity extends AppCompatActivity {
private Spinner spinner;
private ArrayAdapter<Categoria> adapterCategoria;
private ProductoRepository productoRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_productos);
        productoRepository = new ProductoRepository();
        spinner = (Spinner) findViewById(R.id.spinner);
        //PROBAR LA LINEA DE ABAJO SINO AGREGARLE LA OTRA CON EL DROPDOWN
        adapterCategoria = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,productoRepository.getCategorias());
        spinner.setAdapter(adapterCategoria);
    }
}
