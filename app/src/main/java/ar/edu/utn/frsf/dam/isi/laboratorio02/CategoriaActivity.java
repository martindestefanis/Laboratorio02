package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;

public class CategoriaActivity extends AppCompatActivity {
    private EditText textoCat;
    private Button btnCrear;
    private Button btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categorias);
        textoCat = (EditText) findViewById(R.id.txtNombreCategoria);
        btnCrear = (Button) findViewById(R.id.btnCrearCategoria);
        btnCrear.setOnClickListener(new View.OnClickListener() {
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
                            textoCat.setText("");
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
            }
        });
        btnMenu = (Button) findViewById(R.id.btnCategoriaVolver);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CategoriaActivity.this,
                        MainActivity.class);
                startActivity(i);
            }
        });
    }
}

