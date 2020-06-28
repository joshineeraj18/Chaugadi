package com.bhairaviwellbeing.chaugadi.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.bhairaviwellbeing.chaugadi.GameData;
import com.bhairaviwellbeing.chaugadi.NextRoomID;
import com.bhairaviwellbeing.chaugadi.R;
import com.bhairaviwellbeing.chaugadi.Room;
import com.bhairaviwellbeing.chaugadi.RoomSetupActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateRoomFragment extends Fragment {

    Button btn_create_room;
    String room_ID,setNext;
    Boolean isroomcreated = false;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_room, container, false);

        btn_create_room = view.findViewById(R.id.btn_create_room);





        btn_create_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_create_room.setEnabled(false);
                btn_create_room.setText("Please Wait");
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final DatabaseReference nextroomref = FirebaseDatabase.getInstance().getReference("room_creation").child("newnextroom");
                final DatabaseReference userroomref = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("room");
                final DatabaseReference creatingroomref = FirebaseDatabase.getInstance().getReference("Rooms");
                final DatabaseReference roomlistref = FirebaseDatabase.getInstance().getReference("room_creation").child("Rooms_list");

                roomlistref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        while (!isroomcreated){
                            final String roomno =String.format("%6d",100000+(int)((899999)* Math.random()));
                            if(!dataSnapshot.hasChild(roomno)){
                                isroomcreated = true;
                                GameData newGameData = new GameData();
                                creatingroomref.child(roomno).setValue(newGameData);

                                Room room = new Room(roomno);
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("room_creation").child("Rooms_list").child(room.getRoom_name());
                                reference.setValue(room);

                                userroomref.setValue(roomno);

                                Intent intent = new Intent(getContext(), RoomSetupActivity.class);
                                intent.putExtra("Room",roomno);
                                intent.putExtra("Player","one");
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                                try {
                                    roomlistref.removeEventListener(this);
                                }catch (Exception e){
                                    Toast.makeText(getContext(),e.toString(),Toast.LENGTH_SHORT);
                                }
                                startActivity(intent);
                                getActivity().finish();
                            }
                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

//                nextroomref.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                        NextRoomID roomID = dataSnapshot.getValue(NextRoomID.class);
//                        if(roomID.isIsroomopen()){
//
//                            roomID.setIsroomopen(false);
//                            nextroomref.setValue(roomID);
//
//                            room_ID = roomID.getNexrroomid();
//                            setNext = room_ID.substring(0,1)+Integer.toString((Integer.parseInt(room_ID.substring(1))) + 1);
//                            roomID.setNexrroomid(setNext);
//                            roomID.setIsroomopen(true);
//                            nextroomref.setValue(roomID);
//
//                            userroomref.setValue(room_ID);
//
//                            GameData newGameData = new GameData();
//                            creatingroomref.child(room_ID).setValue(newGameData);
//
//                            Room room = new Room(room_ID);
//                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("room_creation").child("Rooms_list").child(room.getRoom_name());
//                            reference.setValue(room);
//
//                            Intent intent = new Intent(getContext(), RoomSetupActivity.class);
//                            intent.putExtra("Room",room_ID);
//                            intent.putExtra("Player","one");
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            try {
//                                nextroomref.removeEventListener(this);
//                            }catch (Exception e){
//                            }
//                            startActivity(intent);
//
////                            ((TestActivity)getActivity()).room_no = room_ID;
////                           ((TestActivity)getActivity()).lin_lay_room.setVisibility(View.GONE);
//
//                        }else {
//                            Toast.makeText(getContext(),"Retry after some time",Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });



            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();

    }
}
