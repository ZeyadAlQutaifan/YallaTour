package dashboard;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.yallatour.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import modules.Comment;
import modules.Dashboard;
import modules.Place;
import util.Constant;
import util.Global;

public class AddPlaceActivity extends AppCompatActivity {

    private ArrayList<String> selectedImagesUri = new ArrayList<>();

    private LinearLayout images_list;
    private double longitude, latitude;
    private TextInputEditText etTitle, etDescription;
    private List<String> imagesUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);
        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        initComponent();
    }

    private void initComponent() {
        images_list = findViewById(R.id.images_list);

    }

    public void savePlace(View view) {

        if (!etTitle.getText().toString().isEmpty()) {
            if (etTitle.getText().toString().length() > 30) {
                if (!etDescription.getText().toString().isEmpty()) {
                    if (etDescription.getText().toString().length() > 350) {
                        if (latitude != 0 && longitude != 0){
                            if(selectedImagesUri.size() >=2) {
                                uploadImages().addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            uploadPlace();
                                            Global.updateDashboard(Constant.INCREASE_PLACE);

                                        }
                                    }
                                });
                            }else{
                                Toast.makeText(this, "You must select 2 images at least ", Toast.LENGTH_LONG).show();

                            }
                        }else{
                            Toast.makeText(this, "Please choose a location", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        etDescription.requestFocus();
                        etDescription.setError("Description must be less than 350 character ");
                    }
                }else{
                    etDescription.requestFocus();
                    etDescription.setError("Please insert a description");
                }
            } else {
                etTitle.requestFocus();
                etTitle.setError("Title must be less than 30 character ");

            }
        } else {
            etTitle.requestFocus();
            etTitle.setError("please insert a title ");

        }
    }


    private void uploadPlace() {

        Place place = preparePlace();
        Constant.places.push().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Constant.places.push().setValue(place).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(AddPlaceActivity.this, "Done", Toast.LENGTH_SHORT).show();

                        finish();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Global.updateDashboard(Constant.INCREASE_PLACE);
    }

    private Task<UploadTask.TaskSnapshot> uploadImages() {

        Task<UploadTask.TaskSnapshot> tasks = null;
        for (int i = 0; i < selectedImagesUri.size(); i++) {
            StorageReference imageName = Constant.placesImagesFolder.child(etTitle.getText().toString()).child(etTitle.getText().toString() + System.currentTimeMillis());
            int finalI = i;
            tasks = imageName.putFile(Uri.parse(selectedImagesUri.get(finalI))).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            selectedImagesUri.set(finalI, String.valueOf(uri));
                        }
                    });
                }
            });
        }
        return tasks;
    }

    private Place preparePlace() {

        Place place = new Place();
        place.setTitle(etTitle.getText().toString());
        place.setDescription(etDescription.getText().toString());
        place.setImages(selectedImagesUri);
        place.setLat(latitude);
        place.setLng(longitude);
        place.setViews(0);
        place.setNavigations(0);

        return place;
    }

    public void openMapForResult(View view) {
        startActivityForResult(new Intent(getApplicationContext(), LocationPickerActivity.class), 2);
    }

    public void pickImages(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        } else {
            openGallery();
        }

    }

    private void openGallery() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    private ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {

            // Permission is granted. Continue the action or workflow in your
            // app.
        } else {
            return;
        }
    });

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            //   List<Bitmap> bitmaps = new ArrayList<>();
            ClipData clipData = data.getClipData();
            if (clipData != null) {

                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri uri = clipData.getItemAt(i).getUri();


                    selectedImagesUri.add(String.valueOf(uri));
                    Log.v(Constant.TAG_V, selectedImagesUri.toString());

                    //Log.v("AAA" ,selectionResult.get(i).toString() );

                    addView(Uri.parse(selectedImagesUri.get(i)));


                }
            } else {

            }

        }
        if (requestCode == 2 && resultCode == RESULT_OK) {
            latitude = data.getDoubleExtra("LAT", 0);
            longitude = data.getDoubleExtra("LON", 0);

        }
    }

    private void addView(Uri uri) {
        View v = getLayoutInflater().inflate(R.layout.image_columns, null, false);
        ImageView imageView = v.findViewById(R.id.img);
        ImageView imageView4 = v.findViewById(R.id.imageView4);
        imageView4.setZ(2);
        Glide.with(this)
                .load(uri)

                .centerCrop()
                .into(imageView);
        images_list.addView(v);


    }


}