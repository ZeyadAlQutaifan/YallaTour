package fragments;

import static android.app.Activity.RESULT_OK;

import static androidx.core.graphics.TypefaceCompatUtil.getTempFile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.example.yallatour.LoginActivity;
import com.example.yallatour.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.vansuita.pickimage.bean.PickResult;

import com.vansuita.pickimage.bundle.PickSetup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import component.InputPasswordDialog;
import component.PickImageDialog;
import component.YesNoDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import modules.User;
import util.Constant;
import util.Global;

public class ProfileFragment extends Fragment {
    EditText etFirstName , etLastName;
    EditText etEmail;
    CircleImageView imgUser;
    FloatingActionButton btnSaveChanges,btnEditProfile;
    Button btnSignOut , btnResetPass;
    User user;
    Uri profileImageUri = null ;
    PickImageDialog pickImageDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        imgUser = view.findViewById(R.id.imgUser);
        etEmail = view.findViewById(R.id.etEmail);
        btnResetPass = view.findViewById(R.id.btnResetPass);
        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               pickImageDialog = new PickImageDialog(getActivity() , true);
                pickImageDialog.create();
                pickImageDialog.setCancelable(true);
                pickImageDialog.getPickFromCamera().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
                        }else{
                            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(takePicture, 0);
                        }
                    }
                });
                pickImageDialog.getPickFromGallery().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto , 1);
                    }
                });


            }
        });
        etFirstName = view.findViewById(R.id.etFirstName);
        etLastName = view.findViewById(R.id.etLastName);

        btnSaveChanges = view.findViewById(R.id.btnSaveChanges);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);


        enableComponent(false);
        Global.getUser(Constant.USER.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                etEmail.setText(user.getEmail());
                etFirstName.setText(user.getFirstName());
                etLastName.setText(user.getLastName());

                Glide.with(getActivity()).load(Global.getUserImageNotFound(user.getImageUrl())).centerCrop().into(imgUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnSignOut = view.findViewById(R.id.btnSignOut);
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YesNoDialog yesNoDialog = new YesNoDialog(getActivity() , "Logout" , "Are you sure you want to logout" , true );
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
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                        yesNoDialog.close();
                    }
                });

            }
        });
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableComponent(true);

            }
        });
        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<TextView> input = new ArrayList<>(Arrays.asList(new EditText[]{etEmail, etFirstName , etLastName}));
                if (Global.validField(input)) {

                    enableComponent(false);
                    User newUser = prepareNewUser();

                        updateUser(newUser);

                }
            }
        });
        return view;
    }

    private void updateUser(User user1) {

        final String[] strPassword = {""};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter Your Password");

// Set up the input
        final EditText input = new EditText(getActivity());
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                strPassword[0] = input.getText().toString();
                FirebaseUser user = Constant.USER;
                // Get auth credentials from the user for re-authentication
                AuthCredential credential = EmailAuthProvider
                        .getCredential(user.getEmail()  , strPassword[0]); // Current Login Credentials \\
                // Prompt the user to re-provide their sign-in credentials
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d(Constant.TAG_V, "User re-authenticated.");
                                //Now change your email address \\
                                //----------------Code for Changing Email Address----------\\
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                user.updateEmail(etEmail.getText().toString())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Constant.users.child(Constant.USER.getUid()).setValue(user1);
                                                    //  Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                //----------------------------------------------------------\\
                            }
                        });

          enableComponent(false);
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

        builder.show();



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
}