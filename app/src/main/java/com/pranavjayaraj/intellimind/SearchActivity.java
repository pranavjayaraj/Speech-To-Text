package com.pranavjayaraj.intellimind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
/**
 * Created by Pranav on 28/8/19.
 */
public class SearchActivity extends AppCompatActivity {

    private String queryString;//Title
    private ImageView back;//Back button
    private TextView query; //Query textview

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchlayout);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //Get the query from previous activity and display as heading
        Intent intent = getIntent();
        queryString = intent.getStringExtra(String.valueOf(R.string.query));
        query = findViewById(R.id.query);
        query.setText(queryString);
        //Display loading animation
        final LottieAnimationView animationView = (LottieAnimationView) findViewById(R.id.menuAnimation2);
        animationView.setVisibility(View.VISIBLE);
        animationView.setAnimation("animations/search.json");
        animationView.loop(true);
        animationView.playAnimation();
    }
}
