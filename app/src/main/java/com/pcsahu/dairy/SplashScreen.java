package com.pcsahu.dairy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    private ImageView buffaloLogo;
    private TextView welcomeText;
    private TextView sahuDairyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Initialize Views
        buffaloLogo = findViewById(R.id.buffalo_logo);
        welcomeText = findViewById(R.id.welcome_text);
        sahuDairyText = findViewById(R.id.sahu_dairy_text);

        // Load animations
        Animation logoAnimation = AnimationUtils.loadAnimation(this, R.anim.logomove);
        Animation fadeInWelcomeText = AnimationUtils.loadAnimation(this, R.anim.fade_in_text);
        Animation fadeInSahuDairyText = AnimationUtils.loadAnimation(this, R.anim.fade_in_text);

        // Set initial visibility
        welcomeText.setVisibility(TextView.INVISIBLE);
        sahuDairyText.setVisibility(TextView.INVISIBLE);

        // Start animations
        buffaloLogo.startAnimation(logoAnimation);

        // Set listeners to make the text fade-in after logo animation
        logoAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                welcomeText.setVisibility(TextView.VISIBLE);
                welcomeText.startAnimation(fadeInWelcomeText);

                fadeInWelcomeText.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        sahuDairyText.setVisibility(TextView.VISIBLE);
                        sahuDairyText.startAnimation(fadeInSahuDairyText);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        // After all animations, transition to the main activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 4000);  // Delay to make sure the animations finish before transitioning
    }
}
