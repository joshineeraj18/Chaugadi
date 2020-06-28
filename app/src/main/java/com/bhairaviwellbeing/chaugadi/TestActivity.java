package com.bhairaviwellbeing.chaugadi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bhairaviwellbeing.chaugadi.Fragments.CreateRoomFragment;
import com.bhairaviwellbeing.chaugadi.Fragments.JoinRoomFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {

    ViewPager viewPager_r;
    TabLayout tab_r;
    public LinearLayout lin_lay_room;
    public String room_no;
    MediaPlayer  mp;
    TextView room_no_tv;

    DatabaseReference reference;
    FirebaseUser user;


    @Override
    protected void onResume() {
        super.onResume();
        mp = MediaPlayer.create(TestActivity.this, R.raw.back1);
        mp.start();

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());

        reference.child("room").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String room_no = dataSnapshot.getValue(String.class);
                if(!room_no.equals("no-room")){
                    Intent intent = new Intent(TestActivity.this,GameActivity.class);
                    intent.putExtra("Room",room_no);
                    intent.putExtra("Activity","TestActivity");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mp.isPlaying()){
            mp.stop();
            mp.release();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        viewPager_r = findViewById(R.id.viewpager_room);
        tab_r = findViewById(R.id.tab_room);
        lin_lay_room = findViewById(R.id.linlay_room);
        room_no_tv = findViewById(R.id.room_id_textview);


        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(new JoinRoomFragment(),"Join Room");
        viewPagerAdapter.addFragment(new CreateRoomFragment(),"Create Room");

        viewPager_r.setAdapter(viewPagerAdapter);

        tab_r.setupWithViewPager(viewPager_r);



    }
}

class ViewPagerAdapter extends FragmentPagerAdapter{

    private ArrayList<Fragment> fragments;
    private ArrayList<String>  titles;


    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        this.fragments = new ArrayList<>();
        this.titles = new ArrayList<>();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addFragment(Fragment fragment, String title){
        fragments.add(fragment);
        titles.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
