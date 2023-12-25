package dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.meshwar.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;
import com.makeramen.roundedimageview.RoundedImageView;

import modules.Place;
import util.Constant;
import util.Global;

public class ShowPLacesActivity extends AppCompatActivity {
    private RecyclerView mRecycleView;

    private Query query;
    private FirebaseRecyclerOptions<Place> options;
    private FirebaseRecyclerAdapter<Place, ShowPLacesActivity.PlaceViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_show_places);
        //Recycle View
        mRecycleView = findViewById(R.id.recyclerView);
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        try {


            query = Constant.places;
            options = new FirebaseRecyclerOptions.Builder<Place>().setQuery(query, Place.class).build();
            firebaseRecyclerAdapter
                    = new FirebaseRecyclerAdapter<Place, ShowPLacesActivity.PlaceViewHolder>(options) {

                @NonNull
                @Override
                public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false);

                    return new PlaceViewHolder(v);
                }

                @Override
                protected void onBindViewHolder(@NonNull PlaceViewHolder holder, int position, @NonNull Place model) {

                    holder.txtTitle.setText(Global.getNullString(model.getTitle()));
                    holder.txtDescription.setText(Global.getNullString(model.getDescription()).length() > 180 ? Global.getNullString(model.getDescription()).substring(0, 179) + ".." : Global.getNullString(model.getDescription()));
                    Glide.with(getApplicationContext())
                            .load(Global.getPlaceImageNotFound(model.getImages().get(0)))
                            .centerCrop()
                            .into(holder.imageView);
                    holder.txtNavigationsCount.setText(String.valueOf(model.getNavigations()));
                    holder.txtViewsCount.setText(String.valueOf(model.getViews()));
                    holder.txtCommentsCount.setText(String.valueOf(model.getCommentsCount()));
                    holder.container.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String id = String.valueOf(getSnapshots().getSnapshot(holder.getBindingAdapterPosition()).getKey());


                            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                            builder.setTitle("Option");
                            PopupMenu popupMenu = new PopupMenu(ShowPLacesActivity.this, holder.container);

                            // Inflating popup menu from popup_menu.xml file
                            popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem menuItem) {
                                    // Toast message on menu item clicked
                                    Log.v(Constant.TAG_V, "You Clicked " + menuItem.getTitle());
                                    Toast.makeText(ShowPLacesActivity.this, "You Clicked " + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                                    switch (menuItem.getItemId()) {
                                        case R.id.item_delete:
                                            deleteItem(getSnapshots().getSnapshot(holder.getBindingAdapterPosition()).getKey());
                                            break;
                                        case R.id.item_update:
                                            updateItem(getSnapshots().getSnapshot(holder.getBindingAdapterPosition()).getKey(), model);
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
            mRecycleView.setAdapter(firebaseRecyclerAdapter);
            firebaseRecyclerAdapter.startListening();
        } catch (IndexOutOfBoundsException e) {

        }
    }

    private void deleteItem(String key) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure, You wanted to delete this item");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Constant.places.child(key).removeValue();
                        Global.updateDashboard(Constant.DECREASE_PLACE);
                    }
                });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void updateItem(String key, Place model) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();

    }

    public void toAddPlace(View view) {
        startActivity(new Intent(ShowPLacesActivity.this, AddPlaceActivity.class));
    }

    public static class PlaceViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle;
        TextView txtDescription;
        RoundedImageView imageView;
        TextView txtViewsCount;
        TextView txtNavigationsCount;
        TextView txtCommentsCount;
        CardView container;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            imageView = itemView.findViewById(R.id.imageView);
            txtNavigationsCount = itemView.findViewById(R.id.txtNavigationsCount);
            txtCommentsCount = itemView.findViewById(R.id.txtCommentsCount);
            txtViewsCount = itemView.findViewById(R.id.txtViewsCount);
            container = itemView.findViewById(R.id.container);
        }
    }

}