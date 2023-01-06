package com.example.yallatour;

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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;
import com.makeramen.roundedimageview.RoundedImageView;

import modules.Place;
import util.Constant;
import util.Global;

public class AllPlacesActivity extends AppCompatActivity {
 private RecyclerView recyclerView ;
 private Query query;
    FirebaseRecyclerOptions<Place> options ;
    FirebaseRecyclerAdapter<Place, AllPlacesActivity.PlaceViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_places);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this
        ));
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadAllPlaces();
    }

    private void loadAllPlaces() {
        query = Constant.places;
        options = new FirebaseRecyclerOptions.Builder<Place>().setQuery(query , Place.class).build();
        firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<Place,AllPlacesActivity.PlaceViewHolder >(options){

            @NonNull
            @Override
            public AllPlacesActivity.PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false);

                return new AllPlacesActivity.PlaceViewHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull AllPlacesActivity.PlaceViewHolder holder, int position, @NonNull Place model) {

                holder.txtTitle.setText(Global.getNullString(model.getTitle()));
                if(model.getDescription().length() > 170){
                    holder.txtDescription.setText(Global.getNullString(model.getDescription().substring(0 , 166) + "..."));
                }else{
                    holder.txtDescription.setText(Global.getNullString(model.getDescription()));
                }

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
                        final int i = holder.getAdapterPosition();
                        Intent intent = new Intent(getApplicationContext(), PlaceActivity.class);
                        intent.putExtra(Constant.PASSING_OBJECT_KEY, model);
                        intent.putExtra(Constant.PASSING_REF_KEY, getSnapshots().getSnapshot(i).getKey());
                        startActivity(intent);
                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();


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