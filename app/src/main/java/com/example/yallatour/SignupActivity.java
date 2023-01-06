package com.example.yallatour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import modules.User;
import util.Constant;
import util.Global;

public class SignupActivity extends AppCompatActivity {

   private  EditText etFirstName , etLastName , etEmail , etPassword , etConfirmPassword ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);



    }

    public void backToLogin(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    public void signupUser(View view) {
        List<TextView> input = new ArrayList<>();
        input.add(etFirstName);
        input.add(etLastName);
        input.add(etEmail);
        input.add(etPassword);
        input.add(etConfirmPassword);

        if (Global.validField( input)) {


            Constant.AUTH.createUserWithEmailAndPassword(etEmail.getText().toString() , etPassword.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {

                                    try {
                                        uploadUser();
                                        Global.dialogYesNo(SignupActivity.this, "Account Created " , "Now you can verify your account and login" , true , new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                                Constant.AUTH.signOut();
                                                finish();

                                            }
                                        });
                                        Constant.AUTH.signOut();

                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }
    private synchronized void uploadUser() throws InterruptedException {
        Constant.USER = Constant.AUTH.getCurrentUser();

       Constant.users.child(Constant.USER.getUid()).setValue( prepareUser() );
       Global.updateDashboard(Constant.INCREASE_USER);
        notifyAll();
    }
    private User prepareUser(){
        User user = new User() ;
        user.setAdmin(false);
        user.setFirstName(etFirstName.getText().toString());
        user.setLastName(etLastName.getText().toString());
        user.setEmail(etEmail.getText().toString());
        user.setImageUrl("");
        return user;
    }
}