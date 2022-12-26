package com.example.yallatour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import dashboard.AdminMainActivity;
import modules.User;
import util.Constant;
import util.Global;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        TextView texy = findViewById(R.id.texy);
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
                                    startActivity(new Intent(WelcomeActivity.this, dashboard.AdminMainActivity.class));
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