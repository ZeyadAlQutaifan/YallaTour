package com.example.meshwar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import dashboard.AdminMainActivity;
import modules.User;
import util.Constant;
import util.Global;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Handler vehiclehandler = new Handler();
        vehiclehandler.postDelayed(new Runnable() {
            public void run() {

                if (Constant.AUTH.getCurrentUser() != null) {

                    Constant.USER = Constant.AUTH.getCurrentUser();

                    String id = Constant.USER.getUid();

                    Global.getUser(id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            User user = snapshot.getValue(User.class);
                            if(user != null){
                                Constant.isAdmin =user.isAdmin();
                                if (Constant.isAdmin) {
                                    startActivity(new Intent(WelcomeActivity.this, AdminMainActivity.class));
                                    finish();
                                } else {
                                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                                    finish();
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });




                } else {
                    startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                }

            }

        }, 50);

    }



    public void slideUp(View view) {
        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));

        //  customType(WelcomeActivity.this,"bottom-to-up");
    }
}