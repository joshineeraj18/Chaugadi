package com.bhairaviwellbeing.chaugadi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    MaterialEditText username, email, password;
    Button btn_register;


    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_register = findViewById(R.id.btn_register);


        auth = FirebaseAuth.getInstance();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_register.setEnabled(false);
                btn_register.setText("Please Wait...");
                String txt_username = username.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                if(TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_username) ){
                    Toast.makeText(RegisterActivity.this, "All fields are required",Toast.LENGTH_SHORT).show();
                    btn_register.setEnabled(true);
                    btn_register.setText("Register");
                }else if (txt_password.length() < 6 ){
                    Toast.makeText(RegisterActivity.this, "Password must have atleast 6 characters",Toast.LENGTH_SHORT).show();
                    btn_register.setEnabled(true);
                    btn_register.setText("Register");
                }else {
                    register(txt_username,txt_email,txt_password);
                }
            }
        });
        
        
        



    }

    private void register(final String username, String email, String password) {

        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //Start Paste
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            final HashMap<String, String> hashmap = new HashMap<>();
                            hashmap.put("id",userid);
                            hashmap.put("username", username);
                            hashmap.put("imageURL", "default");
                            hashmap.put("status", "offline");
                            hashmap.put("search", username.toLowerCase());
                            hashmap.put("score","0");
                            hashmap.put("room","no-room");

                            reference.setValue(hashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(RegisterActivity.this, TestActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }

                                }
                            });
                        }else{
                            Toast.makeText(RegisterActivity.this, "You can't register with this email or password",Toast.LENGTH_SHORT).show();
                            btn_register.setEnabled(true);
                            btn_register.setText("Register");
                        }

                        //End Paste

                    }
                });
    }
}
