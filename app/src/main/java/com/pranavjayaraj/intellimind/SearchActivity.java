package com.pranavjayaraj.intellimind;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class SearchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchlayout);
        final LottieAnimationView animationView = (LottieAnimationView) findViewById(R.id.menuAnimation2);
        animationView.setVisibility(View.VISIBLE);
        animationView.setAnimation("animations/search.json");
        animationView.loop(true);
        animationView.playAnimation();
    }
}
