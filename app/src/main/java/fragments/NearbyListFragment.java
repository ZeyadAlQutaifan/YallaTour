package fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yallatour.NearbyMapActivity;
import com.example.yallatour.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;

import modules.Nearby;
import util.Constant;


public class NearbyListFragment extends Fragment {


   private RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_nearby, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2 , StaggeredGridLayoutManager.VERTICAL ));
        loadNearby();
        return view;
    }
    private void loadNearby() {
        Query query = Constant.nearby;
        FirebaseRecyclerOptions<Nearby> options = new FirebaseRecyclerOptions.Builder<Nearby>().setQuery(query, Nearby.class).build();
        FirebaseRecyclerAdapter<Nearby, NearbyListFragment.NearbyViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Nearby, NearbyListFragment.NearbyViewHolder>(options) {

            @NonNull
            @Override
            public NearbyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nearby, parent, false);
                return new NearbyListFragment.NearbyViewHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull NearbyViewHolder holder, int position, @NonNull Nearby model) {
                Glide.with(getActivity()).load(model.getImgUrl()).into(holder.imgIcon);
                holder.txtTitle.setText(model.getTitle());
                holder.container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity() , NearbyMapActivity.class);
                        intent.putExtra("KEY" , model.getKey());
                        startActivity(intent);
                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
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