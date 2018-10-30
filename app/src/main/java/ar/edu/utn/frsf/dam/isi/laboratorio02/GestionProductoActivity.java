package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.concurrent.ExecutionException;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRetrofit;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        idProductoBuscar = (EditText)findViewById(R.id.abmProductoIdBuscar);
        nombreProducto = (EditText)findViewById(R.id.abmProductoNombre);
        descProducto = (EditText)findViewById(R.id.abmProductoDescripcion);
        precioProducto = (EditText)findViewById(R.id.abmProductoPrecio);
        comboCategorias = (Spinner)findViewById(R.id.abmProductoCategoria);
        btnMenu = (Button)findViewById(R.id.btnAbmProductoVolver);
        btnGuardar = (Button)findViewById(R.id.btnAbmProductoCrear);
        btnBuscar = (Button)findViewById(R.id.btnAbmProductoBuscar);
        btnBorrar= (Button)findViewById(R.id.btnAbmProductoBorrar);

        btnBuscar.setEnabled(false);
        btnBorrar.setEnabled(false);
        idProductoBuscar.setEnabled(false);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                CategoriaRest catRest = new CategoriaRest();
                final Categoria[] cats = catRest.listarTodas().toArray(new Categoria[0]);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        comboAdapter = new ArrayAdapter<Categoria>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, cats);
                        comboCategorias.setAdapter(comboAdapter);
                        comboCategorias.setSelection(0);
                    }
                });
            }
        };
        Thread hiloCargarCombo = new Thread(r);
        hiloCargarCombo.start();

        opcionNuevoBusqueda = new ToggleButton(this);
        opcionNuevoBusqueda.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        opcionNuevoBusqueda.setTextOff("Crear Nuevo Producto");
        opcionNuevoBusqueda.setTextOn("Buscar por ID y Actualizar");
        opcionNuevoBusqueda.setChecked(false);
        opcionNuevoBusqueda.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                flagActualizacion = isChecked;
                btnBuscar.setEnabled(isChecked);
                btnBorrar.setEnabled(isChecked);
                idProductoBuscar.setEnabled(isChecked);
            }
        });

        LinearLayout layoutToggle = (LinearLayout)findViewById(R.id.lyToggleButton);
        layoutToggle.addView(opcionNuevoBusqueda);


        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idProductoBuscar.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Debe ingresar un ID", Toast.LENGTH_LONG);
                }
                int id = Integer.parseInt(idProductoBuscar.getText().toString());
                ProductoRetrofit clienteRest =
                        RestClient.getInstance()
                                .getRetrofit()
                                .create(ProductoRetrofit.class);
                Call<Producto> altaCall= clienteRest.buscarProductoPorId(id);
                altaCall.enqueue(new Callback<Producto>() {
                    @Override
                    public void onResponse(Call<Producto> call, Response<Producto> resp) {
                        // procesar la respuesta
                        if(resp.code()==200 || resp.code()==201){
                            Producto p;
                            p = resp.body();
                            nombreProducto.setText(p.getNombre());
                            descProducto.setText(p.getDescripcion());
                            precioProducto.setText(Double.toString(p.getPrecio()));
                            comboCategorias.setSelection(p.getCategoria().getId()-1);
                        }
                        else {
                            Toast.makeText(GestionProductoActivity.this, "Error al buscar un producto. Código de error: " + resp.code(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Producto> call, Throwable t) {
                    }
                });
            }
        });

        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = Integer.parseInt(idProductoBuscar.getText().toString());
                ProductoRetrofit clienteRest =
                        RestClient.getInstance()
                                .getRetrofit()
                                .create(ProductoRetrofit.class);
                Call<Producto> altaCall= clienteRest.borrar(id);
                altaCall.enqueue(new Callback<Producto>() {
                    @Override
                    public void onResponse(Call<Producto> call, Response<Producto> resp) {
                        // procesar la respuesta
                        switch (resp.code()) {
                            case 200:
                                nombreProducto.setText("");
                                descProducto.setText("");
                                precioProducto.setText("");
                                comboCategorias.setSelection(-1);
                                Toast.makeText(GestionProductoActivity.this, "Producto borrado correctamente", Toast.LENGTH_SHORT).show();
                                break;
                            case 201:
                                Toast.makeText(GestionProductoActivity.this, "Producto borrado correctamente", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(GestionProductoActivity.this, "Error al actualizar producto. Código de error: " + resp.code(), Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                    @Override
                    public void onFailure(Call<Producto> call, Throwable t) {
                    }
                });
            }
        });

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GestionProductoActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nombreProducto.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Debe ingresar un nombre", Toast.LENGTH_LONG);
                    return;
                }
                if (descProducto.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Debe ingresar una descripción", Toast.LENGTH_LONG);
                    return;
                }
                if (precioProducto.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Debe ingresar un precio", Toast.LENGTH_LONG);
                    return;
                }
                if (!comboCategorias.isSelected()){
                    Toast.makeText(getApplicationContext(), "Debe seleccionar una categoría", Toast.LENGTH_LONG);
                    return;
                }
                if(flagActualizacion) {
                    int id = Integer.parseInt(idProductoBuscar.getText().toString());
                    Double precio = Double.parseDouble(precioProducto.getText().toString());
                    Producto p = new Producto(nombreProducto.getText().toString(), descProducto.getText().toString(), precio, (Categoria) comboCategorias.getSelectedItem());
                    ProductoRetrofit clienteRest =
                            RestClient.getInstance()
                                    .getRetrofit()
                                    .create(ProductoRetrofit.class);
                    Call<Producto> altaCall= clienteRest.actualizarProducto(id, p);
                    altaCall.enqueue(new Callback<Producto>() {
                        @Override
                        public void onResponse(Call<Producto> call, Response<Producto> resp) {
                            // procesar la respuesta
                            switch (resp.code()) {
                                case 200:
                                    Toast.makeText(GestionProductoActivity.this, "Producto actualizado correctamente", Toast.LENGTH_SHORT).show();
                                    break;
                                case 201:
                                    Toast.makeText(GestionProductoActivity.this, "Producto actualizado correctamente", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(GestionProductoActivity.this, "Error al actualizar producto. Código de error: " + resp.code(), Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }

                        @Override
                        public void onFailure(Call<Producto> call, Throwable t) {
                        }
                    });
                }
                else {
                    Double precio = Double.parseDouble(precioProducto.getText().toString());
                    Producto p = new Producto(nombreProducto.getText().toString(), descProducto.getText().toString(), precio, (Categoria) comboCategorias.getSelectedItem());
                    ProductoRetrofit clienteRest =
                            RestClient.getInstance()
                                    .getRetrofit()
                                    .create(ProductoRetrofit.class);
                    Call<Producto> altaCall = clienteRest.crearProducto(p);
                    altaCall.enqueue(new Callback<Producto>() {
                        @Override
                        public void onResponse(Call<Producto> call, Response<Producto> resp) {
                            // procesar la respuesta
                            switch (resp.code()) {
                                case 200:
                                    Toast.makeText(GestionProductoActivity.this, "Producto creado correctamente", Toast.LENGTH_SHORT).show();
                                    break;
                                case 201:
                                    Toast.makeText(GestionProductoActivity.this, "Producto creado correctamente", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(GestionProductoActivity.this, "Error al crear producto. Código de error: " + resp.code(), Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }

                        @Override
                        public void onFailure(Call<Producto> call, Throwable t) {
                        }
                    });
                }
            }
        });
    }
}
