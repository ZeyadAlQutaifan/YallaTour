package dashboard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.yallatour.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import modules.Nearby;
import util.Constant;
import util.Global;

public class UpdateNearbyActivity extends AppCompatActivity {
    private TextView etTitle;
    private ImageView nearbyImage;
    private AutoCompleteTextView autoCompleteTextView;
    private Uri imageUri = null;
    Nearby nearby;
    String KEY_TO_UPDATE = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update_nearby);
        etTitle = findViewById(R.id.etTitle);
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        nearbyImage = findViewById(R.id.nearbyImage);
        nearby = (Nearby) getIntent().getSerializableExtra(Constant.PASSING_OBJECT_KEY);
        etTitle.setText(nearby.getTitle());

        String[] items = getResources().getStringArray(R.array.simple_items);

        int index = 0;
        for (int i = 0; i < items.length; i++) {
            if (items[i].equals(nearby.getKey())) {
                index = i;
            }
        }
        autoCompleteTextView.setListSelection(index);
        Glide.with(this).load(nearby.getImgUrl()).into(nearbyImage);
        KEY_TO_UPDATE = getIntent().getStringExtra(Constant.PASSING_REF_KEY);
    }

    public void chooseNearbyServiceImage(View view) {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 1);

    }

    private void uploadService() {

        Nearby nearby = prepareNearby();

                Constant.nearby.child(KEY_TO_UPDATE).setValue(nearby).addOnCompleteListener(task -> {
                    Toast.makeText(UpdateNearbyActivity.this, "Done", Toast.LENGTH_SHORT).show();
                    Log.v(Constant.TAG_V , "Done");
                    finish();
                });




    }

    private void uploadImage() {


        StorageReference imageName = Constant.nearbyImagesFolder.child(etTitle.getText().toString()).child(etTitle.getText().toString() + System.currentTimeMillis());
if(imageUri != null) {
    imageName.putFile(imageUri).addOnSuccessListener(taskSnapshot -> imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
        @Override
        public void onSuccess(Uri uri) {
            imageUri = uri;
            uploadService();

        }
    }));
}else{
    uploadService();
}
    }


    private Nearby prepareNearby() {
        Nearby n1 = new Nearby();
        n1.setTitle(etTitle.getText().toString());
        if (imageUri == null)
            n1.setImgUrl(nearby.getImgUrl());
        else
            n1.setImgUrl(String.valueOf(imageUri));

        if(autoCompleteTextView.getText().toString().isEmpty()){
            n1.setKey(nearby.getKey());
        }else {
            n1.setKey(autoCompleteTextView.getText().toString());
        }

        return n1;
    }

    public void saveNearbyService(View view) {

            if (!etTitle.getText().toString().isEmpty()) {

                uploadImage();
            }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                imageUri = selectedImage;
                nearbyImage.setImageURI(selectedImage);
            }
        }

    }
}