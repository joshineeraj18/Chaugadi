package com.bhairaviwellbeing.chaugadi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {


    LinearLayout cardRow1, cardRow2;
    Button btn_Shuffle;

    FirebaseUser user;
    DatabaseReference reference;
    DatabaseReference reference2;
    DatabaseReference logReference;

    ImageView im_deck_shuffle;
    public static String room_no;
    int mySeatNo = 0;
    GameData gameData;
    Card myCards[];
    Card tempCards[];

    ImageView cardsIV[];
    ImageView thrown_cards[];

    Intent intent;
    Deck deck;

    TextView message;
    PopupWindow popupWindow;
    PopupWindow popupWindow2;

    LinearLayout ll_color;
    ImageView img_color;
    TextView tv_claim;

    LinearLayout score_board, fee_score_board;
    TextView scoreA, scoreB;
    TextView fee_score_A, fee_score_B;

    TextView u1, u2, u3;
    Button start;
    Boolean latch = true;

    MediaPlayer cardsound;

    TextView claim_u1;
    TextView claim_u2;
    TextView claim_u3;
    TextView claim_u4;

    TextView myscore;
    TextView oppscore;

    int logno = 0;

    TextView points;
    ImageView point_side;

    String sourceActivity;
    Boolean onResumet = false;

    Computer pc1;
    Map<String, Integer> claim_result_1 = new HashMap<String, Integer>();
    Map<String, Integer> claim_result_2 = new HashMap<String, Integer>();
    Map<String, Integer> claim_result_3 = new HashMap<String, Integer>();
    Map<String, Integer> claim_result_4 = new HashMap<String, Integer>();

    RelativeLayout score_bar;
    Button tuser1, tuser2, tuser3, tuser4;
    ImageView t_iv_cards[];


    int sca, scb;

    int t_user = 1;

    ValueEventListener ve1;

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        //  super.onBackPressed();

        new AlertDialog.Builder(this)
                .setTitle("Exit Room!!!")
                .setMessage("Do you want to Exit Room:" + room_no + "?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        final DatabaseReference temp = reference2;

                        reference2 = null;

                        temp.removeEventListener(ve1);
                        reference.child("room").setValue("no-room");


                        if (gameData.getId2().contains("COMPUTER")) {
                            gameData.setId2("empty");
                            temp.child("id2").setValue("empty");
                        }
                        if (gameData.getId3().contains("COMPUTER")) {
                            gameData.setId3("empty");
                            temp.child("id3").setValue("empty");
                        }
                        if (gameData.getId4().contains("COMPUTER")) {
                            gameData.setId4("empty");
                            temp.child("id4").setValue("empty");
                        }


                        switch (gameData.getMyPosition(user.getUid())) {
                            case 1:
                                temp.child("id1").setValue("empty");
                                break;
                            case 2:
                                temp.child("id2").setValue("empty");
                                break;
                            case 3:
                                temp.child("id3").setValue("empty");
                                break;
                            case 4:
                                temp.child("id4").setValue("empty");
                                break;
                        }


                        temp.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                gameData = dataSnapshot.getValue(GameData.class);

                                if (gameData.getId1().equals("empty")
                                        && gameData.getId2().equals("empty")
                                        && gameData.getId3().equals("empty")
                                        && gameData.getId4().equals("empty")) {
                                    temp.child("isroomActive").setValue(false);
                                    temp.removeValue();
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("room_creation").child("Rooms_list");
                                    ref.child(room_no).removeValue();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        Intent intent1 = new Intent(GameActivity.this, TestActivity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent1);
                        finish();

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_game);

            btn_Shuffle = findViewById(R.id.btn_shuffle);
            im_deck_shuffle = findViewById(R.id.deck_shuffle);
            message = findViewById(R.id.txt_view_message);

            cardRow1 = findViewById(R.id.lay);
            cardRow2 = findViewById(R.id.lay2);
            initializeViews();
            btn_Shuffle.setVisibility(View.GONE);
            im_deck_shuffle.setVisibility(View.GONE);

            pc1 = new Computer();
//            score_bar.setOnClickListener(this);


            cardsound = MediaPlayer.create(GameActivity.this, R.raw.cardthrow1);

            intent = getIntent();
            room_no = intent.getStringExtra("Room");
            sourceActivity = intent.getStringExtra("Activity");


            if (sourceActivity.equals("TestActivity")) {

                reference2 = FirebaseDatabase.getInstance().getReference("Rooms").child(room_no);

                reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        gameData = dataSnapshot.getValue(GameData.class);

                        if (gameData.getState() == GameData.CLAIM) {
                            onResumet = true;
                        }
                        mySeatNo = gameData.getMyPosition(user.getUid());

                        u1.setText(gameData.getPlayerAtSeat(gameData.getnextSeat(mySeatNo)).replaceAll("\\B|\\b", "\n"));
                        u2.setText(gameData.getPlayerAtSeat(gameData.getnextSeat(gameData.getnextSeat(mySeatNo))));
                        u3.setText(gameData.getPlayerAtSeat(gameData.getnextSeat(gameData.getnextSeat(gameData.getnextSeat(mySeatNo)))).replaceAll("\\B|\\b", "\n"));

                        if (!(gameData.getState() == GameData.IDEAL || gameData.getState() == GameData.SHUFFLING)) {
                            String str = gameData.getDeck();
                            str = str.substring((mySeatNo - 1) * 26, (mySeatNo * 26));

                            for (int i = 0; i <= 12; i++) {

                                myCards = new Card[13];

                                if (!str.substring(0, 2).equals("00")) {
                                    myCards[i] = new Card(str.substring(0, 2));
                                    str = str.substring(2);
                                    cardsIV[i].setVisibility(View.VISIBLE);
                                    cardsIV[i].setImageResource(myCards[i].getRes());
                                    cardsIV[i].setClickable(false);

                                } else {
                                    str = str.substring(2);
                                    myCards[i] = new Card();
                                    cardsIV[i].setVisibility(View.INVISIBLE);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }


            user = FirebaseAuth.getInstance().getCurrentUser();
            reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
            reference2 = FirebaseDatabase.getInstance().getReference("Rooms").child(room_no);
            logReference = FirebaseDatabase.getInstance().getReference("Log");


            ve1 = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    gameData = dataSnapshot.getValue(GameData.class);
                    mySeatNo = gameData.getMyPosition(user.getUid());

                    if (reference2 == null) {
                        gameData.setState(GameData.EXIT);
                        return;
                    }


                    if (onResumet) {
                        gameData.setState(GameData.COLLECT_CARDS);
                    }


                    switch (gameData.getState()) {

                        case GameData.IDEAL:

                            if (gameData.getChal_card_1() == 25 &&
                                    gameData.getChal_card_2() == 25 &&
                                    gameData.getChal_card_3() == 25 &&
                                    gameData.getChal_card_4() == 25 && mySeatNo == 1) {
                                gameData.setState(GameData.SHUFFLING);
                                reference2.setValue(gameData);
                            } else {
                                message.setVisibility(View.VISIBLE);
                                message.setText("Please wait for all to join");

                                if (gameData.getChal_card_1() == 25) {
                                    message.append("\n" + gameData.getPlayerAtSeat(1) + " is in");
                                }
                                if (gameData.getChal_card_2() == 25) {
                                    message.append("\n" + gameData.getPlayerAtSeat(2) + " is in");
                                }
                                if (gameData.getChal_card_3() == 25) {
                                    message.append("\n" + gameData.getPlayerAtSeat(3) + " is in");
                                }
                                if (gameData.getChal_card_4() == 25) {
                                    message.append("\n" + gameData.getPlayerAtSeat(4) + " is in");
                                }


                            }
                            break;
                        case GameData.SHUFFLING:
                            //                      score_board.setVisibility(View.VISIBLE);
                            claim_u1.setVisibility(View.INVISIBLE);
                            claim_u2.setVisibility(View.INVISIBLE);
                            claim_u3.setVisibility(View.INVISIBLE);
                            claim_u4.setVisibility(View.INVISIBLE);
                            img_color.setVisibility(View.INVISIBLE);

                            ll_color.setVisibility(View.INVISIBLE);


                            if (gameData.getScoreA() > 0) {
                                sca = 0;
                                scb = Math.abs(gameData.getScoreA());
                            } else {
                                scb = 0;
                                sca = Math.abs(gameData.getScoreA());
                            }

                            scoreA.setText(String.format("%2d", sca));
                            scoreB.setText(String.format("%2d", scb));

                            if (mySeatNo == 1 || mySeatNo == 3) {
                                myscore.setText(String.format("%2d", gameData.getFeesA()));
                                oppscore.setText(String.format("%2d", gameData.getFeesB()));
                            } else {
                                myscore.setText(String.format("%2d", gameData.getFeesB()));
                                oppscore.setText(String.format("%2d", gameData.getFeesA()));
                            }


                            if (mySeatNo == 1 || mySeatNo == 3) {
                                if (gameData.getScoreA() > 0) {
                                    point_side.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
                                } else {
                                    point_side.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
                                }
                            } else {
                                if (gameData.getScoreA() < 0) {
                                    point_side.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
                                } else {
                                    point_side.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
                                }
                            }

                            points.setText(String.format("%2d", Math.abs(gameData.getScoreA())));


                            thrown_cards[0].setVisibility(View.INVISIBLE);
                            thrown_cards[1].setVisibility(View.INVISIBLE);
                            thrown_cards[2].setVisibility(View.INVISIBLE);
                            thrown_cards[3].setVisibility(View.INVISIBLE);

                            gameData.setFeesA(0);
                            gameData.setFeesB(0);

                            u1.setText(gameData.getPlayerAtSeat(gameData.getnextSeat(mySeatNo)).replaceAll("\\B", "\n"));
                            u2.setText(gameData.getPlayerAtSeat(gameData.getnextSeat(gameData.getnextSeat(mySeatNo))));
                            u3.setText(gameData.getPlayerAtSeat(gameData.getnextSeat(gameData.getnextSeat(gameData.getnextSeat(mySeatNo)))).replaceAll("\\B", "\n"));


                            if (gameData.getShufflingSeat() == mySeatNo) {
                                deck = new Deck();
                                btn_Shuffle.setText("Press to Shuffle");
                                btn_Shuffle.setVisibility(View.VISIBLE);
                                im_deck_shuffle.setVisibility(View.VISIBLE);
                                message.setText("Please Shuffle the Cards \nand Distribute");
                            } else {

                                if (gameData.getPlayerIDAtSeat(gameData.getShufflingSeat()).contains("COMPUTER") && mySeatNo == 1) {
                                    gameData.initiateDeck();
                                    gameData.sortplayercards();
                                    gameData.setState(GameData.COLLECT_CARDS);
                                    reference2.setValue(gameData);

                                }
                                message.setText(gameData.getPlayerAtSeat(gameData.getShufflingSeat()) + " is shuffling the cards \nplease wait");
                            }
                            break;

                        case GameData.COLLECT_CARDS:

                            btn_Shuffle.setVisibility(View.INVISIBLE);
                            im_deck_shuffle.setVisibility(View.INVISIBLE);

                            message.setText("");

                            myCards = gameData.getMycards(mySeatNo);

                            for (int i = 0; i <= 12; i++) {
                                cardsIV[i].setVisibility(View.VISIBLE);
                                cardsIV[i].setImageResource(myCards[i].getRes());
                                cardsIV[i].setClickable(false);
                            }

                            if (mySeatNo == gameData.getShufflingSeat()) {
                                gameData.setClaim_seat(gameData.getnextSeat(gameData.getShufflingSeat()));
                                gameData.setClaim_number(1);
                                gameData.setState(GameData.CLAIM);
                                reference2.setValue(gameData);
                            } else if (onResumet) {
                                onResumet = false;
                                gameData.setState(GameData.CLAIM);
                                gameData.setChal_card_1(0);
                                reference2.setValue(gameData);
                            }

                            if (gameData.getPlayerIDAtSeat(gameData.getShufflingSeat()).contains("COMPUTER") && mySeatNo == 1) {
                                gameData.setClaim_seat(gameData.getnextSeat(gameData.getShufflingSeat()));
                                gameData.setClaim_number(1);
                                gameData.setState(GameData.CLAIM);
                                reference2.setValue(gameData);
                            }
                            break;

                        case GameData.CLAIM: {
                            switch (gameData.getClaim_number()) {
                                case 1:
                                    if (gameData.getClaim_seat() == mySeatNo) {
                                        message.setText("Its your term to claim\nPlease claim");
                                        {
                                            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                                            final View popupView = inflater.inflate(R.layout.popup_window, null);
                                            DisplayMetrics dm = new DisplayMetrics();
                                            getWindowManager().getDefaultDisplay().getMetrics(dm);
                                            final int width = dm.widthPixels;
                                            final int height = dm.heightPixels;

                                            boolean focusable = true;
                                            popupWindow = new PopupWindow(popupView, (int) (width * 0.46), (int) (height * 0.3), focusable);

                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                popupWindow.setElevation(20);
                                            }

                                            findViewById(R.id.game_layout).post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    popupWindow.showAtLocation(findViewById(R.id.game_layout), Gravity.CENTER, 0, 0);
                                                }
                                            });
                                            popupWindow.setOutsideTouchable(false);

                                            popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                                                @Override
                                                public boolean onTouch(View v, MotionEvent motionEvent) {
                                                    if (motionEvent.getX() < 0 || motionEvent.getX() > (int) (width * 0.46))
                                                        return true;
                                                    if (motionEvent.getY() < 0 || motionEvent.getY() > (int) (height * 0.3))
                                                        return true;
                                                    return false;
                                                }
                                            });


                                            final Button keypad[] = new Button[7];
                                            keypad[0] = popupView.findViewById(R.id.btn_8);
                                            keypad[1] = popupView.findViewById(R.id.btn_9);
                                            keypad[2] = popupView.findViewById(R.id.btn_10);
                                            keypad[3] = popupView.findViewById(R.id.btn_11);
                                            keypad[4] = popupView.findViewById(R.id.btn_12);
                                            keypad[5] = popupView.findViewById(R.id.btn_13);
                                            keypad[6] = popupView.findViewById(R.id.btn_pass);


//                                        String temp = new String();
//                                        for (int i = 0; i <= 12; i++) {
//                                            temp = myCards[i].toString() + temp;
//                                        }
//                                        String tmp2 = new String();
//                                        switch (mySeatNo) {
//                                            case 1:
//                                                tmp2 = temp + gameData.getDeck().substring(26);
//                                                gameData.setDeck(tmp2);
//                                                break;
//                                            case 2:
//                                                tmp2 = gameData.getDeck().substring(0, 26) + temp + gameData.getDeck().substring(52);
//                                                gameData.setDeck(tmp2);
//                                                break;
//                                            case 3:
//                                                tmp2 = gameData.getDeck().substring(0, 52) + temp + gameData.getDeck().substring(78);
//                                                gameData.setDeck(tmp2);
//                                                break;
//                                            case 4:
//                                                tmp2 = gameData.getDeck().substring(0, 78) + temp;
//                                                gameData.setDeck(tmp2);
//                                                break;
//                                        }

                                            for (int i = 0; i <= 6; i++) {
                                                keypad[i].setOnClickListener(GameActivity.this);
                                            }


                                        }    //Setting up Popup Window
                                    } else {


                                        message.setText("Please wait " + gameData.getPlayerAtSeat(gameData.getClaim_seat()) + " is Claiming");

                                        if (gameData.getPlayerIDAtSeat(gameData.getClaim_seat()).contains("COMPUTER") && mySeatNo == 1) {

                                            claim_result_1 = pc1.surProcess(0, 0, 0, gameData.getClaim_seat(), gameData.getDeck());


                                            gameData.setNextClaimNumber((claim_result_1.get("Sur") < 7) ? 7 : claim_result_1.get("Sur"));
                                            gameData.setNextClaimData();
                                            //reference2.setValue(gameData);


                                            final Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    // Do something after 5s = 5000ms
                                                    reference2.setValue(gameData);
                                                }
                                            }, 3000);
                                        }
                                    }
                                    break;
                                case 2:
                                    if (gameData.getClaim_seat() == mySeatNo) {

                                        if (gameData.getChal_card_1() == 7) {
                                            message.setText(gameData.getPlayerAtSeat(gameData.getPrvSeat(gameData.getClaim_seat())) + " Said: Pass\nIts your term to claim\nPlease claim");
                                        } else {
                                            message.setText(gameData.getPlayerAtSeat(gameData.getPrvSeat(gameData.getClaim_seat())) + " Claims:" + gameData.getChal_card_1() + "\nIts your term to claim\nPlease claim");
                                        }

                                        {
                                            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                                            final View popupView = inflater.inflate(R.layout.popup_window, null);

                                            DisplayMetrics dm = new DisplayMetrics();
                                            getWindowManager().getDefaultDisplay().getMetrics(dm);
                                            final int width = dm.widthPixels;
                                            final int height = dm.heightPixels;

                                            boolean focusable = true;
                                            popupWindow = new PopupWindow(popupView, (int) (width * 0.46), (int) (height * 0.3), focusable);

                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                popupWindow.setElevation(20);
                                            }

                                            findViewById(R.id.game_layout).post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    popupWindow.showAtLocation(findViewById(R.id.game_layout), Gravity.CENTER, 0, 0);
                                                }
                                            });
                                            popupWindow.setOutsideTouchable(false);

                                            popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                                                @Override
                                                public boolean onTouch(View v, MotionEvent motionEvent) {
                                                    if (motionEvent.getX() < 0 || motionEvent.getX() > (int) (width * 0.46))
                                                        return true;
                                                    if (motionEvent.getY() < 0 || motionEvent.getY() > (int) (height * 0.3))
                                                        return true;
                                                    return false;
                                                }
                                            });


                                            Button keypad[] = new Button[7];
                                            keypad[0] = popupView.findViewById(R.id.btn_8);
                                            keypad[1] = popupView.findViewById(R.id.btn_9);
                                            keypad[2] = popupView.findViewById(R.id.btn_10);
                                            keypad[3] = popupView.findViewById(R.id.btn_11);
                                            keypad[4] = popupView.findViewById(R.id.btn_12);
                                            keypad[5] = popupView.findViewById(R.id.btn_13);
                                            keypad[6] = popupView.findViewById(R.id.btn_pass);

                                            int i;
                                            if (gameData.getChal_card_1() == 7) {
                                                i = 0;
                                            } else {
                                                i = gameData.getChal_card_1() - 7;
                                            }
                                            for (; i <= 6; i++) {
                                                keypad[i].setOnClickListener(GameActivity.this);
                                            }
                                        }    //Setting up Popup Window
                                    } else {
                                        if (gameData.getChal_card_1() == 7) {
                                            message.setText(gameData.getPlayerAtSeat(gameData.getPrvSeat(gameData.getClaim_seat())) + " Said: Pass");
                                        } else {
                                            message.setText(gameData.getPlayerAtSeat(gameData.getPrvSeat(gameData.getClaim_seat())) + " Claims:" + gameData.getChal_card_1() + "\n" +
                                                    gameData.getPlayerAtSeat(gameData.getClaim_seat()) + " is claiming");
                                        }

                                        if (gameData.getPlayerIDAtSeat(gameData.getClaim_seat()).contains("COMPUTER") && mySeatNo == 1) {

                                            claim_result_2 = pc1.surProcess(gameData.getChal_card_1(), 0, 0, gameData.getClaim_seat(), gameData.getDeck());

                                            gameData.setNextClaimNumber((int) claim_result_2.get("Sur"));
                                            gameData.setNextClaimData();
                                            //reference2.setValue(gameData);


                                            final Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    // Do something after 5s = 5000ms
                                                    reference2.setValue(gameData);
                                                }
                                            }, 3000);
                                        }
                                    }
                                    break;
                                case 3:
                                    if (gameData.getClaim_seat() == mySeatNo) {

                                        if (gameData.getChal_card_2() == 0) {
                                            message.setText(gameData.getPlayerAtSeat(gameData.getPrvSeat(gameData.getClaim_seat())) + " Said: Pass\nIts your term to claim\nPlease claim");
                                        } else {
                                            message.setText(gameData.getPlayerAtSeat(gameData.getPrvSeat(gameData.getClaim_seat())) + " Claims:" + gameData.getChal_card_2() + "\nIts your term to claim\nPlease claim");
                                        }

                                        {
                                            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                                            final View popupView = inflater.inflate(R.layout.popup_window, null);
                                            DisplayMetrics dm = new DisplayMetrics();
                                            getWindowManager().getDefaultDisplay().getMetrics(dm);
                                            final int width = dm.widthPixels;
                                            final int height = dm.heightPixels;

                                            boolean focusable = true;
                                            popupWindow = new PopupWindow(popupView, (int) (width * 0.46), (int) (height * 0.3), focusable);

                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                popupWindow.setElevation(20);
                                            }

                                            findViewById(R.id.game_layout).post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    popupWindow.showAtLocation(findViewById(R.id.game_layout), Gravity.CENTER, 0, 0);
                                                }
                                            });
                                            popupWindow.setOutsideTouchable(false);

                                            popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                                                @Override
                                                public boolean onTouch(View v, MotionEvent motionEvent) {
                                                    if (motionEvent.getX() < 0 || motionEvent.getX() > (int) (width * 0.46))
                                                        return true;
                                                    if (motionEvent.getY() < 0 || motionEvent.getY() > (int) (height * 0.3))
                                                        return true;
                                                    return false;
                                                }
                                            });


                                            Button keypad[] = new Button[7];
                                            keypad[0] = popupView.findViewById(R.id.btn_8);
                                            keypad[1] = popupView.findViewById(R.id.btn_9);
                                            keypad[2] = popupView.findViewById(R.id.btn_10);
                                            keypad[3] = popupView.findViewById(R.id.btn_11);
                                            keypad[4] = popupView.findViewById(R.id.btn_12);
                                            keypad[5] = popupView.findViewById(R.id.btn_13);
                                            keypad[6] = popupView.findViewById(R.id.btn_pass);
                                            int i;
                                            if (gameData.getChal_card_2() == 0) {
                                                i = gameData.getChal_card_1() - 7;
                                            } else {
                                                i = gameData.getChal_card_2() - 7;
                                            }
                                            for (; i <= 6; i++) {
                                                keypad[i].setOnClickListener(GameActivity.this);
                                            }
                                        }    //Setting up Popup Window
                                    } else {
                                        if (gameData.getChal_card_2() == 0) {
                                            message.setText(gameData.getPlayerAtSeat(gameData.getPrvSeat(gameData.getClaim_seat())) + " Said: Pass");
                                        } else {
                                            message.setText(gameData.getPlayerAtSeat(gameData.getPrvSeat(gameData.getClaim_seat())) + " Claims:" + gameData.getChal_card_2() + "\n" +
                                                    gameData.getPlayerAtSeat(gameData.getClaim_seat()) + " is claiming");
                                        }

                                        if (gameData.getPlayerIDAtSeat(gameData.getClaim_seat()).contains("COMPUTER") && mySeatNo == 1) {

                                            claim_result_3 = pc1.surProcess(gameData.getChal_card_2(), gameData.getChal_card_1(), 0, gameData.getClaim_seat(), gameData.getDeck());

                                            gameData.setNextClaimNumber((int) claim_result_3.get("Sur"));
                                            gameData.setNextClaimData();
                                            //reference2.setValue(gameData);


                                            final Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    // Do something after 5s = 5000ms
                                                    reference2.setValue(gameData);
                                                }
                                            }, 3000);
                                        }
                                    }
                                    break;
                                case 4:
                                    if (gameData.getClaim_seat() == mySeatNo) {

                                        if (gameData.getChal_card_3() == 0) {
                                            message.setText(gameData.getPlayerAtSeat(gameData.getPrvSeat(gameData.getClaim_seat())) + " Said: Pass\nIts your term to claim\nPlease claim");
                                        } else {
                                            message.setText(gameData.getPlayerAtSeat(gameData.getPrvSeat(gameData.getClaim_seat())) + " Claims:" + gameData.getChal_card_3() + "\nIts your term to claim\nPlease claim");
                                        }

                                        {
                                            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                                            final View popupView = inflater.inflate(R.layout.popup_window, null);
                                            DisplayMetrics dm = new DisplayMetrics();
                                            getWindowManager().getDefaultDisplay().getMetrics(dm);
                                            final int width = dm.widthPixels;
                                            final int height = dm.heightPixels;

                                            boolean focusable = true;
                                            popupWindow = new PopupWindow(popupView, (int) (width * 0.46), (int) (height * 0.3), focusable);

                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                popupWindow.setElevation(20);
                                            }

                                            findViewById(R.id.game_layout).post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    popupWindow.showAtLocation(findViewById(R.id.game_layout), Gravity.CENTER, 0, 0);
                                                }
                                            });
                                            popupWindow.setOutsideTouchable(false);

                                            popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                                                @Override
                                                public boolean onTouch(View v, MotionEvent motionEvent) {
                                                    if (motionEvent.getX() < 0 || motionEvent.getX() > (int) (width * 0.46))
                                                        return true;
                                                    if (motionEvent.getY() < 0 || motionEvent.getY() > (int) (height * 0.3))
                                                        return true;
                                                    return false;
                                                }
                                            });


                                            Button keypad[] = new Button[7];
                                            keypad[0] = popupView.findViewById(R.id.btn_8);
                                            keypad[1] = popupView.findViewById(R.id.btn_9);
                                            keypad[2] = popupView.findViewById(R.id.btn_10);
                                            keypad[3] = popupView.findViewById(R.id.btn_11);
                                            keypad[4] = popupView.findViewById(R.id.btn_12);
                                            keypad[5] = popupView.findViewById(R.id.btn_13);
                                            keypad[6] = popupView.findViewById(R.id.btn_pass);

                                            int i;
                                            gameData.setState(GameData.CLAIM_COLOR);
                                            if (gameData.getChal_card_3() == 0) {
                                                if (gameData.getChal_card_2() == 0) {
                                                    i = gameData.getChal_card_1() - 7;
                                                } else {
                                                    i = gameData.getChal_card_2() - 7;
                                                }
                                            } else {
                                                i = gameData.getChal_card_3() - 7;
                                            }

                                            for (; i <= 6; i++) {
                                                keypad[i].setOnClickListener(GameActivity.this);
                                            }
                                        }    //Setting up Popup Window
                                    } else {
                                        if (gameData.getChal_card_3() == 0) {
                                            message.setText(gameData.getPlayerAtSeat(gameData.getPrvSeat(gameData.getClaim_seat())) + " Said: Pass");
                                        } else {
                                            message.setText(gameData.getPlayerAtSeat(gameData.getPrvSeat(gameData.getClaim_seat())) + " Claims:" + gameData.getChal_card_3() + "\n" +
                                                    gameData.getPlayerAtSeat(gameData.getClaim_seat()) + " is claiming");
                                        }

                                        if (gameData.getPlayerIDAtSeat(gameData.getClaim_seat()).contains("COMPUTER") && mySeatNo == 1) {

                                            claim_result_4 = pc1.surProcess(gameData.getChal_card_3(), gameData.getChal_card_2(), gameData.getChal_card_1(), gameData.getClaim_seat(), gameData.getDeck());

                                            gameData.setNextClaimNumber((int) claim_result_4.get("Sur"));
                                            gameData.setNextClaimData();
                                            //reference2.setValue(gameData);
                                            gameData.setState(GameData.CLAIM_COLOR);

                                            final Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    // Do something after 5s = 5000ms
                                                    reference2.setValue(gameData);
                                                }
                                            }, 3000);
                                        }
                                    }
                                    break;
                            }
                            break;
                        }
                        case GameData.CLAIM_COLOR: {
                            if (gameData.getClaim_seat() == mySeatNo) {

                                message.setText("Please Select the Color");

                                if (gameData.getId1().contains("COMPUTER")) {
                                    gameData.setChal_card_1(25);
                                }
                                if (gameData.getId2().contains("COMPUTER")) {
                                    gameData.setChal_card_2(25);
                                }
                                if (gameData.getId3().contains("COMPUTER")) {
                                    gameData.setChal_card_3(25);
                                }
                                if (gameData.getId4().contains("COMPUTER")) {
                                    gameData.setChal_card_4(25);
                                }


                                {
                                    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                                    final View popupView2 = inflater.inflate(R.layout.popup_color, null);

                                    DisplayMetrics dm = new DisplayMetrics();
                                    getWindowManager().getDefaultDisplay().getMetrics(dm);

                                    final int width = dm.widthPixels;
                                    final int height = dm.heightPixels;
                                    boolean focusable = true;
                                    popupWindow = new PopupWindow(popupView2, (int) (width * 0.56), (int) (height * 0.3), focusable);

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        popupWindow.setElevation(20);
                                    }

                                    findViewById(R.id.game_layout).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            popupWindow.showAtLocation(findViewById(R.id.game_layout), Gravity.CENTER, 0, 0);
                                        }
                                    });

                                    popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                                        @Override
                                        public boolean onTouch(View v, MotionEvent motionEvent) {
                                            if (motionEvent.getX() < 0 || motionEvent.getX() > (int) (width * 0.46))
                                                return true;
                                            if (motionEvent.getY() < 0 || motionEvent.getY() > (int) (height * 0.3))
                                                return true;
                                            return false;
                                        }
                                    });

                                    Button keypad[] = new Button[4];
                                    keypad[0] = popupView2.findViewById(R.id.btn_spades);
                                    keypad[1] = popupView2.findViewById(R.id.btn_club);
                                    keypad[2] = popupView2.findViewById(R.id.btn_diamond);
                                    keypad[3] = popupView2.findViewById(R.id.btn_heart);

                                    for (int i = 0; i <= 3; i++) {
                                        keypad[i].setOnClickListener(GameActivity.this);
                                    }
                                } //Setting up popup window


                            } else {
                                if (gameData.getChal_card_4() == 0) {
                                    message.setText(gameData.getPlayerAtSeat(gameData.getShufflingSeat()) + " Said: Pass");
                                } else {
                                    message.setText(gameData.getPlayerAtSeat(gameData.getShufflingSeat()) + " Claims:" + gameData.getChal_card_4());
                                }
                                message.append("\nPlease Wait " + gameData.getPlayerAtSeat(gameData.getClaim_seat()) + " is selecting color");


                                if (gameData.getPlayerIDAtSeat(gameData.getClaim_seat()).contains("COMPUTER") && mySeatNo == 1) {

                                    int index;

                                    if (gameData.getClaim_number() == gameData.getChal_card_1()) {
                                        index = 0;
                                        gameData.setClaim_color(claim_result_1.get("Suit"));
                                        gameData.setChalof(gameData.getClaim_seat(), 25);
                                        gameData.setState(GameData.CLAIM_READY);
                                    } else if (gameData.getClaim_number() == gameData.getChal_card_2()) {
                                        index = 1;
                                        gameData.setClaim_color(claim_result_2.get("Suit"));
                                        gameData.setChalof(gameData.getClaim_seat(), 25);
                                        gameData.setState(GameData.CLAIM_READY);

                                    } else if (gameData.getClaim_number() == gameData.getChal_card_3()) {
                                        index = 2;
                                        gameData.setClaim_color(claim_result_3.get("Suit"));
                                        gameData.setChalof(gameData.getClaim_seat(), 25);
                                        gameData.setState(GameData.CLAIM_READY);

                                    } else {
                                        index = 3;
                                        gameData.setClaim_color(claim_result_4.get("Suit"));
                                        gameData.setChalof(gameData.getClaim_seat(), 25);
                                        gameData.setState(GameData.CLAIM_READY);
                                    }


                                    if (gameData.getId1().contains("COMPUTER")) {
                                        gameData.setChal_card_1(25);
                                    }
                                    if (gameData.getId2().contains("COMPUTER")) {
                                        gameData.setChal_card_2(25);
                                    }
                                    if (gameData.getId3().contains("COMPUTER")) {
                                        gameData.setChal_card_3(25);
                                    }
                                    if (gameData.getId4().contains("COMPUTER")) {
                                        gameData.setChal_card_4(25);
                                    }

                                    //reference2.setValue(gameData);


                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Do something after 5s = 5000ms
                                            reference2.setValue(gameData);
                                        }
                                    }, 3000);
                                }
                            }

                            break;
                        }
                        case GameData.CLAIM_READY:
                            if (gameData.getClaim_seat() == mySeatNo) {
                                if (gameData.getChalof(1) == 25 &&
                                        gameData.getChalof(2) == 25 &&
                                        gameData.getChalof(3) == 25 &&
                                        gameData.getChalof(4) == 25) {

                                    final View popupView3;
                                    {
                                        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                                        popupView3 = inflater.inflate(R.layout.popup_claim_ready, null);

                                        DisplayMetrics dm = new DisplayMetrics();
                                        getWindowManager().getDefaultDisplay().getMetrics(dm);

                                        int width = dm.widthPixels;
                                        int height = dm.heightPixels;
                                        boolean focusable = true;
                                        popupWindow2 = new PopupWindow(popupView3, (int) (width * 0.46), (int) (height * 0.3), focusable);

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                            popupWindow2.setElevation(20);
                                        }

                                        TextView tv_claim_player = popupView3.findViewById(R.id.text_claim_player_name);
                                        ImageView iv_claim = popupView3.findViewById(R.id.iv_claim_color);
                                        TextView tv_claim_num = popupView3.findViewById(R.id.text_claim_num);

                                        tv_claim_player.setText(gameData.getPlayerAtSeat(gameData.getClaim_seat()));
                                        switch (gameData.getClaim_color()) {
                                            case 1:
                                                iv_claim.setImageResource(R.drawable.nw_01);
                                                img_color.setImageResource(R.drawable.nw_01);
                                                break;
                                            case 2:
                                                iv_claim.setImageResource(R.drawable.nw_04);
                                                img_color.setImageResource(R.drawable.nw_04);
                                                break;
                                            case 3:
                                                iv_claim.setImageResource(R.drawable.nw_03);
                                                img_color.setImageResource(R.drawable.nw_03);
                                                break;
                                            case 4:
                                                iv_claim.setImageResource(R.drawable.nw_02);
                                                img_color.setImageResource(R.drawable.nw_02);
                                                break;
                                        }
                                        tv_claim_num.setText(String.format("%02d", gameData.getClaim_number()));
                                        claim_u1.setText(String.format("%02d", gameData.getClaim_number()));
                                        claim_u1.setVisibility(View.VISIBLE);
//                                tv_claim.setText(String.format("%02d",gameData.getClaim_number()));


                                        findViewById(R.id.game_layout).post(new Runnable() {
                                            @Override
                                            public void run() {
                                                popupWindow2.showAtLocation(findViewById(R.id.game_layout), Gravity.CENTER, 0, 0);
                                            }
                                        });
                                    } // Setting up popup

                                    gameData.setChal_seat(mySeatNo);
                                    gameData.setState(GameData.CHAL_1);
                                    Button start = popupView3.findViewById(R.id.btn_start);
                                    start.setOnClickListener(GameActivity.this);

                                } else {
                                    message.setText("Please Wait for all players to get ready");
                                }
                            } else {
                                if (gameData.getChalof(mySeatNo) != 25) {
                                    if (latch) {
                                        latch = false;

                                        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                                        final View popupView3 = inflater.inflate(R.layout.popup_claim_ready, null);
                                        DisplayMetrics dm = new DisplayMetrics();
                                        getWindowManager().getDefaultDisplay().getMetrics(dm);

                                        final int width = dm.widthPixels;
                                        final int height = dm.heightPixels;
                                        boolean focusable = true;
                                        popupWindow2 = new PopupWindow(popupView3, (int) (width * 0.46), (int) (height * 0.3), focusable);

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                            popupWindow2.setElevation(20);
                                        }

                                        TextView tv_claim_player = popupView3.findViewById(R.id.text_claim_player_name);
                                        ImageView iv_claim = popupView3.findViewById(R.id.iv_claim_color);
                                        TextView tv_claim_num = popupView3.findViewById(R.id.text_claim_num);

                                        tv_claim_player.setText(gameData.getPlayerAtSeat(gameData.getClaim_seat()));
                                        switch (gameData.getClaim_color()) {
                                            case 1:
                                                iv_claim.setImageResource(R.drawable.nw_01);
                                                img_color.setImageResource(R.drawable.nw_01);
                                                break;
                                            case 2:
                                                iv_claim.setImageResource(R.drawable.nw_04);
                                                img_color.setImageResource(R.drawable.nw_04);
                                                break;
                                            case 3:
                                                iv_claim.setImageResource(R.drawable.nw_03);
                                                img_color.setImageResource(R.drawable.nw_03);
                                                break;
                                            case 4:
                                                iv_claim.setImageResource(R.drawable.nw_02);
                                                img_color.setImageResource(R.drawable.nw_02);
                                                break;
                                        }
                                        tv_claim_num.setText(String.format("%02d", gameData.getClaim_number()));

                                        int tmpSeat = gameData.getClaim_seat() - mySeatNo;
                                        if (tmpSeat < 0) {
                                            tmpSeat = 4 + tmpSeat;
                                        }
                                        switch (tmpSeat) {
                                            case 0:
                                                claim_u1.setText(String.format("%02d", gameData.getClaim_number()));
                                                claim_u1.setVisibility(View.VISIBLE);
                                                break;
                                            case 1:
                                                claim_u2.setText(String.format("%02d", gameData.getClaim_number()));
                                                claim_u2.setVisibility(View.VISIBLE);
                                                break;
                                            case 2:
                                                claim_u3.setText(String.format("%02d", gameData.getClaim_number()));
                                                claim_u3.setVisibility(View.VISIBLE);
                                                break;
                                            case 3:
                                                claim_u4.setText(String.format("%02d", gameData.getClaim_number()));
                                                claim_u4.setVisibility(View.VISIBLE);
                                                break;

                                        }
//                                    tv_claim.setText(String.format("%02d",gameData.getClaim_number()));

                                        findViewById(R.id.game_layout).post(new Runnable() {
                                            @Override
                                            public void run() {
                                                popupWindow2.showAtLocation(findViewById(R.id.game_layout), Gravity.CENTER, 0, 0);
                                            }
                                        });

                                        popupWindow2.setTouchInterceptor(new View.OnTouchListener() {
                                            @Override
                                            public boolean onTouch(View v, MotionEvent motionEvent) {
                                                if (motionEvent.getX() < 0 || motionEvent.getX() > (int) (width * 0.46))
                                                    return true;
                                                if (motionEvent.getY() < 0 || motionEvent.getY() > (int) (height * 0.3))
                                                    return true;
                                                return false;
                                            }
                                        });


                                        start = popupView3.findViewById(R.id.btn_start);
                                        start.setOnClickListener(GameActivity.this);
                                    }
                                } else {
                                    message.setText("Please wait for all players to get ready");
                                    if (gameData.getPlayerIDAtSeat(gameData.getClaim_seat()).contains("COMPUTER") && mySeatNo == 1) {
                                        gameData.setChal_seat(gameData.getClaim_seat());
                                        gameData.setState(GameData.CHAL_1);
                                        reference2.setValue(gameData);

                                    }
                                }

                            }
                            break;
                        case GameData.CHAL_1:
                            latch = true;
                            img_color.setVisibility(View.VISIBLE);
                            thrown_cards[0].setVisibility(View.INVISIBLE);
                            thrown_cards[1].setVisibility(View.INVISIBLE);
                            thrown_cards[2].setVisibility(View.INVISIBLE);
                            thrown_cards[3].setVisibility(View.INVISIBLE);

                            if (gameData.getChal_seat() == mySeatNo) {
//                            fee_score_board.setVisibility(View.VISIBLE);
                                if (mySeatNo == 1 || mySeatNo == 3) {
                                    myscore.setText(String.format("%2d", gameData.getFeesA()));
                                    oppscore.setText(String.format("%2d", gameData.getFeesB()));
                                } else {
                                    myscore.setText(String.format("%2d", gameData.getFeesB()));
                                    oppscore.setText(String.format("%2d", gameData.getFeesA()));
                                }
//                            fee_score_A.setText(String.format("%2d",gameData.getFeesA()));
//                            fee_score_B.setText(String.format("%2d",gameData.getFeesB()));

//                            score_board.setVisibility(View.VISIBLE);
                                scoreA.setText(String.format("%2d", sca));
                                scoreB.setText(String.format("%2d", scb));

                                message.setText("Its your Chal Please play");
                                for (int i = 0; i <= 12; i++) {
                                    cardsIV[i].setClickable(true);
                                }
                            } else {
//                            fee_score_board.setVisibility(View.VISIBLE);
//                            fee_score_A.setText(String.format("%2d",gameData.getFeesA()));
//                            fee_score_B.setText(String.format("%2d",gameData.getFeesB()));

                                if (mySeatNo == 1 || mySeatNo == 3) {
                                    myscore.setText(String.format("%2d", gameData.getFeesA()));
                                    oppscore.setText(String.format("%2d", gameData.getFeesB()));
                                } else {
                                    myscore.setText(String.format("%2d", gameData.getFeesB()));
                                    oppscore.setText(String.format("%2d", gameData.getFeesA()));
                                }


//                            score_board.setVisibility(View.VISIBLE);

                                scoreA.setText(String.format("%2d", sca));
                                scoreB.setText(String.format("%2d", scb));

                                for (int i = 0; i <= 12; i++) {
                                    cardsIV[i].setClickable(false);
                                }
                                message.setText("Please wait " + gameData.getPlayerAtSeat(gameData.getChal_seat()) + " is playing");

                                if (gameData.getPlayerIDAtSeat(gameData.getChal_seat()).contains("COMPUTER") && mySeatNo == 1) {
                                    int cardPlayed = pc1.chalProcess(0, 0, 0, gameData.getChal_seat(), gameData.getClaim_color(), gameData.getDeck());

                                    Log log = new Log(logno++, cardPlayed, 0, 0, 0, gameData.getChal_seat(), gameData.getClaim_color(), gameData.getDeck());
                                    logReference.child(room_no).child(log.getName()).setValue(log);

                                    updateDeckStringForPC(cardPlayed);
                                    gameData.setNextChalCard(cardPlayed);
                                    gameData.setNextChalData();


                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Do something after 5s = 5000ms
                                            reference2.setValue(gameData);
                                        }
                                    }, 1000);
                                }
                            }
                            break;
                        case GameData.CHAL_2:
                            cardsound.start();
                            updateThrownCards();
                            if (gameData.getChal_seat() == mySeatNo) {
                                message.setText("Its your Chal Please play");

                                Card card = new Card(gameData.getChal_card_1());
                                int num = 0;

                                for (int i = 0; i <= 12; i++) {
                                    if (card.getColor() == myCards[i].getColor()) {
                                        if (cardsIV[i].getVisibility() == View.VISIBLE) {
                                            cardsIV[i].setClickable(true);
                                            num++;
                                        }
                                    }
                                }
                                if (num == 0) {
                                    for (int i = 0; i <= 12; i++) {
                                        cardsIV[i].setClickable(true);
                                    }
                                } else {
                                    num = 0;
                                }
                            } else {
                                message.setText("Please wait " + gameData.getPlayerAtSeat(gameData.getChal_seat()) + " is playing");
                                for (int i = 0; i <= 12; i++) {
                                    cardsIV[i].setClickable(false);
                                }


                                if (gameData.getPlayerIDAtSeat(gameData.getChal_seat()).contains("COMPUTER") && mySeatNo == 1) {
                                    int cardPlayed = pc1.chalProcess(
                                            gameData.getChal_card_1(),
                                            0,
                                            0,
                                            gameData.getChal_seat(),
                                            gameData.getClaim_color(),
                                            gameData.getDeck());

                                    Log log = new Log(logno++, cardPlayed, gameData.getChal_card_1(), 0, 0, gameData.getChal_seat(), gameData.getClaim_color(), gameData.getDeck());
                                    logReference.child(room_no).child(log.getName()).setValue(log);

                                    updateDeckStringForPC(cardPlayed);
                                    gameData.setNextChalCard(cardPlayed);
                                    gameData.setNextChalData();


                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Do something after 5s = 5000ms
                                            reference2.setValue(gameData);
                                        }
                                    }, 1000);
                                }
                            }
                            break;
                        case GameData.CHAL_3:
                            cardsound.start();
                            updateThrownCards();
                            if (gameData.getChal_seat() == mySeatNo) {
                                message.setText("Its your Chal Please play");

                                Card card = new Card(gameData.getChal_card_1());
                                int num = 0;

                                for (int i = 0; i <= 12; i++) {
                                    if (card.getColor() == myCards[i].getColor()) {
                                        if (cardsIV[i].getVisibility() == View.VISIBLE) {
                                            cardsIV[i].setClickable(true);
                                            num++;
                                        }
                                    }
                                }
                                if (num == 0) {
                                    for (int i = 0; i <= 12; i++) {
                                        cardsIV[i].setClickable(true);
                                    }
                                } else {
                                    num = 0;
                                }

                            } else {
                                message.setText("Please wait " + gameData.getPlayerAtSeat(gameData.getChal_seat()) + " is playing");
                                for (int i = 0; i <= 12; i++) {
                                    cardsIV[i].setClickable(false);
                                }


                                if (gameData.getPlayerIDAtSeat(gameData.getChal_seat()).contains("COMPUTER") && mySeatNo == 1) {
                                    int cardPlayed = pc1.chalProcess(
                                            gameData.getChal_card_2(),
                                            gameData.getChal_card_1(),
                                            0,
                                            gameData.getChal_seat(),
                                            gameData.getClaim_color(),
                                            gameData.getDeck());

                                    Log log = new Log(logno++, cardPlayed, gameData.getChal_card_2(), gameData.getChal_card_1(), 0, gameData.getChal_seat(), gameData.getClaim_color(), gameData.getDeck());
                                    logReference.child(room_no).child(log.getName()).setValue(log);


                                    updateDeckStringForPC(cardPlayed);
                                    gameData.setNextChalCard(cardPlayed);
                                    gameData.setNextChalData();


                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Do something after 5s = 5000ms
                                            reference2.setValue(gameData);
                                        }
                                    }, 1000);
                                }
                            }
                            break;
                        case GameData.CHAL_4:
                            cardsound.start();
                            updateThrownCards();
                            if (gameData.getChal_seat() == mySeatNo) {
                                message.setText("Its your Chal Please play");

                                Card card = new Card(gameData.getChal_card_1());
                                int num = 0;

                                for (int i = 0; i <= 12; i++) {
                                    if (card.getColor() == myCards[i].getColor()) {
                                        if (cardsIV[i].getVisibility() == View.VISIBLE) {
                                            cardsIV[i].setClickable(true);
                                            num++;
                                        }
                                    }
                                }
                                if (num == 0) {
                                    for (int i = 0; i <= 12; i++) {
                                        cardsIV[i].setClickable(true);
                                    }
                                } else {
                                    num = 0;
                                }

                            } else {
                                message.setText("Please wait " + gameData.getPlayerAtSeat(gameData.getChal_seat()) + " is playing");
                                for (int i = 0; i <= 12; i++) {
                                    cardsIV[i].setClickable(false);
                                }


                                if (gameData.getPlayerIDAtSeat(gameData.getChal_seat()).contains("COMPUTER") && mySeatNo == 1) {
                                    int cardPlayed = pc1.chalProcess(
                                            gameData.getChal_card_3(),
                                            gameData.getChal_card_2(),
                                            gameData.getChal_card_1(),
                                            gameData.getChal_seat(),
                                            gameData.getClaim_color(),
                                            gameData.getDeck());

                                    Log log = new Log(logno++,
                                            cardPlayed,
                                            gameData.getChal_card_3(),
                                            gameData.getChal_card_2(),
                                            gameData.getChal_card_1(), gameData.getChal_seat(), gameData.getClaim_color(), gameData.getDeck());
                                    logReference.child(room_no).child(log.getName()).setValue(log);


                                    updateDeckStringForPC(cardPlayed);
                                    gameData.setNextChalCard(cardPlayed);
                                    gameData.setNextChalData();


                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Do something after 5s = 5000ms
                                            reference2.setValue(gameData);
                                        }
                                    }, 1000);
                                }

                            }
                            break;
                        case GameData.CHAL_RESULT:

                            if(gameData.getScoreA() > 51 || gameData.getScoreA() < -51){
                                if(mySeatNo == 1){
                                    if(gameData.getId2().contains("COMPUTER")){
                                        gameData.setChal_card_2(25);
                                    }else {
                                        gameData.setChal_card_2(0);
                                    }
                                    if(gameData.getId3().contains("COMPUTER")){
                                        gameData.setChal_card_3(25);
                                    }else {
                                        gameData.setChal_card_3(0);
                                    }
                                    if(gameData.getId4().contains("COMPUTER")){
                                        gameData.setChal_card_4(25);
                                    }else {
                                        gameData.setChal_card_4(0);
                                    }
                                }
                                gameData.setState(GameData.GAME_POINT);
                                latch = true;
                                reference2.setValue(gameData);
                            }else {
                                cardsound.start();
                                updateThrownCards();
                                for (int i = 0; i <= 12; i++) {
                                    cardsIV[i].setClickable(false);
                                }
                                if (gameData.getPrvSeat(gameData.getChal_seat()) == mySeatNo) {

                                    if (gameData.getChal_round() < 13) {
                                        gameData.calculate_chal_result();
                                        gameData.setState(GameData.CHAL_1);
                                    } else {
                                        gameData.calculate_chal_result();

                                        if (gameData.getClaim_seat() == 1 || gameData.getClaim_seat() == 3) {

                                            if (gameData.getClaim_number() > gameData.getFeesA()) {
                                                gameData.setScoreA(gameData.getScoreA() - (gameData.getClaim_number() * (gameData.getClaim_number() - gameData.getFeesA() + 1)));
                                            } else {
                                                gameData.setScoreA(gameData.getScoreA() + gameData.getFeesA());
                                            }
                                        } else {

                                            if (gameData.getClaim_number() > gameData.getFeesB()) {
                                                gameData.setScoreA(gameData.getScoreA() + (gameData.getClaim_number() * (gameData.getClaim_number() - gameData.getFeesB() + 1)));
                                            } else {
                                                gameData.setScoreA(gameData.getScoreA() - gameData.getFeesB());
                                            }
                                        }

                                        //Setting Up the Shuffling Seat as per Score

                                        if (gameData.getShufflingSeat() == 1 || gameData.getShufflingSeat() == 3) {
                                            if (gameData.getScoreA() > 0) {
                                                gameData.setShufflingSeat(gameData.getnextSeat(gameData.getShufflingSeat()));
                                            }
                                        } else {
                                            if (gameData.getScoreA() < 0) {
                                                gameData.setShufflingSeat(gameData.getnextSeat(gameData.getShufflingSeat()));
                                            }
                                        }

                                            gameData.setState(GameData.CHAL_RESULT);

                                    }

                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Do something after 5s = 5000ms
                                            reference2.setValue(gameData);
                                        }
                                    }, 3000);
                                } else if (gameData.getPlayerIDAtSeat(gameData.getPrvSeat(gameData.getChal_seat())).contains("COMPUTER")
                                        && mySeatNo == 1) {
                                    if (gameData.getChal_round() < 13) {
                                        gameData.calculate_chal_result();
                                        gameData.setState(GameData.CHAL_1);

                                    } else {
                                        gameData.calculate_chal_result();

                                        if (gameData.getClaim_seat() == 1 || gameData.getClaim_seat() == 3) {

                                            if (gameData.getClaim_number() > gameData.getFeesA()) {
                                                gameData.setScoreA(gameData.getScoreA() - (gameData.getClaim_number() * (gameData.getClaim_number() - gameData.getFeesA() + 1)));
                                            } else {
                                                gameData.setScoreA(gameData.getScoreA() + gameData.getFeesA());
                                            }
                                        } else {

                                            if (gameData.getClaim_number() > gameData.getFeesB()) {
                                                gameData.setScoreA(gameData.getScoreA() + (gameData.getClaim_number() * (gameData.getClaim_number() - gameData.getFeesB() + 1)));
                                            } else {
                                                gameData.setScoreA(gameData.getScoreA() - gameData.getFeesB());
                                            }
                                        }

                                        if (gameData.getShufflingSeat() == 1 || gameData.getShufflingSeat() == 3) {
                                            if (gameData.getScoreA() > 0) {
                                                gameData.setShufflingSeat(gameData.getnextSeat(gameData.getShufflingSeat()));
                                            }
                                        } else {
                                            if (gameData.getScoreA() < 0) {
                                                gameData.setShufflingSeat(gameData.getnextSeat(gameData.getShufflingSeat()));
                                            }
                                        }

                                        gameData.setState(GameData.SHUFFLING);


                                    }

                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Do something after 5s = 5000ms
                                            reference2.setValue(gameData);
                                        }
                                    }, 3000);
                                }
                            }

                            break;

                        case GameData.EXIT:
                            message.setText("Bye... Bye...");
                            break;

                        case GameData.GAME_POINT:
                            if (latch) {
                                latch = false;
                                if(gameData.getScoreA()< -51){
                                    if(mySeatNo == 1 || mySeatNo == 3){
                                        popupforCongratulate(false);
                                    }else {
                                        popupforCongratulate(true);
                                    }
                                }else {
                                    if(mySeatNo == 1 || mySeatNo == 3){
                                        popupforCongratulate(true);
                                    }else {
                                        popupforCongratulate(false);
                                    }

                                }
                            }
                            break;

                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            reference2.addValueEventListener(ve1);


            btn_Shuffle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (btn_Shuffle.getText().toString().equals("Press to Shuffle")) {
                        //  btn_Shuffle.setEnabled(false);
                        btn_Shuffle.setText("Please Wait..");
                        gameData.initiateDeck();
                        gameData.sortplayercards();
                        btn_Shuffle.setText("Press to Distribute");
                        btn_Shuffle.setEnabled(true);
                    } else if (btn_Shuffle.getText().toString().equals("Press to Distribute")) {
                        //   btn_Shuffle.setEnabled(false);
                        //   btn_Shuffle.setText("Please Wait..");
                        gameData.setState(GameData.COLLECT_CARDS);
                        reference2.setValue(gameData);

                    }

                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Room is deleted", Toast.LENGTH_SHORT).show();

        }
    }

    private void updateThrownCards() {
        int prvSeat = (gameData.getChal_seat() == 1) ? (4) : (gameData.getChal_seat() - 1);
        int tSeat = prvSeat - mySeatNo;
        if (tSeat < 0) {
            tSeat = 4 + tSeat;
        }
        thrown_cards[tSeat].setImageResource(gameData.getLastThrownCard().getRes());
        thrown_cards[tSeat].setVisibility(View.VISIBLE);

        if (tSeat == 0) {
            TranslateAnimation mAnimation = new TranslateAnimation(
                    TranslateAnimation.ABSOLUTE, 0f,
                    TranslateAnimation.ABSOLUTE, 0f,
                    TranslateAnimation.RELATIVE_TO_PARENT, 1f,
                    TranslateAnimation.RELATIVE_TO_PARENT, 0f);

            mAnimation.setDuration(300);
            mAnimation.setRepeatCount(0);
            mAnimation.setRepeatMode(Animation.REVERSE);
            mAnimation.setInterpolator(new LinearInterpolator());
            thrown_cards[tSeat].setAnimation(mAnimation);
        }

        if (tSeat == 1) {
            TranslateAnimation mAnimation = new TranslateAnimation(
                    TranslateAnimation.RELATIVE_TO_PARENT, 1f,
                    TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                    TranslateAnimation.ABSOLUTE, 0f,
                    TranslateAnimation.ABSOLUTE, 0f);

            mAnimation.setDuration(300);
            mAnimation.setRepeatCount(0);
            mAnimation.setRepeatMode(Animation.REVERSE);
            mAnimation.setInterpolator(new LinearInterpolator());
            thrown_cards[tSeat].setAnimation(mAnimation);
        }

        if (tSeat == 2) {
            TranslateAnimation mAnimation = new TranslateAnimation(
                    TranslateAnimation.ABSOLUTE, 0f,
                    TranslateAnimation.ABSOLUTE, 0f,
                    TranslateAnimation.RELATIVE_TO_PARENT, -1f,
                    TranslateAnimation.RELATIVE_TO_PARENT, 0f);

            mAnimation.setDuration(300);
            mAnimation.setRepeatCount(0);
            mAnimation.setRepeatMode(Animation.REVERSE);
            mAnimation.setInterpolator(new LinearInterpolator());
            thrown_cards[tSeat].setAnimation(mAnimation);
        }

        if (tSeat == 3) {
            TranslateAnimation mAnimation = new TranslateAnimation(
                    TranslateAnimation.ABSOLUTE, -100f,
                    TranslateAnimation.ABSOLUTE, 0f,
                    TranslateAnimation.ABSOLUTE, 0f,
                    TranslateAnimation.ABSOLUTE, 0f);

            mAnimation.setDuration(300);
            mAnimation.setRepeatCount(0);
            mAnimation.setRepeatMode(Animation.REVERSE);
            mAnimation.setInterpolator(new LinearInterpolator());
            thrown_cards[tSeat].setAnimation(mAnimation);
        }


    }

    private void initializeViews() {
        cardsIV = new ImageView[13];
        thrown_cards = new ImageView[4];

        cardsIV[0] = findViewById(R.id.iv_card1);
        cardsIV[1] = findViewById(R.id.iv_card2);
        cardsIV[2] = findViewById(R.id.iv_card3);
        cardsIV[3] = findViewById(R.id.iv_card4);
        cardsIV[4] = findViewById(R.id.iv_card5);
        cardsIV[5] = findViewById(R.id.iv_card6);
        cardsIV[6] = findViewById(R.id.iv_card7);
        cardsIV[7] = findViewById(R.id.iv_card8);
        cardsIV[8] = findViewById(R.id.iv_card9);
        cardsIV[9] = findViewById(R.id.iv_card10);
        cardsIV[10] = findViewById(R.id.iv_card11);
        cardsIV[11] = findViewById(R.id.iv_card12);
        cardsIV[12] = findViewById(R.id.iv_card13);

        thrown_cards[0] = findViewById(R.id.thrown_card1);
        thrown_cards[1] = findViewById(R.id.thrown_card2);
        thrown_cards[2] = findViewById(R.id.thrown_card3);
        thrown_cards[3] = findViewById(R.id.thrown_card4);

        ll_color = findViewById(R.id.lay_color);
        img_color = findViewById(R.id.img_color);
        tv_claim = findViewById(R.id.text_claim);

        ll_color.setVisibility(View.INVISIBLE);

        score_board = findViewById(R.id.score_board);
        score_board.setVisibility(View.INVISIBLE);
        scoreA = findViewById(R.id.score_A);
        scoreB = findViewById(R.id.score_B);

        fee_score_board = findViewById(R.id.fee_score_board);
        fee_score_board.setVisibility(View.INVISIBLE);
        fee_score_A = findViewById(R.id.fee_score_A);
        fee_score_B = findViewById(R.id.fee_score_B);

        u1 = findViewById(R.id.tv_user1_name);
        u2 = findViewById(R.id.tv_user2_name);
        u3 = findViewById(R.id.tv_user3_name);

        claim_u1 = findViewById(R.id.claim_u1);
        claim_u2 = findViewById(R.id.claim_u2);
        claim_u3 = findViewById(R.id.claim_u3);
        claim_u4 = findViewById(R.id.claim_u4);

        claim_u1.setVisibility(View.INVISIBLE);
        claim_u2.setVisibility(View.INVISIBLE);
        claim_u3.setVisibility(View.INVISIBLE);
        claim_u4.setVisibility(View.INVISIBLE);

        myscore = findViewById(R.id.my_score);
        oppscore = findViewById(R.id.opp_score);
        points = findViewById(R.id.points);
        point_side = findViewById(R.id.point_color);

        thrown_cards[0].setVisibility(View.INVISIBLE);
        thrown_cards[1].setVisibility(View.INVISIBLE);
        thrown_cards[2].setVisibility(View.INVISIBLE);
        thrown_cards[3].setVisibility(View.INVISIBLE);

        score_bar = findViewById(R.id.score_bar);


        for (int i = 0; i <= 12; i++) {
            cardsIV[i].setVisibility(View.INVISIBLE);
            cardsIV[i].setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_card1:
                cardsIV[0].setVisibility(View.INVISIBLE);
                gameData.setNextChalCard(myCards[0].getCardNo());
                gameData.setNextChalData();
                updateDeckString(1);
                reference2.setValue(gameData);
                break;
            case R.id.iv_card2:
                cardsIV[1].setVisibility(View.INVISIBLE);
                gameData.setNextChalCard(myCards[1].getCardNo());
                gameData.setNextChalData();
                updateDeckString(2);
                reference2.setValue(gameData);
                break;

            case R.id.iv_card3:
                cardsIV[2].setVisibility(View.INVISIBLE);
                gameData.setNextChalCard(myCards[2].getCardNo());
                gameData.setNextChalData();
                updateDeckString(3);
                reference2.setValue(gameData);
                break;

            case R.id.iv_card4:
                cardsIV[3].setVisibility(View.INVISIBLE);
                gameData.setNextChalCard(myCards[3].getCardNo());
                gameData.setNextChalData();
                updateDeckString(4);
                reference2.setValue(gameData);
                break;

            case R.id.iv_card5:
                cardsIV[4].setVisibility(View.INVISIBLE);
                gameData.setNextChalCard(myCards[4].getCardNo());
                gameData.setNextChalData();
                updateDeckString(5);
                reference2.setValue(gameData);
                break;

            case R.id.iv_card6:
                cardsIV[5].setVisibility(View.INVISIBLE);
                gameData.setNextChalCard(myCards[5].getCardNo());
                gameData.setNextChalData();
                updateDeckString(6);
                reference2.setValue(gameData);
                break;

            case R.id.iv_card7:
                cardsIV[6].setVisibility(View.INVISIBLE);
                gameData.setNextChalCard(myCards[6].getCardNo());
                gameData.setNextChalData();
                updateDeckString(7);
                reference2.setValue(gameData);
                break;

            case R.id.iv_card8:
                cardsIV[7].setVisibility(View.INVISIBLE);
                gameData.setNextChalCard(myCards[7].getCardNo());
                gameData.setNextChalData();
                updateDeckString(8);
                reference2.setValue(gameData);
                break;

            case R.id.iv_card9:
                cardsIV[8].setVisibility(View.INVISIBLE);
                gameData.setNextChalCard(myCards[8].getCardNo());
                gameData.setNextChalData();
                updateDeckString(9);
                reference2.setValue(gameData);
                break;

            case R.id.iv_card10:
                cardsIV[9].setVisibility(View.INVISIBLE);
                gameData.setNextChalCard(myCards[9].getCardNo());
                gameData.setNextChalData();
                updateDeckString(10);
                reference2.setValue(gameData);
                break;

            case R.id.iv_card11:
                cardsIV[10].setVisibility(View.INVISIBLE);
                gameData.setNextChalCard(myCards[10].getCardNo());
                gameData.setNextChalData();
                updateDeckString(11);
                reference2.setValue(gameData);
                break;

            case R.id.iv_card12:
                cardsIV[11].setVisibility(View.INVISIBLE);
                gameData.setNextChalCard(myCards[11].getCardNo());
                gameData.setNextChalData();
                updateDeckString(12);
                reference2.setValue(gameData);
                break;

            case R.id.iv_card13:
                cardsIV[12].setVisibility(View.INVISIBLE);
                gameData.setNextChalCard(myCards[12].getCardNo());
                gameData.setNextChalData();
                updateDeckString(13);
                reference2.setValue(gameData);
                break;

            case R.id.btn_8:
                popupWindow.dismiss();
                gameData.setNextClaimNumber(8);
                gameData.setNextClaimData();
                reference2.setValue(gameData);
                break;
            case R.id.btn_9:
                popupWindow.dismiss();
                gameData.setNextClaimNumber(9);
                gameData.setNextClaimData();
                reference2.setValue(gameData);
                break;
            case R.id.btn_10:
                popupWindow.dismiss();
                gameData.setNextClaimNumber(10);
                gameData.setNextClaimData();
                reference2.setValue(gameData);
                break;
            case R.id.btn_11:
                popupWindow.dismiss();
                gameData.setNextClaimNumber(11);
                gameData.setNextClaimData();
                reference2.setValue(gameData);
                break;
            case R.id.btn_12:
                popupWindow.dismiss();
                gameData.setNextClaimNumber(12);
                gameData.setNextClaimData();
                reference2.setValue(gameData);
                break;
            case R.id.btn_13:
                popupWindow.dismiss();
                gameData.setNextClaimNumber(13);
                gameData.setNextClaimData();
                reference2.setValue(gameData);
                break;
            case R.id.btn_pass:
                popupWindow.dismiss();
                if (gameData.getClaim_number() == 1) {
                    gameData.setNextClaimNumber(7);
                } else {
                    gameData.setNextClaimNumber(0);
                }
                gameData.setNextClaimData();
                reference2.setValue(gameData);
                break;
            case R.id.btn_spades:
                popupWindow.dismiss();
                gameData.setClaim_color(1);
                gameData.setChalof(mySeatNo, 25);
                gameData.setState(GameData.CLAIM_READY);
                reference2.setValue(gameData);
                break;
            case R.id.btn_club:
                popupWindow.dismiss();
                gameData.setClaim_color(2);
                gameData.setChalof(mySeatNo, 25);
                gameData.setState(GameData.CLAIM_READY);
                reference2.setValue(gameData);
                break;
            case R.id.btn_heart:
                popupWindow.dismiss();
                gameData.setClaim_color(4);
                gameData.setChalof(mySeatNo, 25);
                gameData.setState(GameData.CLAIM_READY);
                reference2.setValue(gameData);
                break;
            case R.id.btn_diamond:
                popupWindow.dismiss();
                gameData.setClaim_color(3);
                gameData.setChalof(mySeatNo, 25);
                gameData.setState(GameData.CLAIM_READY);
                reference2.setValue(gameData);
                break;
            case R.id.btn_start:
                popupWindow2.dismiss();

                switch (mySeatNo) {
                    case 1:
                        reference2.child("chal_card_1").setValue(25);
                        break;
                    case 2:
                        reference2.child("chal_card_2").setValue(25);
                        break;
                    case 3:
                        reference2.child("chal_card_3").setValue(25);
                        break;
                    case 4:
                        reference2.child("chal_card_4").setValue(25);
                        break;

                }
                if (gameData.getClaim_seat() == mySeatNo) {
                    reference2.setValue(gameData);
                }
                break;
            case R.id.score_bar:
                popupforAllcards();
                break;
            case R.id.btn_u1:
                t_user = 1;
                deck = new Deck(gameData.getDeck());
                tempCards = deck.getCards(((t_user - 1) * 13), t_user * 13 - 1);
//                Arrays.sort(tempCards);

                for (int i = 0; i <= 12; i++) {
                    t_iv_cards[i].setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.btn_u2:
                t_user = 2;
                deck = new Deck(gameData.getDeck());
                tempCards = deck.getCards(((t_user - 1) * 13), t_user * 13 - 1);
//                Arrays.sort(tempCards);
                for (int i = 0; i <= 12; i++) {
                    t_iv_cards[i].setImageResource(tempCards[i].getRes());
                }
                break;
            case R.id.btn_u3:
                t_user = 3;
                deck = new Deck(gameData.getDeck());
                tempCards = deck.getCards(((t_user - 1) * 13), t_user * 13 - 1);
//                Arrays.sort(tempCards);
                for (int i = 0; i <= 12; i++) {
                    t_iv_cards[i].setImageResource(tempCards[i].getRes());
                }
                break;
            case R.id.btn_u4:
                t_user = 4;
                deck = new Deck(gameData.getDeck());
                tempCards = deck.getCards(((t_user - 1) * 13), t_user * 13 - 1);
//                Arrays.sort(tempCards);
                for (int i = 0; i <= 12; i++) {
                    t_iv_cards[i].setImageResource(tempCards[i].getRes());
                }
                break;

            case R.id.btn_continue:
                popupWindow.dismiss();

                switch (mySeatNo) {
                    case 1:
                        reference2.child("chal_card_1").setValue(25);
                        gameData.setChal_card_1(25);
                        message.setText("Please Wait For all to get ready");
                        break;
                    case 2:
                        reference2.child("chal_card_2").setValue(25);
                        message.setText("Please Wait For all to get ready");
                        break;
                    case 3:
                        reference2.child("chal_card_3").setValue(25);
                        message.setText("Please Wait For all to get ready");
                        break;
                    case 4:
                        reference2.child("chal_card_4").setValue(25);
                        message.setText("Please Wait For all to get ready");
                        break;
                }

                if(gameData.getChal_card_1() == 25 &&
                        gameData.getChal_card_2() == 25 &&
                        gameData.getChal_card_3() == 25 &&
                        gameData.getChal_card_4() == 25 && mySeatNo == 1){
                    gameData.setScoreA(0);
                    gameData.setFeesA(0);
                    gameData.setFeesB(0);
                    gameData.setState(GameData.IDEAL);
                    reference2.setValue(gameData);
                }
                break;


        }
    }

    private void popupforAllcards() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.popup_cardview, null);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        final int width = dm.widthPixels;
        final int height = dm.heightPixels;

        boolean focusable = true;
        popupWindow = new PopupWindow(popupView, (int) (width * 0.7), (int) (height * 0.6), focusable);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.setElevation(20);
        }

        findViewById(R.id.game_layout).post(new Runnable() {
            @Override
            public void run() {
                popupWindow.showAtLocation(findViewById(R.id.game_layout), Gravity.CENTER, 0, 0);
            }
        });
        popupWindow.setOutsideTouchable(false);

