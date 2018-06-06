package com.example.diego.memorice;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NuevoJuegoActivity extends AppCompatActivity {
    android.app.ActionBar actionBar;
    TextView textView;
    ActionBar.LayoutParams layoutparams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_juego);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);


        final Intent intent = new Intent(this, CreditsActivity.class);

        final RadioGroup tableros = (RadioGroup) findViewById(R.id.tableros);
        final RadioGroup tiempos = (RadioGroup) findViewById(R.id.tiempos);

        Button siguiente = (Button) findViewById(R.id.button_siguiente);
        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checked_tableros = tableros.getCheckedRadioButtonId();
                int checked_tiempos = tiempos.getCheckedRadioButtonId();
                int col = 4, time = 1;
                switch (checked_tableros){
                    case R.id.cuatroXseis:
                        col = 4;
                        break;
                    case R.id.cincoXseis:
                        col = 5;
                        break;
                    case R.id.seisXseis:
                        col = 6;
                        break;
                }
                switch (checked_tiempos){
                    case R.id.unMin:
                        time = 1;
                        break;
                    case R.id.dosMin:
                        time = 2;
                        break;
                    case R.id.tresMin:
                        time = 3;
                        break;
                }
                intent.putExtra("col", col);
                intent.putExtra("tiempo", time);

                Context context = getApplicationContext();
                CharSequence text = col+" "+time+" ";
                int duration = Toast.LENGTH_SHORT;

                startActivity(intent);

            }
        });
    }
} 