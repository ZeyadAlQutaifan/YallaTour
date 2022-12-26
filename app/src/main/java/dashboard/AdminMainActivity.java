package dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yallatour.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import fragments.MainFragment;
import modules.Dashboard;
import util.Constant;

public class AdminMainActivity extends AppCompatActivity {

    TextView txtCommentsCount , txtUserCount , txtNavigationsCount , txtPlacesCount , txtNearbyCount ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        txtCommentsCount =findViewById(R.id.txtCommentsCount);
        txtUserCount =findViewById(R.id.txtUserCount);
        txtNavigationsCount =findViewById(R.id.txtNavigationsCount);
        txtPlacesCount =findViewById(R.id.txtPlacesCount);
        txtNearbyCount =findViewById(R.id.txtNearbyCount);


    }

    @Override
    protected void onStart() {
        super.onStart();
        Constant.dashboard.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()) {
                    Dashboard dashboard = snapshot.getValue(Dashboard.class);
                    txtCommentsCount.setText(String.valueOf(dashboard.getCommentsCount()));
                    txtUserCount.setText(String.valueOf(dashboard.getUsersCount()));
                    txtNavigationsCount.setText(String.valueOf(dashboard.getNavigationsCount()));
                    txtPlacesCount.setText(String.valueOf(dashboard.getPlacesCount()));
                    txtNearbyCount.setText(String.valueOf(dashboard.getNearbyCount()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminMainActivity.this, "Unable to load dashboard", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void toPLaces(View view) {
       startActivity(new Intent(AdminMainActivity.this , ShowPLacesActivity.class));
    }

    public void toNearbyPlaces(View view) {
        startActivity(new Intent(getApplicationContext() , ShowNearbyActivity.class));
    }

    public void toProfile(View view) {
        startActivity(new Intent(getApplicationContext() , ProfileActivity.class));
    }
}