package com.example.diego.memorice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

import javax.net.ssl.SNIHostName;

public class ContinuarJuegoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continuar_juego);

        ListView juegos;
        ArrayAdapter<String> adaptador;

        juegos = (ListView) findViewById(R.id.juegos);

        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.saved_games), this.MODE_PRIVATE
        );
        Set<String> defaultSet = new HashSet<>();
        Set<String> files = sharedPref.getStringSet(getString(R.string.saved_files), defaultSet);

        final String[] items = files.toArray(new String[files.size()]);

        adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        juegos.setAdapter(adaptador);
        juegos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent intent = new Intent(ContinuarJuegoActivity.this, TableroActivity.class);
                intent.putExtra("date",items[position]);   // le paso el nombre del archivo de fecha
                intent.putExtra("continuar",true);  //le paso continuar al tablero activity para que sepa como inicializar
                startActivity(intent);
            }
        });
    }
}
