package dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.yallatour.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;

import fragments.NearestFragment;
import modules.Nearby;
import modules.Place;
import util.Constant;
import util.Global;

public class ShowNearbyActivity extends AppCompatActivity {
RecyclerView recyclerView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_nearby);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2 , StaggeredGridLayoutManager.VERTICAL ));
        loadNearby();
    }
    private void loadNearby() {
        Query query = Constant.nearby;
        FirebaseRecyclerOptions<Nearby> options = new FirebaseRecyclerOptions.Builder<Nearby>().setQuery(query, Nearby.class).build();
        FirebaseRecyclerAdapter<Nearby, ShowNearbyActivity.NearbyViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Nearby, ShowNearbyActivity.NearbyViewHolder>(options) {

            @NonNull
            @Override
            public ShowNearbyActivity.NearbyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nearby, parent, false);
                return new ShowNearbyActivity.NearbyViewHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull ShowNearbyActivity.NearbyViewHolder holder, int position, @NonNull Nearby model) {
                Glide.with(getApplicationContext()).load(model.getImgUrl()).into(holder.imgIcon);
                holder.txtTitle.setText(model.getTitle());
                holder.container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                        builder.setTitle("Option");
                        PopupMenu popupMenu = new PopupMenu(ShowNearbyActivity.this, holder.container);

                        // Inflating popup menu from popup_menu.xml file
                        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                // Toast message on menu item clicked
                                Log.v(Constant.TAG_V ,"You Clicked " + menuItem.getTitle() );
                                Toast.makeText(ShowNearbyActivity.this, "You Clicked " + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                                switch (menuItem.getItemId()){
                                    case R.id.item_delete:
                                        deleteItem(getSnapshots().getSnapshot(holder.getBindingAdapterPosition()).getKey());
                                        break;
                                    case R.id.item_update:
                                        updateItem(getSnapshots().getSnapshot(holder.getBindingAdapterPosition()).getKey() , model);
                                        break;
                                }
                                return true;
                            }
                        });
                        // Showing the popup menu
                        popupMenu.show();

                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    private void updateItem(String key , Nearby nearby) {

        Intent intent = new Intent(getApplicationContext() , UpdateNearbyActivity.class);
        intent.putExtra(Constant.PASSING_OBJECT_KEY , nearby);
        intent.putExtra(Constant.PASSING_REF_KEY , key);

        startActivity(intent);
    }

    private void deleteItem(String key) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure, You wanted to delete this item");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Constant.nearby.child(key).removeValue();
                                Global.updateDashboard(Constant.DECREASE_NEARBY);
                            }
                        });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public void toAddNearby(View view) {
        startActivity(new Intent(getApplicationContext() , AddNearbyActivity.class));
    }

    private static class NearbyViewHolder extends RecyclerView.ViewHolder{

        ImageView imgIcon ;
        TextView txtTitle ;
        CardView container ;
        public NearbyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.imgIcon);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            container = itemView.findViewById(R.id.container);


        }
    }
}