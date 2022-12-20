package com.example.yallatour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;

import java.util.ArrayList;
import java.util.List;

import dashboard.AdminMainActivity;
import util.Constant;
import util.Global;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void toSignup(View view) {

        startActivity(new Intent(LoginActivity.this, SignupActivity.class));
    }

    public void login(View view) {
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);
        List<TextView> inputs = new ArrayList<>();
        inputs.add(etEmail);
        inputs.add(etPassword);
        if (Global.validField(inputs)) {

            Constant.AUTH.signInWithEmailAndPassword(etEmail.getText().toString().trim(), etPassword.getText().toString().trim())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Constant.USER = Constant.AUTH.getCurrentUser();
                            if (Constant.USER.isEmailVerified()) {
                                if(!isAdmin()){
                                    startActivity(new Intent(LoginActivity.this, AdminMainActivity.class));
                                }else {
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                }
                                finish();
                            }else{
                                Constant.USER.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                       Global.dialogYesNo(LoginActivity.this, "Verification Email" , "Email Verification Hase been sent to your email address \n please verify your email then login" , true , new DialogInterface.OnClickListener() {
                                           public void onClick(DialogInterface dialog, int id) {
                                               dialog.dismiss();
                                               Constant.AUTH.signOut();
                                           }
                                       });
                                        Log.v(Constant.TAG_V , "send email verification ");
                                        Constant.AUTH.signOut();
                                    }
                                });
                            }
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.v(Constant.TAG_V , e.getMessage());
                        }
                    });
        }

    }
    private synchronized boolean isAdmin(){
        Constant.isAdmin = true;
        return true ;
    }
}