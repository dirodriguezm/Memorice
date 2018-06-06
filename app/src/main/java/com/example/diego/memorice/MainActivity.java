package com.example.diego.memorice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArraySet;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button nuevo = (Button) findViewById(R.id.nuevo);
        Button continuar = (Button) findViewById(R.id.continuar);

        final Intent intentNuevo = new Intent(this, NuevoJuegoActivity.class);
        final Intent intentContinuar = new Intent(this, ContinuarJuegoActivity.class);

        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.saved_games),this.MODE_PRIVATE
        );

        Set<String> defaultSet = new HashSet<>();

        Set<String> files = sharedPref.getStringSet(getString(R.string.saved_files), defaultSet);

        if(files.size() == defaultSet.size()){
            continuar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = getApplicationContext();
                    CharSequence text = "No hay juegos guardados";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            });
        }
        else{
            continuar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(intentContinuar);
                }
            });
        }

        nuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentNuevo);
            }
        });







    }
}
