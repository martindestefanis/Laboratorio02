package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class HistorialActivity extends AppCompatActivity{

    private Button btnHistorialNuevo, btnhistorialMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historial_pedidos);

        /* ACA VA LO DEL LIST VIEW*/
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
