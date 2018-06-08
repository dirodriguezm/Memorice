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
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class TableroActivity extends AppCompatActivity  {
    private int board_size, ancho, alto, remaining;
    private ArrayList<Card> cards, pressed_cards;
    private TextView timer;
    private int seconds;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablero);

        timer = (TextView) findViewById(R.id.tiempo);

        Intent intent = getIntent();
        this.date = ""+new Date();
        boolean continuar = intent.getBooleanExtra("continuar",false); //recibo continuar, si es falso inicializa de 0



        if(continuar){ // si venimos de la ContinuarJuegoActivity
            this.date = intent.getStringExtra("date"); //recibo la fecha
            SharedPreferences sp = getSharedPreferences(this.date, Context.MODE_PRIVATE);
            this.ancho = sp.getInt("ancho",0); //obtengo el ancho del juego guardado
            this.alto = sp.getInt("alto",0); // obtengo el alto del juego guardado este no importa porque abajo se pone siempre como 6
            String text = sp.getString("tiempo", ""); // obtengo el tiempo en que fue guardado
            this.timer.setText(text); // le pongo el tiempo al timer pero aun no está el codigo que inicia el contador
            this.board_size = ancho * alto;
            //a partir de los datos guardados inicializo las cartas en el orden que estaban y si estaban presionadas o completadas
            this.cards = createContinuedCards(sp.getString("resources",""),
                    sp.getString("pressed",""),
                    sp.getString("completed",""));

            this.pressed_cards = createContinuedPressedCards(cards); // lo mismo pero guardo las que estan presionadas en arreglo
            Log.i("CONTINUE",""+this.pressed_cards.size()); //datos obtenidos bien
        }
        else { // cuando venimos de NuevoJuegoActivity
            this.ancho = intent.getIntExtra("col", 4); // obtenemos el ancho del tablero
            int tiempo = intent.getIntExtra("tiempo", 1);
            this.alto = 6;
            this.board_size = ancho * alto;
            int millis = 1000 * 60 * tiempo;

            new CountDownTimer(millis, 1000) {

                public void onTick(long millisUntilFinished) {
                    seconds = (int) (millisUntilFinished / 1000);
                    String secondsStr = secondsToString(seconds);
                    timer.setText(secondsStr);
                }

                public void onFinish() {
                    Intent intent = new Intent(TableroActivity.this,FinJuegoActivity.class);
                    String tiempo = (String) timer.getText();
                    int pares = ((board_size / 2) - remaining);
                    int puntaje = seconds * 10 + pares * 20; // bacan jaja

                    intent.putExtra("tiempo", tiempo);
                    intent.putExtra("pares", String.valueOf(pares));
                    intent.putExtra("puntaje", String.valueOf(puntaje));

                    startActivity(intent);
                }
            }.start();

            this.cards = createCardsArray(board_size); //inicializamos las cartas
            this.pressed_cards = new ArrayList<>();
            //este ciclo for es para ordenar las cartas aleatoriamente
            Random random = new Random();
            for (int i = 0; i < board_size; i++) {
                int r = random.nextInt(board_size);
                Card tmp = cards.get(i);
                cards.set(i, cards.get(r));
                cards.set(r, tmp);
            }
        }
        this.remaining = this.board_size / 2;
        GridView gridview = findViewById(R.id.gridview);
        gridview.setNumColumns(ancho);
        TableroAdapter adapter = new TableroAdapter(this, this.cards);
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
                if (pressed_cards.size() == 2) { //si se dio vuelta un par

                    if (pressed_cards.get(0).getResource() == pressed_cards.get(1).getResource()) { //si son iguales
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
                                    saveState(date); // cada vez que se encuentra un par se llama a saveState
                                }
                                if(remaining == 0){
                                    Intent intent = new Intent(TableroActivity.this,FinJuegoActivity.class);
                                    String tiempo = (String) timer.getText();
                                    int puntaje = seconds * 10 + ((board_size / 2) - remaining) * 20;
                                    int pares = (board_size / 2);

                                    intent.putExtra("tiempo", tiempo);
                                    intent.putExtra("pares", String.valueOf(pares));
                                    intent.putExtra("puntaje", String.valueOf(puntaje));

                                    startActivity(intent);
                                }
                            }
                        }.start();
                    } else { //si no son iguales
                        new CountDownTimer(1000,1){
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }
                            @Override
                            public void onFinish() {
                                for(Card card: pressed_cards){
                                    card.press(); //las damos vuelta de nuevo
                                }
                                pressed_cards = new ArrayList<>();

                            }
                        }.start();
                    }
                }
            }
        });

    }

    private String secondsToString(int pTime) {
        return String.format("%02d:%02d", pTime / 60, pTime % 60);
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

    /**
     * Inicializa las cartas dependiendo el tamaño de la pantalla y le asigna el resource de imagen
     * @param board_size tamaño del grid
     * @return arreglo con las cartas inicializadas
     */
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

    /**
     * Inicializa las cartas pero con los valores del sharedpreference. Los arreglos están en formato string,
     * entonces hay que pasarlos a un array llamando a la funcion Arrays.asList()
     * @param resources Arreglo de los resources de todas las cartas pero en string, obtenido desde sharedpreference
     *                  Ej: [213109944, 213110943, ...] los numeros son el resource
     * @param pressed Arreglo de cartas presionadas EJ: [false, false, true, false, false, ... ]
     * @param completed Arreglo de cartas completadas Ej: [false, false, true, true, false, false, ...]
     * @return
     */
    ArrayList<Card> createContinuedCards(String resources, String pressed, String completed){
        ArrayList<Card> cards = new ArrayList<>();
        List<String> r = new ArrayList<>(Arrays.asList(resources.split(",")));
        List<String> p = new ArrayList<>(Arrays.asList(pressed.split(",")));
        List<String> c = new ArrayList<>(Arrays.asList(completed.split(",")));
        int i = 0;
        for (String resource: r) {
            Log.i("CONTINUANDO",resource);
            Card card = new Card(this, Integer.parseInt(resource));
            if( p.get(i).equals("true")){
                card.press();
            }
            if( c.get(i).equals("true")){
                card.complete();
            }
            cards.add(card);
            i++;
        }
        return cards;
    }

    /**
     * Guarda en arreglo las cartas presionadas.
     * @param cards Arreglo de cartas una vez inicializadas.
     * @return
     */
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

    /**
     * Transforma el arreglo de cartas a string
     * @return El arreglo de resources en forma de string
     */
    String getCardResources(){
        StringBuilder sb = new StringBuilder();
        for(Card card: this.cards){
            sb.append(card.getResource());
            sb.append(",");
        }
        return sb.toString();
    }

    /**
     * Transforma el arreglo de cartas a string
     * @return El arreglo de cartas presionadas en forma de string
     */
    String getPressedCards(){
        StringBuilder sb = new StringBuilder();
        for(Card card: this.cards){
            sb.append(card.isPressed());
            sb.append(",");
        }
        return sb.toString();
    }

    /**
     * Transforma el arreglo de cartas a string
     * @return El arreglo de cartas completadas en forma de string
     */
    String getCompletedCards(){
        StringBuilder sb = new StringBuilder();
        for(Card card: this.cards){
            sb.append(card.isCompleted());
            sb.append(",");
        }
        return sb.toString();
    }


    /**
     * Guarda en sharedpreference el estado del juego
     * @param date el nombre del archivo que se va a guardas, es la fecha del juego.
     */
    void saveState(String date){

        SharedPreferences sp = this.getSharedPreferences("Juegos", Context.MODE_PRIVATE);
        Set<String> def = new HashSet<>();
        Set<String> archivos = sp.getStringSet("archivos", def);
        archivos.add(""+date);
        SharedPreferences.Editor editor = sp.edit();
        editor.putStringSet("archivos",archivos);
        editor.commit();
        sp = this.getSharedPreferences("" + date, Context.MODE_PRIVATE);
        editor = sp.edit();
        Log.i("SAVE", date);
        editor.putString("resources",getCardResources());
        editor.putString("pressed",getPressedCards());
        editor.putString("completed",getCompletedCards());
        editor.putInt("alto",this.alto);
        editor.putInt("ancho",this.ancho);
        Log.i("SAVED", ""+this.ancho);
        editor.putString("tiempo",this.timer.getText().toString());
        editor.commit();

    }


}
