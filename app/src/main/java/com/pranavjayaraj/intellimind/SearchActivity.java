package com.pranavjayaraj.intellimind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.pranavjayaraj.intellimind.R;

public class SearchActivity extends AppCompatActivity {

    private String Query;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchlayout);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Intent intent = getIntent();
        Query = intent.getStringExtra(String.valueOf(R.string.query));
        final TextView textView = (TextView) findViewById(R.id.query);
        textView.setText(Query);
        final LottieAnimationView animationView = (LottieAnimationView) findViewById(R.id.menuAnimation2);
        animationView.setVisibility(View.VISIBLE);
        animationView.setAnimation("animations/search.json");
        animationView.loop(true);
        animationView.playAnimation();
    }
}
