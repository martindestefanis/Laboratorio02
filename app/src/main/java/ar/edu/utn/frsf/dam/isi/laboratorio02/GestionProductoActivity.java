package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ToggleButton;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;

public class GestionProductoActivity extends AppCompatActivity {

    private Button btnMenu;
    private Button btnGuardar;
    private Spinner comboCategorias;
    private EditText nombreProducto;
    private EditText descProducto;
    private EditText precioProducto;
    private ToggleButton opcionNuevoBusqueda;
    private EditText idProductoBuscar;
    private Button btnBuscar;
    private Button btnBorrar;
    private Boolean flagActualizacion;
    private ArrayAdapter<Categoria> comboAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_producto);

        flagActualizacion = false;
        opcionNuevoBusqueda = (ToggleButton)findViewById(R.id.abmProductoAltaNuevo);
        idProductoBuscar = (EditText)findViewById(R.id.abmProductoIdBuscar);
        nombreProducto = (EditText)findViewById(R.id.abmProductoNombre);
        descProducto = (EditText)findViewById(R.id.abmProductoDescripcion);
        precioProducto = (EditText)findViewById(R.id.abmProductoPrecio);
        comboCategorias = (Spinner)findViewById(R.id.abmProductoCategoria);
        btnMenu = (Button)findViewById(R.id.btnAbmProductoVolver);
        btnGuardar = (Button)findViewById(R.id.btnAbmProductoCrear);
        btnBuscar = (Button)findViewById(R.id.btnAbmProductoBuscar);
        btnBorrar= (Button)findViewById(R.id.btnAbmProductoBorrar);
        opcionNuevoBusqueda.setChecked(false);
        btnBuscar.setEnabled(false);
        btnBorrar.setEnabled(false);
        idProductoBuscar.setEnabled(false);
        opcionNuevoBusqueda.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                flagActualizacion = isChecked;
                btnBuscar.setEnabled(isChecked);
                btnBorrar.setEnabled(isChecked);
                idProductoBuscar.setEnabled(isChecked);
            }
        });
    }
}
