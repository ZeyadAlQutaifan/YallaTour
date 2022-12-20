package com.example.yallatour;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import util.Constant;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        TextView texy = findViewById(R.id.texy);
        Handler vehiclehandler = new Handler();
        vehiclehandler.postDelayed(new Runnable(){
            public void run(){
                if(Constant.AUTH.getCurrentUser() != null){



                    startActivity(new Intent(WelcomeActivity.this , MainActivity.class));
                    finish();

                }
                else {
                    startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                }

            }

        },5000);

    }

    public void slideUp(View view){
        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));

      //  customType(WelcomeActivity.this,"bottom-to-up");
    }
}