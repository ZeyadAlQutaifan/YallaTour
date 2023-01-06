package com.example.yallatour;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import modules.Place;
import modules.Review;
import modules.User;
import util.Constant;
import util.Global;

public class PlaceActivity extends AppCompatActivity {

private Place place ;
private String key  = "";
    private List<SlideModel> imageList = new ArrayList<>();
private TextView txtTitle, txtDexcription, txtDistance , txtCity ;
private ImageView imgMain;
private RatingBar placeRating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        place = (Place) getIntent().getSerializableExtra(Constant.PASSING_OBJECT_KEY);
        key = getIntent().getStringExtra(Constant.PASSING_REF_KEY);
        Log.v(Constant.TAG_V , "KEY ==>" + key);
        txtTitle = findViewById(R.id.txtTitle);
        txtDexcription = findViewById(R.id.txtDescription);
        txtDistance = findViewById(R.id.txtDistance);
        imgMain = findViewById(R.id.imgMain);
        txtCity = findViewById(R.id.txtCity);
        placeRating = findViewById(R.id.rating);
        placeRating.setEnabled(false);


        Global.getPlace(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                place = snapshot.getValue(Place.class);
                place.increaseViews();
                Global.getPlace(key).setValue(place);
                for (int i = 0 ; i<place.getImages().size() ; i++){
                    if(place.getImages().get(i) != null && !place.getImages().get(i).isEmpty()) {
                        imageList.add(new SlideModel(place.getImages().get(i), ScaleTypes.CENTER_CROP));
                    }
                }
                Glide.with(getApplicationContext()).load(place.getImages().get(0)).centerCrop().into(imgMain);
                txtTitle.setText(Global.getNullString(place.getTitle()));
                txtDexcription.setText(Global.getNullString(place.getDescription()));
                txtDistance.setText(String.format("%.2f" , Global.distFrom(Constant.LATITUDE , Constant.LONGITUDE , place.getLat() , place.getLng()) )+ " KM");
                placeRating.setRating((float)place.getRate());

                txtCity.setText(Global.getNullCity(place.getCity()));
                showWeatherStatus();
                ImageSlider imageSlider = findViewById(R.id.slider);
                imageSlider.setImageList(imageList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void showWeatherStatus() {
        // TODO Call open weather map, show result in frameWeatherData frame
        String weatherURL = Constant.WEATHER_URI +
                Constant.LATITUDE_BLOCK.replace("{#}", String.valueOf(place.getLat())) +
                Constant.AND +
                Constant.LONGITUDE_BLOCK.replace("{#}", String.valueOf(place.getLng())) +
                Constant.AND +
                Constant.KEY_BLOCK.replace("{#}", Constant.WEATHER_KEY);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        // this is the error listener method which
// we will call if we get any error from API.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, weatherURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject main = response.getJSONObject("main");
                    String temp = main.getString("temp");
                    String city = response.getString("name");
                    Log.v(Constant.TAG_V , "tenp ==>" + temp + "\n city ==> " + city);


                } catch (JSONException e) {
                    // if we do not extract data from json object properly.
                    // below line of code is use to handle json exception
                    e.printStackTrace();
                }
            }
        }, error -> {
            // below line is use to display a toast message along with our error.
            Toast.makeText(getApplicationContext(), "Fail to get data..", Toast.LENGTH_SHORT).show();
        });
        queue.add(jsonObjectRequest);

    }

    public void navigateToPlace(View view) {

        Uri gmmIntentUri = Uri.parse("geo:"+place.getLat()+"," + place.getLng());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
            Global.updateDashboard(Constant.INCREASE_NAVIGATION);
            place.increaseNavigations();
            Global.getPlace(key).setValue(place);
        }
    }


    public void openReviews(View view) {




        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                PlaceActivity.this, R.style.BottomSheetDialogTheme

        );
        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(
                        R.layout.dialog_bottom_sheet,
                        findViewById(R.id.bottom_sheet_container)
                );

        Button btnRate = bottomSheetView.findViewById(R.id.btnRate);
        FrameLayout frame_no_reviews = bottomSheetView.findViewById(R.id.frame_no_reviews);
        RecyclerView recyclerViewReviews = bottomSheetView.findViewById(R.id.recyclerViewReviews);
        recyclerViewReviews.setHasFixedSize(true);
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(PlaceActivity.this));


        final int[] reviewsCount = {0};
        Query query=  Constant.reviews.orderByChild("placeKey").equalTo(key);;
        FirebaseRecyclerOptions<Review> options =new FirebaseRecyclerOptions.Builder<Review>().setQuery(query , Review.class).build();;
        FirebaseRecyclerAdapter<Review, ReviewViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Review , ReviewViewHolder>(options) {

                    @NonNull
                    @Override
                    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
                        return new ReviewViewHolder(v);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull ReviewViewHolder holder, int position, @NonNull Review model) {
                  //      if (model.getPlaceKey().equals(key)){

                            frame_no_reviews.setVisibility(View.INVISIBLE);
                            recyclerViewReviews.setVisibility(View.VISIBLE);
                            reviewsCount[0]++ ;
                            Log.v(Constant.TAG_V , reviewsCount[0] + "");
                            holder.txtBody.setText(model.getBody());
                            holder.txtDate.setText(model.getTime());
                            holder.txtRate.setText(String.valueOf(model.getRate()));
                            Global.getUser(model.getOwnerID()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    User user = snapshot.getValue(User.class);
                                    holder.txtOwnerName.setText(user.getFirstName() + " " + user.getLastName());
                                    Glide.with(PlaceActivity.this ).load(Global.getUserImageNotFound(user.getImageUrl())).into(holder.img_profile);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                     //   }
                    }
                } ;

        recyclerViewReviews.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();


        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PlaceActivity.this);
                builder.setCancelable(true);
                View dialogView = LayoutInflater.from(PlaceActivity.this).inflate(R.layout.dialog_rate_place, null);
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.show();
                RatingBar rating = dialogView.findViewById(R.id.rating);
                TextInputEditText etComment = dialogView.findViewById(R.id.etComment);
                Button btnPost = dialogView.findViewById(R.id.btnPost);
                btnPost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Constant.reviews.push().addListenerForSingleValueEvent(new ValueEventListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(etComment.getText().toString().length() <= 300 || !etComment.getText().toString().isEmpty()){
                                    if(rating.getRating() >0) {
                                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd");
                                        LocalDate localDate = LocalDate.now();
                                        Review review = new Review(Constant.USER.getUid(), etComment.getText().toString(), dtf.format(localDate), key, rating.getRating());
                                        Constant.reviews.push().setValue(review).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                // TODO do something onComplete
                                                btnRate.setVisibility(View.GONE);
                                                Global.getUser(Constant.USER.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        User user = snapshot.getValue(User.class);
                                                        List<String> ratedPlaces = user.getReviewedPlaces();
                                                        ratedPlaces.add(key);
                                                        user.setReviewedPlaces(ratedPlaces);
                                                        Global.getUser(Constant.USER.getUid()).setValue(user);
                                                        Place newPlace = place;
                                                        newPlace.increaseRates();
                                                        int rates = newPlace.getRates();
                                                        double rate = newPlace.getRate() + rating.getRating();
                                                        newPlace.setRate(rate / rates);
                                                        Global.getPlace(key).setValue(newPlace);
                                                        placeRating.setRating((float)newPlace.getRate());
                                                        alertDialog.dismiss();
                                                        firebaseRecyclerAdapter.startListening();
                                                        Global.updateDashboard(Constant.INCREASE_COMMENT);
                                                        place.increaseCommentCount();
                                                        Global.getPlace(key).setValue(place);
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                            }
                                        });

                                    }else{
                                        Toast.makeText(PlaceActivity.this, "Rate should not be less than 0.5 ", Toast.LENGTH_LONG).show();

                                    }
                                }else{
                                    Toast.makeText(PlaceActivity.this, "Comment should not be more than 300 character or empty", Toast.LENGTH_LONG).show();

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                });
                alertDialog.create();
            }
        });

        Global.getUser(Constant.USER.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                for (int i = 0; i < user.getReviewedPlaces().size() ; i++){
                    if (user.getReviewedPlaces().get(i).equals(key)){
                        btnRate.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Log.v(Constant.TAG_V , "Dismiss");
                firebaseRecyclerAdapter.startListening();
            }
        });




    }
    public static  class ReviewViewHolder extends  RecyclerView.ViewHolder{

        TextView txtOwnerName ;
        TextView txtBody ;
        CircleImageView img_profile ;
        TextView txtDate ;
        TextView txtRate ;


        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOwnerName = itemView.findViewById(R.id.txtOwnerName);
            txtBody = itemView.findViewById(R.id.txtBody);
            img_profile = itemView.findViewById(R.id.img_profile);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtRate = itemView.findViewById(R.id.txtRate);



        }
    }


}