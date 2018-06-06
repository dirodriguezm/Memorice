package com.example.diego.memorice;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class CreditsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
        TextView credits = (TextView) findViewById(R.id.credit_text);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_credits);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.credits);
        credits.setText("Creada por: \nMatías Medina \nY \nDiego Rodriguez");
        credits.startAnimation(animation);

    }
}
