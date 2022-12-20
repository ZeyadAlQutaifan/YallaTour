package dashboard;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

import com.bumptech.glide.Glide;
import com.example.yallatour.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import util.Constant;

public class AddPlaceActivity extends AppCompatActivity {

    private ArrayList<Uri> selectedImagesUri = new ArrayList<>();

    private LinearLayout images_list;
    private double longitude, latitude;
    private TextInputEditText etTitle, etDescription;
    private List<String> imagesUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);
        initComponent();
    }

    private void initComponent() {
        images_list = findViewById(R.id.images_list);

    }

    public void savePlace(View view) {

    }

    public void openMapForResult(View view) {
        // start google maps to pick a location
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


                    selectedImagesUri.add(uri);
                    Log.v(Constant.TAG_V , selectedImagesUri.toString());

                    //Log.v("AAA" ,selectionResult.get(i).toString() );

                    addView(selectedImagesUri.get(i));


                }
            } else {

            }

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