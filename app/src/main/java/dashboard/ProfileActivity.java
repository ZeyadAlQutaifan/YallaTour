package dashboard;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yallatour.LoginActivity;
import com.example.yallatour.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import component.PickImageDialog;
import component.YesNoDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import modules.User;
import util.Constant;
import util.Global;

public class ProfileActivity extends AppCompatActivity {

    EditText etFirstName , etLastName;
    EditText etEmail;
    CircleImageView imgUser;
    FloatingActionButton btnSaveChanges,btnEditProfile;
    Button btnSignOut , btnResetPass;
    User user;
    Uri profileImageUri = null ;
    PickImageDialog pickImageDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        imgUser = findViewById(R.id.imgUser);
        etEmail = findViewById(R.id.etEmail);
        btnResetPass = findViewById(R.id.btnResetPass);
        imgUser.setOnClickListener(view -> {
            pickImageDialog = new PickImageDialog(getApplicationContext() , true);
            pickImageDialog.create();
            pickImageDialog.setCancelable(true);
            pickImageDialog.getPickFromCamera().setOnClickListener(v -> {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissionLauncher.launch(Manifest.permission.CAMERA);
                }else{
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                }
            });
            pickImageDialog.getPickFromGallery().setOnClickListener(v -> {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);
            });


        });
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);

        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        btnEditProfile = findViewById(R.id.btnEditProfile);


        enableComponent(false);
        Global.getUser(Constant.USER.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                etEmail.setText(user.getEmail());
                etFirstName.setText(user.getFirstName());
                etLastName.setText(user.getLastName());

                Glide.with(getApplicationContext()).load(Global.getUserImageNotFound(user.getImageUrl())).centerCrop().into(imgUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnSignOut = findViewById(R.id.btnSignOut);
        btnSignOut.setOnClickListener(view -> {
            YesNoDialog yesNoDialog = new YesNoDialog(ProfileActivity.this, "Logout" , "Are you sure you want to logout" , true );
            yesNoDialog.create();
            yesNoDialog.setTxtNegativeButtonText("No, stay logged in ");
            yesNoDialog.setNegativeButtonClickListener(v -> yesNoDialog.close());
            yesNoDialog.setTxtPositiveButtonText("Yes, Logout");
            yesNoDialog.setPositiveButtonClickListener(v -> {
                Constant.AUTH.signOut();
                Constant.USER = null;
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                yesNoDialog.close();
            });

        });
        btnEditProfile.setOnClickListener(view -> enableComponent(true));
        btnSaveChanges.setOnClickListener(view -> {
            List<TextView> input = new ArrayList<>(Arrays.asList(etEmail, etFirstName, etLastName));
            if (Global.validField(input)) {

                enableComponent(false);
                User newUser = prepareNewUser();

                updateUser(newUser);

            }
        });


        btnResetPass.setOnClickListener(v ->{
            Constant.AUTH.sendPasswordResetEmail(Constant.USER.getEmail())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                            builder.setMessage("A reset password link has been sent to your email ("+Constant.USER.getEmail()+")");
                            builder.setCancelable(false);
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    });
        });




    }

    private void updateUser(User user1) {

        final String[] strPassword = {""};

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Enter Your Password");

// Set up the input
        final EditText input = new EditText(ProfileActivity.this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

               }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enableComponent(true);
                dialog.cancel();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                enableComponent(true);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();



    }

    // TODO Upload User image
    private User prepareNewUser() {
        User user = new User();
        user.setFirstName(etFirstName.getText().toString());
        user.setLastName(etLastName.getText().toString());

        user.setEmail(etEmail.getText().toString());
        return user;
    }

    private void enableComponent(boolean flag) {
        etFirstName.setEnabled(flag);
        etLastName.setEnabled(flag);
        etFirstName.requestFocus();
        etEmail.setEnabled(flag);
        if (flag) {
            btnEditProfile.setVisibility(View.INVISIBLE);
            btnSaveChanges.setVisibility(View.VISIBLE);
        } else {
            btnEditProfile.setVisibility(View.VISIBLE);
            btnSaveChanges.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                    imgUser.setImageBitmap(selectedImage);
                    pickImageDialog.dismiss();
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    imgUser.setImageURI(selectedImage);
                    pickImageDialog.dismiss();
                }
                break;
        }

    }
    private ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePicture, 0);

        }
    });


    public void signout(View view) {
        YesNoDialog yesNoDialog = new YesNoDialog(getApplicationContext() , "Logout" , "Are you sure you want to logout" , true );
        yesNoDialog.create();
        yesNoDialog.setTxtNegativeButtonText("No, stay logged in ");
        yesNoDialog.setNegativeButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yesNoDialog.close();
            }
        });
        yesNoDialog.setTxtPositiveButtonText("Yes, Logout");
        yesNoDialog.setPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.AUTH.signOut();
                Constant.USER = null;
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
               finish();
                yesNoDialog.close();
            }
        });
    }
}