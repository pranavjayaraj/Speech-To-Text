package com.pranavjayaraj.intellimind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class SearchActivity extends AppCompatActivity {

    private String Query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchlayout);
        Intent intent = getIntent();
        Query = intent.getStringExtra("query");
        final TextView textView = (TextView) findViewById(R.id.query);
        textView.setText(Query);
        final LottieAnimationView animationView = (LottieAnimationView) findViewById(R.id.menuAnimation2);
        animationView.setVisibility(View.VISIBLE);
        animationView.setAnimation("animations/search.json");
        animationView.loop(true);
        animationView.playAnimation();
    }
}
