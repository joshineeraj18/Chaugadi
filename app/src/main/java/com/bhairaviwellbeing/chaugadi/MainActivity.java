package com.bhairaviwellbeing.chaugadi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ImageView card1;
    ImageView card2;
    ImageView card3;
    ImageView card4;
    ImageView card5;
    ImageView card6;
    ImageView card7;
    ImageView card8;
    ImageView card9;
    ImageView card10;
    ImageView card11;
    ImageView card12;
    ImageView card13;

    ArrayList<ImageView> imvlist;

    RelativeLayout cardsview;


    Button bt_show,bt_shuffle;

    Deck deck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deck = new Deck();


        bt_show = findViewById(R.id.btn_show);
        bt_shuffle = findViewById(R.id.btn_shuffle);
        cardsview = (RelativeLayout) findViewById(R.id.rellay7);

        initializeCards();

        bt_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView = new ImageView(MainActivity.this);
                imageView.setImageResource(R.drawable.card_5d);
                imvlist.add(imageView);
                cardsview.addView(imageView);
            }
        });

        bt_shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deck.shuffle(100);
            }
        });

    }

    private void initializeCards() {




    }
}
