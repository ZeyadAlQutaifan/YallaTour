package adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.meshwar.PlaceActivity;
import com.example.meshwar.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import modules.Place;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlacesViewHolder> {
    List<Place> mList = new ArrayList<>();
    Context context;

    public PlaceAdapter(Context context, List<Place> mList) {
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public PlacesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_list, parent, false);
        return new PlacesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlacesViewHolder holder, int position) {
        if (position % 2 == 0)
            Glide.with(context).load(mList.get(position).getImages().get(0)).apply(new RequestOptions().override(700, 1200)).centerCrop().into(holder.imageView);
        else
            Glide.with(context).load(mList.get(position).getImages().get(0)).apply(new RequestOptions().override(700, 900)).centerCrop().into(holder.imageView);

        holder.etTitle.setText(mList.get(position).getTitle());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, PlaceActivity.class));
            }
        });
        //  holder.gradientFrame.setLayoutParams(new FrameLayout.LayoutParams(holder.imageView.getLayoutParams().width , holder.imageView.getLayoutParams().height));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class PlacesViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        RoundedImageView imageView;
        TextView etTitle;

        public PlacesViewHolder(@NonNull View itemView) {
            super(itemView);
            // gradientFrame = itemView.findViewById(R.id.gradientFrame);
            imageView = itemView.findViewById(R.id.imageView);
            cardView = itemView.findViewById(R.id.cardView);
            etTitle = itemView.findViewById(R.id.etTitle);


        }
    }
}
