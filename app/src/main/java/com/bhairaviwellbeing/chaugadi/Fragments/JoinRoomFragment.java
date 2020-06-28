package com.bhairaviwellbeing.chaugadi.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bhairaviwellbeing.chaugadi.GameData;
import com.bhairaviwellbeing.chaugadi.R;
import com.bhairaviwellbeing.chaugadi.RoomSetupActivity;
import com.bhairaviwellbeing.chaugadi.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class JoinRoomFragment extends Fragment {


    Button join_room;
    EditText roomid;
    String room_id;
    FirebaseUser user;
    DatabaseReference reference;
    DatabaseReference reference2;
    String username,id;
    int playerNo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_join_room, container, false);
        join_room = view.findViewById(R.id.btn_join_room);
        roomid = view.findViewById(R.id.room_id_input);

        //Getting user ID and Username
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = new User();
                user = dataSnapshot.getValue(User.class);
                username = user.getUsername();
                id = user.getId();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        join_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                join_room.setEnabled(false);
                join_room.setText("Please Wait");

                if(roomid.getText().toString().equals("")){
                    Toast.makeText(getContext(),"Room ID can not left blank",Toast.LENGTH_SHORT).show();
                    join_room.setText("Submit");
                    join_room.setEnabled(true);
                }else{
                    room_id = roomid.getText().toString();

                    reference = FirebaseDatabase.getInstance().getReference("room_creation").child("Rooms_list");
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(room_id)){
                                reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("room");
                                reference.setValue(room_id);
                                reference.removeEventListener(this);
                                reference2 = FirebaseDatabase.getInstance().getReference("Rooms").child(room_id);
                                reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        GameData gameData = dataSnapshot.getValue(GameData.class);

                                        playerNo = gameData.addPlayer(username,user.getUid());
                                        if(playerNo != 0){
                                            reference2.setValue(gameData);

                                            Intent intent = new Intent(getContext(), RoomSetupActivity.class);
                                            intent.putExtra("Room", room_id);
                                            intent.putExtra("Player", "other");
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            reference2.removeEventListener(this);
                                            startActivity(intent);
                                        }else {
                                            Toast.makeText(getContext(),"Room is Full",Toast.LENGTH_SHORT).show();
                                            join_room.setEnabled(true);
                                            join_room.setText("Submit");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }else {
                                Toast.makeText(getContext(),"Room does not exist!",Toast.LENGTH_SHORT).show();
                                join_room.setEnabled(true);
                                join_room.setText("Submit");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
        return view;
    }
}
