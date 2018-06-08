package com.example.diego.memorice;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class FinJuegoActivity extends AppCompatActivity {
    private TextView tiempoRes, paresRes, puntajeRes, credits;
    private Button volver;
    private Intent main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fin_juego);

        Intent intent = getIntent();

        String tiempoText = intent.getStringExtra("tiempo");
        String paresText = intent.getStringExtra("pares");
        String puntajeText = intent.getStringExtra("puntaje");

        tiempoRes = (TextView) findViewById(R.id.tiempo_res);
        tiempoRes.setText(tiempoText);

        paresRes = (TextView) findViewById(R.id.pares_res);
        paresRes.setText(paresText);

        puntajeRes = (TextView) findViewById(R.id.puntaje_res);
        puntajeRes.setText(puntajeText);

        volver = (Button) findViewById(R.id.button_volver);

        main = new Intent(this, MainActivity.class);

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(main);
            }
        });

        credits = (TextView) findViewById(R.id.creditos);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.credits);
        credits.setText("Creada por: \nMatías Medina \nY \nDiego Rodriguez");
        credits.startAnimation(animation);

    }
}
