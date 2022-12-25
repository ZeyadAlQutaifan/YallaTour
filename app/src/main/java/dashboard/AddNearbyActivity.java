package dashboard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yallatour.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import modules.Nearby;
import modules.Place;
import util.Constant;
import util.Global;

public class AddNearbyActivity extends AppCompatActivity {

    private TextView etTitle ;
    private ImageView nearbyImage ;
    private AutoCompleteTextView autoCompleteTextView ;

    private Uri imageUri = null ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_nearby);
        etTitle = findViewById(R.id.etTitle);
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        nearbyImage = findViewById(R.id.nearbyImage);


    }

    public void chooseNearbyServiceImage(View view) {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , 1);

    }

    private void uploadService(){

        Nearby nearby = prepareNearby();
        Constant.nearby.push().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Constant.nearby.push().setValue(nearby).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(AddNearbyActivity.this, "Done", Toast.LENGTH_SHORT).show();

                        finish();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Global.updateDashboard(Constant.INCREASE_NEARBY);
    }

    private   Task<UploadTask.TaskSnapshot> uploadImage() {

        Task<UploadTask.TaskSnapshot>  tasks = null;
        StorageReference imageName = Constant.nearbyImagesFolder.child(etTitle.getText().toString()).child(etTitle.getText().toString() + System.currentTimeMillis ());

            tasks = imageName.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imageUri = uri;
                            uploadService();

                        }
                    });}
            });

        return tasks;
    }


    private Nearby prepareNearby() {
        Nearby nearby = new Nearby();
        nearby.setTitle(etTitle.getText().toString());
        nearby.setImgUrl(String.valueOf(imageUri));
        nearby.setKey(autoCompleteTextView.getText().toString());
        return nearby;
    }

    public void saveNearbyService(View view) {
        if (!imageUri.equals(null)){
            if(!etTitle.getText().toString().isEmpty()){

                uploadImage();
            }
        }else{

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {

            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    imageUri = selectedImage;
                    nearbyImage.setImageURI(selectedImage);
                }
                break;
        }

    }

}