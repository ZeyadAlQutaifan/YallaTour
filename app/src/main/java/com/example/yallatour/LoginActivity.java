package com.example.yallatour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import dashboard.AdminMainActivity;
import modules.User;
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
            ProgressDialog mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setTitle("Signing in ");
            mProgressDialog.setMessage("please wait ");
            mProgressDialog.show();
            Constant.AUTH.signInWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            // if login is success
                            Constant.USER = Constant.AUTH.getCurrentUser();
                   //         if (Constant.USER.isEmailVerified()) {
                                mProgressDialog.dismiss();
                                String id = Constant.USER.getUid();
                                Global.getUser(id).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        User user = snapshot.getValue(User.class);
                                        if (user != null) {
                                            Constant.isAdmin = user.isAdmin();
                                            if (Constant.isAdmin) {
                                                startActivity(new Intent(LoginActivity.this, AdminMainActivity.class));
                                                finish();
                                            } else {
                                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                                finish();
                                            }
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                          /*  } else {
                                Constant.USER.sendEmailVerification()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                                Global.dialogYesNo(LoginActivity.this, "Verification Email", "Email Verification Hase been sent to your email address \n please verify your email then login", true, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.dismiss();
                                                        Constant.AUTH.signOut();
                                                    }
                                                });
                                                Log.v(Constant.TAG_V, "send email verification ");
                                                Constant.AUTH.signOut();
                                            }
                                        });
                            }*/
                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Global.dialogYesNo(LoginActivity.this, "Error", e.getMessage(), true, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    mProgressDialog.dismiss();
                                }
                            });
                            Log.v(Constant.TAG_V, e.getMessage());
                        }
                    });
        }

    }

}