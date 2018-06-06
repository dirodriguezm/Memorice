package com.example.diego.memorice;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.HashSet;
import java.util.Set;

public class ContinuarJuegoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continuar_juego);

        ListView juegos;
        ArrayAdapter<String> adaptador;

        juegos = (ListView)findViewById(R.id.juegos);

        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.saved_games),this.MODE_PRIVATE
        );
        Set<String> defaultSet = new HashSet<>();
        Set<String> files = sharedPref.getStringSet(getString(R.string.saved_files), defaultSet);

        String[] items = files.toArray(new String[files.size()]);

        adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, items);
        juegos.setAdapter(adaptador);
    }
}
