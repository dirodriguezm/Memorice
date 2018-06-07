package com.example.diego.memorice;


import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NuevoJuegoActivity extends AppCompatActivity implements View.OnClickListener{
    TextView textView;
    private Button[] buttonsTab = new Button[3];
    private Button buttonUnfocusTab;
    private int[] buttonsIdsTab = {R.id.button_tamaño1, R.id.button_tamaño2, R.id.button_tamaño3};

    private Button[] buttonsDif = new Button[3];
    private Button buttonUnfocusDif;
    private int[] buttonsIdsDif = {R.id.button_dificultad1, R.id.button_dificultad2, R.id.button_dificultad3};

    private Button comenzar;

    private int col = 4, time = 1;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_juego);

        intent = new Intent(this, CreditsActivity.class);

        for(int i = 0; i < buttonsTab.length; i++){
            buttonsTab[i] = (Button) findViewById(buttonsIdsTab[i]);
            buttonsTab[i].setBackgroundColor(Color.rgb(207, 207, 207));
            buttonsTab[i].setOnClickListener(this);
        }

        buttonUnfocusTab = buttonsTab[0];

        for(int i = 0; i < buttonsDif.length; i++){
            buttonsDif[i] = (Button) findViewById(buttonsIdsDif[i]);
            buttonsDif[i].setBackgroundColor(Color.rgb(207, 207, 207));
            buttonsDif[i].setOnClickListener(this);
        }

        buttonUnfocusDif = buttonsDif[0];

        setFocusDif(buttonUnfocusDif, buttonsDif[0]);
        setFocusTab(buttonUnfocusTab, buttonsTab[0]);

        comenzar = (Button) findViewById(R.id.button_comenzar);
        comenzar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //setForcus(btn_unfocus, (Button) findViewById(v.getId()));
        //Or use switch
        switch (v.getId()){
            case R.id.button_dificultad1 :
                time = 1;
                setFocusDif(buttonUnfocusDif, buttonsDif[0]);
                break;

            case R.id.button_dificultad2 :
                time = 2;
                setFocusDif(buttonUnfocusDif, buttonsDif[1]);
                break;

            case R.id.button_dificultad3 :
                time = 3;
                setFocusDif(buttonUnfocusDif, buttonsDif[2]);
                break;
            case R.id.button_tamaño1 :
                col = 4;
                setFocusTab(buttonUnfocusTab, buttonsTab[0]);
                break;

            case R.id.button_tamaño2 :
                col = 5;
                setFocusTab(buttonUnfocusTab, buttonsTab[1]);
                break;

            case R.id.button_tamaño3 :
                col = 6;
                setFocusTab(buttonUnfocusTab, buttonsTab[2]);
                break;
            case R.id.button_comenzar :
                intent.putExtra("col", col);
                intent.putExtra("tiempo", time);
                startActivity(intent);
                break;

        }
    }

    private void setFocusDif(Button btn_unfocus, Button btn_focus){
        btn_unfocus.setTextColor(Color.rgb(49, 50, 51));
        btn_unfocus.setBackgroundColor(Color.rgb(207, 207, 207));
        btn_focus.setTextColor(Color.rgb(255, 255, 255));
        btn_focus.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        this.buttonUnfocusDif = btn_focus;
    }

    private void setFocusTab(Button btn_unfocus, Button btn_focus){
        btn_unfocus.setTextColor(Color.rgb(49, 50, 51));
        btn_unfocus.setBackgroundColor(Color.rgb(207, 207, 207));
        btn_focus.setTextColor(Color.rgb(255, 255, 255));
        btn_focus.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        this.buttonUnfocusTab = btn_focus;
    }
}