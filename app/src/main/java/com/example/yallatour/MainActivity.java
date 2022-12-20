package com.example.yallatour;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import fragments.MainFragment;
import fragments.NearestFragment;
import fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    private TextView tabItem1 , tabItem2 ,tabItem3  , txtTitle;

    //selected tab number, we have 3 tabs so value must be lie between 1-3
    // we are setting default value 1. because by default first tab will be selected
    private int selectedTabNumber = 1 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabItem1 = findViewById(R.id.tabItem1);
        tabItem2 = findViewById(R.id.tabItem2);
        tabItem3 = findViewById(R.id.tabItem3);
      //  txtTitle = findViewById(R.id.txtTitle);
//        txtTitle.setText(tabItem1.getText());

        // selecting first fragment bu default
        getSupportFragmentManager().beginTransaction().setReorderingAllowed(true) .replace(R.id.fragmentContainer , MainFragment.class , null)
                        .commit();
        tabItem1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTab(1);
            }
        });
        tabItem2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTab(2);

            }
        });
        tabItem3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTab(3);

            }
        });

    }
    private void selectTab(int tabNumber){
        TextView selectedTextView ;
        TextView nonSelectedTextView1 ;
        TextView nonSelectedTextView2 ;

       if(tabNumber ==1){
           // user has selected first tab, so 1st TExtView is selected
           selectedTextView = tabItem1;
           // other two Textviews are non selected
           nonSelectedTextView1 = tabItem2;
           nonSelectedTextView2 = tabItem3;

           // setting main fragment to the fragment container
           getSupportFragmentManager().beginTransaction().setReorderingAllowed(true) .replace(R.id.fragmentContainer , MainFragment.class , null)
                   .commit();

       }else if(tabNumber ==2){
           // user has selected second tab, so 1st TExtView is selected
           selectedTextView = tabItem2;
           // other two Textviews are non selected
           nonSelectedTextView1 = tabItem1;
           nonSelectedTextView2 = tabItem3;

           // setting nearest fragment to the fragment container
           getSupportFragmentManager().beginTransaction().setReorderingAllowed(true) .replace(R.id.fragmentContainer , NearestFragment.class , null)
                   .commit();
       }else {
           // user has selected third tab, so 3th TextView is selected
           selectedTextView = tabItem3;
           // other two Textviews are non selected
           nonSelectedTextView1 = tabItem1;
           nonSelectedTextView2 = tabItem2;

           // setting nearest fragment to the fragment container
           getSupportFragmentManager().beginTransaction().setReorderingAllowed(true) .replace(R.id.fragmentContainer , ProfileFragment.class , null)
                   .commit();
       }
       float slideTo = (tabNumber - selectedTabNumber) * selectedTextView.getWidth();

       // creating translate animation
        TranslateAnimation translateAnimation = new TranslateAnimation(0 , slideTo , 0 ,0);
        translateAnimation.setDuration(200);

        // checking for the previously selected tab
        if(selectedTabNumber == 1){
            tabItem1.startAnimation(translateAnimation);
        }else if (selectedTabNumber == 2){
            tabItem2.startAnimation(translateAnimation);

        }else{
            tabItem3.startAnimation(translateAnimation);
        }
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // change design of selected tab's TextView
                selectedTextView.setBackgroundResource(R.drawable.round_back_white100);
                selectedTextView.setTypeface(null , Typeface.BOLD);
                selectedTextView.setTextColor(Color.BLACK);

                // change design of non  selected tab's TextView
                nonSelectedTextView1.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                nonSelectedTextView1.setTextColor(Color.parseColor("#80FFFFFF"));
                nonSelectedTextView1.setTypeface(null , Typeface.NORMAL);
                nonSelectedTextView2.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                nonSelectedTextView2.setTextColor(Color.parseColor("#80FFFFFF"));
                nonSelectedTextView2.setTypeface(null , Typeface.NORMAL);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        selectedTabNumber = tabNumber;
       // txtTitle.setText(selectedTextView.getText());
    }
}