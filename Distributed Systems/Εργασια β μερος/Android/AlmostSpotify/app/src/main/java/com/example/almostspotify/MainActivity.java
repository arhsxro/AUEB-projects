package com.example.almostspotify;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private ImageView logo_text;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //assign which xml is going to show me to pou anoigw thn efarmogh

        logo_text = (ImageView) findViewById(R.id.textLogo);
        iv = (ImageView) findViewById(R.id.iv_welcome);
        Animation welcome_anim = AnimationUtils.loadAnimation(this,R.anim.welcometransition );
        logo_text.startAnimation(welcome_anim);
        iv.startAnimation(welcome_anim);

        Button next = (Button) findViewById(R.id.welcome_button);
        next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //go to second screen
                Intent myIntent = new Intent(view.getContext(), ConsumerImpl.class);
                startActivityForResult(myIntent, 0);
            }
        });

    }

}