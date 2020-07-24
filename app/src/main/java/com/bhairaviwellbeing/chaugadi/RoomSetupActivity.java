package com.bhairaviwellbeing.chaugadi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class RoomSetupActivity extends AppCompatActivity implements View.OnClickListener {

    Button start;
    Button add_pc;

    LinearLayout player1_dp;
    LinearLayout player2_dp;
    LinearLayout player3_dp;
    LinearLayout player4_dp;
    RelativeLayout relativeLayout;

    TextView room_tv;

    TextView player1,player1DP;
    TextView player2,player2DP;
    TextView player3,player3DP;
    TextView player4,player4DP;

    String player1_name;
    String player2_name;
    String player3_name;
    String player4_name;

    String player1_id;
    String player2_id;
    String player3_id;
    String player4_id;

    int seat;

    Boolean isRegistered = false;
    int[] seats = new int[]{1,2,3,4};

    Intent intent;
    String room_no,playerno;
    FirebaseUser user;
    DatabaseReference reference,reference2,reference3,reference4;
    ValueEventListener ve,ve1,ve2,ve3;
    HashMap<DatabaseReference,ValueEventListener> hashMap;
    MediaPlayer doorsound;
    GameData gameData;


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit Room!!!")
                .setMessage("Do you want to Exit Room:"+room_no+"?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        reference2.removeEventListener(ve1);
                        reference.child("room").setValue("no-room");
                        switch (gameData.getMyPosition(user.getUid())){
                            case 1:
                                reference2.child("id1").setValue("empty");
                                break;
                            case 2:
                                reference2.child("id2").setValue("empty");
                                break;
                            case 3:
                                reference2.child("id3").setValue("empty");
                                break;
                            case 4:
                                reference2.child("id4").setValue("empty");
                                break;
                        }

                        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                gameData = dataSnapshot.getValue(GameData.class);
                                if(gameData.getId1().equals("empty")
                                        && gameData.getId2().equals("empty")
                                        && gameData.getId3().equals("empty")
                                        && gameData.getId4().equals("empty")){
                                    reference2.removeValue();
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("room_creation").child("Rooms_list");
                                    ref.child(room_no).removeValue();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        Intent intent1 = new Intent(RoomSetupActivity.this,TestActivity.class);
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_setup);


        intent = getIntent();
        setupView();
        player1.setOnClickListener(this);
        player2.setOnClickListener(this);
        player3.setOnClickListener(this);
        player4.setOnClickListener(this);
        start.setOnClickListener(this);
        add_pc.setOnClickListener(this);



    }

    @Override
    protected void onResume() {
        super.onResume();

        room_no = intent.getStringExtra("Room");
        playerno = intent.getStringExtra("Player");
        doorsound = MediaPlayer.create(RoomSetupActivity.this,R.raw.doorclose);

        room_tv.setText("Room\n"+room_no);

        hashMap =new HashMap<>();

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        reference2 = FirebaseDatabase.getInstance().getReference("Rooms").child(room_no);

        if(playerno.equals("one")) {

            ve1 = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = new User();
                    user = dataSnapshot.getValue(User.class);
                    player1.setText(user.getUsername());
                    reference2.child("player1").setValue(user.getUsername());
                    reference2.child("id1").setValue(user.getId());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            reference.addListenerForSingleValueEvent(ve1);
            hashMap.put(reference,ve1);

            ve2 = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    gameData = dataSnapshot.getValue(GameData.class);

                    player1_name = gameData.getPlayer1();
                    player2_name = gameData.getPlayer2();
                    player3_name = gameData.getPlayer3();
                    player4_name = gameData.getPlayer4();

                    player1_id =gameData.getId1();
                    player2_id =gameData.getId2();
                    player3_id =gameData.getId3();
                    player4_id =gameData.getId4();

                    add_pc.setVisibility(View.VISIBLE);



                    if (!player2_name.equals("empty")) {
                        doorsound.start();

                        player2.setText(player2_name);
                        player2.setVisibility(View.VISIBLE);
                    }

                    if (!player3_name.equals("empty")) {
                        doorsound.start();

                        player3.setText(player3_name);
                        player3.setVisibility(View.VISIBLE);
                    }

                    if (!player4_name.equals("empty")) {
                        doorsound.start();

                        player4.setText(player4_name);
                        player4.setVisibility(View.VISIBLE);

                        add_pc.setEnabled(false);

                        player1DP.setText(player1_name);
                        relativeLayout.setVisibility(View.VISIBLE);
                        player2_dp.setVisibility(View.VISIBLE);

                        player2.setClickable(true);
                        player3.setClickable(true);
                        player4.setClickable(true);
                        seat = 2;

                        TranslateAnimation mAnimation = new TranslateAnimation(
                                TranslateAnimation.ABSOLUTE, 0f,
                                TranslateAnimation.ABSOLUTE, 0f,
                                TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                                TranslateAnimation.RELATIVE_TO_PARENT, 0.05f);

                        mAnimation.setDuration(300);
                        mAnimation.setRepeatCount(-1);
                        mAnimation.setRepeatMode(Animation.REVERSE);
                        mAnimation.setInterpolator(new LinearInterpolator());
                        player2_dp.setAnimation(mAnimation);

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            reference2.addValueEventListener(ve2);
            hashMap.put(reference2,ve2);

        }else {

            ve2 = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    gameData = dataSnapshot.getValue(GameData.class);

                    player1_name = gameData.getPlayer1();
                    player2_name = gameData.getPlayer2();
                    player3_name = gameData.getPlayer3();
                    player4_name = gameData.getPlayer4();


                    if (!player1_name.equals("empty")) {
                        doorsound.start();

                        player1.setText(player1_name);
                        player1.setVisibility(View.VISIBLE);
                    }

                    if (!player2_name.equals("empty")) {
                        doorsound.start();

                        player2.setText(player2_name);
                        player2.setVisibility(View.VISIBLE);
                    }

                    if (!player3_name.equals("empty")) {
                        doorsound.start();

                        player3.setText(player3_name);
                        player3.setVisibility(View.VISIBLE);
                    }

                    if (!player4_name.equals("empty")) {
                        doorsound.start();

                        player4.setText(player4_name);
                        player4.setVisibility(View.VISIBLE);
                    }


                    if(gameData.isGameStarted()){

                        player1DP.setText(gameData.getPlayerAtSeat(gameData.getMyPosition(user.getUid())));


                        player2DP.setText(
                                gameData.getPlayerAtSeat(
                                        gameData.getnextSeat(
                                                gameData.getMyPosition(
                                                        user.getUid()))));

                        player3DP.setText(gameData.getPlayerAtSeat(
                                gameData.getnextSeat(
                                        gameData.getnextSeat(
                                                gameData.getMyPosition(
                                                        user.getUid())))));
                        player4DP.setText(
                                gameData.getPlayerAtSeat(
                                        gameData.getnextSeat(
                                                gameData.getnextSeat(
                                                        gameData.getnextSeat(
                                                                gameData.getMyPosition(
                                                                        user.getUid()))))));

                        player1.setVisibility(View.GONE);
                        player2.setVisibility(View.GONE);
                        player3.setVisibility(View.GONE);
                        player4.setVisibility(View.GONE);

                        relativeLayout.setVisibility(View.VISIBLE);
                        player1_dp.setVisibility(View.VISIBLE);
                        player2_dp.setVisibility(View.VISIBLE);
                        player3_dp.setVisibility(View.VISIBLE);
                        player4_dp.setVisibility(View.VISIBLE);

                        start.setEnabled(true);
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            reference2.addValueEventListener(ve2);
            hashMap.put(reference2,ve2);

        }

    }

    private void setupView() {
        start = findViewById(R.id.btn_start);
        add_pc = findViewById(R.id.add_pc);

        player1_dp = findViewById(R.id.linlay_player1);
        player2_dp = findViewById(R.id.linlay_player2);
        player3_dp = findViewById(R.id.linlay_player3);
        player4_dp = findViewById(R.id.linlay_player4);
        room_tv = findViewById(R.id.room_id_textview);
        relativeLayout = findViewById(R.id.relative_ch);

        player1 = findViewById(R.id.player_1);
        player2 = findViewById(R.id.player_2);
        player3 = findViewById(R.id.player_3);
        player4 = findViewById(R.id.player_4);

        player1DP =findViewById(R.id.player_1_dp);
        player2DP =findViewById(R.id.player_2_dp);
        player3DP =findViewById(R.id.player_3_dp);
        player4DP =findViewById(R.id.player_4_dp);

        player2_dp.setVisibility(View.GONE);
        player3_dp.setVisibility(View.GONE);
        player4_dp.setVisibility(View.GONE);

        start.setEnabled(false);
        add_pc.setEnabled(true);
        add_pc.setVisibility(View.INVISIBLE);

        player2.setVisibility(View.GONE);
        player3.setVisibility(View.GONE);
        player4.setVisibility(View.GONE);

        player1.setClickable(false);
        player2.setClickable(false);
        player3.setClickable(false);
        player4.setClickable(false);

        relativeLayout.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {

        player3_dp.clearAnimation();
        player4_dp.clearAnimation();


        switch (v.getId()){

            case R.id.player_2:
                if(seat == 2){
                    player2DP.setText(player2_name);
                    player2.setClickable(false);

                    player2_dp.clearAnimation();
                    player3_dp.setVisibility(View.VISIBLE);

                    TranslateAnimation mAnimation = new TranslateAnimation(
                            TranslateAnimation.ABSOLUTE, 0f,
                            TranslateAnimation.ABSOLUTE, 0f,
                            TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                            TranslateAnimation.RELATIVE_TO_PARENT, 0.05f);

                    mAnimation.setDuration(300);
                    mAnimation.setRepeatCount(-1);
                    mAnimation.setRepeatMode(Animation.REVERSE);
                    mAnimation.setInterpolator(new LinearInterpolator());
                    player3_dp.setAnimation(mAnimation);
                    seat++;

                }else {if(seat == 3){
                    player2.setClickable(false);
                    player3.setClickable(false);
                    player4.setClickable(false);

                    seats[2]=2;
                    player3DP.setText(player2_name);
                    player3_dp.clearAnimation();

                    player4_dp.setVisibility(View.VISIBLE);

                    if (seats[1] == 3) {
                        seats[3]= 4;
                        player4DP.setText(player4_name);
                    }else {
                        seats[3]=3;
                        player4DP.setText(player3_name);

                        player1.setVisibility(View.GONE);
                        player2.setVisibility(View.GONE);
                        player3.setVisibility(View.GONE);
                        player4.setVisibility(View.GONE);
                    }
                    start.setEnabled(true);
                }
                }
                break;

            case R.id.player_3:
                if(seat == 2){
                    player2DP.setText(player3_name);
                    player3.setClickable(false);

                    player2_dp.clearAnimation();
                    player3_dp.setVisibility(View.VISIBLE);

                    TranslateAnimation mAnimation = new TranslateAnimation(
                            TranslateAnimation.ABSOLUTE, 0f,
                            TranslateAnimation.ABSOLUTE, 0f,
                            TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                            TranslateAnimation.RELATIVE_TO_PARENT, 0.05f);

                    mAnimation.setDuration(300);
                    mAnimation.setRepeatCount(-1);
                    mAnimation.setRepeatMode(Animation.REVERSE);
                    mAnimation.setInterpolator(new LinearInterpolator());
                    player3_dp.setAnimation(mAnimation);
                    seat++;
                    seats[1]=3;
                }else {if(seat == 3){
                    player2.setClickable(false);
                    player3.setClickable(false);
                    player4.setClickable(false);
                    seats[2]=3;
                    player3DP.setText(player3_name);

                    player3_dp.clearAnimation();
                    player4_dp.setVisibility(View.VISIBLE);


                    if (seats[1] == 2) {
                        seats[3]= 4;
                        player4DP.setText(player4_name);
                    }else {
                        seats[3]=2;
                        player4DP.setText(player2_name);
                        player1.setVisibility(View.GONE);
                        player2.setVisibility(View.GONE);
                        player3.setVisibility(View.GONE);
                        player4.setVisibility(View.GONE);
                    }
                    start.setEnabled(true);

                }

                }
                break;

            case R.id.player_4:
                if(seat == 2){
                    player2DP.setText(player4_name);
                    player4.setClickable(false);
                    player2_dp.clearAnimation();
                    player3_dp.setVisibility(View.VISIBLE);

                    TranslateAnimation mAnimation = new TranslateAnimation(
                            TranslateAnimation.ABSOLUTE, 0f,
                            TranslateAnimation.ABSOLUTE, 0f,
                            TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                            TranslateAnimation.RELATIVE_TO_PARENT, 0.05f);

                    mAnimation.setDuration(300);
                    mAnimation.setRepeatCount(-1);
                    mAnimation.setRepeatMode(Animation.REVERSE);
                    mAnimation.setInterpolator(new LinearInterpolator());
                    player3_dp.setAnimation(mAnimation);


                    seat++;
                    seats[1]=4;
                }else {if(seat == 3){
                    player2.setClickable(false);
                    player3.setClickable(false);
                    player4.setClickable(false);

                    seats[2]=4;
                    player3DP.setText(player4_name);
                    player4_dp.setVisibility(View.VISIBLE);

                    if (seats[1] == 2) {
                        seats[3]= 3;
                        player4DP.setText(player3_name);
                    }else {
                        seats[3]=2;
                        player4DP.setText(player2_name);
                        player1.setVisibility(View.GONE);
                        player2.setVisibility(View.GONE);
                        player3.setVisibility(View.GONE);
                        player4.setVisibility(View.GONE);
                    }
                    start.setEnabled(true);

                }

                }
                break;

            case R.id.btn_start:

                if(playerno.equals("other")){

                    switch (gameData.getMyPosition(user.getUid())){
                        case 2:
                            gameData.setChal_card_2(25);
                            reference2.setValue(gameData);
                            break;
                        case 3:
                            gameData.setChal_card_3(25);
                            reference2.setValue(gameData);
                            break;
                        case 4:
                            gameData.setChal_card_4(25);
                            reference2.setValue(gameData);
                            break;
                    }
                    Intent intent = new Intent(RoomSetupActivity.this,GameActivity.class);
                    intent.putExtra("Room",room_no);
                    intent.putExtra("Activity","RoomSetupActivity");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else {
                    start.setEnabled(false);
                    start.setText("Please Wait...");

                    final String[] pnames = new String[]{player1_name, player2_name, player3_name, player4_name};
                    final String[] pid = new String[]{player1_id, player2_id, player3_id, player4_id,};


//                    reference3 = FirebaseDatabase.getInstance().getReference("Rooms").child(room_no);
//                    hashMap.put(reference3,ve3);
//                    ve3 = new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            GameData gameData = dataSnapshot.getValue(GameData.class);

                            gameData.setPlayer2(pnames[seats[1] - 1]);
                            gameData.setPlayer3(pnames[seats[2] - 1]);
                            gameData.setPlayer4(pnames[seats[3] - 1]);

                            gameData.setId1(pid[seats[0]-1]);
                            gameData.setId2(pid[seats[1]-1]);
                            gameData.setId3(pid[seats[2]-1]);
                            gameData.setId4(pid[seats[3]-1]);

                            gameData.setShufflingSeat(2);
                            gameData.setState(GameData.IDEAL);
                            gameData.setChal_card_1(25);

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

                            gameData.setGameStarted(true);

                            reference2.setValue(gameData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(RoomSetupActivity.this, GameActivity.class);
                                        intent.putExtra("Room",room_no);
                                        intent.putExtra("Activity","RoomSetupActivity");
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }
                            });

//                        }

//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    };
//                    reference3.addValueEventListener(ve3);


                }
                break;

            case R.id.add_pc:

                if(gameData.getPlayer2().equals("empty")){
                    gameData.addPlayer("Com1","COMPUTER1");
                    gameData.setChal_card_2(25);
                    reference2.setValue(gameData);
                }else if(gameData.getPlayer3().equals("empty")){
                    gameData.addPlayer("Com2","COMPUTER2");
                    gameData.setChal_card_3(25);
                    reference2.setValue(gameData);
                }else if(gameData.getPlayer4().equals("empty")){
                    gameData.addPlayer("Com3","COMPUTER3");
                    gameData.setChal_card_4(25);
                    reference2.setValue(gameData);
                }else {

                }


                break;

                default:
                //code
                break;


        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            for (Map.Entry<DatabaseReference,ValueEventListener> entry : hashMap.entrySet()){
                DatabaseReference ref = entry.getKey();
                ValueEventListener ve = entry.getValue();
                ref.removeEventListener(ve);
            }

            if(doorsound.isPlaying()){
                doorsound.stop();
            }
            doorsound.release();
        }catch (Exception e)
        {
            Toast.makeText(RoomSetupActivity.this,"Error:"+e.toString(),Toast.LENGTH_SHORT).show();
        }

    }
}
