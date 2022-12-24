package fragments;

import static android.app.Activity.RESULT_OK;

import static androidx.core.graphics.TypefaceCompatUtil.getTempFile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
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
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.example.yallatour.LoginActivity;
import com.example.yallatour.R;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.vansuita.pickimage.bean.PickResult;

import com.vansuita.pickimage.bundle.PickSetup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import component.PickImageDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import modules.User;
import util.Constant;
import util.Global;

public class ProfileFragment extends Fragment {
    EditText etName;
    EditText etEmail;
    CircleImageView imgUser;
    Button btnSaveChanges, btnEditProfile, btnSignOut;
    User user;
    Uri profileImageUri = null ;
    PickImageDialog pickImageDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        imgUser = view.findViewById(R.id.imgUser);

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
        etName = view.findViewById(R.id.etName);
        etEmail = view.findViewById(R.id.etEmail);

        btnSaveChanges = view.findViewById(R.id.btnSaveChanges);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);


        enableComponent(false);
        Global.getUser(Constant.USER.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                etEmail.setText(user.getEmail());
                etName.setText(user.getUsername());
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
                Constant.AUTH.signOut();
                Constant.USER = null;
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
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
                List<TextView> input = new ArrayList<>(Arrays.asList(new EditText[]{etEmail, etName}));
                if (Global.validField(input)) {

                    enableComponent(false);
                }
            }
        });
        return view;
    }

    private User prepareNewUser() {
        User user = new User();
        user.setUsername(etName.getText().toString());
        user.setEmail(etEmail.getText().toString());
        return user;
    }

    private void enableComponent(boolean flag) {
        etName.setEnabled(flag);
        etName.requestFocus();
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