package com.example.diego.memorice;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class TableroActivity extends AppCompatActivity  {
    private int board_size, ancho, alto, remaining;
    private ArrayList<Card> cards, pressed_cards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablero);
        boolean continuar = false;
        if(continuar){
            String date = "";
            SharedPreferences sp = getSharedPreferences(date, Context.MODE_PRIVATE);
            this.ancho = sp.getInt("ancho",0);
            this.alto = sp.getInt("alto",0);
            this.board_size = ancho * alto;
            this.cards = createContinuedCards(sp.getStringSet("resources", new HashSet<String>()),
                    sp.getStringSet("pressed",new HashSet<String>()),
                    sp.getStringSet("completed",new HashSet<String>()));
            this.pressed_cards = createContinuedPressedCards(cards);
        }
        else {
            this.ancho = 4;
            this.alto = 6;
            this.board_size = ancho * alto;
            this.cards = createCardsArray(board_size);
            this.pressed_cards = new ArrayList<>();
            Random random = new Random();
            for (int i = 0; i < board_size; i++) {
                int r = random.nextInt(board_size);
                Card tmp = cards.get(i);
                cards.set(i, cards.get(r));
                cards.set(r, tmp);
            }
        }
        this.remaining = this.board_size / 2;
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        GridView gridview = findViewById(R.id.gridview);
        gridview.setNumColumns(ancho);
        TableroAdapter adapter = new TableroAdapter(this, cards);
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Card card = cards.get(position);
                if(pressed_cards.size() < 2){
                    card.press();
                    if (card.isPressed()) {
                        pressed_cards.add(card);
                        printPressed();
                    } else {
                        pressed_cards = new ArrayList<>();
                    }
                }
                if (pressed_cards.size() == 2) {

                    if (pressed_cards.get(0).getResource() == pressed_cards.get(1).getResource()) {
                        new CountDownTimer(1000,1){
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }
                            @Override
                            public void onFinish() {
                                if(pressed_cards.size() == 2) {
                                    pressed_cards.get(0).complete();
                                    pressed_cards.get(1).complete();
                                    pressed_cards = new ArrayList<>();
                                    remaining--;
                                }
                                if(remaining == 0){
                                    Intent intent = new Intent(TableroActivity.this,MainActivity.class);
                                    startActivity(intent);
                                }
                            }
                        }.start();
                    } else {
                        new CountDownTimer(1000,1){
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }
                            @Override
                            public void onFinish() {
                                for(Card card: pressed_cards){
                                    card.press();
                                }
                                pressed_cards = new ArrayList<>();

                            }
                        }.start();
                    }
                }
            }
        });

    }



    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // Shows the system bars by removing all the flags
// except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    Integer[] createResourcesArray(){
        Integer[] resources = {
                R.drawable.adn, R.drawable.adn,
                R.drawable.atomo, R.drawable.atomo,
                R.drawable.calc, R.drawable.calc,
                R.drawable.estad, R.drawable.estad,
                R.drawable.globo, R.drawable.globo,
                R.drawable.grafico, R.drawable.grafico,
                R.drawable.grafo, R.drawable.grafo,
                R.drawable.iman,R.drawable.iman,
                R.drawable.lupa, R.drawable.lupa,
                R.drawable.matraz, R.drawable.matraz,
                R.drawable.medicina, R.drawable.medicina,
                R.drawable.micro,R.drawable.micro,
                R.drawable.molecula, R.drawable.molecula,
                R.drawable.petri,R.drawable.petri,
                R.drawable.satelite, R.drawable.satelite,
                R.drawable.sistema,R.drawable.sistema,
                R.drawable.tele, R.drawable.tele,
                R.drawable.tubo, R.drawable.tubo
        };
        return resources;
    }

    ArrayList<Card> createCardsArray(int board_size){
        ArrayList<Card> cards = new ArrayList<>();
        Integer[] resources = createResourcesArray();
        resources = Arrays.copyOfRange(resources,0,board_size);
        for(int i = 0; i < resources.length; i++){
            Card card = new Card(this,resources[i]);
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int height = metrics.heightPixels;
            int width = metrics.widthPixels;
            card.setLayoutParams(new ViewGroup.LayoutParams((width/ancho) -16  , (height/alto) -16 ));
            card.setScaleType(ImageButton.ScaleType.CENTER_CROP);
            card.setPadding(8, 8, 8, 8);
            card.setImageResource(R.drawable.blank);
            card.setFocusable(false);
            card.setFocusableInTouchMode(false);
            cards.add(card);
        }
        return cards;
    }

    ArrayList<Card> createContinuedCards(Set<String> resources, Set<String> pressed, Set<String> completed){
        ArrayList<Card> cards = new ArrayList<>();
        Object[] r = resources.toArray();
        Object[] p = pressed.toArray();
        Object[] c = completed.toArray();
        int i = 0;
        for (String resource: resources) {
            Card card = new Card(this, Integer.parseInt(resource));
            if( p[i].equals("true")){
                card.press();
            }
            if( c[i].equals("true")){
                card.complete();
            }
        }
        return cards;
    }

    ArrayList<Card> createContinuedPressedCards(ArrayList<Card> cards){
        ArrayList<Card> pressedCards = new ArrayList<>();
        for (Card card : cards){
            if(card.isPressed()){
                pressedCards.add(card);
            }
        }
        return pressedCards;
    }

    void printPressed(){
        String array = "";
        for(Card card: pressed_cards){
            array += card.getResource() + "\t";
        }
        Log.i("card", array + " size : " + pressed_cards.size());
    }

    Set<String> getCardResources(){
        Set<String> set = new HashSet<>();
        for(Card card: this.cards){
            set.add("" + card.getResource());
        }
        return set;
    }

    Set<String> getPressedCards(){
        Set<String> set = new HashSet<>();
        for(Card card: this.cards){
            set.add("" + card.isPressed());
        }
        return set;
    }

    Set<String> getCompletedCards(){
        Set<String> set = new HashSet<>();
        for(Card card: this.cards){
            set.add("" + card.isCompleted());
        }
        return set;
    }


    void saveState(ArrayList<Card> cards){

        SharedPreferences sp = this.getSharedPreferences("Juegos", Context.MODE_PRIVATE);
        Set<String> def = new HashSet<>();
        Set<String> archivos = sp.getStringSet("archivos", def);
        Date date = new Date();
        archivos.add(""+date);
        SharedPreferences.Editor editor = sp.edit();
        editor.putStringSet("archivos",archivos);
        editor.commit();
        sp = this.getSharedPreferences("" + date, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putStringSet("resources",getCardResources());
        editor.putStringSet("pressed",getPressedCards());
        editor.putStringSet("completed",getCompletedCards());
        editor.putInt("alto",this.alto);
        editor.putInt("ancho",this.ancho);
        editor.commit();

    }


}
