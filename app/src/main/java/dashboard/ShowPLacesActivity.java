package dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yallatour.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;
import com.makeramen.roundedimageview.RoundedImageView;

import org.w3c.dom.Text;

import modules.Place;
import util.Constant;
import util.Global;

public class ShowPLacesActivity extends AppCompatActivity {
private RecyclerView mRecycleView ;

    private Query query;
    private FirebaseRecyclerOptions<Place> options;
    private  FirebaseRecyclerAdapter<Place , ShowPLacesActivity.PlaceViewHolder > firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_places);
        //Recycle View
        mRecycleView = findViewById(R.id.recyclerView);
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        query = Constant.places;
        options = new FirebaseRecyclerOptions.Builder<Place>().setQuery(query , Place.class).build();
        firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<Place,ShowPLacesActivity.PlaceViewHolder >(options){

            @NonNull
            @Override
            public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false);

                return new PlaceViewHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull PlaceViewHolder holder, int position, @NonNull Place model) {

                holder.txtTitle.setText(Global.getNullString(model.getTitle()));
                holder.txtDescription.setText(Global.getNullString(model.getDescription()));
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
                        Log.v(Constant.TAG_V , "==>" + id);
                    }
                });
            }
        };
        mRecycleView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();

    }

    public void toAddPlace(View view) {
        startActivity(new Intent(ShowPLacesActivity.this , AddPlaceActivity.class));
    }
    public static class PlaceViewHolder extends  RecyclerView.ViewHolder{

        TextView txtTitle ;
        TextView txtDescription ;
        RoundedImageView imageView ;
        TextView txtViewsCount ;
        TextView txtNavigationsCount ;
        TextView txtCommentsCount ;
        CardView container ;
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