//        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent motionEvent) {
//                if (motionEvent.getX() < 0 || motionEvent.getX() > (int) (width * 0.46))
//                    return true;
//                if (motionEvent.getY() < 0 || motionEvent.getY() > (int) (height * 0.3))
//                    return true;
//                return false;
//            }
//        });


        tuser1 = popupView.findViewById(R.id.btn_u1);
        tuser2 = popupView.findViewById(R.id.btn_u2);
        tuser3 = popupView.findViewById(R.id.btn_u3);
        tuser4 = popupView.findViewById(R.id.btn_u4);

        tuser1.setOnClickListener(this);
        tuser2.setOnClickListener(this);
        tuser3.setOnClickListener(this);
        tuser4.setOnClickListener(this);

        tuser1.setText(gameData.getPlayerAtSeat(1));
        tuser2.setText(gameData.getPlayerAtSeat(2));
        tuser3.setText(gameData.getPlayerAtSeat(3));
        tuser4.setText(gameData.getPlayerAtSeat(4));

        t_iv_cards = new ImageView[13];


        t_iv_cards[0] = popupView.findViewById(R.id.iv_cardt1);
        t_iv_cards[1] = popupView.findViewById(R.id.iv_cardt2);
        t_iv_cards[2] = popupView.findViewById(R.id.iv_cardt3);
        t_iv_cards[3] = popupView.findViewById(R.id.iv_cardt4);
        t_iv_cards[4] = popupView.findViewById(R.id.iv_cardt5);
        t_iv_cards[5] = popupView.findViewById(R.id.iv_cardt7);
        t_iv_cards[6] = popupView.findViewById(R.id.iv_cardt8);
        t_iv_cards[7] = popupView.findViewById(R.id.iv_cardt6);
        t_iv_cards[8] = popupView.findViewById(R.id.iv_cardt9);
        t_iv_cards[9] = popupView.findViewById(R.id.iv_cardt10);
        t_iv_cards[10] = popupView.findViewById(R.id.iv_cardt11);
        t_iv_cards[11] = popupView.findViewById(R.id.iv_cardt12);
        t_iv_cards[12] = popupView.findViewById(R.id.iv_cardt13);

        deck = new Deck(gameData.getDeck());
        tempCards = deck.getCards(((t_user - 1) * 13), t_user * 13 - 1);
        for (int i = 0; i <= 12; i++) {
            t_iv_cards[i].setImageResource(tempCards[i].getRes());
        }
    }

    private void popupforCongratulate(Boolean isWinner) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.popup_congratulations, null);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        final int width = dm.widthPixels;
        final int height = dm.heightPixels;

        boolean focusable = true;
        popupWindow = new PopupWindow(popupView, (int) (width * 0.7), (int) (height * 0.6), focusable);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.setElevation(20);
        }

        findViewById(R.id.game_layout).post(new Runnable() {
            @Override
            public void run() {
                popupWindow.showAtLocation(findViewById(R.id.game_layout), Gravity.CENTER, 0, 0);
            }
        });
        popupWindow.setOutsideTouchable(false);
        TextView message;

        if(isWinner){
            message = popupView.findViewById(R.id.text_congratulation);
            message.setText("Congratulation");
            message = popupView.findViewById(R.id.text_you_won);
            message.setText("You won This game");

        }else {
            message = popupView.findViewById(R.id.text_congratulation);
            message.setText("You Lost");
            message = popupView.findViewById(R.id.text_you_won);
            message.setText("Better Luck Next Time");
        }


        Button conti;
        conti = popupView.findViewById(R.id.btn_continue);
        conti.setOnClickListener(this);

    }

    private void updateDeckString(int position) {
        gameData.setThrowncardindeck(mySeatNo, position);
    }

    private void updateDeckStringForPC(int playedCard) {
        gameData.setThrowncardindeck(playedCard);
    }
}