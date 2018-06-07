package com.example.diego.memorice;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import java.util.ArrayList;

class TableroAdapter extends BaseAdapter {
    private Context context;
    private Integer[] resources;
    private int ancho, alto;
    private ArrayList<Card> pressed_cards;
    private ArrayList<Card> cards;

    public TableroAdapter(Context context, ArrayList<Card> cards) {
        this.context = context;
        this.cards = cards;
    }

    @Override
    public int getCount() {
        return cards.size();
    }

    @Override
    public Card getItem(int position) {
        if( this.cards.size() > position) {
            return this.cards.get(position);
        }
        else return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Card card = this.cards.get(position);
        return card;
    }





}
