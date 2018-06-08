package com.example.diego.memorice;


import android.content.Context;

public class Card extends android.support.v7.widget.AppCompatImageView{

    private boolean pressed;
    private boolean completed;
    private int resource;
    public Card(Context context, int resource) {
        super(context);
        this.pressed = false;
        this.resource = resource;
        this.completed = false;
    }

    public void press(){
        if(!completed) {
            this.pressed = !this.pressed;
            if (this.pressed) {
                this.setImageResource(this.resource);
            } else {
                this.setImageResource(R.drawable.blank);
            }
        }

    }

    public boolean isPressed(){
        return this.pressed;
    }

    public int getResource(){
        return this.resource;
    }

    public void setResource(int resource){
        this.resource = resource;
    }

    public void complete(){
        this.completed = true;
        this.pressed = false;
        //this.resource = R.drawable.completed;
        //this.setImageResource(this.resource);
        this.setVisibility(INVISIBLE);
    }

    public boolean isCompleted(){
        return this.completed;
    }

}
