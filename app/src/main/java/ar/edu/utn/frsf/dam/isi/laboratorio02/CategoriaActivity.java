package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.CategoriaDAO;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.MyDatabase;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;

public class CategoriaActivity extends AppCompatActivity {
    private EditText textoCat;
    private Button btnCrear;
    private Button btnMenu;
    private Button btnVerCategorias;
    private CategoriaDAO categoriaDAO;
    private TextView tvResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categorias);

        textoCat = (EditText) findViewById(R.id.txtNombreCategoria);
        btnCrear = (Button) findViewById(R.id.btnCrearCategoria);
        btnVerCategorias = (Button) findViewById(R.id.btnVerCategorias);
        tvResultado = (TextView) findViewById(R.id.tvResultado);

        categoriaDAO = MyDatabase.getInstance(this).getCategoriaDAO();

        /*btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CategoriaRest unaCatRest = new CategoriaRest();
                Thread unHilo = new Thread() {
                    @Override
                    public void run(){
                        final Categoria unaCat = new Categoria();
                        unaCat.setNombre(textoCat.getText().toString());
                        try {
                            unaCatRest.crearCategoria(unaCat);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(CategoriaActivity.this, "La categoria ha sido creada", Toast.LENGTH_LONG).show();
                                }
                            });

                        } catch (IllegalStateException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Error: No se pudo ejecutar la operacion", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }
                };
                unHilo.start();
                textoCat.setText("");
            }
        });*/

        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Categoria unaCat = new Categoria();
                unaCat.setNombre(textoCat.getText().toString());
                Thread unHilo = new Thread() {
                    @Override
                    public void run(){
                        try {
                            categoriaDAO.insert(unaCat);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),"La categoría " + unaCat.getNombre() + " fue creada correctamente",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Error: No se pudo crear la categoría", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                };
                unHilo.start();
                textoCat.setText("");
            }
        });

        btnVerCategorias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Runnable r1 = new Runnable() {
                    @Override
                    public void run() {
                        final String resultado = consultaCategorias();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvResultado.setText(resultado);
                            }
                        });
                    }
                };
                Thread t1 = new Thread(r1);
                t1.start();
            }
        });

        btnMenu = (Button) findViewById(R.id.btnCategoriaVolver);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CategoriaActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    private String consultaCategorias(){
        List<Categoria> lista = categoriaDAO.getAll();
        final StringBuilder resultado = new StringBuilder(" === DEPARTAMENTOS ==="+ "\r\n");
        for (Categoria d : lista) {
            resultado.append(d.getId() + ": " + d.getNombre() + "\r\n");
        }
        return resultado.toString();
    }
}

